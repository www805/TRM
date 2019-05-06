package com.avst.trm.v1.web.sweb.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentListParam;
import com.avst.trm.v1.web.sweb.service.policeservice.ArraignmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("getArraignment")
    @GetMapping(value = "/getArraignment")
    public ModelAndView getArraignment(Model model) {
        model.addAttribute("title", "笔录提讯");
        return new ModelAndView("server_web/police/arraignment/arraignment", "arraignmentModel", model);

    }

    /**
     * 获取案件列表pp
     * @param param
     * @return
     */
    @RequiresPermissions("getArraignmentList")
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
    @RequiresPermissions("getArraignmentByCaseSsid")
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
    @RequiresPermissions("getArraignmentShow")
    @GetMapping(value = "/getArraignmentShow")
    public ModelAndView getArraignmentShow(Model model,String ssid) {
        model.addAttribute("title", "笔录详情");
        model.addAttribute("ssid",ssid);
        return new ModelAndView("server_web/police/arraignment/getArraignmentShow", "arraignmentModel", model);

    }

    /**
     * 获取笔录详情数据:根据笔录ssid查询
     * @param ssid
     * @return
     */
    @RequiresPermissions("getArraignmentBySsid")
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



}
