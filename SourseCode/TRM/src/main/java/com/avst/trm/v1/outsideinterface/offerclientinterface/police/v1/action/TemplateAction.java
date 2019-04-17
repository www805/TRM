package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.action;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseAction;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetProblemsParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplatesParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service.TemplateService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetProblemTypesVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetProblemsVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetTemplateTypesVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetTemplatesVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于模板
 */
@RestController
@RequestMapping("/v1/police/template")
public class TemplateAction extends ForClientBaseAction {

    @Autowired
    private TemplateService templateService;

    /**
     * 获取模板列表
     * @param param
     * @return
     */
    @GetMapping(value = "/getTemplates",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getTemplates(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplates(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取问题列表
     * @param param
     * @return
     */
    @PostMapping(value = "/getProblems",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getProblems(ReqParam<GetProblemsParam> param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.getProblems(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 获取模板类型
     * @param param
     * @return
     */
    @GetMapping(value = "/getTemplateTypes",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getTemplateTypes(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.getTemplateTypes(result);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取问题类型
     * @param param
     * @return
     */
    @GetMapping(value = "/getProblemTypes",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getProblemTypes(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.getProblemTypes(result);
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
    public RResult updateTemplate(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    public RResult getTemplateById(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    @PostMapping(value = "/addTemplate",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addTemplate(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.addTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 修改问题
     * @param param
     * @return
     */
    @GetMapping(value = "/updateProblem",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateProblem(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    public RResult getProblemById(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    @GetMapping(value = "/addProblem",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addProblem(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.addProblem(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 添加模板类型
     * @param param
     * @return
     */
    @GetMapping(value = "/addTemplateType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addTemplateType(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    @GetMapping(value = "/updateTemplateType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateTemplateType(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.updateTemplateType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 添加问题类型
     * @param param
     * @return
     */
    @GetMapping(value = "/addProblemType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addProblemType(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
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
    @GetMapping(value = "/updateProblemType",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateProblemType(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.updateProblemType(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 设置默认模板
     * @param param
     * @return
     */
    @GetMapping(value = "/setDefaultTemplate",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult setDefaultTemplate(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else {
            templateService.setDefaultTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    public static void main(String[] args) {

    }



}
