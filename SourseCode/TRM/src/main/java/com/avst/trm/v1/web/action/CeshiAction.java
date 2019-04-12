package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
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

import java.util.List;

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
        return new ModelAndView("users/list", "userModel", model);

    }

    @GetMapping(value = "/users/{id}")
    public ModelAndView getuserbyid(Model model,@PathVariable("id")String id) {

        model.addAttribute("title", "第"+id+"个人");
        return new ModelAndView("role", "userModel", model);

    }

    @GetMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("main", "main", model);

    }

    /***
     * 用户列表
     * @param model
     * @return
     */
    @GetMapping(value = "/getUserList")
    public ModelAndView getUserList(Model model) {

        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "用户列表");
        return new ModelAndView("users/list", "userModel", model);

    }

    /***
     * 添加用户
     * @param model
     * @return
     */
    @GetMapping(value = "/getAddUser")
    public ModelAndView getAddUser(Model model) {

        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist3(rResult);

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
        ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改用户");
        return new ModelAndView("users/addOrUpdateUser", "userModel", model);

    }

    @Autowired
    private Base_roleMapper base_roleMapper;

    /***
     * 角色列表
     * @param model
     * @return
     */
    @GetMapping(value = "/getUserRole")
    public ModelAndView getUserRole(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        List<Base_role> base_roles = base_roleMapper.selectList(null);

        rResult.setData(base_roles);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "角色列表");
        return new ModelAndView("users/role", "roleModel", model);

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
        return new ModelAndView("users/addOrUpdateRole", "roleModel", model);
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
        return new ModelAndView("users/addOrUpdateRole", "roleModel", model);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = "/home")
    public ModelAndView getHome(Model model) {

        RResult rResult=createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "首页");
        return new ModelAndView("home", "homeModel", model);

    }


    /**
     * 系统配置
     * @param model
     * @return
     */
    @GetMapping(value = "/getServerConfig")
    public ModelAndView getServerConfig(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "系统配置");
        return new ModelAndView("serverconfig", "configModel", model);

    }

    /**
     *提讯笔录
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignment")
    public ModelAndView getArraignment(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "提讯笔录");
        return new ModelAndView("arraignment", "arraignmentModel", model);

    }

    /***
     * 笔录统计
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignment_count")
    public ModelAndView getArraignment_count(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "笔录使用情况统计表");
        return new ModelAndView("arraignment_count", "arraignment_countModel", model);
    }

    /**
     * 关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getKeyword")
    public ModelAndView getKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "关键字");
        return new ModelAndView("keyword", "keywordModel", model);
    }

    /**
     * 添加关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getAddKeyword")
    public ModelAndView getAddKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "添加关键字");
        return new ModelAndView("addOrUpdateKeyword", "keywordModel", model);
    }

    /***
     * 修改关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateKeyword")
    public ModelAndView getUpdateKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改关键字");
        return new ModelAndView("addOrUpdateKeyword", "keywordModel", model);
    }

    @GetMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("login1", "login", model);


    }

}
