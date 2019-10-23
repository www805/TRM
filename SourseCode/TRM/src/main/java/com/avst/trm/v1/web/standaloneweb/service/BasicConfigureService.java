package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.SystemIpUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetFDAudioConfParam_out;
import com.avst.trm.v1.feignclient.ec.req.GetFDNetWorkParam_out;
import com.avst.trm.v1.feignclient.ec.req.SetFDAudioVolumeParam_out;
import com.avst.trm.v1.feignclient.ec.req.SetFDnetworkParam_out;
import com.avst.trm.v1.web.standaloneweb.req.GetNetworkConfigureParam;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import com.avst.trm.v1.web.standaloneweb.vo.GetSQInfoVO;
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


    public void getSQInfo(RResult result) {

        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息

        //授权功能
        List gnArrayList = new ArrayList();
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
        }

        GetSQInfoVO sqInfoVO = new GetSQInfoVO();
        sqInfoVO.setSqEntity(getSQEntity);
        sqInfoVO.setGnlist(gnArrayList);
        sqInfoVO.setQtlistp(map);

        result.setData(sqInfoVO);
        this.changeResultToSuccess(result);

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
}
