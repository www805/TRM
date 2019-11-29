package com.avst.trm.v1.web.cweb.action.baseaction;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 磁盘文件管理
 */
@RestController
@RequestMapping("/filespace")
public class FileSpaceAction {


    @RequestMapping("/getFileSpacePage")
    public ModelAndView getFileSpacePage(Model model) {
        model.addAttribute("title", "磁盘文件管理");
        return new ModelAndView("client_web/base/getFileSpacePage", "getFileSpacePage", model);
    }


}
