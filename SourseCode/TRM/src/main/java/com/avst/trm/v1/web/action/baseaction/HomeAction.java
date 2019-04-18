package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/publicweb/home")
public class HomeAction extends BaseAction{


    @GetMapping(value = "/{pageid}")
    public ModelAndView gotomain(Model model,@PathVariable("pageid")String pageid) {

        return new ModelAndView(pageid, "main", model);
    }


    @GetMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        String type=CommonCache.getCurrentServerType();

        //根据type跳service处理

        //获取统计数据信息

        model.addAttribute("title", "智能提讯管理系统");
        return new ModelAndView("police/main", "main", model);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = "/home")
    public ModelAndView getHome(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "首页");
        return new ModelAndView("police/home", "homeModel", model);

    }


    @GetMapping(value = "/login")
    public ModelAndView gotologin(Model model, HttpServletRequest request) {

        model.addAttribute("title", "layui测试主页");

        request.getSession().setAttribute(Constant.INIT_WEB,CommonCache.getinit_WEB());

        return new ModelAndView("/police/login", "login", model);


    }


    @GetMapping(value = "/error")
    public ModelAndView getError(Model model) {

        model.addAttribute("title", "错误页面404");

        return new ModelAndView("police/404", "error", model);


    }


}
