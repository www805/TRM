package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.action.baseaction.MainAction;
import com.avst.trm.v1.web.cweb.req.policereq.GetCaseByIdParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordsParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetUserByCardParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 关于笔录
 */
@RestController
@RequestMapping("/cweb/police/record")
public class RecordAction extends MainAction {
    @Autowired
    private RecordService recordService;

/*
 ①  获取笔录列表：/v1/police/record/getRecords
②  添加笔录：/v1/police/record/addRecord
③  查看单个笔录：/v1/police/record/getRecordById
④  实时录音文件上传：/v1/police/record/uploadRecord
⑤  笔录主页面数据：/v1/police/record/recordIndex
⑥  获取笔录类型列表：/v1/police/record/getRecordtypes
⑦  获取单个笔录类型：/v1/police/record/getRecordtypeById
⑧  添加笔录类型：/v1/police/record/addRecordtype
⑨  修改笔录类型：/v1/police/record/updateRecordtype
⑩  添加笔录模板：/v1/police/record/addRecordTemplate
⑪  添加案件提讯：/v1/police/record/addCaseToArraignment
     */
    /**
     *获取笔录列表
     * @param param
     * @return
     */
    @GetMapping(value = "/getRecords")
    public RResult getRecords(@RequestBody ReqParam<GetRecordsParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.getRecords(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 添加笔录
     * @param param
     * @return
     */
    @PostMapping(value = "/addRecord",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addRecord(@RequestBody ReqParam param){
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
     * 添查看单个笔录
     * @param param
     * @return
     */
    @GetMapping(value = "/getRecordById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getRecordById(@RequestBody ReqParam param){
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

    @PostMapping(value = "/uploadRecord",produces = MediaType.APPLICATION_XML_VALUE)
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

    @GetMapping(value = "/recordIndex",produces = MediaType.APPLICATION_XML_VALUE)
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

    @GetMapping(value = "/getRecordtypes",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getRecordtypes(@RequestBody ReqParam param){
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

    @GetMapping(value = "/getRecordtypeById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getRecordtypeById(@RequestBody ReqParam param){
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

    @PostMapping(value = "/addRecordtype",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addRecordtype(@RequestBody ReqParam param){
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

    @PostMapping(value = "/updateRecordtype",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult updateRecordtype(@RequestBody ReqParam param){
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

    @PostMapping(value = "/addRecordTemplate",produces = MediaType.APPLICATION_XML_VALUE)
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

    @PostMapping(value = "/addCaseToArraignment",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addCaseToArraignment(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            recordService.addCaseToArraignment(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 根据证件id联查
     * @param param
     * @return
     */
    @PostMapping(value = "/getUserByCard",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getUserByCard(@RequestBody  ReqParam<GetUserByCardParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            //clientUserService.getUserByCard(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 根据案件id联查
     * @param param
     * @return
     */
    @PostMapping(value = "/getCaseById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getCaseById(@RequestBody  ReqParam<GetCaseByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
           // clientUserService.getCaseById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }








}
