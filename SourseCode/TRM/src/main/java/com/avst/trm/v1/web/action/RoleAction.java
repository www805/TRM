package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleAction extends BaseAction{

    @Autowired
    private Base_roleMapper base_roleMapper;

    /***
     * 角色列表
     * @param model
     * @return
     */
    @GetMapping(value = "/getRoleList")
    public ModelAndView getRoleList(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        List<Base_role> base_roles = base_roleMapper.selectList(null);

        rResult.setData(base_roles);

        model.addAttribute("base_roles", base_roles);


        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "角色列表");
        return new ModelAndView("role/getRoleList", "roleModel", model);

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

        List<Base_role> base_roles = base_roleMapper.selectList(null);

        rResult.setData(base_roles);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "添加角色");
        return new ModelAndView("role/addOrUpdateRole", "roleModel", model);
    }

    /***
     * 修改角色
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateRole")
    public ModelAndView getUpdateRole(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        List<Base_role> base_roles = base_roleMapper.selectList(null);

        rResult.setData(base_roles);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改角色");
        return new ModelAndView("role/addOrUpdateRole", "roleModel", model);
    }



}
