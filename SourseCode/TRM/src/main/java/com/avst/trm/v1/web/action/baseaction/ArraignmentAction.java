package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/arraignment")
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
        return new ModelAndView("police/arraignment/getArraignment", "arraignmentModel", model);

    }

    @RequestMapping(value = "/getArraignmentList",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult getlist2(Model model, Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        //ceshiService.getadminlist3(rResult,param);
        return rResult;
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
        return new ModelAndView("police/arraignment/getArraignment_count", "arraignment_countModel", model);
    }

    /***
     * 笔录详细
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignmentShow/{id}")
    public ModelAndView getArraignmentByid(Model model,@PathVariable("id") int id) {

        model.addAttribute("title", "笔录详情 " + id);
        return new ModelAndView("police/arraignment/getArraignmentShow", "arraignmentModel", model);
    }


}
