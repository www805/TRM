package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_notification;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Notification;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_notificationMapper;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.WordToHtmlUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.cweb.req.policereq.GetNotificationParam;
import com.avst.trm.v1.web.cweb.vo.policevo.GetNotificationVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("NotificationService")
public class NotificationService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_notificationMapper policeNotificationMapper;

    @Autowired
    private Base_filesaveMapper filesaveMapper;

    @Value("${file.notification}")
    private String filePath;

    @Value("${upload.basepath}")
    private String uploadbasepath;

    /**
     * 获取告知书列表
     * @param result
     * @param param
     */
    public void getNotifications(RResult result, ReqParam<GetNotificationParam> param) {

        GetNotificationVO getNotificationVO=new GetNotificationVO();

        //请求参数转换
        GetNotificationParam getNotificationParam = param.getParam();
        if (null==getNotificationParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();

        if (StringUtils.isNotBlank(getNotificationParam.getNotificationname())){
            ew.like("notificationname", getNotificationParam.getNotificationname().trim());
        }

        ew.orderBy("updatetime", false);

        //分页处理
        int count = policeNotificationMapper.countgetNotifications(ew);
        getNotificationParam.setRecordCount(count);

        Page<Notification> page=new Page<Notification>(getNotificationParam.getCurrPage(),getNotificationParam.getPageSize());
        List<Notification> notifications = policeNotificationMapper.getNotifications(page, ew);

        getNotificationVO.setPagelist(notifications);
        getNotificationVO.setPageparam(getNotificationParam);
        result.setData(getNotificationVO);
        changeResultToSuccess(result);
    }

    /**
     * 上传告知书
     * @param rResult
     * @param file
     */
    public void uploadNotification(RResult rResult, MultipartFile file) {
        if (file.isEmpty()) {
            rResult.setMessage("上传失败，请选择文件");
            return;
        }

        //1 获取文件名
        String fileName = file.getOriginalFilename();

        //2 获取随机文件名
        String imageName = OpenUtil.getUUID_32();

        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        imageName += suffix;

        if(!".doc".equalsIgnoreCase(suffix) &&
                !".docx".equalsIgnoreCase(suffix) &&
                !".pdf".equalsIgnoreCase(suffix) &&
                !".png".equalsIgnoreCase(suffix) &&
                !".jpg".equalsIgnoreCase(suffix) &&
                !".jpeg".equalsIgnoreCase(suffix) ){
            rResult.setMessage("上传失败，只能上传doc/docx/pdf/png/jpg/jpeg文件");
            return;
        }

        String filePathNew = OpenUtil.createpath_fileByBasepath(filePath);

        String realpath = filePathNew + imageName;
        File dest = new File(realpath);
        try {
            file.transferTo(dest);

            //解析下载地址
            String uploadpath=OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),realpath);
            rResult.setData(uploadpath);

            if(realpath.endsWith(".doc")){
                String replace = realpath.replace(".doc", ".html");
                WordToHtmlUtil.wordToHtml(realpath, replace);
            }else if(realpath.endsWith(".docx")){
                String replace = realpath.replace(".docx", ".html");
                WordToHtmlUtil.wordToHtml(realpath, replace);
            }

            Police_notification notification = new Police_notification();
            notification.setNotificationname(fileName);
            notification.setSsid(OpenUtil.getUUID_32());
            notification.setUpdatetime(new Date());

            //上传的文件保存到数据库表里
            Base_filesave filesave = new Base_filesave();
            filesave.setRealfilename(imageName);//真实路径文件名
            filesave.setUploadfilename(fileName);//文件本身的文件名
            filesave.setRecordrealurl(realpath);//真实存储地址
            filesave.setRecorddownurl(uploadpath);//下载地址

            filesave.setDatassid(notification.getSsid());//从属表的ssid
            filesave.setSsid(OpenUtil.getUUID_32());

            notification.setNotification_filesavessid(filesave.getSsid());
            policeNotificationMapper.insert(notification);
            Integer insert = filesaveMapper.insert(filesave);
            this.changeResultToSuccess(rResult);
            rResult.setMessage("上传成功");

        } catch (Exception e) {
            e.printStackTrace();
            rResult.setMessage("文件上传失败");
            LogUtil.intoLog(4,this.getClass(),"文件上传失败,realpath:"+realpath);
        }

    }

    /**
     * 下载告知书
     * @param result
     * @param param
     */
    public void downloadNotification(RResult result, ReqParam<GetNotificationParam> param) {
        //请求参数转换
        GetNotificationParam getNotificationParam = param.getParam();
        if (null==getNotificationParam){
            result.setMessage("参数为空");
            return;
        }
        try {

            Base_filesave filesave = new Base_filesave();
            filesave.setDatassid(getNotificationParam.getSsid());
            filesave = filesaveMapper.selectOne(filesave);
            if(null!=filesave&&StringUtils.isNotEmpty(filesave.getRecorddownurl())){
                filesave.setRecorddownurl(uploadbasepath + filesave.getRecorddownurl());
                result.setData(filesave);
                changeResultToSuccess(result);
            }else{
                result.setMessage("请求下载地址失败");
                LogUtil.intoLog(4,this.getClass(),"请求下载地址失败，数据库查找数据为空，getNotificationParam.getSsid()："
                        +getNotificationParam.getSsid()+"---filesave.getRecorddownurl():"+filesave.getRecorddownurl());
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.intoLog(4,this.getClass(),"请求下载地址失败，抛错，getNotificationParam.getSsid()："+getNotificationParam.getSsid());
        }
    }

    /**
     * 删除告知书
     * @param result
     * @param param
     */
    public void deleteNotificationById(RResult result, ReqParam<GetNotificationParam> param) {

        //请求参数转换
        GetNotificationParam getNotificationParam = param.getParam();
        if (null==getNotificationParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();

        if (StringUtils.isNotBlank(getNotificationParam.getSsid())){
            ew.eq("ssid", getNotificationParam.getSsid().trim());
        }

        Integer delete_bool = policeNotificationMapper.delete(ew);

        EntityWrapper ew2 = new EntityWrapper();
        if (StringUtils.isNotBlank(getNotificationParam.getSsid())){
            ew2.eq("datassid", getNotificationParam.getSsid().trim());
        }
        delete_bool = filesaveMapper.delete(ew2);
        LogUtil.intoLog(this.getClass(),"delete_bool：" + delete_bool);

        if(delete_bool == 1){
            result.setData("删除成功!");
            changeResultToSuccess(result);
        }

    }
}
