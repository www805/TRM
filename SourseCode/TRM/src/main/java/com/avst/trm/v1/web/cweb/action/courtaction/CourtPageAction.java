package com.avst.trm.v1.web.cweb.action.courtaction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 跳转页面
 */
@Controller
@RequestMapping("/cweb/court/courtPage")
public class CourtPageAction {

    @GetMapping("towaitCourt")
    public ModelAndView towaitCourt(Model model, String ssid){
        model.addAttribute("title","庭审制作");
        model.addAttribute("recordssid",ssid);
        return new ModelAndView("client_web/court/waitCourt", "Model", model);
    }

    @GetMapping("togetCourtDetail")
    public ModelAndView togetCourtDetails(Model model, String ssid){
        model.addAttribute("title","庭审查看");
        model.addAttribute("recordssid",ssid);
        return new ModelAndView("client_web/court/getCourtDetail", "Model", model);
    }

    @GetMapping("tobeforeCourt")
    public ModelAndView toaddCaseToUser(Model model){
        model.addAttribute("title","庭审办案");
        return new ModelAndView("client_web/court/beforeCourt", "Model", model);
    }



}
