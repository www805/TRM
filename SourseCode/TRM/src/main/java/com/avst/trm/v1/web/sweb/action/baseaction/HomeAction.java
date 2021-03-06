package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.cache.AppServerCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.LoginParam;
import com.avst.trm.v1.web.sweb.service.policeservice.HomeService;
import com.avst.trm.v1.web.sweb.service.policeservice.LoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/sweb/base/home")
public class HomeAction extends BaseAction{

    @Resource
    private LoginService loginService;

    @Autowired
    private HomeService homeService;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @RequestMapping(value = "/{pageid}")
    public ModelAndView gotomain(Model model,@PathVariable("pageid")String pageid) {

        return new ModelAndView(pageid, "server_web/base/main", model);
    }


    @RequestMapping(value = "/main")
    public ModelAndView gotomain(Model model) {
        String type=CommonCache.getCurrentServerType();

        AppCacheParam param = AppServerCache.getAppServerCache();
        if(StringUtils.isBlank(param.getTitle())){
            this.getNavList();
            param = AppServerCache.getAppServerCache();
            Base_serverconfig base_serverconfig = base_serverconfigMapper.selectById(1);
            if (null != base_serverconfig) {
                param.setTitle(base_serverconfig.getSysname());
            }
        }
        Map<String, Object> data = param.getData();
        model.addAttribute("homeurl",(String) data.get("home-url"));//这里key的下划线可能需要改一下
        model.addAttribute("title",param.getTitle());
        model.addAttribute("guidepageUrl",param.getGuidepageUrl());
        return new ModelAndView("server_web/base/main", "main", model);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping(value = "/home")
    public ModelAndView getHome(Model model) {
        RResult rResult=createNewResultOfFail();
        homeService.getAllCount(rResult, model);
        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "首页");
        return new ModelAndView("server_web/base/home", "homeModel", model);
    }

    /**
     * 用户登录页面
     * @param model
     * @param request
     * @param loginParam
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView gotologin(Model model, HttpServletRequest request, LoginParam loginParam) {
        RResult rResult=createNewResultOfFail();

        AppCacheParam param = AppServerCache.getAppServerCache();
        if(StringUtils.isBlank(param.getTitle()) || StringUtils.isBlank(param.getGuidepageUrl())){
            this.getNavList();
            param = AppServerCache.getAppServerCache();
            Base_serverconfig base_serverconfig = base_serverconfigMapper.selectById(1);
            if (null != base_serverconfig) {
                param.setTitle(base_serverconfig.getSysname());
            }
        }

        model.addAttribute("result", rResult);
        model.addAttribute("guidepageUrl",  param.getGuidepageUrl());
        model.addAttribute("title", "欢迎登录后台" + param.getTitle());//欢迎登录后台管理界面平台

        request.getSession().setAttribute(Constant.INIT_WEB,CommonCache.getinit_WEB());
        return new ModelAndView("server_web/base/login", "login", model);
    }

    @PostMapping(value = "/checklogin")
    @ResponseBody
    public RResult checklogin(Model model, HttpServletRequest request, HttpServletResponse response, LoginParam loginParam) {
        RResult result=createNewResultOfFail();
        loginService.gotologin(result,request,response,loginParam);
        AppServerCache.delAppServerCache();//清空logo导航栏缓存
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public RResult logout(Model model,HttpServletRequest request) {
        RResult rResult=createNewResultOfFail();
        loginService.logout(rResult, request);
        return rResult;
    }

    /**
     * 获取导航栏目
     * @return
     */
    @RequestMapping("/getNavList")
    @ResponseBody
    public  RResult getNavList(){
        RResult result=this.createNewResultOfFail();
        homeService.getNavList(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 心跳
     * @return
     */
    @RequestMapping("/getPant")
    @ResponseBody
    public  RResult getPant(){
        RResult result=this.createNewResultOfFail();
        result.setData("心跳正常");
        changeResultToSuccess(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/404")
    public ModelAndView getError(Model model) {
        model.addAttribute("title", "错误页面404");
        return new ModelAndView("server_web/404", "error", model);
    }

    //没有权限
    @RequestMapping(value = "/unauth")
    public ModelAndView getounauth(Model model) {
        return new ModelAndView("error/unauth", "unauth", model);
    }



}
