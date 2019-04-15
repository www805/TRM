package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.service.CeshiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeAction extends BaseAction{

    @GetMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        model.addAttribute("title", "智能提讯管理系统");
        return new ModelAndView("main", "main", model);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public ModelAndView getHome(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "首页");
        return new ModelAndView("home", "homeModel", model);

    }


    /**
     * 系统配置
     * @param model
     * @return
     */
    @GetMapping(value = "/getServerConfig")
    public ModelAndView getServerConfig(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "系统配置");
        return new ModelAndView("serverconfig", "configModel", model);

    }


    @GetMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("login1", "login", model);


    }

}
