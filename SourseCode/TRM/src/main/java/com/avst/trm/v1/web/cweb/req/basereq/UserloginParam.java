package com.avst.trm.v1.web.cweb.req.basereq;

public class UserloginParam {
    private String loginaccount;//用户账号

    private  String password;//密码

    private boolean rememberpassword;//记住密码

    public boolean isRememberpassword() {
        return rememberpassword;
    }

    public void setRememberpassword(boolean rememberpassword) {
        this.rememberpassword = rememberpassword;
    }

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
