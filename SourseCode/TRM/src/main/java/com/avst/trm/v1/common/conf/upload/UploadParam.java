package com.avst.trm.v1.common.conf.upload;

public class UploadParam {

    private String path;

    private int state=0;//0等待上传,1上传中,2上传结束

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
