package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.LoginParam;
import com.avst.trm.v1.web.sweb.service.policeservice.HomeService;
import com.avst.trm.v1.web.sweb.service.policeservice.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/publicweb/home")
public class HomeAction extends BaseAction{

    @Resource
    private LoginService loginService;

    @Autowired
    private HomeService homeService;

    @GetMapping(value = "/{pageid}")
    public ModelAndView gotomain(Model model,@PathVariable("pageid")String pageid) {

        return new ModelAndView(pageid, "server_web/main", model);
    }


    @GetMapping(value = "/main")
    public ModelAndView gotomain(Model model) {

        String type=CommonCache.getCurrentServerType();

        //根据type跳service处理

        //获取统计数据信息

        model.addAttribute("title", "智能提讯管理系统");
        return new ModelAndView("server_web/main", "main", model);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = "/home")
    public ModelAndView getHome(Model model) {
        RResult rResult=createNewResultOfFail();
        homeService.getAllCount(rResult, model);
        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "首页");
        return new ModelAndView("server_web/police/home", "homeModel", model);

    }

    /**
     * 用户登录页面
     * @param model
     * @param request
     * @param loginParam
     * @return
     */
    @GetMapping(value = "/login")
    public ModelAndView gotologin(Model model, HttpServletRequest request, LoginParam loginParam) {
        RResult rResult=createNewResultOfFail();

        model.addAttribute("result", rResult);
        model.addAttribute("title", "欢迎登录后台管理界面平台");

        request.getSession().setAttribute(Constant.INIT_WEB,CommonCache.getinit_WEB());
//        request.getSession().setAttribute(Constant.MANAGE_WEB,"123");

        return new ModelAndView("server_web/police/login", "login", model);
    }

    @PostMapping(value = "/checklogin")
    @ResponseBody
    public RResult checklogin(Model model, HttpServletRequest request, LoginParam loginParam) {
        RResult result=createNewResultOfFail();
        loginService.gotologin(result,request,loginParam);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public RResult logout(Model model,HttpServletRequest request) {
        RResult rResult=createNewResultOfFail();
        this.changeResultToSuccess(rResult);
        rResult.setMessage("退出成功");
        request.getSession().setAttribute(Constant.MANAGE_WEB,null);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return rResult;
    }

    @GetMapping(value = "/404")
    public ModelAndView getError(Model model) {
        model.addAttribute("title", "错误页面404");
        return new ModelAndView("server_web/404", "error", model);
    }


}
