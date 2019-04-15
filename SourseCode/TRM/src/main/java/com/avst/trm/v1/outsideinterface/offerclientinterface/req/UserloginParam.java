package com.avst.trm.v1.outsideinterface.offerclientinterface.req;

public class UserloginParam {
    private String loginaccount;//用户账号

    private  String password;//密码

    public String getLoginaccount() {
        return loginaccount;
    }

    public void setLoginaccount(String loginaccount) {
        this.loginaccount = loginaccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
