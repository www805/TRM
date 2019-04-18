package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/config")
public class ServerConfigAction extends BaseAction{


    /**
     * 系统配置
     * @param model
     * @return
     */
    @GetMapping(value = "/getServerConfig")
    public ModelAndView getServerConfig(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "系统配置");
        return new ModelAndView("police/serverconfig", "configModel", model);

    }

    /***
     * 上传接口
     * @param model
     * @return
     */
    @GetMapping(value = "/uploadByImg")
    public ModelAndView uploadByImg(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "上传成功");
        return new ModelAndView("police/serverconfig", "configModel", model);

    }

}
