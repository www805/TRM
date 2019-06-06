package com.avst.trm.v1.web.cweb.action.policeaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.action.baseaction.MainAction;
import com.avst.trm.v1.web.cweb.req.policereq.GetNotificationParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordsParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetTemplateByIdParam;
import com.avst.trm.v1.web.cweb.req.policereq.TemplateWordParam;
import com.avst.trm.v1.web.cweb.service.policeservice.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 告知书列表
 */
@RestController
@RequestMapping("/cweb/police/notification")
public class NotificationAction extends MainAction {

    @Autowired
    private NotificationService notificationService;

    /*
 ① 获取告知书列表：/cweb/police/notification/getNotifications
 ② 告知书文件上传：/cweb/police/notification/uploadNotification
 ③ 告知书文件下载：/cweb/police/notification/downloadNotification
 ④ 删除告知书：   /cweb/police/notification/deleteNotificationById
     */

    /**
     * 获取告知书列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/getNotifications")
    public RResult getNotifications(@RequestBody ReqParam<GetNotificationParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            notificationService.getNotifications(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 上传告知书
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadNotification")
    @ResponseBody
    public RResult uploadNotification(@RequestParam("file") MultipartFile file) {
        RResult rResult = createNewResultOfFail();
        notificationService.uploadNotification(rResult, file);
        return rResult;
    }

    /**
     * 下载告知书
     * @param param
     * @return
     */
    @RequestMapping(value = "/downloadNotification")
    public RResult downloadNotification(@RequestBody ReqParam<GetNotificationParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            notificationService.downloadNotification(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 删除告知书
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteNotificationById")
    public RResult deleteNotificationById(@RequestBody ReqParam<GetNotificationParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            notificationService.deleteNotificationById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }
}
