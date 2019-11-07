package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.AddOrUpdateWorkunitParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetworkunitListParam;
import com.avst.trm.v1.web.sweb.service.policeservice.WorkunitService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sweb/base/workunit")
public class WorkunitAction extends BaseAction {
    @Autowired
    private WorkunitService workunitService;

    /**
     * 跳转工作单位列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/toworkunit")
    public ModelAndView toworkunit(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("toworkunit")) {
            model.addAttribute("title","工作单位");
            return new ModelAndView("server_web/base/workunit/workunit", "workunitModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }


    /***
     * 工作单位列表分页
     * @return
     */
    @RequestMapping(value = "/getworkunitList")
    @ResponseBody
    public RResult getworkunitList(GetworkunitListParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getworkunitList")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                workunitService.getworkunitList(result,param);
            }
        }else{
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 工作单位添加
     * @return
     */
    @RequestMapping(value = "/addWorkunit")
    @ResponseBody
    public RResult addWorkunit(AddOrUpdateWorkunitParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("addWorkunit")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                workunitService.addWorkunit(result,param);
            }
        }else{
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /***
     * 工作单位修改
     * @return
     */
    @RequestMapping(value = "/updateWorkunit")
    @ResponseBody
    public RResult updateWorkunit(AddOrUpdateWorkunitParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateWorkunit")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                workunitService.updateWorkunit(result,param);
            }
        }else{
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


}


