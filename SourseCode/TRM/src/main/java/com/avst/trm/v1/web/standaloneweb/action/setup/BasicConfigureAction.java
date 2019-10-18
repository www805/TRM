package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统设置action
 */
@Controller
@RequestMapping("/cweb/setUp/basicConfigure")
public class BasicConfigureAction extends BaseAction {





    /**
     * 基本配置界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoBasicConfigure")
    public ModelAndView gotoBasicConfigure(Model model){
        model.addAttribute("title","基本配置");
        return  new ModelAndView("standalone_web/setup/basicConfigure/basicConfigure","basicConfigureModel", model);
    }

    /**
     * 软硬件信息
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoSoftAndHardInfo")
    public ModelAndView gotoSoftAndHardInfo(Model model){
        model.addAttribute("title","软硬件信息");
        return  new ModelAndView("standalone_web/setup/basicConfigure/softAndHardInfo","softAndHardInfoModel", model);
    }

    /**
     * 网络配置界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoNetworkConfigure")
    public ModelAndView gotoNetwork(Model model){
        model.addAttribute("title","设备网络配置");
        return  new ModelAndView("standalone_web/setup/basicConfigure/networkConfigure","networkConfigureModel", model);
    }

    /**
     * 日期时间界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoDateTime")
    public ModelAndView gotoDateTime(Model model){
        model.addAttribute("title","日期时间");
        return  new ModelAndView("standalone_web/setup/basicConfigure/dateTime","dateTimeModel", model);
    }





}



