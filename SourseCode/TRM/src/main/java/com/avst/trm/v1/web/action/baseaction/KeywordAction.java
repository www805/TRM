package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.service.policeservice.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/keyword")
public class KeywordAction extends BaseAction{

    @Autowired
    private KeywordService keywordService;

    /**
     * 关键字
     * @param model
     * @param param
     * @return
     */
    @GetMapping(value = "/getKeyword")
    public ModelAndView getUser(Model model, Getlist3Param param) {

        RResult rResult=createNewResultOfFail();

        param.setPageSize(3);//测试
        keywordService.findKeywordlist(rResult,param);

        model.addAttribute("result", rResult);

        model.addAttribute("title", "关键字");
        return new ModelAndView("police/keyword/getKeyword", "keywordModel", model);

    }

    /***
     * 关键字列表分页
     * @param model
     * @return
     */
    @RequestMapping(value = "/getKeywordList")
    @ResponseBody
    public RResult getUserList(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        keywordService.findKeywordlist(rResult,param);
        return rResult;
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
        return new ModelAndView("police/keyword/addOrUpdateKeyword", "keywordModel", model);
    }

    /***
     * 修改关键字
     * @param model
     * @return
     */
    @GetMapping(value = "/getUpdateKeyword/{id}")
    public ModelAndView getUpdateKeyword(Model model, @PathVariable("id") int id) {

        RResult rResult = createNewResultOfFail();
        //ceshiService.getadminlist3(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改关键字" + id);
        return new ModelAndView("police/keyword/addOrUpdateKeyword", "keywordModel", model);
    }


}
