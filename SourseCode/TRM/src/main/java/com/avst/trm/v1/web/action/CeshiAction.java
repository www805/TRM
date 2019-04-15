package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.Getlist3Param;
import com.avst.trm.v1.web.service.CeshiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/web/ceshi")
public class CeshiAction extends BaseAction{

    @Autowired
    private CeshiService ceshiService;

    @RequestMapping(value = "/ceshi")
    public RResult getlist(String username) {
        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist(rResult,username);
        return rResult;
    }

    @RequestMapping(value = "/ceshi2")
    @ResponseBody
    public RResult getlist2(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        ceshiService.getadminlist3(rResult,param);
        return rResult;
    }

    @RequestMapping(value = "/ceshi3")
    public ModelAndView getlist3(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        ceshiService.getadminlist3(rResult,param);

        model.addAttribute("result", rResult);

        return new ModelAndView("list", "userModel", model);

    }

    @RequestMapping(value = "/users/{id}")
    public ModelAndView getuserbyid(Model model,@PathVariable("id")String id) {

        model.addAttribute("title", "第"+id+"个人");
        return new ModelAndView("role", "userModel", model);

    }

    @RequestMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("main", "main", model);

    }



    @RequestMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("login1", "login", model);

    }
}
