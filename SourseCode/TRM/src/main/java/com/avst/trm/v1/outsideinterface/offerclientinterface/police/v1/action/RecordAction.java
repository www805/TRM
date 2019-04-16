package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.action;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseAction;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于笔录
 */
@RestController
@RequestMapping("/v1/record")
public class RecordAction extends ForClientBaseAction {
    @Autowired
    private RecordService recordService;

    /**
     *获取笔录列表
     * @param param
     * @return
     */
    @GetMapping(value = "/getRecords",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getRecords(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else{
            recordService.getRecords(result,param);
        }
        return result;
    }

    /**
     * 添加笔录
     * @param param
     * @return
     */
    @PostMapping(value = "/addRecord",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult addRecord(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey= CommonCache.getClientKey();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token)){
            result.setMessage("授权异常");
        }else{
            recordService.addRecord(result,param);
        }
        return result;
    }






}
