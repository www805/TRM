package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction{


    /***
     * 用户列表
     * @param model
     * @return
     */
    @GetMapping(value = "/getUserList")
    public ModelAndView getUserList(Model model) {

        RResult rResult=createNewResultOfFail();

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "用户列表");
        return new ModelAndView("users/getUserList", "userModel", model);

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
        return new ModelAndView("users/addOrUpdateUser", "userModel", model);

    }

    /**
     * 修改用户
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateUser")
    public ModelAndView getUpdateUser(Model model) {

        RResult rResult=createNewResultOfFail();

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改用户");
        return new ModelAndView("users/addOrUpdateUser", "userModel", model);

    }




}
