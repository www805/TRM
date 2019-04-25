package com.avst.trm.v1.web.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.policereq.GetArraignmentListParam;
import com.avst.trm.v1.web.req.policereq.GetArraignment_countListParam;
import com.avst.trm.v1.web.service.policeservice.ArraignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/arraignment")
public class ArraignmentAction extends BaseAction{
    @Autowired
    private ArraignmentService arraignmentService;


    /**
     * 跳转笔录提讯页面pp
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignment")
    public ModelAndView getArraignment(Model model) {
        model.addAttribute("title", "笔录提讯");
        return new ModelAndView("police/arraignment/arraignment", "arraignmentModel", model);

    }

    /**
     * 获取案件列表pp
     * @param param
     * @return
     */
    @RequestMapping(value = "/getArraignmentList")
    @ResponseBody
    public RResult getArraignmentList(GetArraignmentListParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            arraignmentService.getArraignmentList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取笔录列表
     * @return
     */
    @RequestMapping(value = "/getArraignmentByCaseSsid")
    @ResponseBody
    public RResult getArraignments(String caseSsid) {
        RResult result=createNewResultOfFail();
        if (null==caseSsid){
            result.setMessage("参数为空");
        }else{
            arraignmentService.getArraignmentByCaseSsid(result,caseSsid);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 跳转笔录详情数据
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignmentShow")
    public ModelAndView getArraignmentShow(Model model,String ssid) {
        model.addAttribute("title", "笔录详情");
        model.addAttribute("ssid",ssid);
        return new ModelAndView("police/arraignment/getArraignmentShow", "arraignmentModel", model);

    }

    /**
     * 获取笔录详情数据:根据笔录ssid查询
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getArraignmentBySsid")
    @ResponseBody
    public RResult getArraignmentBySsid(String ssid) {
        RResult result=createNewResultOfFail();
        if (null==ssid){
            result.setMessage("参数为空");
        }else{
            arraignmentService.getArraignmentBySsid(result,ssid);
        }

        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /*************************/



    /***
     * 跳转笔录统计页面
     * @param model
     * @return
     */
    @GetMapping(value = "/getArraignment_count")
    public ModelAndView getArraignment_count(Model model) {
        model.addAttribute("title", "笔录使用情况统计表");
        return new ModelAndView("police/arraignment/getArraignment_count", "arraignment_countModel", model);
    }


    /**
     *
     * 获取统计数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/getArraignment_countList")
    @ResponseBody
    public RResult getArraignment_countList(GetArraignment_countListParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            arraignmentService.getArraignment_countList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



}
