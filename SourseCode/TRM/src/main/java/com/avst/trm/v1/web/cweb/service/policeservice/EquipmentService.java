package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.PtdjmapCache;
import com.avst.trm.v1.common.cache.RecordStatusCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.cache.param.RecordStatusCacheParam;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordrealByRecordssidParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("equipmentService")
public class EquipmentService extends BaseService {

    @Autowired
    private EquipmentControl equipmentControl;

    public void getFDState(RResult result, ReqParam param) {

        try {
            RResult fdState = equipmentControl.getFDState(param);
            if(null!=fdState&&fdState.getActioncode().equals(Code.SUCCESS.toString())){
                result.setData(fdState.getData());
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"EquipmentService getFDState获取设备状态信息失败..,RResult:"+fdState==null?null: JacksonUtil.objebtToString(fdState));
                result.setMessage("设备状态请求失败!");
            }
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass()," EquipmentService getFDState获取设备状态信息失败...");
            result.setMessage("设备状态请求失败!");
        }

    }

    public void getdvdOutOrIn(RResult result, ReqParam<DvdOutOrInParam_out> param) {

        try {
            RResult fdState = equipmentControl.dvdOutOrIn(param);

            result.setData(fdState.getData());
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getdvdOutOrIn  光盘出仓/进仓 请求错误...");
        }


    }

    public void getstartRec_Rom(RResult result, ReqParam<StartRec_RomParam_out> param) {

        try {
            RResult fdState = equipmentControl.startRec_Rom(param);

            result.setData(fdState.getData());
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getstartRec_Rom  开始光盘刻录 请求错误...");
        }

    }

    public void getstopRec_Rom(RResult result, ReqParam<StopRec_RomParam_out> param) {

        try {
            RResult fdState = equipmentControl.stopRec_Rom(param);

            result.setData(fdState.getData());
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getdvdOutOrIn  结束光盘刻录 请求错误...");
        }

    }

    public void getyuntaiControl(RResult result, ReqParam<YuntaiControlParam_out> param) {

        try {
            RResult fdState = equipmentControl.yuntaiControl(param);

            result.setData(fdState.getData());
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getyuntaiControl  云台控制 请求错误...");
        }

    }

    public void getptdjconst(RResult result, ReqParam param) {

        try {
            RResult fdState = equipmentControl.getptdjconst(param);

            HashMap<String, Object> map = new HashMap<>();

            //从缓存中获取，如果没有就从外部获取
            Map<String, Object> ptdjmaps = PtdjmapCache.getPtdjmapCache();
            if (null == ptdjmaps) {
                ptdjmaps = ptdjmap();
                PtdjmapCache.setPtdjmapCache(ptdjmaps);
            }

            map.put("ptdjmaps", ptdjmaps);
            map.put("ptdjtitles", fdState.getData());

            result.setData(map);
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getptdjconst  获取当前配置片头字段 请求错误...");
        }

    }

    public void ptdj(RResult result, ReqParam<PtdjParam_out> param) {

        try {
            RResult fdState = equipmentControl.ptdj(param);

            result.setData(fdState.getData());
            result.setMessage(fdState.getMessage());
            result.setEndtime(fdState.getEndtime());
            result.setActioncode(fdState.getActioncode());
            result.setNextpageid(fdState.getNextpageid());
            result.setVersion(fdState.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.addptdj  片头叠加 请求错误...");
        }

    }

    /**
     * 获取片头叠加外部文件
     * @return
     */
    private Map<String, Object> ptdjmap() {

        AppCacheParam cacheParam = AppCache.getAppCacheParam();
        Map<String, Object> cweb = cacheParam.getData();
        Map<String, Object> ptdj = (Map<String, Object>) cweb.get("ptdj");


        return ptdj;
    }



    public void getBurnTime(RResult result, ReqParam param) {

        try {
            RResult resultBurn = equipmentControl.getBurnTime(param);

            result.setData(resultBurn.getData());
            result.setMessage(resultBurn.getMessage());
            result.setEndtime(resultBurn.getEndtime());
            result.setActioncode(resultBurn.getActioncode());
            result.setNextpageid(resultBurn.getNextpageid());
            result.setVersion(resultBurn.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getBurnTime  获取刻录选时 请求错误...");
        }
    }

    public void updateBurnTime(RResult result, ReqParam param) {

        try {
            RResult resultBurn = equipmentControl.updateBurnTime(param);

            result.setData(resultBurn.getData());
            result.setMessage(resultBurn.getMessage());
            result.setEndtime(resultBurn.getEndtime());
            result.setActioncode(resultBurn.getActioncode());
            result.setNextpageid(resultBurn.getNextpageid());
            result.setVersion(resultBurn.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.updateBurnTime  修改刻录选时 请求错误...");
        }
    }

    public void changeBurnMode(RResult result, ReqParam<ChangeBurnModeParam_out> param) {

        try {
            RResult resultBurn = equipmentControl.changeBurnMode(param);

            result.setData(resultBurn.getData());
            result.setMessage(resultBurn.getMessage());
            result.setEndtime(resultBurn.getEndtime());
            result.setActioncode(resultBurn.getActioncode());
            result.setNextpageid(resultBurn.getNextpageid());
            result.setVersion(resultBurn.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.changeBurnMode  刻录模式选择 请求错误...");
        }

    }

    public void getCDNumber(RResult result, ReqParam<GetCDNumberParam_out> param) {

        try {
            RResult resultBurn = equipmentControl.getCDNumber(param);

            result.setData(resultBurn.getData());
            result.setMessage(resultBurn.getMessage());
            result.setEndtime(resultBurn.getEndtime());
            result.setActioncode(resultBurn.getActioncode());
            result.setNextpageid(resultBurn.getNextpageid());
            result.setVersion(resultBurn.getVersion());
        } catch (Exception e) {
            LogUtil.intoLog(this.getClass(),"com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action.getCDNumber  光盘序号 请求错误...");
        }
    }

    public void getFDAudPowerMap(RResult result, ReqParam<GetFDAudPowerMapParam_out> param) {

        GetFDAudPowerMapParam_out paramParam = param.getParam();

        RResult rResult = equipmentControl.getFDAudPowerMap(paramParam);

        result.setData(rResult.getData());
        result.setMessage(rResult.getMessage());
        result.setVersion(rResult.getVersion());
        result.setNextpageid(rResult.getNextpageid());
        result.setActioncode(rResult.getActioncode());

    }

    public void putRecessStatus(RResult result, ReqParam<RecordStatusCacheParam> param) {

        RecordStatusCacheParam paramParam = param.getParam();
        paramParam.setLasttime(new Date().getTime());
        RecordStatusCache.setRecordStatusCache(paramParam);

        changeResultToSuccess(result);
    }

}
