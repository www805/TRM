package com.avst.trm.v1.web.cweb.req.basereq;

public class GetFileSpaceListParam {

    private String ssid;
    private String path;//路径
    private String ssType;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }
}
