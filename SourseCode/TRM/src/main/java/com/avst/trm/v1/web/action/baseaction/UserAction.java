package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.policeservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/user")
public class UserAction extends BaseAction{

    @Autowired
    private UserService userService;

    /**
     * 用户列表
     * @param model
     * @return
     */
    @GetMapping(value = "/getUser")
    public ModelAndView getUser(Model model, Getlist3Param param) {

        RResult rResult=createNewResultOfFail();

        param.setPageSize(3);//测试
        userService.getadminlist3(rResult,param);

        model.addAttribute("result", rResult);

        model.addAttribute("title", "用户列表");
        return new ModelAndView("police/users/getUserList", "userModel", model);

    }

    /***
     * 用户列表分页
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public RResult getUserList(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        userService.getadminlist3(rResult,param);
        return rResult;
    }


    /***
     * 添加用户
     * @param model
     * @return
     */
    @GetMapping(value = "/getAddUser")
    public ModelAndView getAddUser(Model model) {

        RResult rResult=createNewResultOfFail();

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "添加用户");
        return new ModelAndView("police/users/addOrUpdateUser", "userModel", model);

    }

    /**
     * 修改用户
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateUser/{id}")
    public ModelAndView getUpdateUser(Model model, @PathVariable("id") int id) {

        RResult rResult=createNewResultOfFail();

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改用户" + id);
        return new ModelAndView("police/users/addOrUpdateUser", "userModel", model);

    }




}
