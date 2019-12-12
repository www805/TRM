package com.avst.trm.v1.web.cweb.action.courtaction;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 跳转页面
 */
@Controller
@RequestMapping("/cweb/court/courtPage")
public class CourtPageAction {

    @GetMapping("towaitCourt")
    public ModelAndView towaitCourt(Model model, String ssid){
        model.addAttribute("title","笔录制作");
        model.addAttribute("recordssid",ssid);
        return new ModelAndView("client_web/court/waitCourt", "Model", model);
    }

    @GetMapping("togetCourtDetail")
    public ModelAndView togetCourtDetails(Model model, String ssid){
        model.addAttribute("title","笔录查看");
        model.addAttribute("recordssid",ssid);
        return new ModelAndView("client_web/court/getCourtDetail", "Model", model);
    }

    @GetMapping("tobeforeCourt")
    public ModelAndView toaddCaseToUser(Model model){
        model.addAttribute("title","庭审办案");
        return new ModelAndView("client_web/court/beforeCourt", "Model", model);
    }

    @GetMapping("touserinfograde")
    public ModelAndView touserinfograde(Model model, HttpSession session){
        Gson gson=new Gson();
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
        if (null!=user&&user.getSuperrolebool()==1){
            model.addAttribute("title","人员类型级别查看");
            return new ModelAndView("client_web/court/userinfogradeIndex", "Model", model);
        }else {
            return new ModelAndView("redirect:/cweb/base/main/unauth");
        }
    }

    @GetMapping("toAddOrUpdateUserinfograde")
    public ModelAndView toAddOrUpdateUserinfograde(Model model,String ssid){
        model.addAttribute("title","人员类型级别编辑");
        model.addAttribute("ssid",ssid);
        return new ModelAndView("client_web/court/AddOrUpdateUserinfograde", "Model", model);
    }
    @GetMapping("toquickCourt")
    public ModelAndView toquickCourt(Model model,String indexbool){
        AppCacheParam param = AppCache.getAppCacheParam();
        if(StringUtils.isBlank(param.getGuidepageUrl())){
            param = AppCache.getAppCacheParam();
        }
        model.addAttribute("guidepageUrl",  param.getGuidepageUrl());
        model.addAttribute("title","庭审办案");
        model.addAttribute("indexbool", indexbool);//是否为首页1
        return new ModelAndView("client_web/court/quickCourt", "Model", model);
    }






}
