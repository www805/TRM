package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problemtype;
import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetype;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.action.baseaction.MainAction;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.service.policeservice.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 关于模板
 */
@RestController
@RequestMapping("/cweb/police/template")
public class TemplateAction extends MainAction {

    @Autowired
    private TemplateService templateService;

    /**
     * ①  获取模板列表：/cweb/police/template/getTemplates √
     * ②  修改模板：/cweb/police/template/updateTemplate √
     * ③  查看单个模板：/cweb/police/template/getTemplateById √
     * ④  添加模板：/cweb/police/template/addTemplate √
     * ⑤  获取模板类型列表：/cweb/police/template/getTemplateTypes √
     * ⑥  添加模板类型：/cweb/police/template/addTemplateType √
     * ⑦  修改模板类型：/cweb/police/template/updateTemplateType √
     * ⑧  设置默认模板：/cweb/police/template/setDefaultTemplate √
     * ⑨  获取问题列表：/cweb/police/template/getProblems √
     * ⑩  修改问题：/cweb/police/template/updateProblem √
     * ⑪  查询单个问题：/cweb/police/template/getProblemById √
     * ⑫  添加问题：/cweb/police/template/addProblem √
     * ⑬  获取问题类型列表：/cweb/police/template/getProblemTypes √
     * ⑭  添加问题类型：/cweb/police/template/addProblemType √
     * ⑮  修改问题类型：/cweb/police/template/updateProblemType √
     * ⑯  获取模板编辑页主数据：/cweb/police/template/addOrupdateTemplateIndex
     * ⑰  查询单个模板类型：/cweb/police/template/getTemplateTypeById √
     * ⑱  查询单个问题类型：/cweb/police/template/getProblemTypeById √
     * ⑲  获取模板主页数据：/cweb/police/template/templateIndex
     * ⒇  获取授权功能列表：/cweb/police/template/getSQGnlist
     */
    /**
     * 获取模板列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTemplates")
    public RResult getTemplates(@RequestBody ReqParam<GetTemplatesParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplates(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 修改模板
     * @param param
     * @return
     */
    @PostMapping(value = "/updateTemplate")
    public RResult updateTemplate(@RequestBody ReqParam<UpdateTemplateParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.updateTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 查看单个模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTemplateById")
    public RResult getTemplateById(@RequestBody ReqParam<GetTemplateByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplateById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 添加模板
     * @param param
     * @return
     */
    @PostMapping(value = "/addTemplate")
    public RResult addTemplate(@RequestBody ReqParam<AddTemplateParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.addTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取模板类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTemplateTypes")
    public RResult getTemplateTypes(@RequestBody ReqParam<ProblemtypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplateTypes(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 添加模板类型
     * @param param
     * @return
     */
    @PostMapping(value = "/addTemplateType")
    public RResult addTemplateType(@RequestBody ReqParam<AddTemplatetypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.addTemplateType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 修改模板类型
     * @param param
     * @return
     */
    @PostMapping(value = "/updateTemplateType")
    public RResult updateTemplateType(@RequestBody ReqParam<UpdateTemplateTypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.updateTemplateType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 设置默认模板
     * @param param
     * @return
     */
    @PostMapping(value = "/setDefaultTemplate")
    public RResult setDefaultTemplate(@RequestBody ReqParam<DefaultTemplateParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.setDefaultTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取问题列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProblems")
    public RResult getProblems(@RequestBody ReqParam<GetProblemsParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getProblems(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;

    }

    /**
     * 修改问题
     * @param param
     * @return
     */
    @PostMapping(value = "/updateProblem")
    public RResult updateProblem(@RequestBody ReqParam<UpdateProblemParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.updateProblem(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 查看单个题目
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProblemById")
    public RResult getProblemById(@RequestBody ReqParam<GetProblemsByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getProblemById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 添加问题
     * @param param
     * @return
     */
    @PostMapping(value = "/addProblem")
    public RResult addProblem(@RequestBody ReqParam<AddProblemParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.addProblem(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取问题类型
     * @param param
     * @return
     */
    @PostMapping(value = "/getProblemTypes")
    public RResult getProblemTypes(@RequestBody ReqParam<ProblemtypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getProblemTypes(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加问题类型
     * @param param
     * @return
     */
    @PostMapping(value = "/addProblemType")
    public RResult addProblemType(@RequestBody ReqParam<AddProblemtypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.addProblemType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 修改问题类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateProblemType")
    public RResult updateProblemType(@RequestBody ReqParam<UpdateProblemtypeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.updateProblemType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    @PostMapping(value = "/addOrupdateTemplateIndex")
    public RResult addOrupdateTemplateIndex(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.addOrupdateTemplateIndex(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 查询单个模板类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTemplateTypeById")
    public RResult getTemplateTypeById(@RequestBody ReqParam<Police_templatetype> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplateTypeById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 查询单个问题类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProblemTypeById")
    public RResult getProblemTypeById(@RequestBody ReqParam<Police_problemtype> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getProblemTypeById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    @RequestMapping(value = "/templateIndex")
    public RResult templateIndex(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.templateIndex(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 导出word
     * @param param
     * @return
     */
    @RequestMapping(value = "/exportWord")
    public RResult exportWord(@RequestBody ReqParam<TemplateWordParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.templateWord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 导出Ecxcel
     * @param param
     * @return
     */
    @RequestMapping(value = "/exportExcel")
    public RResult exportExcel(@RequestBody ReqParam<TemplateWordParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.templateExcel(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /***
     * 导入上传接口uploadFile
     * @return
     */
    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public RResult uploadFile(@RequestParam("file") MultipartFile file,String tmplateTypeId,String repeatStatus) {
        RResult rResult = createNewResultOfFail();
        templateService.uploadFile(rResult, file, tmplateTypeId, repeatStatus);
        return rResult;
    }


    /**
     * 获取授权功能列表（用来判断当前是否是单机版）
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSQGnlist")
    public RResult getSQGnlist(@RequestBody ReqParam<GetSQGnlistParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            templateService.getSQGnlist(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }










































}
