package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.cache.SysYmlCache;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQVersion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.avst.trm.v1.common.cache.CommonCache.getSQEntity;

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
    @Autowired
    private Police_recordMapper police_recordMapper;//为了得到笔录类型

    @GetMapping("totemplateIndex")
    public ModelAndView totemplateIndex(Model model){
        model.addAttribute("title","问题模板");
        return new ModelAndView("client_web/police/template/templateIndex", "templateIndex", model);
    }

    /**
     * /v1/police/policePage/toaddOrupdateTemplate
     * @param model
     * @return
     */
    @GetMapping("toaddOrupdateTemplate")
    public ModelAndView toaddOrupdateTemplate(Model model){
        model.addAttribute("title","新增 / 修改模板");
        return new ModelAndView("client_web/police/template/addOrupdateTemplate", "addOrupdateTemplate", model);
    }

    /**
     * 模板类型
     * @param model
     * @return
     */
    @GetMapping("totemplateTypeList")
    public ModelAndView totemplateTypeList(Model model){
        model.addAttribute("title", "模板类型");
        return new ModelAndView("client_web/police/template/templateTypeList", "templateTypeListModel", model);
    }

    /**
     * 问题类型
     * @param model
     * @return
     */
    @GetMapping("toproblemTypeList")
    public ModelAndView toproblemTypeList(Model model){
        model.addAttribute("title", "问题类型");
        return new ModelAndView("client_web/police/template/problemTypeList", "problemTypeListModel", model);
    }

    /**
     * 告知书列表
     * @param model
     * @return
     */
    @GetMapping("notificationList")
    public ModelAndView notificationList(Model model){
        model.addAttribute("title", "告知书");
        return new ModelAndView("client_web/police/notification/notificationList", "notificationListModel", model);
    }

    /**
     * 变更模板
     * @param model
     * @return
     */
//    @GetMapping("templateList")
//    public ModelAndView templateList(Model model){
//        return new ModelAndView("client_web/police/template/templateList", "problemIndex", model);
//    }

//    @GetMapping("toproblemIndex")
//    public ModelAndView toproblemIndex(Model model){
//        return new ModelAndView("client_web/police/template/problemIndex", "problemIndex", model);
//    }
//
//    @GetMapping("toaddOrupdateProblem")
//    public ModelAndView toaddOrupdateProblem(Model model){
//        return new ModelAndView("client_web/police/template/addOrupdateProblem", "addOrupdateProblem", model);
//    }

//    @GetMapping("toaddOrupdateTemplateType")
//    public ModelAndView toaddOrupdateTemplateType(Model model){
//        return new ModelAndView("client_web/police/template/addOrupdateTemplateType", "addOrupdateTemplateType", model);
//    }

