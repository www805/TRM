package com.avst.trm.v1.web.cweb.vo.basevo;

public class UserloginVO {
    private String type;

    private Integer firstloginbool;//是否为第一次登陆：1是 -1不是

    private String ssid;//用户ssid

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getFirstloginbool() {
        return firstloginbool;
    }

    public void setFirstloginbool(Integer firstloginbool) {
        this.firstloginbool = firstloginbool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
