package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.service.CeshiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

}
