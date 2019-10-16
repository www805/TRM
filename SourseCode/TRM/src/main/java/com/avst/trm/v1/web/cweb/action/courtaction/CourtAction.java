package com.avst.trm.v1.web.cweb.action.courtaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.courtreq.AddCaseToUserParam;
import com.avst.trm.v1.web.cweb.req.policereq.AddCaseToArraignmentParam;
import com.avst.trm.v1.web.cweb.service.courtService.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/cweb/court/court")
public class CourtAction extends BaseAction {
    @Autowired
    private CourtService courtService;

    @RequestMapping(value = "/addCaseToUser")
    public RResult addCaseToUser(@RequestBody ReqParam<AddCaseToUserParam> param, HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{

            courtService.addCaseToUser(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }





}
