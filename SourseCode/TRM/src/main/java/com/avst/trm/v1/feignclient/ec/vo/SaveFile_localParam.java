package com.avst.trm.v1.feignclient.ec.vo;

public class SaveFile_localParam {

    private String sourseRelativePath;//源文件的相对路径，相对存储服务器的base路径

    private String ssType;//请求的是哪一个存储服务，avst ss_avst

    private String iid;//文件在源设备中的唯一识别码

    public String getSourseRelativePath() {
        return sourseRelativePath;
    }

    public void setSourseRelativePath(String sourseRelativePath) {
        this.sourseRelativePath = sourseRelativePath;
    }

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}
