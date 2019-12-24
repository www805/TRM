package com.avst.trm.v1.web.sweb.action.courtaction;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
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
@RequestMapping("/sweb/court/courtPage")
public class CourtPageAction {

    @GetMapping("touserinfograde")
    public ModelAndView touserinfograde(Model model, HttpSession session){
        Gson gson=new Gson();
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_WEB)), AdminAndWorkunit.class);
        if (null!=user&&user.getSuperrolebool()==1){
            model.addAttribute("title","人员类型级别查看");
            return new ModelAndView("client_web/court/userinfogradeIndex", "Model", model);
        }else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }







}
