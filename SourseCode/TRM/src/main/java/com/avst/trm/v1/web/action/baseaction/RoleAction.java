package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.ChangeboolRoleParam;
import com.avst.trm.v1.web.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.baseservice.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/role")
public class RoleAction extends BaseAction{

    @Autowired
    private RoleService roleService;

    /***
     * 跳转用户列表页pp
     * @param model
     * @return
     */
    @GetMapping(value = "/getRole")
    public ModelAndView getUser(Model model) {
        model.addAttribute("title", "角色列表");
        return new ModelAndView("police/role/getRoleList", "roleModel", model);

    }

    /***
     * 角色列表分页pp
     * @return
     */
    @RequestMapping(value = "/getRoleList")
    @ResponseBody
    public RResult getRoleList(GetRoleListParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            roleService.getRoleList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部角色pp
     * @return
     */
    @RequestMapping(value = "/getRoles")
    @ResponseBody
    public RResult getRoles() {
        RResult result=createNewResultOfFail();
        roleService.getRoles(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 删除角色：改变状态pp
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteRole")
    @ResponseBody
    public RResult deleteRole(ChangeboolRoleParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            roleService.changeboolRole(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 跳转到添加或者修改页面pp
     * @param model
     * @return
     */
    @GetMapping(value = "/getAddOrUpdateRole")
    public ModelAndView getAddOrUpdateRole(Model model,String ssid) {
        model.addAttribute("ssid", ssid);
        model.addAttribute("title", "添加/修改角色");
        return new ModelAndView("police/role/addOrUpdateRole", "roleModel", model);
    }

    /**
     * 查询单个pp
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getRoleBySsid")
    @ResponseBody
    public RResult getRoleBySsid(String ssid){
        RResult result=createNewResultOfFail();
        if (null==ssid){
            result.setMessage("参数为空");
        }else{
            roleService.getRoleBySsid(result,ssid);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 添加角色pp
     * @return
     */
    @RequestMapping(value = "/addRole")
    @ResponseBody
    public RResult addRole(Base_role param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            roleService.addRole(result,param);
        }

        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /***
     * 修改角色pp
     * @return
     */
    @RequestMapping(value = "/updateRole")
    @ResponseBody
    public RResult updateRole(Base_role param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            roleService.updateRole(result,param);
        }

        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }







}
