package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphAnalysisParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphRealTimeImageParam;
import com.avst.trm.v1.feignclient.ec.req.tts.Str2TtsParam;
import com.avst.trm.v1.feignclient.mc.req.*;
import com.avst.trm.v1.feignclient.mc.vo.SetMCAsrTxtBackVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.*;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/***
 * 对接外部接口
 */
@RestController
@RequestMapping("/v1/police/out")
public class OutAction extends BaseAction {
    @Autowired
    private OutService outService;

    @Autowired
    private EquipmentControl equipmentControl;




//-----------------------------------------------mc start 分割线----------------------------------------

    /**
     * 开始会议
     * @return
     */
    @RequestMapping("/ceshi")
    public RResult ceshi(int type) {
        RResult result = this.createNewResultOfFail();

        if(type==1){
            ReqParam<CheckPolygraphStateParam> param=new ReqParam<CheckPolygraphStateParam>();
            CheckPolygraphStateParam checkPolygraphStateParam=new CheckPolygraphStateParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            LogUtil.intoLog(this.getClass(),JacksonUtil.objebtToString(equipmentControl.checkPolygraphState(param)));
        }else if(type==2){
            ReqParam<GetPolygraphAnalysisParam> param=new ReqParam<GetPolygraphAnalysisParam>();
            GetPolygraphAnalysisParam checkPolygraphStateParam=new GetPolygraphAnalysisParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            LogUtil.intoLog(this.getClass(),JacksonUtil.objebtToString(equipmentControl.getPolygraphAnalysis(param)));

        }else if (type==3){
            ReqParam<GetPolygraphRealTimeImageParam> param=new ReqParam<GetPolygraphRealTimeImageParam>();
            GetPolygraphRealTimeImageParam checkPolygraphStateParam=new GetPolygraphRealTimeImageParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            LogUtil.intoLog(this.getClass(),JacksonUtil.objebtToString(equipmentControl.getPolygraphRealTimeImage(param)));

        }


        return result;
    }


    /**
     * 开始会议
     * @param param
     * @return
     */
    @RequestMapping("/startRercord")
    public RResult startRercord(@RequestBody ReqParam<StartRercordParam> param,HttpSession session) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.startRercord(result, param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 会议暂停或者继续
     * @param param
     * @return
     */
    @RequestMapping("/pauseOrContinueRercord")
    public RResult pauseOrContinueRercord(@RequestBody ReqParam<PauseOrContinueRercordParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
           outService.pauseOrContinueRercord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 结束会议
     * @param param
     * @return
     */
    @RequestMapping("/overRercord")
    public RResult overRercord(@RequestBody ReqParam<OverMCParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.overRercord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 会议语音识别返回
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("/outtRercordAsrTxtBack")
    public boolean setRercordAsrTxtBack(@RequestBody ReqParam<SetMCAsrTxtBackVO> param, HttpSession session) {
        if (null == param) {
            LogUtil.intoLog(this.getClass(),"参数为空");
        } else {
            return outService.setRercordAsrTxtBack(param, session);
        }
        return false;
    }


    /**
     * 会议asr实时回放sql，
     * @param param
     * @return
     */
    @RequestMapping("/getRecord")
    public RResult getRecord(@RequestBody ReqParam<GetPhssidByMTssidParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     *会议实时回放进行中
     * @param param
     * @return
     */
    @RequestMapping("/getRecordrealing")
    public RResult getRecordrealing(@RequestBody ReqParam<GetMCaLLUserAsrTxtListParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecordrealing(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 获取会议状态
     * @param param
     * @return
     */
    @RequestMapping("/getRecordState")
    public RResult getRecordState(@RequestBody ReqParam<GetMCStateParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecordState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    //-----------------------------------------------mc end 分割线----------------------------------------



    //-----------------------------------------------ec start 分割线--------------------------------------

    /**
     * 检测播放状态 ：暂不使用
     * @param
     * @return
     */
    @RequestMapping("/checkPlayFileState")
    public RResult checkPlayFileState(@RequestBody CheckRecordFileStateParam param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
             outService.checkPlayFileState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取播放地址
     * @param param
     * @return
     */
    @RequestMapping("/getPlayUrl")
    public RResult getPlayUrl(@RequestBody GetURLToPlayParam param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getPlayUrl(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * //获取测谎心里分析数据 缓存
     * @param param
     * @return
     */
    @RequestMapping("/getPolygraphdata")
    public RResult getPolygraphdata(@RequestBody  ReqParam<GetPolygraphdataParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getPolygraphdata(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取测谎心里分析数据 sql
     * @param param
     * @return
     */
    @RequestMapping("/getPHDataBack")
    public RResult getPHDataBack(@RequestBody  ReqParam<GetPHDataBackParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getPHDataBack(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    @RequestMapping("/str2Tts")
    public RResult str2Tts(@RequestBody  ReqParam<Str2TtsParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.str2Tts(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/getMc_model")
    public RResult getMc_model(@RequestBody  ReqParam<GetMc_modelParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getMc_model(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/getTdByModelSsid")
    public RResult getTdByModelSsid(@RequestBody  ReqParam<GetTdByModelSsidParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getTdByModelSsid(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     *获取会议缓存
     * @param param
     * @return
     */
    @RequestMapping("/getMCCacheParamByMTssid")
    public RResult getMCCacheParamByMTssid(@RequestBody  ReqParam<GetMCCacheParamByMTssidParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getMCCacheParamByMTssid(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping("/getTDCacheParamByMTssid")
    public RResult getTDCacheParamByMTssid(@RequestBody  ReqParam<GetTDCacheParamByMTssidParam_out> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getTDCacheParamByMTssid(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }





    /**
     * 获取各个设备的状态
     * @param param
     * @return
     */
    @RequestMapping("/getEquipmentsState")
    public RResult getEquipmentsState(@RequestBody  ReqParam<GetEquipmentsStateParam> param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getEquipmentsState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取各个客户端的状态:未使用==
     * @param param
     * @return
     */
    @RequestMapping("/getClient")
    public RResult getClient(@RequestBody  ReqParam param){
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getClient(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }




}
