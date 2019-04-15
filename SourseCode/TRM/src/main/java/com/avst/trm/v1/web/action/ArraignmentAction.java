package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/arraignment")
public class ArraignmentAction extends BaseAction{

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
        return new ModelAndView("arraignment/arraignment", "arraignmentModel", model);

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
        return new ModelAndView("arraignment/arraignment_count", "arraignment_countModel", model);
    }

    /***
     * 笔录详细
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignmentByid/{id}")
    public ModelAndView getArraignmentByid(Model model,@PathVariable("id") int id) {

        model.addAttribute("title", "笔录详情 " + id);
        return new ModelAndView("arraignment/arraignmentByid", "arraignmentModel", model);
    }


}
