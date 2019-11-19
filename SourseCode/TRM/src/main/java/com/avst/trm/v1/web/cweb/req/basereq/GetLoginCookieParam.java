package com.avst.trm.v1.web.cweb.req.basereq;

public class GetLoginCookieParam {
    private String  loginaccount_mark;//登录标识
    private String  rememberme_mark;//登录标识

    public String getLoginaccount_mark() {
        return loginaccount_mark;
    }

    public void setLoginaccount_mark(String loginaccount_mark) {
        this.loginaccount_mark = loginaccount_mark;
    }

    public String getRememberme_mark() {
        return rememberme_mark;
    }

    public void setRememberme_mark(String rememberme_mark) {
        this.rememberme_mark = rememberme_mark;
    }
}
