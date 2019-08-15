package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.util.ChangeIP;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RRParam;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMc_modelParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetTDCacheParamByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetTdByModelSsidParam_out;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import com.avst.trm.v1.web.sweb.req.policereq.ServerconfigParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerConfigByIdVO;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerIpVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ServerIpService extends BaseService {

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private EquipmentControl equipmentControl;

    /**
     * 获取ip和会议模板
     * @param result
     */
    public void getServerIp(RResult<GetServerIpVO> result){

        GetServerIpVO getServerIpVO = new GetServerIpVO();


        //远程请求
        ReqParam<GetTDCacheParamByMTssidParam_out> reqParam = new ReqParam();
        GetTDCacheParamByMTssidParam_out param = new GetTDCacheParamByMTssidParam_out();
        param.setMcType("MC_AVST");
        reqParam.setParam(param);
        RResult mc_model = meetingControl.getTDByMTList(reqParam);

        //获取配置文件
        String trmIP = NetTool.getMyIP();

        getServerIpVO.setTrmip(trmIP);
        getServerIpVO.setModeltds(mc_model.getData());

        result.setData(getServerIpVO);
        this.changeResultToSuccess(result);
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

        String fileBasepath= PropertiesListenerConfig.getProperty("sysBasepath");

        if(StringUtils.isNotBlank(fileBasepath)){
            ChangeIP.updateIp(getServerIpParam, fileBasepath);
            changeResultToSuccess(rResult);
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
                || StringUtils.isBlank(getServerIpParam.getPolygraphip().getEtip())
                || StringUtils.isBlank(getServerIpParam.getStorageip().getEtip())
                || StringUtils.isBlank(getServerIpParam.getTtsetinfoip().getEtip())){
            rResult.setMessage("笔录系统ip、审讯设备ip、测谎仪ip、语音识别服务ip、存储设备ip、文字转语音服务ip不能为空");
            return false;
        }
        return true;
    }



}
