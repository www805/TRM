package com.avst.trm.v1.common.cache.param;

import java.util.Map;

public class AppCacheParam {

    private String title;
    private Map<String,Object> data;

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
