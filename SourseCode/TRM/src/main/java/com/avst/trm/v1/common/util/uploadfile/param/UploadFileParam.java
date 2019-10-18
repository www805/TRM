package com.avst.trm.v1.common.util.uploadfile.param;

import com.avst.trm.v1.common.util.uploadfile.UploadFileCache;
import com.avst.trm.v1.common.util.uploadfile.UploadFileThread;

import java.util.List;
import java.util.Map;

/**
 * 每一个上传任务的对象
 */
public class UploadFileParam<T> {

    private String uploadFileType;//上传的类型

    private String ssid;//上传的唯一标识

    private List<FileParam<T>> fileList;//上传的文件集合

    private UploadFileThread uploadFileThread;//上传任务的执行线程

    private int totalFileCount;//总共需要上传多少个文件

    private int doneFileCount=-1;//已经上传了多少个文件

    private int uploadNum;//上传的次数,有的文件可能存在二次上传

    public int getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(int uploadNum) {
        this.uploadNum = uploadNum;
    }

    public UploadFileThread getUploadFileThread() {
        return uploadFileThread;
    }

    public void setUploadFileThread(UploadFileThread uploadFileThread) {
        this.uploadFileThread = uploadFileThread;
    }

    public int getTotalFileCount() {
        return fileList==null?0:fileList.size();
    }

    public void setTotalFileCount(int totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    public int getDoneFileCount() {

        if(doneFileCount==-1){
            if(null!=fileList&&fileList.size() > 0){
                int num=0;
                for(FileParam fileParam:fileList){
                    if(fileParam.isBool()){
                        num++;
                    }
                }
                doneFileCount=num;
            }else{
                doneFileCount=0;
            }
        }
        return doneFileCount;
    }

    public void setDoneFileCount(int doneFileCount) {
        this.doneFileCount = doneFileCount;
    }

    public String getUploadFileType() {
        return uploadFileType;
    }

    public void setUploadFileType(String uploadFileType) {
        this.uploadFileType = uploadFileType;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public List<FileParam<T>> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileParam<T>> fileList) {
        this.fileList = fileList;
    }
}
