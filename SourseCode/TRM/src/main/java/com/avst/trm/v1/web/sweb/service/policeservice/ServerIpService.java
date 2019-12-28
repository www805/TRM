package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.AppServerCache;
import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.conf.type.BASEType;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.ipandport.ChangeIPAndPort;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.SystemIpUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.ipandport.UpdatePortParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetToOutMiddleware_FTPParam;
import com.avst.trm.v1.feignclient.ec.vo.GetMiddleware_FTPVO;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetTDCacheParamByMTssidParam_out;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerPortALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.UpdateIpParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerIpVO;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerPortALLVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServerIpService extends BaseService {

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @Autowired
    private MainService mainService;

    /**
     * 获取ip和会议模板
     * @param result
     */
    public void getServerIp(RResult<GetServerIpVO> result){

        GetServerIpVO getServerIpVO = new GetServerIpVO();

        //获取所有网卡和网卡中的所有ip
        Map<String, List<GetNetworkConfigureVO>> map = SystemIpUtil.getLocalMachineInfo();
        getServerIpVO.setTrmipMap(map);

        try {
            //远程请求
            ReqParam<GetTDCacheParamByMTssidParam_out> reqParam = new ReqParam();
            GetTDCacheParamByMTssidParam_out param = new GetTDCacheParamByMTssidParam_out();
            param.setMcType("MC_AVST");
            reqParam.setParam(param);
            RResult mc_model = meetingControl.getTDByMTList(reqParam);

            getServerIpVO.setModeltds(mc_model.getData());
            result.setData(getServerIpVO);
            this.changeResultToSuccess(result);
        } catch (Exception ex) {
            result.setData(getServerIpVO);
            result.setMessage("远程请求设备、会议ip失败");
            LogUtil.intoLog(4,this.getClass(),"远程请求设备、会议ip失败，抛错了");
        }

    }

    /**
     * 修改IP配置
     * @param rResult
     * @param getServerIpParam
     */
    public void updateServerIp(RResult rResult, GetServerIpParam getServerIpParam) {
        if(!checkKeyword(rResult, getServerIpParam)){
            return;
        }

        getServerIpParam.setBaseType(BASEType.Base);//远程请求通行证
        getServerIpParam.setTrmip(ServerIpCache.getServerIp());//把本机IP赋值到这里
        String asrEtip = getServerIpParam.getAsrip().getEtip();
        String fluEtip = getServerIpParam.getFlushbonadingip().getEtip();
        String polyEtip = getServerIpParam.getPolygraphip().getEtip();

        if(OpenUtil.isIp(getServerIpParam.getTrmip()) == false){
            rResult.setMessage("笔录系统ip，不是一个正确的IP");
            return;
        }
        if(OpenUtil.isIp(asrEtip) == false){
            rResult.setMessage("请输入测谎仪ip，不是一个正确的IP");
            return;
        }
        if(OpenUtil.isIp(fluEtip) == false){
            rResult.setMessage("请输入审讯设备ip，不是一个正确的IP");
            return;
        }
        if(OpenUtil.isIp(polyEtip) == false){
            rResult.setMessage("请输入语音识别服务ip，不是一个正确的IP");
            return;
        }

        String fileBasepath= PropertiesListenerConfig.getProperty("sysBasepath");

        if(StringUtils.isNotBlank(fileBasepath)){
            ChangeIPAndPort.updateIp(getServerIpParam, fileBasepath);
            changeResultToSuccess(rResult);
        }

    }

    public void updateIp(HttpServletRequest request, HttpServletResponse response, RResult rResult, UpdateIpParam updateIpParam) {


        if (StringUtils.isBlank(updateIpParam.getName())) {
            rResult.setMessage("网卡名不能为空");
            return;
        }
        if (OpenUtil.isIp(updateIpParam.getIp()) == false) {
            rResult.setMessage("ip不能为空");
            return;
        }
        if (OpenUtil.isIp(updateIpParam.getSubnetMask()) == false) {
            rResult.setMessage("子网掩码不能为空");
            return;
        }
        if (OpenUtil.isIp(updateIpParam.getGateway()) == false) {
            rResult.setMessage("默认网关不能为空");
            return;
        }

        SystemIpUtil.setLocalIP(updateIpParam.getName(), updateIpParam.getIp(), updateIpParam.getSubnetMask(), updateIpParam.getGateway());

        //消除session
//        HttpSession session = request.getSession();
//        session.invalidate();

        //清除cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        LogUtil.intoLog(1, this.getClass(), "已清除session、cookie");
        LogUtil.intoLog(1, this.getClass(), "因修改了IP，所以开始睡眠12秒");
        try {
            Thread.sleep(10 * 1000);//睡眠10秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //以下代码用来判断ip是否修改成功
//        Boolean bool = false;
//
//        Map<String, List<GetNetworkConfigureVO>> map = SystemIpUtil.getLocalMachineInfo();
//        Iterator<Map.Entry<String, List<GetNetworkConfigureVO>>> entries = map.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<String, List<GetNetworkConfigureVO>> entry = entries.next();
//            String key = entry.getKey();
//
//            List<GetNetworkConfigureVO> list = entry.getValue();
//            for (GetNetworkConfigureVO vo : list) {
//                LogUtil.intoLog(1, this.getClass(), "从网卡获取到的ip：" + vo.getIp() + "  期望改的ip：" + updateIpParam.getIp());
//                if (vo.getIp().equals(updateIpParam.getIp())) {
//                    bool = true;
//                    break;
//                }
//            }
//
//            if (bool) {
//                LogUtil.intoLog(1, this.getClass(), "本机ip修改成功：" + updateIpParam.getIp());
//                break;
//            }
//        }
//
//        if (!bool) {
//            LogUtil.intoLog(4, this.getClass(), "ip修改失败，其他配置文件ip、缓存IP、总控IP不会被继续修改");
//            rResult.setMessage("ip修改失败，其他配置文件ip、缓存IP、总控IP不会被继续修改");
//            return;
//        }

        //把其他的配置文件ip、缓存IP、总控IP 也修改
        //判断数据库里的IP是否一样，多网卡的情况下如果修改的IP是一样就修改完数据库的IP再提交到总控里面去
        Base_serverconfig base_serverconfig = base_serverconfigMapper.selectById(1);
        if (null != base_serverconfig && base_serverconfig.getServerip().equals(updateIpParam.getEip())) {
            //修改系统配置的ip
            Base_serverconfig serverconfig = new Base_serverconfig();
            serverconfig.setId(1);
            serverconfig.setServerip(updateIpParam.getIp());
            Integer integer = base_serverconfigMapper.updateById(serverconfig);
            //上报到总控去
            if (integer > 0) {
                //修改配置文件
                String defaultZone = PropertiesListenerConfig.getProperty("eureka.client.serviceUrl.defaultZone");
                String upload = PropertiesListenerConfig.getProperty("upload.basepath");
                String basepath = PropertiesListenerConfig.getProperty("re.basepath");
                String host = PropertiesListenerConfig.getProperty("socketio.server.host");

                defaultZone = defaultZone.replace(updateIpParam.getEip(), updateIpParam.getIp());
                upload = upload.replace(updateIpParam.getEip(), updateIpParam.getIp());
                basepath = basepath.replace(updateIpParam.getEip(), updateIpParam.getIp());
                host = host.replace(updateIpParam.getEip(), updateIpParam.getIp());

                PropertiesListenerConfig.setProperty("eureka.client.serviceUrl.defaultZone", defaultZone);
                PropertiesListenerConfig.setProperty("upload.basepath", upload);
                PropertiesListenerConfig.setProperty("re.basepath", basepath);
                PropertiesListenerConfig.setProperty("socketio.server.host", host);

                ServerIpCache.setServerIp(updateIpParam.getIp());//设置上报到总控的ip
                //系统文件修改
                String fileBasepath = PropertiesListenerConfig.getProperty("sysBasepath");
                fileBasepath = fileBasepath.endsWith("/") ? fileBasepath : (fileBasepath + "/");
                boolean outbool = ChangeIPAndPort.otherPCConnectChangeIP(fileBasepath, ServerIpCache.getServerIp());

                AppCache.delAppCacheParam();
                AppServerCache.delAppServerCache();

                LogUtil.intoLog(1, this.getClass(), "配置文件ip、缓存IP、总控IP 修改成功");
                changeResultToSuccess(rResult);
            }else{
                LogUtil.intoLog(4, this.getClass(), "修改系统配置的ip失败");
            }

        }else{
            LogUtil.intoLog(4, this.getClass(), "成功修改的ip与数据库的ip不一致，不继续上报总控");
        }
    }

    /**
     * 获取其他全部设备IP
     * @param rResult
     * @param param
     */
    public void getServerIpALL(RResult rResult, GetServerIpALLParam param) {

        try {
            //获取其他所有的ip
            ReqParam<GetServerIpALLParam> reqParam = new ReqParam<>();
            param.setBaseType(BASEType.Base);
            reqParam.setParam(param);

            RResult rResult1 = equipmentControl.getServerIpALL(reqParam);
            rResult.setData(rResult1.getData());
            changeResultToSuccess(rResult);
            LogUtil.intoLog(1, this.getClass(), "获取其他全部设备IP，请求成功");
        } catch (Exception e) {
            LogUtil.intoLog(4,this.getClass(),"获取其他全部设备IP，远程请求接口出错！");
        }

    }

    /**
     * 获取所有服务端口
     * @param rResult
     * @param param
     */
    public void getServerPortALL(RResult rResult, GetServerPortALLParam param) {

        GetServerPortALLVO vo = new GetServerPortALLVO();

        try {
            //请求总控获取所有服务的端口
//            RResult zkResult = new RResult();
//            mainService.getServerStatus(zkResult, null);
//            if(null != zkResult.getData()){
//                List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) zkResult.getData();
//                for (LinkedHashMap<String, Object> hashMap : data) {
//
//                    String url = (String) hashMap.get("url");
//                    int startNum = url.indexOf(":",10);
//                    int endNum = url.indexOf("/", 10);
//                    String portStr = url.substring(startNum + 1, endNum);
//
//                    if("trm".equals(hashMap.get("servername"))){
//                        vo.setTrmport(portStr);
//                    }else if("zk".equals(hashMap.get("servername"))){
//                        vo.setZkport(portStr);
//                    }else if("mc".equals(hashMap.get("servername"))){
//                        vo.setMcport(portStr);
//                    }else if("ec".equals(hashMap.get("servername"))){
//                        vo.setEcport(portStr);
//                    }
//                }
//            }

//            String socketioport = PropertiesListenerConfig.getProperty("socketio.server.port");
            String sysBasepath = PropertiesListenerConfig.getProperty("sysBasepath");
            String fileBasepath = sysBasepath.endsWith("/") ? sysBasepath : (sysBasepath + "/");
            String listen = "";

            //从配置文件读trm、zk、ec、mc端口
            String socketioport = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/trm.properties", "socketio.server.port");
            String trmPort = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/trm.properties", "server.port");
            String mcPort = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/mc.properties", "server.port");
            String ecPort = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/ec.properties", "server.port");
            String ftpPort = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/ec.properties", "ftpport");
            String zkPort = ChangeIPAndPort.getPropertiesByKey(fileBasepath + "WORKJAR/zk.properties", "server.port");
            vo.setTrmport(trmPort);
            vo.setMcport(mcPort);
            vo.setEcport(ecPort);
            vo.setZkport(zkPort);
            vo.setFtpport(ftpPort);
            vo.setSocketioport(socketioport);
            vo.setAnzhuangpath(sysBasepath);

            //从ec获取ftp集中控制端口
//            ReqParam<GetToOutMiddleware_FTPParam> reqParam = new ReqParam<>();
//            GetToOutMiddleware_FTPParam ftpParam = new GetToOutMiddleware_FTPParam();
//            ftpParam.setFdType(FDType.FD_AVST);
//            reqParam.setParam(ftpParam);
//            RResult ftpPortResult = equipmentControl.getToOutFtpPort(reqParam);
//            if(null != ftpPortResult.getData()){
//                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) ftpPortResult.getData();
//                if (null != map.get("saveinfoport")) {
//                    vo.setSaveinfoport((String) map.get("saveinfoport"));
//                }
//                if (null != map.get("ftpport")) {
//                    vo.setFtpport((String) map.get("ftpport"));
//                }
//            }

            //NGINX。conf
            String nginxconfpath = fileBasepath + "other/nginx-1.8.1/nginx-1.8.1/conf/nginx.conf";
            List<String> nginxlist = ReadWriteFile.readTxtFileToList(nginxconfpath, "utf8");
            if (null != nginxlist && nginxlist.size() > 0) {
                for (String str : nginxlist) {
                    if (str.trim().startsWith("listen ")) {
                        listen = str.replace("listen ", "").replace(";","").trim();
                        break;
                    }
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"nginx.conf没有读到数据");
            }

            vo.setNginxport(listen);
        } catch (Exception e) {
            LogUtil.intoLog(4,this.getClass(),"获取所有服务端口，远程请求接口出错！");
        }

        rResult.setData(vo);
        changeResultToSuccess(rResult);
    }

    public void setServerPortALL(RResult rResult, GetServerPortALLParam param) {

        if (null == param.getTrmport()) {
            rResult.setMessage("trm端口不能为空");
            return;
        }
        if (null == param.getZkport()) {
            rResult.setMessage("zk端口不能为空");
            return;
        }
        if (null == param.getMcport()) {
            rResult.setMessage("mc端口不能为空");
            return;
        }
        if (null == param.getEcport()) {
            rResult.setMessage("ec端口不能为空");
            return;
        }
//        if (null == param.getSaveinfoport()) {
//            rResult.setMessage("ec存储设备端口不能为空");
//            return;
//        }
        if (null == param.getFtpport()) {
            rResult.setMessage("ftp端口不能为空");
            return;
        }
        if (null == param.getSocketioport()) {
            rResult.setMessage("socketioport端口不能为空");
            return;
        }
        if (null == param.getNginxport()) {
            rResult.setMessage("nginx端口不能为空");
            return;
        }


        UpdatePortParam updatePortParam = new UpdatePortParam();
        updatePortParam.setTrmserverport(param.getTrmport());
        updatePortParam.setZkserverport(param.getZkport());
        updatePortParam.setMcserverport(param.getMcport());
        updatePortParam.setEcserverport(param.getEcport());
        updatePortParam.setFtpserverport(param.getFtpport());
        updatePortParam.setSocketioserverport(param.getSocketioport());
        updatePortParam.setNginxserverport(param.getNginxport());
//        updatePortParam.setAnzhuangpath(param.getAnzhuangpath());

        boolean b = ChangeIPAndPort.updatePort(updatePortParam);
        if (b) {
            changeResultToSuccess(rResult);
        }else{
            rResult.setMessage("端口修改失败。。");
        }

    }

    /**
     * 校验参数
     * @param rResult
     * @param getServerIpParam
     * @return
     */
    private Boolean checkKeyword(RResult rResult, GetServerIpParam getServerIpParam) {

        if(StringUtils.isBlank(getServerIpParam.getTrmip())
                || StringUtils.isBlank(getServerIpParam.getAsrip().getEtip())
                || StringUtils.isBlank(getServerIpParam.getFlushbonadingip().getEtip())
                || StringUtils.isBlank(getServerIpParam.getPolygraphip().getEtip())){
            rResult.setMessage("笔录系统ip、审讯设备ip、测谎仪ip、语音识别服务ip不能为空");
            return false;
        }
        return true;
    }

}
