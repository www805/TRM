package com.avst.trm.v1.common.cache.param;

public class RecordStatusCacheParam {

    private String recordssid;
    private String mtssid;
    private long lasttime;

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    @Override
    public String toString() {
        return "RecordStatusCacheParam{" +
                "recordssid='" + recordssid + '\'' +
                ", mtssid='" + mtssid + '\'' +
                ", lasttime='" + lasttime + '\'' +
                '}';
    }
}
