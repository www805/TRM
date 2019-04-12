package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.service.CeshiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/web/ceshi")
public class CeshiAction extends BaseAction{

    @Autowired
    private CeshiService ceshiService;

    @GetMapping(value = "/ceshi")
    public RResult getlist(String username) {
        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist(rResult,username);
        return rResult;
    }

    @GetMapping(value = "/ceshi2")
    public RResult getlist2(int size) {
        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist2(rResult,size);
        return rResult;
    }

    @GetMapping(value = "/ceshi3")
    public ModelAndView getlist3(Model model) {
        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "用户管理");
        model.addAttribute("title2", "用户管理");
        return new ModelAndView("users/list", "userModel", model);

    }

    @GetMapping(value = "/users/{id}")
    public ModelAndView getuserbyid(Model model,@PathVariable("id")String id) {

        model.addAttribute("title", "第"+id+"个人");
        return new ModelAndView("users/view", "userModel", model);

    }

    @GetMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("main", "main", model);

    }


    @GetMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("login1", "login", model);

    }

    /**
     * 测试提交ppdsafhjdsyhfjhhjhdjfgsdf
     */

}
