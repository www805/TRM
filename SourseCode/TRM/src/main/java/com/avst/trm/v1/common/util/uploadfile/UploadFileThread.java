package com.avst.trm.v1.common.util.uploadfile;

import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.uploadfile.param.FileParam;
import com.avst.trm.v1.common.util.uploadfile.param.UploadFileParam;
import com.avst.trm.v1.common.util.uploadfile.param.UploadParam_FD;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class UploadFileThread<T> extends Thread{

    private List<FileParam<T>> fileList;//上传的所有文件集合

    private String uploadType;//上传的类型

    private String ssid;//上传的唯一标识，//上传一个笔录就是笔录的ssid，上传一个案件就是一个案件的ssid

    private String username;//那个用户需要上传

    private String uploadname;//上传任务的别名，没有什么特别大的意义，可以不写

    private Gson gson=new Gson();


    public UploadFileThread(List<FileParam<T>> fileList, String uploadType, String ssid, String username, String uploadname) {
        this.fileList = fileList;
        this.uploadType = uploadType;
        this.ssid = ssid;
        this.username = username;
        this.uploadname = uploadname;
    }

    @Override
    public void run() {

        if(null==fileList||fileList.size()==0){
            LogUtil.intoLog(3,this.getClass(),"上传的文件数据为空"+(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
            return ;
        }
        if( StringUtils.isEmpty(uploadType)){

            LogUtil.intoLog(3,this.getClass(),"上传的类型为空，请注意"+(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
            return ;
        }

        if(UploadFileCache.getUploadFile(ssid)!=null){//不能重复
            LogUtil.intoLog(3,this.getClass(),"警告，这个ssid对应的数据已经在上传中"+(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
            return ;
        }


        if(UploadType.AVST_FD.equals(uploadType)){//往设备上传
            fdUpload(uploadname);
        }else{
            LogUtil.intoLog(3,this.getClass(),"上传的类型暂时没有找到，uploadType："+uploadType+(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
        }

    }

//上传AVST设备
    private void fdUpload(String uploadname){

        //写入缓存
        UploadFileParam uploadFileParam=new UploadFileParam();
        uploadFileParam.setFileList(fileList);
        uploadFileParam.setSsid(ssid);
        uploadFileParam.setUploadFileType(uploadType);
        uploadFileParam.setUploadFileThread(this);
        UploadFileCache.setUploadFile(uploadFileParam);

        //开始执行上传
        for(FileParam fileParam:fileList){
            String filepath=fileParam.getFilePath();
            Map<String,String> map=new HashMap<String,String>();
            try {
                //转换成上传avst设备的参数
                UploadParam_FD uploadParam_fd=gson.fromJson(gson.toJson(fileParam.getUploadparam()),UploadParam_FD.class);

                if(null==uploadParam_fd){
                    LogUtil.intoLog(4,this.getClass(),"设备上传的文件的参数转换异常，这个文件不上传，fileParam："+ JacksonUtil.objebtToString(fileParam.getUploadparam()) +(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
                    continue;
                }else{//给上传的map进行赋值
                    map.put("upload_task_id",uploadParam_fd.getUpload_task_id());
                    map.put("dstPath",uploadParam_fd.getDstPath());
                    map.put("fileName",uploadParam_fd.getFileName());
                    map.put("linkaction",uploadParam_fd.getLinkaction());
                    map.put("discFileName",uploadParam_fd.getDiscFileName());
                    map.put("action",uploadParam_fd.getAction());
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.intoLog(4,this.getClass(),"设备上传的文件的参数转换异常，这个文件不上传，fileParam："+ JacksonUtil.objebtToString(fileParam.getUploadparam()) +(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
                continue;
            }


            String actionurl=fileParam.getActionURL();
            if(null==map||StringUtils.isEmpty(filepath)||StringUtils.isEmpty(actionurl)){

                LogUtil.intoLog(4,this.getClass(),"上传的文件的参数异常，fileParam：这个文件不上传，"+ JacksonUtil.objebtToString(fileParam) +(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
                continue;
            }
            boolean bool=HttpRequest.uploadFile_fd(actionurl,filepath,map);
            if(bool){//暂时不管上传失败的
                UploadFileCache.checkBool(ssid,bool,filepath);
            }
        }

        UploadFileCache.delUploadFile(ssid);
        LogUtil.intoLog(1,this.getClass(),"所有文件上传完毕，ssid："+ ssid +(uploadname!=null?(",上传任务的备注："+uploadname):""),username);
    }
}
