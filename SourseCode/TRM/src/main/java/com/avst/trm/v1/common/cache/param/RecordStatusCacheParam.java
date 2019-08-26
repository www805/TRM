package com.avst.trm.v1.common.cache.param;

public class RecordStatusCacheParam {

    private String ssid;
    private String lasttime;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    @Override
    public String toString() {
        return "RecordStatusCacheParam{" +
                "ssid='" + ssid + '\'' +
                ", lasttime='" + lasttime + '\'' +
                '}';
    }
}
