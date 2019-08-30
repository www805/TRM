package com.avst.trm.v1.common.cache.param;

import java.util.Map;

public class AppCacheParam {

    private String title;
    private Map<String,Object> data;
    private String syslogoimage;
    private String clientimage;
    private String guidepageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getSyslogoimage() {
        return syslogoimage;
    }

    public void setSyslogoimage(String syslogoimage) {
        this.syslogoimage = syslogoimage;
    }

    public String getClientimage() {
        return clientimage;
    }

    public void setClientimage(String clientimage) {
        this.clientimage = clientimage;
    }

    public String getGuidepageUrl() {
        return guidepageUrl;
    }

    public void setGuidepageUrl(String guidepageUrl) {
        this.guidepageUrl = guidepageUrl;
    }

    @Override
    public String toString() {
        return "AppCacheParam{" +
                "title='" + title + '\'' +
                ", data=" + data +
                ", syslogoimage='" + syslogoimage + '\'' +
                ", clientimage='" + clientimage + '\'' +
                ", guidepageUrl='" + guidepageUrl + '\'' +
                '}';
    }
}
