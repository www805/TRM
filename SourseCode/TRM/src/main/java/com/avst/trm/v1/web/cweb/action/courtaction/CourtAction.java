package com.avst.trm.v1.web.cweb.action.courtaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.courtreq.AddUserinfogradeParam;
import com.avst.trm.v1.web.cweb.req.courtreq.GetUserinfogradeByssidParam;
import com.avst.trm.v1.web.cweb.req.courtreq.GetUserinfogradePageParam;
import com.avst.trm.v1.web.cweb.req.courtreq.UpdateUserinfogradeParam;
import com.avst.trm.v1.web.cweb.service.courtService.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/cweb/court/court")
public class CourtAction extends BaseAction {
    @Autowired
    private CourtService courtService;


    /**
     * 获取人员级别类型page
     * @param param
     * @param
     * @return
     */
    @RequestMapping(value = "/getUserinfogradePage")
    public RResult getUserinfogradePage(@RequestBody ReqParam<GetUserinfogradePageParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.getUserinfogradePage(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/getUserinfogradeByssid")
    public RResult getUserinfogradeByssid(@RequestBody ReqParam<GetUserinfogradeByssidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.getUserinfogradeByssid(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/addUserinfograde")
    public RResult addUserinfograde(@RequestBody ReqParam<AddUserinfogradeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.addUserinfograde(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/updateUserinfograde")
    public RResult updateUserinfograde(@RequestBody ReqParam<UpdateUserinfogradeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.updateUserinfograde(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }





}
