package com.avst.trm.v1.web.cweb.policeaction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 跳转页面
 */
@Controller
@RequestMapping("/cweb/police/policePage")
public class PolicePageAction {

    /*********************************关于模板 start*********************************/
    /**
     * /v1/police/policePage/totemplateIndex
     * @param model
     * @return
     */
    @GetMapping("totemplateIndex")
    public ModelAndView totemplateIndex(Model model){
        model.addAttribute("title","笔录模板");
        return new ModelAndView("client_web/police/template/templateIndex", "templateIndex", model);
    }

    /**
     * /v1/police/policePage/toaddOrupdateTemplate
     * @param model
     * @return
     */
    @GetMapping("toaddOrupdateTemplate")
    public ModelAndView toaddOrupdateTemplate(Model model){
        model.addAttribute("title","题目选择");
        return new ModelAndView("client_web/police/template/addOrupdateTemplate", "addOrupdateTemplate", model);
    }

    @GetMapping("toproblemIndex")
    public ModelAndView toproblemIndex(Model model){
        return new ModelAndView("client_web/police/template/problemIndex", "problemIndex", model);
    }

    @GetMapping("toaddOrupdateProblem")
    public ModelAndView toaddOrupdateProblem(Model model){
        return new ModelAndView("client_web/police/template/addOrupdateProblem", "addOrupdateProblem", model);
    }

    @GetMapping("totemplateTypeList")
    public ModelAndView totemplateTypeList(Model model){
        return new ModelAndView("client_web/police/template/templateTypeList", "templateTypeList", model);
    }

    @GetMapping("toaddOrupdateTemplateType")
    public ModelAndView toaddOrupdateTemplateType(Model model){
        return new ModelAndView("client_web/police/template/addOrupdateTemplateType", "addOrupdateTemplateType", model);
    }

    @GetMapping("toproblemTypeList")
    public ModelAndView toproblemTypeList(Model model){
        return new ModelAndView("client_web/police/template/problemTypeList", "problemTypeList", model);
    }

    @GetMapping("toaddOrupdateProblemType")
    public ModelAndView toaddOrupdateProblemType(Model model){
        return new ModelAndView("client_web/police/template/addOrupdateProblemType", "addOrupdateProblemType", model);
    }
    /*********************************关于模板 end*********************************/

    /*********************************关于笔录 start*********************************/
    @GetMapping("torecordIndex")
    public ModelAndView torecordIndex(Model model){
        return new ModelAndView("client_web/police/record/recordIndex", "recordIndex", model);
    }

    @GetMapping("torecordTypeList")
    public ModelAndView torecordTypeList(Model model){
        return new ModelAndView("client_web/police/record/recordTypeList", "recordTypeList", model);
    }

    @GetMapping("toaddOrupdateRecordType")
    public ModelAndView toaddOrupdateRecordType(Model model){
       return new ModelAndView("client_web/police/record/addOrupdateRecordType", "addOrupdateRecordType", model);
    }


    @GetMapping("toaddCaseToUser")
    public ModelAndView toaddCaseToUser(Model model){
        return new ModelAndView("client_web/police/record/addCaseToUser", "addCaseToUser", model);
    }


    @GetMapping("towaitRecord")
    public ModelAndView towaitRecord(Model model){
       return new ModelAndView("client_web/police/record/waitRecord", "waitRecord", model);
    }


    @GetMapping("tomoreTemplate")
    public ModelAndView tomoreTemplate(Model model){
        return new ModelAndView("client_web/police/record/moreTemplate", "moreTemplate", model);
    }


    @GetMapping("torecordReal")
    public ModelAndView torecordReal(Model model){
        return new ModelAndView("client_web/police/record/recordReal", "recordReal", model);
    }


    @GetMapping("togetRecordById")
    public ModelAndView togetRecordById(Model model){
       return new ModelAndView("client_web/police/record/getRecordById", "getRecordById", model);
    }


    @GetMapping("tomoreRecord")
    public ModelAndView tomoreRecord(Model model){
       return new ModelAndView("client_web/police/record/moreRecord", "moreRecord", model);
    }
    /*********************************关于笔录 end*********************************/







}
