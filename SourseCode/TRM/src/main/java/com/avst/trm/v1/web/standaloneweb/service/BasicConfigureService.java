package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.SystemIpUtil;
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
import com.avst.trm.v1.web.standaloneweb.req.GetNetworkConfigureParam;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import com.avst.trm.v1.web.standaloneweb.vo.GetSQInfoVO;
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


@Service
public class BasicConfigureService extends BaseService {

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    private Gson gson=new Gson();

    public void getFDNetWork(RResult result, GetFDNetWorkParam_out param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())) {
            result.setMessage("设备ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getFdType())) {
            result.setMessage("设备类型不能为空");
            return;
        }

        RResult fdAudioConf = equipmentControl.getFDNetWork(param);

        result.setActioncode(fdAudioConf.getActioncode());
        result.setNextpageid(fdAudioConf.getNextpageid());
        result.setData(fdAudioConf.getData());
        result.setMessage(fdAudioConf.getMessage());
        result.setEndtime(fdAudioConf.getEndtime());
        result.setVersion(fdAudioConf.getVersion());

    }


    public void setNetworkConfigure(RResult result, GetNetworkConfigureParam param) {

        if (StringUtils.isBlank(param.getIp())) {
            result.setMessage("ip不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getNetmask())) {
            result.setMessage("子网掩码不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getGateway())) {
            result.setMessage("网关不能为空");
            return;
        }

        SetFDnetworkParam_out setFDnetworkParam_out = new SetFDnetworkParam_out();
        setFDnetworkParam_out.setIp_new(param.getIp());
        setFDnetworkParam_out.setNetmask(param.getNetmask());
        setFDnetworkParam_out.setGateway(param.getGateway());

        RResult resultFD = equipmentControl.setFDnetwork(setFDnetworkParam_out);

        result.setActioncode(resultFD.getActioncode());
        result.setNextpageid(resultFD.getNextpageid());
        result.setData(resultFD.getData());
        result.setMessage(resultFD.getMessage());
        result.setEndtime(resultFD.getEndtime());
        result.setVersion(resultFD.getVersion());

//        SystemIpUtil.setIP(ip, subnetMask, gateway);

//        this.changeResultToSuccess(result);
    }




    public void getFDAudioConf(RResult result, GetFDAudioConfParam_out param) {
        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())){
            result.setMessage("设备ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getFdType())){
            result.setMessage("设备类型不能为空");
            return;
        }

        RResult fdAudioConf = equipmentControl.getFDAudioConf(param);

        result.setActioncode(fdAudioConf.getActioncode());
        result.setNextpageid(fdAudioConf.getNextpageid());
        result.setData(fdAudioConf.getData());
        result.setMessage(fdAudioConf.getMessage());
        result.setEndtime(fdAudioConf.getEndtime());
        result.setVersion(fdAudioConf.getVersion());

    }

    public void setFDAudioVolume(RResult result, SetFDAudioVolumeParam_out param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())){
            result.setMessage("设备ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getFdType())){
            result.setMessage("设备类型不能为空");
            return;
        }

        RResult fdAudioConf = equipmentControl.setFDAudioVolume(param);

        result.setActioncode(fdAudioConf.getActioncode());
        result.setNextpageid(fdAudioConf.getNextpageid());
        result.setData(fdAudioConf.getData());
        result.setMessage(fdAudioConf.getMessage());
        result.setEndtime(fdAudioConf.getEndtime());
        result.setVersion(fdAudioConf.getVersion());
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
}
