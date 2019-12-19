package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.AppServerCache;
import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.conf.type.BASEType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.ChangeIP;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.SystemIpUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetTDCacheParamByMTssidParam_out;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import com.avst.trm.v1.web.sweb.req.basereq.UpdateIpParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerIpVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
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
            ChangeIP.updateIp(getServerIpParam, fileBasepath);
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

        try {
            Thread.sleep(8000);//睡眠8秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //以下代码用来判断ip是否修改成功
        Boolean bool = false;

        Map<String, List<GetNetworkConfigureVO>> map = SystemIpUtil.getLocalMachineInfo();
        Iterator<Map.Entry<String, List<GetNetworkConfigureVO>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<GetNetworkConfigureVO>> entry = entries.next();
            String key = entry.getKey();

            List<GetNetworkConfigureVO> list = entry.getValue();
            for (GetNetworkConfigureVO vo : list) {
                if (vo.getIp().equals(updateIpParam.getIp())) {
                    bool = true;
                    break;
                }
            }

            if (bool) {
                break;
            }
        }

        if (!bool) {
            rResult.setMessage("ip修改失败，其他的不会继续执行");
            return;
        }

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
                boolean outbool = ChangeIP.otherPCConnectChangeIP(fileBasepath, ServerIpCache.getServerIp());

                AppCache.delAppCacheParam();
                AppServerCache.delAppServerCache();

                changeResultToSuccess(rResult);
            }

        }

        //消除session
        HttpSession session = request.getSession();
        session.invalidate();

        //清除cookie，并且退出
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

    }

    /**
     * 获取其他全部设备IP
     * @param rResult
     * @param param
     */
    public void getServerIpALL(RResult rResult, GetServerIpALLParam param) {

        //获取其他所有的ip
        ReqParam<GetServerIpALLParam> reqParam = new ReqParam<>();
        param.setBaseType(BASEType.Base);
        reqParam.setParam(param);

        RResult rResult1 = equipmentControl.getServerIpALL(reqParam);
        rResult.setData(rResult1.getData());
        changeResultToSuccess(rResult);
        return;
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