//    @GetMapping("toaddOrupdateProblemType")
//    public ModelAndView toaddOrupdateProblemType(Model model){
//        return new ModelAndView("client_web/police/template/addOrupdateProblemType", "addOrupdateProblemType", model);
//    }
    /*********************************关于模板 end*********************************/

    /*********************************关于笔录 start*********************************/
    @GetMapping("torecordIndex")
    public ModelAndView torecordIndex(Model model){
        String gnlist=getSQEntity.getGnlist();
        try {
            if (gnlist.indexOf(SQVersion.FY_T)!= -1){
                //法院的
                if (gnlist.indexOf(SQVersion.NX_O)!= -1){
                    model.addAttribute("title","笔录建立");
                }else {
                    model.addAttribute("title", "庭审查看");
                }
            }else {
                model.addAttribute("title","审讯查看");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("client_web/police/record/recordIndex", "recordIndexModel", model);
    }

    @GetMapping("toconversationIndex")
    public ModelAndView toconversationIndex(Model model){
        model.addAttribute("title","审讯查看");
        return new ModelAndView("client_web/police/conversation/conversationIndex", "conversationIndexModel", model);
    }

    @GetMapping("towaitconversation")
    public ModelAndView towaitconversation(Model model,String ssid){
        model.addAttribute("title","审讯控制台");
        if (StringUtils.isNotBlank(ssid)){
            model.addAttribute("recordssid",ssid);
        }
        return new ModelAndView("client_web/police/conversation/waitconversation", "waitconversationModel", model);
    }

    @GetMapping("torecordTypeList")
    public ModelAndView torecordTypeList(Model model){
        model.addAttribute("title","笔录类型");
        return new ModelAndView("client_web/police/record/recordTypeList", "recordTypeListModel", model);
    }

 /*   @GetMapping("toaddOrupdateRecordType")
    public ModelAndView toaddOrupdateRecordType(Model model){
        model.addAttribute("title","笔录类型编辑");
       return new ModelAndView("client_web/police/record/addOrupdateRecordType", "addOrupdateRecordTypeModel", model);
    }*/


    @GetMapping("toaddCaseToUser")
    public ModelAndView toaddCaseToUser(Model model){
        model.addAttribute("title","谈话办案");
        return new ModelAndView("client_web/police/record/addCaseToUser", "addCaseToUserModel", model);
    }


    @GetMapping("towaitRecord")
    public ModelAndView towaitRecord(Model model,String ssid){
        if (StringUtils.isNotBlank(ssid)){
            model.addAttribute("recordssid",ssid);
        }
        String recordtype_conversation2=PropertiesListenerConfig.getProperty("recordtype_conversation2");
        Police_record record=new Police_record();
        record.setSsid(ssid);
        record=police_recordMapper.selectOne(record);
        if (StringUtils.isNotBlank(record.getRecordtypessid())&&StringUtils.isNotBlank(recordtype_conversation2)&&recordtype_conversation2.equals(record.getRecordtypessid())){
            model.addAttribute("title","审讯控制台");
        }else {
            model.addAttribute("title","笔录中");
        }


       return new ModelAndView("client_web/police/record/waitRecord", "waitRecordModel", model);
    }


    @GetMapping("tomoreTemplate")
    public ModelAndView tomoreTemplate(Model model){
        model.addAttribute("title","更多模板");
        return new ModelAndView("client_web/police/record/moreTemplate", "moreTemplateModel", model);
    }


    @GetMapping("torecordReal")
    public ModelAndView torecordReal(Model model){
        model.addAttribute("title","笔录实时文件");
        return new ModelAndView("client_web/police/record/recordReal", "recordRealModel", model);
    }


    @GetMapping("togetRecordById")
    public ModelAndView togetRecordById(Model model,String ssid){
        model.addAttribute("title","笔录详情");
        if (StringUtils.isNotBlank(ssid)){
            model.addAttribute("recordssid",ssid);
        }
       return new ModelAndView("client_web/police/record/getRecordById", "getRecordByIdModel", model);
    }

    @GetMapping("toconversationById")
    public ModelAndView toconversationById(Model model,String ssid){
        model.addAttribute("title","审讯详情");
        if (StringUtils.isNotBlank(ssid)){
            model.addAttribute("recordssid",ssid);
        }
        return new ModelAndView("client_web/police/conversation/getconversationById", "conversationByIdModel", model);
    }


    @GetMapping("tomoreRecord")
    public ModelAndView tomoreRecord(Model model){
       model.addAttribute("title","更多笔录");
       return new ModelAndView("client_web/police/record/moreRecord", "moreRecordModel", model);
    }

    @GetMapping("toaddCaseToUserDetail")
    public ModelAndView toaddCaseToUserDetail(Model model){
        model.addAttribute("title","笔录制作详情");
        return new ModelAndView("client_web/police/record/addCaseToUserDetail", "addCaseToUserDetailModel", model);
    }


    @GetMapping("togetPolygraph")
    public ModelAndView togetPolygraph(Model model){
        model.addAttribute("title","身心检测");
        return new ModelAndView("client_web/police/record/getPolygraph", "getPolygraphModel", model);
    }


    /*********************************关于笔录 end*********************************/



    /*********************************关于案件 start*********************************/
    @GetMapping("tocaseIndex")
    public ModelAndView tocaseIndex(Model model){
        String gnlist=getSQEntity.getGnlist();
        if (gnlist.indexOf(SQVersion.HK_O)!= -1){
            model.addAttribute("title","案件查看");
        }else {
            if (gnlist.indexOf(SQVersion.NX_O)!= -1){
                model.addAttribute("title","笔录管理");
            }else {
                model.addAttribute("title", "案件管理");
            }
        }
        return new ModelAndView("client_web/police/record/caseIndex", "caseIndexModel", model);
    }

    @GetMapping("toaddOrUpdateCase")
    public ModelAndView toCaseIndex(Model model,String ssid){
        model.addAttribute("title","案件编辑");
        model.addAttribute("ssid",ssid);
        return new ModelAndView("client_web/police/record/addOrUpdateCase", "addOrUpdateCaseModel", model);
    }

    /*********************************关于案件 end*********************************/
    @GetMapping("towordTemplateList")
    public ModelAndView towordTemplateList(Model model){
        model.addAttribute("title","笔录模板");
        return new ModelAndView("client_web/police/record/wordTemplateList", "wordTemplateListModel", model);
    }


    /*********************************开始笔录谈话 end*********************************/
    @GetMapping("tostartConversation")
    public ModelAndView tostartConversation(Model model,String parentbool){
        model.addAttribute("title","开始审讯");
        model.addAttribute("parentbool",parentbool);//用于控制页面跳转
        return new ModelAndView("client_web/police/conversation/startConversation", "startConversationModel", model);
    }

    @GetMapping("tocaseStatistics")
    public ModelAndView tocaseStatistics(Model model,String parentbool){
        model.addAttribute("title","案件统计");
        return new ModelAndView("client_web/police/record/caseStatistics", "caseStatisticsModel", model);
    }

    @GetMapping("tocaseToUser")
    public ModelAndView tocaseToUser(Model model){
        model.addAttribute("title","人员案件");
        return new ModelAndView("client_web/police/record/caseToUser", "caseToUserModel", model);
    }








}
