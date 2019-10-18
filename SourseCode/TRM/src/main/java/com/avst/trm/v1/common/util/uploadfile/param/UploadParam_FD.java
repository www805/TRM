package com.avst.trm.v1.common.util.uploadfile.param;

/**
 * 上传到设备的参数集合
 */
public class UploadParam_FD {

    private String upload_task_id;//上传文件到第三方服务器上唯一标识

    private String dstPath;//上传文件到第三方服务器上的存储位置

    private String fileName;//上传文件的文件名

    private String linkaction="burn";//固定标识，不用管

    private String discFileName;//上传文件的文件名

    private String action="upload_file";//固定标识，不用管

    public String getUpload_task_id() {
        return upload_task_id;
    }

    public void setUpload_task_id(String upload_task_id) {
        this.upload_task_id = upload_task_id;
    }

    public String getDstPath() {
        return dstPath;
    }

    public void setDstPath(String dstPath) {
        this.dstPath = dstPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinkaction() {
        return linkaction;
    }

    public void setLinkaction(String linkaction) {
        this.linkaction = linkaction;
    }

    public String getDiscFileName() {
        return discFileName;
    }

    public void setDiscFileName(String discFileName) {
        this.discFileName = discFileName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
