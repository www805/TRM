package com.avst.trm.v1.web.sweb.req.basereq;

public class ResetPasswordParam {
    private String userssid;//需要重置密码的用户ssid

    private String init_password;//初始化密码：默认123456

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getInit_password() {
        return init_password;
    }

    public void setInit_password(String init_password) {
        this.init_password = init_password;
    }
}
