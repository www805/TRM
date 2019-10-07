package com.avst.trm.v1.web.standaloneweb.action;


import com.avst.trm.v1.common.util.baseaction.BaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cweb/police/home")
public class HomeAction extends BaseAction {


    /**
     * 跳转==》主页页
     */
    @RequestMapping(value = "/gotohomepage")
    public ModelAndView gotohome(Model model){
        model.addAttribute("title","智能提讯系统");
        return  new ModelAndView("standalone_web/homepage","Model", model);
    }
}
