package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.req.GetMCAsrTxtBackParam_out;
import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/police/out")
public class OutAction extends BaseAction {
   @Autowired
    private OutService outService;

    @RequestMapping("/startRercord")
    public RResult startRercord(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            result=  outService.startRercord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    @RequestMapping("/overRercord")
    public  RResult overRercord(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            result= outService.overRercord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/setRercordAsrTxtBack")
    public  RResult setRercordAsrTxtBack(@RequestBody ReqParam<AsrTxtParam_toout> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            outService.setRercordAsrTxtBack(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }
}
