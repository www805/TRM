package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.baseservice.UserService;
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
     * 跳转用户列表页
     * @param model
     * @return
     */
    @GetMapping(value = "/getUser")
    public ModelAndView getUser(Model model) {
        model.addAttribute("title","管理员列表");
        return new ModelAndView("police/users/getUserList", "userModel", model);
    }

    /***
     * 用户列表分页
     * @return
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public RResult getUserList(GetUserListParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            userService.getUserList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
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
     * 跳转到修改页面
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateUser/{id}")
    public ModelAndView getUpdateUser(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("id",id);
        return new ModelAndView("police/users/addOrUpdateUser", "userModel", model);

    }




}
