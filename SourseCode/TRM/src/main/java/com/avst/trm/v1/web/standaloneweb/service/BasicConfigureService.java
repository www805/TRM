package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.FdSSidCache;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.feignclient.ec.vo.fd.Flushbonadinginfo;
import com.avst.trm.v1.feignclient.ec.vo.fd.GetFDStatevo;
import com.avst.trm.v1.web.standaloneweb.req.*;
import com.avst.trm.v1.web.standaloneweb.vo.GetSystemInfoVO;
import com.avst.trm.v1.web.standaloneweb.vo.param.HardwareParam;
import com.avst.trm.v1.web.standaloneweb.vo.param.SQParam;
import com.avst.trm.v1.web.standaloneweb.vo.param.SoftwareParam;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class BasicConfigureService extends BaseService {

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    private Gson gson=new Gson();

    private String pattern = ".*[\\u4E00-\\u9FA5].*";

    public static Integer subtime = 0;

    public RResult getFDNetWork(RResult result, GetFDNetWorkParam_out param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return result;
        }

        RResult fdAudioConf = equipmentControl.getFDNetWork(param);

        return fdAudioConf;
    }


    public RResult setNetworkConfigure(RResult result, List<GetNetworkConfigureParam> paramList) {

        RResult resultFD = result;

        for (GetNetworkConfigureParam param : paramList) {

            if (StringUtils.isBlank(param.getIp_new())) {
                result.setMessage("ip不能为空");
                return result;
            }
            if (StringUtils.isBlank(param.getNetmask())) {
                result.setMessage("子网掩码不能为空");
                return result;
            }
            if (StringUtils.isBlank(param.getGateway())) {
                result.setMessage("网关不能为空");
                return result;
            }
            if(Pattern.matches(pattern, param.getNetmask())){
                result.setMessage("子网掩码不能为中文");
                return result;
            }
            if(Pattern.matches(pattern, param.getGateway())){
                result.setMessage("网关不能为中文");
                return result;
            }

            SetFDnetworkParam_out setFDnetworkParam_out=gson.fromJson(gson.toJson(param), SetFDnetworkParam_out.class);

            resultFD = equipmentControl.setFDnetwork(setFDnetworkParam_out);

        }

        return resultFD;
    }


    public void getSystemInfo(RResult result) {

        GetSystemInfoVO getSystemInfoVO=new GetSystemInfoVO();

        //获取笔录系统软件信息
        SoftwareParam softwareParam=new SoftwareParam();//软件信息
        softwareParam.setCompanyname("测试");//等授权

        Base_serverconfig base_serverconfig=new Base_serverconfig();
        base_serverconfig.setId(1);
        base_serverconfig=base_serverconfigMapper.selectOne(base_serverconfig);
        if(null!=base_serverconfig){
            softwareParam.setSysStartTime(base_serverconfig.getWorkstarttime()== null?"":DateUtil.format(base_serverconfig.getWorkstarttime()));
            softwareParam.setSysVersion(base_serverconfig.getSysVersion());
        }else{
            LogUtil.intoLog(4,this.getClass(),"系统配置数据获取异常，警告");
        }

        long sysstarttime=CommonCache.sysStartTime;
        long nowtime= DateUtil.getSeconds();
        softwareParam.setWorkTime(DateUtil.longToTime(nowtime-sysstarttime));
        getSystemInfoVO.setSoftwareParam(softwareParam);

        //获取系统授权信息
        SQParam sqParam=getSQInfo();//授权信息
        getSystemInfoVO.setSqParam(sqParam);

        //获取默认设备的硬件信息
        HardwareParam hardwareParam=new HardwareParam();//硬件信息

        //获取默认设备信息
        ReqParam<GetToOutFlushbonadingListParam> param_ = new ReqParam<>();
        GetToOutFlushbonadingListParam listParam = new GetToOutFlushbonadingListParam();
        listParam.setFdType(FDType.FD_AVST);
        param_.setParam(listParam);
        RResult result_ = equipmentControl.getToOutDefault(param_);
        if (null != result_ && result_.getActioncode().equals(Code.SUCCESS.toString())&&null!=result_.getData()) {
            Flushbonadinginfo flushbonadinginfo=gson.fromJson(gson.toJson(result_.getData()), Flushbonadinginfo.class);
            if (null!=flushbonadinginfo&&null!=flushbonadinginfo.getSsid()){
                String fdssid= flushbonadinginfo.getSsid();

                if(StringUtils.isEmpty(fdssid)){
                    LogUtil.intoLog(4,this.getClass(),"EquipmentService getFDState获取默认设备信息失败..,flushbonadinginfo:"+flushbonadinginfo==null?null: JacksonUtil.objebtToString(flushbonadinginfo));
                }else{
                    ReqParam<GetFDStateParam> stateparam=new ReqParam<GetFDStateParam>();
                    GetFDStateParam getFDStateParam=new GetFDStateParam();
                    getFDStateParam.setStateType(0);
                    getFDStateParam.setFdType(FDType.FD_AVST);
                    getFDStateParam.setFlushbonadingetinfossid(fdssid);
                    stateparam.setParam(getFDStateParam);
                    RResult fdState = equipmentControl.getFDState(stateparam);
                    if(null!=fdState&&fdState.getActioncode().equals(Code.SUCCESS.toString())&&null!=fdState.getData()){
                        GetFDStatevo getFDStatevo=gson.fromJson(gson.toJson(fdState.getData()),GetFDStatevo.class);
                        if(null==getFDStatevo||null==getFDStatevo.getDev_version()){
                            LogUtil.intoLog(4,this.getClass(),"EquipmentService getFDState获取设备状态信息失败..,getFDStatevo:"+getFDStatevo==null?null: JacksonUtil.objebtToString(getFDStatevo));
                        }else{
                            hardwareParam.setFdid(getFDStatevo.getDevmid_id());
                            hardwareParam.setSerialnumber(getFDStatevo.getSerialnumber());
                            String fdversion=getFDStatevo.getDev_version();
                            String fdhw=getFDStatevo.getHw();
                            String fdsw=getFDStatevo.getSw();
                            if(StringUtils.isEmpty(fdversion)){
                                fdversion="未知";
                            }
                            if(StringUtils.isEmpty(fdhw)){
                                fdversion+="-未知";
                            }else{
                                fdversion+="-"+fdhw;
                            }
                            if(StringUtils.isEmpty(fdsw)){
                                fdversion+="未知";
                            }else{
                                fdversion+="-"+fdsw;
                            }
                            hardwareParam.setVersion(fdversion);

                            getSystemInfoVO.setHardwareParam(hardwareParam);
                        }

                    }else{
                        LogUtil.intoLog(4,this.getClass(),"EquipmentService getFDState获取设备状态信息失败..,RResult:"+fdState==null?null: JacksonUtil.objebtToString(fdState));
                    }
                }
            }
        }else{
            LogUtil.intoLog(4,this.getClass(),"请求默认设备信息出错");
        }
        result.setData(getSystemInfoVO);
        changeResultToSuccess(result);//暂时不做错误的判断
    }
    public SQParam getSQInfo() {

        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息

        if(null==getSQEntity){
            LogUtil.intoLog(4,this.getClass(),"获取系统授权信息异常，直接跳出");
            return null;
        }
        SQParam sqParam=new SQParam();
        sqParam.setSqEntity(getSQEntity);
        //授权功能
        List<String> gnArrayList = new ArrayList<String>();
        Map<String, Object> map = new HashMap<>();
        String gnlist = getSQEntity.getGnlist();
        String[] strings = gnlist.split("\\|");
        if (null != strings && strings.length > 0) {

            for (int i = 0; i < strings.length; i++) {
                String str = strings[i];
                if("asr_f".equals(str)){
                    gnArrayList.add(SQGN.asr);
                }else if("fd_f".equals(str)){
                    gnArrayList.add(SQGN.fd);
                }else if("ph_f".equals(str)){
                    gnArrayList.add(SQGN.ph);
                }else if("record_f".equals(str)){
                    gnArrayList.add(SQGN.record);
                }else if("tts_f".equals(str)){
                    gnArrayList.add(SQGN.tts);
                }else if(str.equals(SQVersion.S_V)){
                    map.put("version", "单机版");
                }else if(str.equals(SQVersion.O_V)){
                    map.put("version", "联机版");
                }else if(str.equals(SQVersion.C_E)){
                    map.put("banben", "客户端");
                }else if(str.equals(SQVersion.S_E)){
                    map.put("banben", "服务器");
                }else if(str.equals(SQVersion.GA_T)){
                    map.put("t", "公安版");
                }else if(str.equals(SQVersion.JW_T)){
                    map.put("t", "纪委版");
                }else if(str.equals(SQVersion.JCW_T)){
                    map.put("t", "监察委版");
                }else if(str.equals(SQVersion.FY_T)){
                    map.put("t", "法院版");
                }else if(str.equals(SQVersion.AVST_T)){
                    map.put("t", "顺泰伟诚");
                }else if(str.equals(SQVersion.COMMON_O)){
                    map.put("oem", "通用版");
                }else if(str.equals(SQVersion.HK_O)){
                    map.put("oem", "海康");
                }else if(str.equals(SQVersion.NX_O)){
                    map.put("oem", "宁夏");
                }
            }
            sqParam.setQtlistp(map);
            sqParam.setGnlist(gnArrayList);
        }
        return sqParam;
    }


    public RResult setEcSystemTime(RResult result, SetEcSystemTimeParam param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return result;
        }
        if(StringUtils.isBlank(param.getSystemTime())){
            result.setMessage("系统时间不能为空");
            return result;
        }


        if (subtime <= 8) {
            result.setMessage("设置时间不能太频繁，每次不能少于5-10秒");
            return result;
        }else{
            subtime = 0;
        }

        //执行逻辑
        SetFDTimeParam_out setFDTimeParam_out = new SetFDTimeParam_out();
        setFDTimeParam_out.setFlushbonadingetinfossid(param.getFlushbonadingetinfossid());
        setFDTimeParam_out.setFdType(param.getFdType());
        setFDTimeParam_out.setDateTime(param.getSystemTime());
        RResult fdTimeResult = equipmentControl.setFDTime(setFDTimeParam_out);

        return fdTimeResult;
    }

    public RResult setEcSystemTimeSync(RResult result, SetEcSystemTimeSyncParam param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return result;
        }

        SetFDTimeParam_out setFDTimeParam_out = new SetFDTimeParam_out();
        setFDTimeParam_out.setFlushbonadingetinfossid(param.getFlushbonadingetinfossid());
        setFDTimeParam_out.setFdType(param.getFdType());
        String dateAndMinute = DateUtil.getDateAndMinute();
        setFDTimeParam_out.setDateTime(dateAndMinute);

        RResult fdTimeResult = equipmentControl.setFDTime(setFDTimeParam_out);

        return fdTimeResult;
    }

    public RResult setNTP(RResult result, SetNTPParam param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getNtpip())) {
            result.setMessage("NTP服务器IP不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getTimeInterval())) {
            result.setMessage("NTP同步时间间隔不能为空");
            return result;
        }

        SetFDNTPParam_out setFDNTPParam_out = new SetFDNTPParam_out();
        setFDNTPParam_out.setFlushbonadingetinfossid(param.getFlushbonadingetinfossid());
        setFDNTPParam_out.setFdType(param.getFdType());
        setFDNTPParam_out.setNtp_host(param.getNtpip());
        setFDNTPParam_out.setNtp_port(param.getNtpprot());
        setFDNTPParam_out.setNtp_intervaltime(param.getTimeInterval());

        RResult fdTimeResult = equipmentControl.setFDNTP(setFDNTPParam_out);

        return fdTimeResult;
    }

    public void getFDssid(RResult result) {

        String fdssid = FdSSidCache.getFdSSidCache();//获取默认设备的ssid
        if (StringUtils.isBlank(fdssid)) {
            result.setMessage("默认设备ssid获取失败");
            return;
        }

        //从缓存里获取
        result.setData(fdssid);
        changeResultToSuccess(result);
    }


    public RResult getFDNTP(RResult result, GetFDNTPParam param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return result;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return result;
        }

        GetFDNTPParam_out getFDNTPParam_out = new GetFDNTPParam_out();
        getFDNTPParam_out.setFlushbonadingetinfossid(param.getFlushbonadingetinfossid());
        getFDNTPParam_out.setFdType(param.getFdType());

        RResult fdntpResult = equipmentControl.getFDNTP(getFDNTPParam_out);

        return fdntpResult;
    }
}
