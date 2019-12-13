package com.avst.trm.v1.feignclient.ec.req;

public class GetToOutStorageListParam extends BaseParam{

    private String iid;
    private String ssid;
    private String path;//路径

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
