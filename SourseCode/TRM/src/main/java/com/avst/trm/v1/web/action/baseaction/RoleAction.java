package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.policeservice.RoleService;
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
@RequestMapping("/web/role")
public class RoleAction extends BaseAction{

    @Autowired
    private RoleService roleService;

    /***
     * 跳转用户列表页
     * @param model
     * @param param
     * @return
     */
    @GetMapping(value = "/getRole")
    public ModelAndView getUser(Model model, Getlist3Param param) {
        model.addAttribute("title", "角色列表");
        return new ModelAndView("police/role/getRoleList", "roleModel", model);

    }

    /***
     * 角色列表分页
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
     * 添加角色
     * @param model
     * @return
     */
    @GetMapping(value = "/getAddRole")
    public ModelAndView getAddRole(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

//        List<Base_role> base_roles = base_roleMapper.selectList(null);
//
//        rResult.setData(base_roles);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "添加角色");
        return new ModelAndView("police/role/addOrUpdateRole", "roleModel", model);
    }

    /***
     * 修改角色
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateRole/{id}")
    public ModelAndView getUpdateRole(Model model, @PathVariable("id") int id) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);


        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改角色");
        return new ModelAndView("police/role/addOrUpdateRole", "roleModel", model);
    }



}
