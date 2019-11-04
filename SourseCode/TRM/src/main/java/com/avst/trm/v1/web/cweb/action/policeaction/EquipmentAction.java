package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.cache.param.RecordStatusCacheParam;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.web.cweb.service.policeservice.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 关于设备 RequestMapping依旧使用笔录的
 */
@RestController
@RequestMapping("/cweb/police/record")
public class EquipmentAction extends BaseAction {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 获取设备状态信息
     * @param param
     * @return
     */
    @RequestMapping("/getFDState")
    public RResult getFDState(@RequestBody ReqParam param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getFDState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取当前配置片头字段
     * @param param
     * @return
     */
    @RequestMapping("/getptdjconst")
    public RResult getptdjconst(@RequestBody  ReqParam param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getptdjconst(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 光盘出仓/进仓
     * @param param
     * @return
     */
    @RequestMapping("/getdvdOutOrIn")
    public RResult getdvdOutOrIn(@RequestBody  ReqParam<DvdOutOrInParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getdvdOutOrIn(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 开始光盘刻录
     * @param param
     * @return
     */
    @RequestMapping("/getstartRec_Rom")
    public RResult getstartRec_Rom(@RequestBody  ReqParam<StartRec_RomParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getstartRec_Rom(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 结束光盘刻录
     * @param param
     * @return
     */
    @RequestMapping("/getstopRec_Rom")
    public RResult getstopRec_Rom(@RequestBody  ReqParam<StopRec_RomParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getstopRec_Rom(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 云台控制
     * @param param
     * @return
     */
    @RequestMapping("/getyuntaiControl")
    public RResult getyuntaiControl(@RequestBody  ReqParam<YuntaiControlParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getyuntaiControl(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 片头叠加
     * @param param
     * @return
     */
    @RequestMapping("/ptdj")
    public RResult ptdj(@RequestBody  ReqParam<PtdjParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.ptdj(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取刻录选时
     * @param param
     * @return
     */
    @RequestMapping("/getBurnTime")
    public RResult getBurnTime(@RequestBody  ReqParam<GetBurnTimeParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getBurnTime(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 修改刻录选时
     * @param param
     * @return
     */
    @RequestMapping("/updateBurnTime")
    public RResult updateBurnTime(@RequestBody  ReqParam<GetBurnTimeParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.updateBurnTime(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 刻录模式选择
     * @param param
     * @return
     */
    @RequestMapping("/changeBurnMode")
    public RResult changeBurnMode(@RequestBody  ReqParam<ChangeBurnModeParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.changeBurnMode(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 光盘序号
     * @param param
     * @return
     */
    @RequestMapping("/getCDNumber")
    public RResult getCDNumber(@RequestBody  ReqParam<GetCDNumberParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getCDNumber(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获得 设备现场的音频振幅
     * @param param
     * @return
     */
    @RequestMapping("/getFDAudPowerMap")
    public RResult getFDAudPowerMap(@RequestBody  ReqParam<GetFDAudPowerMapParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.getFDAudPowerMap(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 设备录像重点标记
     * @param param
     * @return
     */
    @RequestMapping("/viewKeyMark")
    public RResult viewKeyMark(@RequestBody  ReqParam<ViewKeyMarkParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = equipmentService.viewKeyMark(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 提供休庭心跳
     * @param param
     * @return
     */
    @RequestMapping("/putRecessStatus")
    public RResult putRecessStatus(@RequestBody  ReqParam<RecordStatusCacheParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            equipmentService.putRecessStatus(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


}
