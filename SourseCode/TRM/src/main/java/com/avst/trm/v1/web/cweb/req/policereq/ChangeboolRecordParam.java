package com.avst.trm.v1.web.cweb.req.policereq;

public class ChangeboolRecordParam {
    private String recordssid;//笔录ssid

    private Integer recordbool;//状态

    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }
}
