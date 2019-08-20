package com.avst.trm.v1.feignclient.mc.vo.param;

import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;

public class PHDataBackVoParam {
    private String num;

    private String phBataBackJson;//身心检测数据

    private String phdate;//时间日期

    private Integer phSubtracSeconds;//需要减去的秒数

    public Integer getPhSubtracSeconds() {
        phSubtracSeconds=Integer.valueOf(PropertiesListenerConfig.getProperty("phSubtracSeconds"));
        return phSubtracSeconds;
    }

    public void setPhSubtracSeconds(Integer phSubtracSeconds) {
        this.phSubtracSeconds = phSubtracSeconds;
    }

    public String getPhdate() {
        return phdate;
    }

    public void setPhdate(String phdate) {
        this.phdate = phdate;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPhBataBackJson() {
        return phBataBackJson;
    }

    public void setPhBataBackJson(String phBataBackJson) {
        this.phBataBackJson = phBataBackJson;
    }
}
