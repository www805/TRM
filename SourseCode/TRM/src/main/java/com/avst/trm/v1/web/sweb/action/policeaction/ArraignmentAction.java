package com.avst.trm.v1.web.sweb.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentListParam;
import com.avst.trm.v1.web.sweb.service.policeservice.ArraignmentService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sweb/police/arraignment")
public class ArraignmentAction extends BaseAction{
    @Autowired
    private ArraignmentService arraignmentService;


    /**
     * 跳转笔录提讯页面pp
     * @param model
     * @return
     */
    @RequestMapping(value = "/getArraignment")
    public ModelAndView getArraignment(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getArraignment")) {
            model.addAttribute("title", "笔录提讯");
            return new ModelAndView("server_web/police/arraignment/arraignment", "arraignmentModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     * 获取案件列表pp
     * @param param
     * @return
     */
    @RequestMapping(value = "/getArraignmentList")
    @ResponseBody
    public RResult getArraignmentList(GetArraignmentListParam param, HttpSession session) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getArraignmentList")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                arraignmentService.getArraignmentList(result,param,session);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取笔录列表
     * @return
     */
    @RequestMapping(value = "/getArraignmentByCaseSsid")
    @ResponseBody
    public RResult getArraignments(String caseSsid) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getArraignmentByCaseSsid")) {
            if (null==caseSsid){
                result.setMessage("参数为空");
            }else{
                arraignmentService.getArraignmentByCaseSsid(result,caseSsid);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 跳转笔录详情数据
     * @param model
     * @return
     */
    @RequestMapping(value = "/getArraignmentShow")
    public ModelAndView getArraignmentShow(Model model,String ssid) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getArraignmentShow")) {
            model.addAttribute("title", "笔录详情");
            model.addAttribute("ssid",ssid);
            return new ModelAndView("server_web/police/arraignment/getArraignmentShow", "arraignmentModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }

    }

    /**
     * 获取笔录详情数据:根据笔录ssid查询
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getArraignmentBySsid")
    @ResponseBody
    public RResult getArraignmentBySsid(String ssid) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getArraignmentBySsid")) {
            if (null==ssid){
                result.setMessage("参数为空");
            }else{
                arraignmentService.getArraignmentBySsid(result,ssid);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



}
