package com.avst.trm.v1.web.cweb.req.basereq;

public class updatePassWordParam {

    private String ssid;
    private String oldpassword;
    private String newpassword;
    private String password;

    private Integer firstloginbool;//是否为第一次登陆：1是 -1不是

    public Integer getFirstloginbool() {
        return firstloginbool;
    }

    public void setFirstloginbool(Integer firstloginbool) {
        this.firstloginbool = firstloginbool;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
