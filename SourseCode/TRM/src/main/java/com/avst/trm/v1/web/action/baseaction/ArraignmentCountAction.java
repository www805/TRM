package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Arraignment_countParam;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.policeservice.Arraignment_countService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/arraignmentCount")
public class ArraignmentCountAction extends BaseAction{

    @Autowired
    private Arraignment_countService arraignmentCountService;

    @PostMapping(value = "/getArraignment_countList")
    @ResponseBody
    public RResult getArraignment_countList(Arraignment_countParam param) {
        RResult rResult=createNewResultOfFail();
        if (null==param){
            rResult.setMessage("参数为空");
        }else{
            arraignmentCountService.getArraignment_countList(rResult,param);
        }
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


}
