package com.avst.trm.v1.web.sweb.req.basereq;

public class ChangeboolUserParam {
    private Integer adminbool;

    private String ssid;

    private Integer temporaryaskbool;

    public Integer getTemporaryaskbool() {
        return temporaryaskbool;
    }

    public void setTemporaryaskbool(Integer temporaryaskbool) {
        this.temporaryaskbool = temporaryaskbool;
    }

    public Integer getAdminbool() {
        return adminbool;
    }

    public void setAdminbool(Integer adminbool) {
        this.adminbool = adminbool;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
