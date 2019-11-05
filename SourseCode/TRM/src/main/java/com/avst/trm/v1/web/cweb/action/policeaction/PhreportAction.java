package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.DelPhreportParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetPhreportsParam;
import com.avst.trm.v1.web.cweb.req.policereq.UploadPhreportParam;
import com.avst.trm.v1.web.cweb.service.policeservice.PhreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于情绪报告
 */
@RestController
@RequestMapping("/cweb/police/phreport")
public class PhreportAction extends BaseAction {

    @Autowired
    private PhreportService phreportService;

    /**
     * 获取情绪报告列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPhreports")
    public RResult getPhreports(@RequestBody ReqParam<GetPhreportsParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            phreportService.getPhreports(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 上传报告
     * @param param
     * @return
     */
    @RequestMapping(value = "/uploadPhreport")
    public RResult uploadPhreport(@RequestBody ReqParam<UploadPhreportParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            phreportService.uploadPhreport(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 删除报告
     * @param param
     * @return
     */
    @RequestMapping(value = "/delPhreport")
    public RResult delPhreport(@RequestBody ReqParam<DelPhreportParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            phreportService.delPhreport(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }
}
