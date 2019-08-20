package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.UpdateRoleToPermissionsParam;
import com.avst.trm.v1.web.sweb.service.baseservice.PermissionsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sweb/base/permissions")
public class PermissionsAction extends BaseAction {
    @Autowired
    private PermissionsService permissionsService;

    /**
     * 跳转角色权限设置页
     * @param model
     * @return
     */
    @RequestMapping(value = "/topermissionsShow")
    public ModelAndView getUser(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("topermissionsShow")) {
            model.addAttribute("title","设置角色权限");
            return new ModelAndView("server_web/base/permissions/permissionsShow", "permissionsModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     *获取全部权限
     * @return
     */
    @RequestMapping(value = "/getPermissions")
    @ResponseBody
    public RResult getPermissions() {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getPermissions")) {
            permissionsService.getPermissions(result);
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 根据角色ssid角色权限
     * @return
     */
    @RequestMapping(value = "/getPermissionsByRoleSsid")
    @ResponseBody
    public RResult getPermissionsByRoleSsid(String rolessid) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getPermissionsByRoleSsid")) {
            if (null==rolessid){
                result.setMessage("参数为空");
            }else{
                permissionsService.getPermissionsByRoleSsid(result,rolessid);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     *设置角色权限
     * @return
     */
    @RequestMapping(value = "/updateRoleToPermissions")
    @ResponseBody
    public RResult updateRoleToPermissions(@RequestBody  UpdateRoleToPermissionsParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateRoleToPermissions")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                permissionsService.updateRoleToPermissions(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取菜单:暂未实现
     * @return
     */
    @RequestMapping(value = "/getPermissionsByMenu")
    @ResponseBody
    public RResult getPermissionsByMenu() {
        RResult result=createNewResultOfFail();
        permissionsService.getPermissionsByMenu(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }





}
