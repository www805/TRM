package com.avst.trm.v1.web.cweb.req.policereq;

public class DelPhreportParam {
    private String ssid;//文件ssid

    private Integer filebool;

    public Integer getFilebool() {
        return filebool;
    }

    public void setFilebool(Integer filebool) {
        this.filebool = filebool;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
