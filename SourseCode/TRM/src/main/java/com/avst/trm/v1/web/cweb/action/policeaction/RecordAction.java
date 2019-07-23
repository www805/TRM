package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.mc.req.GetMCAsrTxtBackParam_out;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.avst.trm.v1.web.cweb.vo.policevo.ExportWordVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 关于笔录
 */
@RestController
@RequestMapping("/cweb/police/record")
public class RecordAction extends BaseAction {
    @Autowired
    private RecordService recordService;


    /**
     *获取笔录列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRecords")
    public RResult getRecords(@RequestBody ReqParam<GetRecordsParam> param,HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecords(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加笔录
     * @param param
     * @return
     */
    @RequestMapping(value = "/addRecord")
    public RResult addRecord(@RequestBody ReqParam<AddRecordParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addRecord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 查看单个笔录
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRecordById")
    public RResult getRecordById(@RequestBody ReqParam<GetRecordByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecordById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/uploadRecord")
    public RResult uploadRecord(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.uploadRecord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/recordIndex")
    public RResult recordIndex(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.recordIndex(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/getRecordtypes")
    public RResult getRecordtypes(@RequestBody ReqParam<GetRecordtypesParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecordtypes(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping(value = "/getPidRecordtypes")
    public RResult getPidRecordtypes(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getPidRecordtypes(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    @RequestMapping(value = "/getRecordtypeById")
    public RResult getRecordtypeById(@RequestBody ReqParam<GetRecordtypeByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecordtypeById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/addRecordtype")
    public RResult addRecordtype(@RequestBody ReqParam<Police_recordtype> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addRecordtype(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/updateRecordtype")
    public RResult updateRecordtype(@RequestBody ReqParam<Police_recordtype> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.updateRecordtype(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/addRecordTemplate")
    public RResult addRecordTemplate(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addRecordTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/addCaseToArraignment")
    public RResult addCaseToArraignment(@RequestBody ReqParam<AddCaseToArraignmentParam> param, HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addCaseToArraignment(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 根据证件类型和证件号
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserByCard")
    public RResult getUserByCard(@RequestBody  ReqParam<GetUserByCardParam> param, HttpSession httpSession){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getUserByCard(result,param,httpSession);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 根据人员ssid联查
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCaseById")
    public RResult getCaseById(@RequestBody  ReqParam<GetCaseByIdParam> param,HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
           recordService.getCaseById(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部证件类型
     * @param param
     * @return
     */
    @RequestMapping("/getCards")
    public RResult getCards(@RequestBody  ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getCards(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 检测是否授权
     * @return
     */
    public boolean checkToken( String token){
        String clientkey= CommonCache.getClientKey();
       LogUtil.intoLog(this.getClass(),"token:"+token+"------clientkey:"+clientkey);
        if (StringUtils.isEmpty(token)||StringUtils.isEmpty(clientkey)){
            return  false;
        }
        if (!token.trim().equals(clientkey.trim())){
            return  false;
        }
        return  true;
    }

   

    /**
     * 获取当前系统时间
     * @param param
     * @return
     */
    @RequestMapping("/getTime")
    public  RResult getTime(@RequestBody ReqParam<GetMCAsrTxtBackParam_out> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getTime(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 导出pdf
     * @param param
     * @return
     */
    @RequestMapping("/exportPdf")
    public  RResult exportPdf(@RequestBody ReqParam<ExportPdfParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.exportPdf(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 导出word
     * @param param
     * @return
     */
    @RequestMapping("/exportWord")
    public  RResult exportWord(@RequestBody ReqParam<ExportWordParam> param, HttpServletRequest request){
        RResult<ExportWordVO> result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.exportWord(result,param,request);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /***
     * 修改提讯数据
     * @param param
     * @return
     */
    @RequestMapping("/updateArraignment")
    public  RResult updateArraignment(@RequestBody ReqParam<UpdateArraignmentParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.updateArraignment(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取案件列表
     */
    @RequestMapping("/getCases")
    public  RResult getCases(@RequestBody ReqParam<GetCasesParam> param,HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getCases(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加案件
     */
    @RequestMapping("/addCase")
    public  RResult addCase(@RequestBody ReqParam<AddCaseParam> param,HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addCase(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 修改案件
     */
    @RequestMapping("/updateCase")
    public  RResult updateCase(@RequestBody ReqParam<UpdateCaseParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.updateCase(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取单个案件
     */
    @RequestMapping("/getCaseBySsid")
    public  RResult getCaseBySsid(@RequestBody ReqParam<GetCaseBySsidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getCaseBySsid(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部用户
     */
    @RequestMapping("/getUserinfoList")
    public  RResult getUserinfoList(@RequestBody ReqParam<GetUserinfoListParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getUserinfoList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加临时询问人
     * @param param
     * @return
     */
    @RequestMapping("/addUser")
    public  RResult addUser(@RequestBody ReqParam<AddUserParam> param, HttpSession session){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addUser(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 根据案件ssid获取笔录
     * @param param
     * @return
     */
    @RequestMapping("/getRecordByCasessid")
    public  RResult getRecordByCasessid(@RequestBody ReqParam<GetRecordByCasessidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecordByCasessid(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    /**
     * 获取笔录word模板-------------------------------
     * @param param
     * @return
     */
    @RequestMapping("/getWordTemplateList")
    public  RResult getWordTemplateList(@RequestBody ReqParam<GetWordTemplateListParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getWordTemplateList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping("/uploadWordTemplate")
    public  RResult uploadWordTemplate(ReqParam param,@RequestParam(value="wordfile",required=false) MultipartFile multipartfile){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.uploadWordTemplate(result,param,multipartfile);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping("/getWordTemplateByssid")
    public  RResult getWordTemplateByssid(@RequestBody ReqParam<GetWordTemplateByssidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getWordTemplateByssid(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/changeboolWordTemplate")
    public  RResult changeboolWordTemplate(@RequestBody ReqParam<ChangeboolWordTemplateParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.changeboolWordTemplate(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/changeboolCase")
    public  RResult changeboolCase(@RequestBody ReqParam<ChangeboolCaseParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.changeboolCase(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 授权客户端的功能列表
     * @param param
     * @return
     */
    @RequestMapping("/gnlist")
    public  RResult gnlist(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            recordService.gnlist(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }




    /**
     * 笔录问答实时保存-----------------------------
     */
    @RequestMapping("/getRecordrealByRecordssid")
    public  RResult getRecordrealByRecordssid(@RequestBody ReqParam<GetRecordrealByRecordssidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecordrealByRecordssid(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 实时保存
     * @param param
     * @return
     */
    @RequestMapping("/setRecordreal")
    public  RResult setRecordreal(@RequestBody ReqParam<SetRecordrealParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            recordService.setRecordreal(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    @RequestMapping("/getRecordreal_LastByRecordssid")
    public  RResult getRecordreal_LastByRecordssid(@RequestBody ReqParam<GetRecordreal_LastByRecordssidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            recordService.getRecordreal_LastByRecordssid(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 笔录重置保存
     * @param param
     * @return
     */
    @RequestMapping("/setRecordreal_Last")
    public  RResult setRecordreal_Last(@RequestBody ReqParam<SetRecordreal_LastParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            recordService.setRecordreal_Last(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



























}
