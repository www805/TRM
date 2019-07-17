package com.avst.trm.v1.common.cache.param;

import java.util.Map;

public class AppCacheParam {

    private String title;
    private Map<String,Object> data;
    private String syslogoimage;
    private String clientimage;

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
}
