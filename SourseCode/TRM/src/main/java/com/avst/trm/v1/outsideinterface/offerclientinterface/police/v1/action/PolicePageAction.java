package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.action;

import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 跳转页面
 */
@RequestMapping("/v1/police/policePage")
public class PolicePageAction {

    /*********************************关于模板 start*********************************/
    @GetMapping("totemplateIndex")
    public ModelAndView totemplateIndex(Model model){
        return new ModelAndView("police/templateIndex", "templateIndex", model);
    }

    @GetMapping("toaddOrupdateTemplate")
    public ModelAndView toaddOrupdateTemplate(Model model){
        return new ModelAndView("police/addOrupdateTemplate", "addOrupdateTemplate", model);
    }

    @GetMapping("toproblemIndex")
    public ModelAndView toproblemIndex(Model model){
        return new ModelAndView("police/problemIndex", "problemIndex", model);
    }

    @GetMapping("toaddOrupdateProblem")
    public ModelAndView toaddOrupdateProblem(Model model){
        return new ModelAndView("police/addOrupdateProblem", "addOrupdateProblem", model);
    }

    @GetMapping("totemplateTypeList")
    public ModelAndView totemplateTypeList(Model model){
        return new ModelAndView("police/templateTypeList", "templateTypeList", model);
    }

    @GetMapping("toaddOrupdateTemplateType")
    public ModelAndView toaddOrupdateTemplateType(Model model){
        return new ModelAndView("police/addOrupdateTemplateType", "addOrupdateTemplateType", model);
    }

    @GetMapping("toproblemTypeList")
    public ModelAndView toproblemTypeList(Model model){
        return new ModelAndView("police/problemTypeList", "problemTypeList", model);
    }

    @GetMapping("toaddOrupdateProblemType")
    public ModelAndView toaddOrupdateProblemType(Model model){
        return new ModelAndView("police/addOrupdateProblemType", "addOrupdateProblemType", model);
    }
    /*********************************关于模板 end*********************************/

    /*********************************关于笔录 start*********************************/
    @GetMapping("torecordIndex")
    public ModelAndView torecordIndex(Model model){
        return new ModelAndView("police/recordIndex", "recordIndex", model);
    }

    @GetMapping("torecordTypeList")
    public ModelAndView torecordTypeList(Model model){
        return new ModelAndView("police/recordTypeList", "recordTypeList", model);
    }

    @GetMapping("toaddOrupdateRecordType")
    public ModelAndView toaddOrupdateRecordType(Model model){
       return new ModelAndView("police/addOrupdateRecordType", "addOrupdateRecordType", model);
    }


    @GetMapping("toaddCaseToUser")
    public ModelAndView toaddCaseToUser(Model model){
        return new ModelAndView("police/addCaseToUser", "addCaseToUser", model);
    }


    @GetMapping("towaitRecord")
    public ModelAndView towaitRecord(Model model){
       return new ModelAndView("police/waitRecord", "waitRecord", model);
    }


    @GetMapping("tomoreTemplate")
    public ModelAndView tomoreTemplate(Model model){
        return new ModelAndView("police/moreTemplate", "moreTemplate", model);
    }


    @GetMapping("torecordReal")
    public ModelAndView torecordReal(Model model){
        return new ModelAndView("police/recordReal", "recordReal", model);
    }


    @GetMapping("togetRecordById")
    public ModelAndView togetRecordById(Model model){
       return new ModelAndView("police/getRecordById", "getRecordById", model);
    }


    @GetMapping("tomoreRecord")
    public ModelAndView tomoreRecord(Model model){
       return new ModelAndView("police/moreRecord", "moreRecord", model);
    }
    /*********************************关于笔录 end*********************************/







}