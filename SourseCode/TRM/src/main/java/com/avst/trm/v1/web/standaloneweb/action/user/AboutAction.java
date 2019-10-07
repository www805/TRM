package com.avst.trm.v1.web.standaloneweb.action.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cweb/police/about")
public class AboutAction {


    /**
     * 跳转==》关于
     */
    @GetMapping("toabout")
    public ModelAndView toabout(Model model){
        model.addAttribute("title","关于");
        return new ModelAndView("standalone_web/user/about", "Model", model);
    }
}
