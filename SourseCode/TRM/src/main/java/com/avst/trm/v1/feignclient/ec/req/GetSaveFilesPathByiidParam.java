package com.avst.trm.v1.feignclient.ec.req;

public class GetSaveFilesPathByiidParam extends BaseParam {

    private String iid;

    private int videobool=0;//是否需要视频文件的路径，1需要，0不需要

    public int getVideobool() {
        return videobool;
    }

    public void setVideobool(int videobool) {
        this.videobool = videobool;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}