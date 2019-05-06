package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.ChangeboolUserParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.sweb.service.baseservice.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
     * 跳转用户列表页pp
     * @param model
     * @return
     */
    @RequiresPermissions("getUser")
    @GetMapping(value = "/getUser")
    public ModelAndView getUser(Model model) {
        model.addAttribute("title","管理员列表");
        return new ModelAndView("server_web/police/users/getUserList", "userModel", model);
    }

    /***
     * 用户列表分页pp
     * @return
     */
    @RequiresPermissions("getUserList")
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


    /**
     * 删除用户：改变用户状态pp
     * @param param
     * @return
     */
    @RequiresPermissions("deleteUser")
    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public RResult deleteUser(ChangeboolUserParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            userService.changeboolUser(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部单位pp
     * @return
     */
    @RequiresPermissions("getWorkunits")
    @RequestMapping(value = "/getWorkunits")
    @ResponseBody
    public RResult getWorkunits(){
        RResult result=createNewResultOfFail();
        userService.getWorkunits(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 跳转到添加或者修改页面pp
     * @param model
     * @return
     */
    @RequiresPermissions("getAddOrUpdateUser")
    @GetMapping(value = "/getAddOrUpdateUser")
    public ModelAndView getAddOrUpdateUser(Model model,String ssid) {
        model.addAttribute("ssid", ssid);
        model.addAttribute("title", "添加/修改用户");
        return new ModelAndView("server_web/police/users/AddOrUpdateUser", "userModel", model);
    }

    /**
     * 查询单个pp
     * @param ssid
     * @return
     */
    @RequiresPermissions("getUserBySsid")
    @RequestMapping(value = "/getUserBySsid")
    @ResponseBody
    public RResult getUserBySsid(String ssid){
        RResult result=createNewResultOfFail();
        if (null==ssid){
            result.setMessage("参数为空");
        }else{
            userService.getUserBySsid(result,ssid);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加用户pp
     * @return
     */
    @RequiresPermissions("addUser")
    @RequestMapping(value = "/addUser")
    @ResponseBody
    public RResult addUser(@RequestBody AdminAndWorkunit param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            userService.addUser(result,param);
        }

        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /***
     * 修改用户pp
     * @return
     */
    @RequiresPermissions("updateUser")
    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public RResult updateUser(@RequestBody  AdminAndWorkunit param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            userService.updateUser(result,param);
        }

        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 用户状态修改
     * @param param
     * @return
     */
    @RequiresPermissions(value = "changeboolUser")
    @RequestMapping(value = "/changeboolUser")
    @ResponseBody
    public RResult changeboolUser(ChangeboolUserParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            userService.changeboolUser(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }













}
