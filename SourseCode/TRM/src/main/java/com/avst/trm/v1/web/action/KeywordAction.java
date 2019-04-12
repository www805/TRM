package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/keyword")
public class KeywordAction extends BaseAction{

    /**
     * 关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getKeyword")
    public ModelAndView getKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "关键字");
        return new ModelAndView("keyword", "keywordModel", model);
    }

    /**
     * 添加关键字KeywordAction
     * @param model
     * @return
     *
     */
    @GetMapping(value = "/getAddKeyword")
    public ModelAndView getAddKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "添加关键字");
        return new ModelAndView("addOrUpdateKeyword", "keywordModel", model);
    }

    /***
     * 修改关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateKeyword")
    public ModelAndView getUpdateKeyword(Model model) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改关键字");
        return new ModelAndView("addOrUpdateKeyword", "keywordModel", model);
    }


}
