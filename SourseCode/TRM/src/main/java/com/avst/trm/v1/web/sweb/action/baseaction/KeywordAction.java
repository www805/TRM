package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.KeywordParam;
import com.avst.trm.v1.web.sweb.req.policereq.AddOrUpdateKeywordParam;
import com.avst.trm.v1.web.sweb.service.policeservice.KeywordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("getKeyword")
    @GetMapping(value = "/getKeyword")
    public ModelAndView getKeyword(Model model, KeywordParam param) {
        RResult rResult=createNewResultOfFail();
        model.addAttribute("result", rResult);
        model.addAttribute("title", "关键字");
        return new ModelAndView("server_web/police/keyword/getKeyword", "keywordModel", model);

    }

    /***
     * 关键字列表分页
     * @return
     */
    @RequiresPermissions("getKeywordList")
    @RequestMapping(value = "/getKeywordList")
    @ResponseBody
    public RResult getKeywordList(KeywordParam param) {
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else{
            keywordService.findKeywordlist(result,param);
        }
        return result;
    }

    /**
     * 添加关键字KeywordAction
     * @param model
     * @return
     */
    @RequiresPermissions("getAddOrUpdateKeyword")
    @GetMapping(value = "/getAddOrUpdateKeyword")
    public ModelAndView getAddKeyword(Model model, AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();

//        keyword.setShieldbool(1);
        rResult.setData(keyword);
        model.addAttribute("RResult", rResult);

        model.addAttribute("title", "添加关键字");
        return new ModelAndView("server_web/police/keyword/addOrUpdateKeyword", "keywordModel", model);
    }

    /**
     * 修改关键字页面
     * @param model
     * @param id
     * @return
     */
    @RequiresPermissions("getAddOrUpdateKeyword")
    @GetMapping(value = "/getAddOrUpdateKeyword/{id}")
    public ModelAndView getUpdateKeyword(Model model,  @PathVariable("id") int id) {

        RResult rResult = createNewResultOfFail(); // AddKeywordParam addKeywordParam,
        keywordService.getKeywordById(rResult, id);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "修改关键字");
        return new ModelAndView("server_web/police/keyword/addOrUpdateKeyword", "keywordModel", model);
    }

    /**
     * 修改关键字
     * @param id
     * @param keyword
     * @return
     */
    @RequiresPermissions("getAddOrUpdateKeyword")
    @PostMapping(value = "/getAddOrUpdateKeyword/{id}")
    @ResponseBody
    public RResult UpdateKeyword(@PathVariable("id") int id, AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        keywordService.UpdateKeyword(rResult, keyword);
        return rResult;
    }

    /**
     * 新增关键字
     * @param keyword
     * @return
     */
    @RequiresPermissions("getAddOrUpdateKeyword")
    @PostMapping(value = "/getAddOrUpdateKeyword")
    @ResponseBody
    public RResult AddKeyword(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        keywordService.AddKeyword(rResult, keyword);
        return rResult;
    }

    /***
     *删除关键字
     * @param keyword
     * @return
     */
    @RequiresPermissions("deleteKeyword")
    @PostMapping(value = "/deleteKeyword")
    @ResponseBody
    public RResult deleteKeyword(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        keywordService.deleteKeyword(rResult, keyword);
        return rResult;
    }

    /**
     * 修改关键字状态
     * @param keyword
     * @return
     */
    @RequiresPermissions("updateShieldbool")
    @PostMapping(value = "/updateShieldbool")
    @ResponseBody
    public RResult updateShieldbool(AddOrUpdateKeywordParam keyword) {
        RResult rResult = createNewResultOfFail();
        keywordService.updateShieldbool(rResult, keyword);
        return rResult;
    }


}
