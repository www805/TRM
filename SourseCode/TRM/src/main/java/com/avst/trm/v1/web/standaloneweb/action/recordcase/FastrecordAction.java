package com.avst.trm.v1.web.standaloneweb.action.recordcase;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 快速笔录
 */
@Controller
@RequestMapping("/cweb/police/fastrecord")
public class FastrecordAction {

    /**
     * 跳转==》关于
     */
    @GetMapping("towaitconversation")
    public ModelAndView towaitconversation(Model model,String ssid){
        model.addAttribute("title","快速谈话");
        if (StringUtils.isNotBlank(ssid)){
            model.addAttribute("recordssid",ssid);
        }
        return new ModelAndView("standalone_web/recordcase/waitconversation", "Model", model);
    }
}
