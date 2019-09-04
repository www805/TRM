package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.KeywordParam;
import com.avst.trm.v1.web.sweb.req.policereq.AddOrUpdateKeywordParam;
import com.avst.trm.v1.web.sweb.service.policeservice.KeywordService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sweb/base/keyword")
public class KeywordAction extends BaseAction{

    @Autowired
    private KeywordService keywordService;

    /**
     * 关键字
     * @param model
     * @param param
     * @return
     */
    @RequestMapping(value = "/getKeyword")
    public ModelAndView getKeyword(Model model, KeywordParam param) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getKeyword")) {
            RResult rResult=createNewResultOfFail();
            model.addAttribute("result", rResult);
            model.addAttribute("title", "关键字");
            return new ModelAndView("server_web/base/keyword/getKeyword", "keywordModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /***
     * 关键字列表分页
     * @return
     */
    @RequestMapping(value = "/getKeywordList")
    @ResponseBody
    public RResult getKeywordList(KeywordParam param) {
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getKeywordList")) {
            if (null==param){
                result.setMessage("参数为空");
            }else{
                keywordService.findKeywordlist(result,param);
            }
        }else {
            result.setMessage("权限不足");
        }
        return result;
    }

    /**
     * 添加关键字KeywordAction
     * @param model
     * @return
     */
    @RequestMapping(value = "/getAddOrUpdateKeyword")
    public ModelAndView getAddKeyword(Model model, AddOrUpdateKeywordParam keyword) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getAddOrUpdateKeyword")) {
            RResult rResult = createNewResultOfFail();
            rResult.setData(keyword);
            model.addAttribute("RResult", rResult);
            model.addAttribute("title", "添加关键字");
            return new ModelAndView("server_web/base/keyword/addOrUpdateKeyword", "keywordModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     * 修改关键字页面
     * @param model
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getAddOrUpdateKeyword/{ssid}")
    public ModelAndView getUpdateKeyword(Model model,  @PathVariable("ssid") String ssid) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getAddOrUpdateKeyword")) {
            RResult rResult = createNewResultOfFail(); // AddKeywordParam addKeywordParam,
            keywordService.getKeywordById(rResult, ssid);
            model.addAttribute("RResult", rResult);
            model.addAttribute("title", "修改关键字");
            return new ModelAndView("server_web/base/keyword/addOrUpdateKeyword", "keywordModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     * 修改关键字
     * @param ssid
     * @param keyword
     * @return
     */
    @PostMapping(value = "/getAddOrUpdateKeyword/{ssid}")
    @ResponseBody
    public RResult UpdateKeyword(@PathVariable("ssid") String ssid, AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getAddOrUpdateKeyword")) {
            keywordService.UpdateKeyword(rResult, keyword);
        }else {
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /**
     * 新增关键字
     * @param keyword
     * @return
     */
    @PostMapping(value = "/getAddOrUpdateKeyword")
    @ResponseBody
    public RResult AddKeyword(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getAddOrUpdateKeyword")) {
            keywordService.AddKeyword(rResult, keyword);
        }else {
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /***
     *删除关键字
     * @param keyword
     * @return
     */
    @PostMapping(value = "/deleteKeyword")
    @ResponseBody
    public RResult deleteKeyword(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("deleteKeyword")) {
            keywordService.deleteKeyword(rResult, keyword);
        }else {
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /**
     * 修改关键字状态
     * @param keyword
     * @return
     */
    @PostMapping(value = "/updateShieldbool")
    @ResponseBody
    public RResult updateShieldbool(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateShieldbool")) {
            keywordService.updateShieldbool(rResult, keyword);
        }else {
            rResult.setMessage("权限不足");
        }
        return rResult;
    }


}
