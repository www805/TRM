package com.avst.trm.v1.web.cweb.req.policereq;

public class ChangeboolWordTemplateParam {
    private String ssid;//word模板dessid

    private Integer defaultbool;//1默认 -1非默认

    private Integer wordtemplatebool;//1正常 -1已删除

    public Integer getWordtemplatebool() {
        return wordtemplatebool;
    }

    public void setWordtemplatebool(Integer wordtemplatebool) {
        this.wordtemplatebool = wordtemplatebool;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getDefaultbool() {
        return defaultbool;
    }

    public void setDefaultbool(Integer defaultbool) {
        this.defaultbool = defaultbool;
    }
}
