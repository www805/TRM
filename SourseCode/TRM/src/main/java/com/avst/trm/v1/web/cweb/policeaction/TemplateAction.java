package com.avst.trm.v1.web.cweb.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.baseaction.MainAction;
import com.avst.trm.v1.web.cweb.req.*;
import com.avst.trm.v1.web.cweb.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 关于模板
 */
@RestController
@RequestMapping("/cweb/police/template")
public class TemplateAction extends MainAction {

    @Autowired
    private TemplateService templateService;

    /**
     * ① 获取模板列表：/cweb/police/template/getTemplates √
     * ②  修改模板：/cweb/police/template/updateTemplate √
     * ③  查看单个模板：/cweb/police/template/getTemplateById √
     * ④  添加模板：/cweb/police/template/addTemplate √
     * ⑤  获取模板类型列表：/cweb/police/template/getTemplateTypes
     * ⑥  添加模板类型：/cweb/police/template/addTemplateType
     * ⑦  修改模板类型：/cweb/police/template/updateTemplateType
     * ⑧  设置默认模板：/cweb/police/template/setDefaultTemplate
     * ⑨  获取问题列表：/cweb/police/template/getProblems *
     * ⑩  修改问题：/cweb/police/template/updateProblem
     * ⑪  查询单个问题：/cweb/police/template/getProblemById
     * ⑫  添加问题：/cweb/police/template/addProblem
     * ⑬  获取问题类型列表：/cweb/police/template/getProblemTypes
     * ⑭  添加问题类型：/cweb/police/template/addProblemType
     * ⑮  修改问题类型：/cweb/police/template/updateProblemType
     * ⑯  获取模板编辑页主数据：/cweb/police/template/addOrupdateTemplateIndex
     * ⑰  查询单个模板类型：/cweb/police/template/getTemplateTypeById
     * ⑱  查询单个问题类型：/cweb/police/template/getProblemTypeById
     * ⑲  获取模板主页数据：/cweb/police/template/templateIndex
     */
    /**
     * 获取模板列表
     * @param param
     * @return
     */
    @GetMapping(value = "/getTemplates",produces = MediaType.APPLICATION_XML_VALUE)
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
    @PostMapping(value = "/updateTemplate",produces = MediaType.APPLICATION_XML_VALUE)
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
    @GetMapping(value = "/getTemplateById",produces = MediaType.APPLICATION_XML_VALUE)
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
    @PostMapping(value = "/addTemplate",produces = MediaType.APPLICATION_JSON_VALUE)
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
    @GetMapping(value = "/getTemplateTypes",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getTemplateTypes(@RequestBody ReqParam param){
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
    @PostMapping(value = "/addTemplateType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addTemplateType(@RequestBody ReqParam param){
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
    @PostMapping(value = "/updateTemplateType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateTemplateType(@RequestBody ReqParam param){
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
    @PostMapping(value = "/setDefaultTemplate",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult setDefaultTemplate(@RequestBody ReqParam param){
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
     * TODO 获取问题列表
     * @param param
     * @return
     */
    @GetMapping(value = "/getProblems",produces = MediaType.APPLICATION_XML_VALUE)
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
    @PostMapping(value = "/updateProblem",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateProblem(@RequestBody ReqParam param){
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
    @GetMapping(value = "/getProblemById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getProblemById(@RequestBody ReqParam param){
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
    @PostMapping(value = "/addProblem",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addProblem(@RequestBody ReqParam param){
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
    @GetMapping(value = "/getProblemTypes",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getProblemTypes(@RequestBody ReqParam param){
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
    @PostMapping(value = "/addProblemType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addProblemType(@RequestBody ReqParam param){
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
    @PostMapping(value = "/updateProblemType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateProblemType(@RequestBody ReqParam param){
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

    @PostMapping(value = "/addOrupdateTemplateIndex",produces = MediaType.APPLICATION_XML_VALUE)
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

    @GetMapping(value = "/getTemplateTypeById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getTemplateTypeById(@RequestBody ReqParam param){
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

    @GetMapping(value = "/getProblemTypeById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getProblemTypeById(@RequestBody ReqParam param){
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

    @GetMapping(value = "/templateIndex",produces = MediaType.APPLICATION_XML_VALUE)
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


















































}
