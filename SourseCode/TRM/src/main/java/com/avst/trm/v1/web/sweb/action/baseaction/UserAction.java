package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.ChangeboolUserParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.sweb.req.basereq.ResetPasswordParam;
import com.avst.trm.v1.web.sweb.service.baseservice.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sweb/base/user")
public class UserAction extends BaseAction{

    @Autowired
    private UserService userService;


    /**
     * 跳转用户列表页pp
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUser")
    public ModelAndView getUser(Model model) {
       Subject subject = SecurityUtils.getSubject();
       if(subject.isPermitted("getUser")) {
           model.addAttribute("title","用户列表");
           return new ModelAndView("server_web/base/users/getUserList", "userModel", model);
       } else {
           return new ModelAndView("redirect:/sweb/base/home/unauth");
       }
    }

    /***
     * 用户列表分页pp
     * @return
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public RResult getUserList(GetUserListParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getUserList")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.getUserList(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 删除用户：改变用户状态pp
     * @param param
     * @return
     */

    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public RResult deleteUser(ChangeboolUserParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("deleteUser")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.changeboolUser(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部单位pp
     * @return
     */
    @RequestMapping(value = "/getWorkunits")
    @ResponseBody
    public RResult getWorkunits(){
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getWorkunits")) {
            userService.getWorkunits(result);
        }else {
             result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 跳转到添加或者修改页面pp
     * @param model
     * @return
     */
    @RequestMapping(value = "/toAddOrUpdateUser")
    public ModelAndView getAddOrUpdateUser(Model model,String ssid) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("toAddOrUpdateUser")) {
            model.addAttribute("ssid", ssid);
            model.addAttribute("title", "添加/修改用户");
            return new ModelAndView("server_web/base/users/addOrUpdateUser", "userModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     * 查询单个pp
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getUserBySsid")
    @ResponseBody
    public RResult getUserBySsid(String ssid){
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getUserBySsid")) {
            if (null==ssid){
                result.setMessage("参数为空");
            }else{
                userService.getUserBySsid(result,ssid);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加用户pp
     * @return
     */
    @RequestMapping(value = "/addUser")
    @ResponseBody
    public RResult addUser(@RequestBody AdminAndWorkunit param,HttpSession session) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("addUser")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.addUser(result,param,session);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /***
     * 修改用户pp
     * @return
     */
    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public RResult updateUser(@RequestBody  AdminAndWorkunit param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateUser")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.updateUser(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 用户状态修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/changeboolUser")
    @ResponseBody
    public RResult changeboolUser(ChangeboolUserParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("changeboolUser")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.changeboolUser(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 跳转到重置密码界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/toresetPassword")
    public ModelAndView toresetPassword(Model model,String ssid) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("toresetPassword")) {
            model.addAttribute("title", "重置密码");
            model.addAttribute("ssid", ssid);
            RResult result = createNewResultOfFail(); // AddKeywordParam addKeywordParam,
            if (StringUtils.isNotBlank(ssid)){
                userService.getUserBySsid(result,ssid);
                model.addAttribute("result", result);
            }
            return new ModelAndView("server_web/base/users/resetPassword", "resetPasswordModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public RResult resetPassword(ResetPasswordParam param,@RequestParam(value="file",required=false) MultipartFile multipartfile) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("resetPassword")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                userService.resetPassword(result,param,multipartfile);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

















}
