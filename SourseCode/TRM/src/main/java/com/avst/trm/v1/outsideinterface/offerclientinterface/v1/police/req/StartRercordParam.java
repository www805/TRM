package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

import com.avst.trm.v1.feignclient.req.StartMCParam_out;

public class StartRercordParam extends StartMCParam_out {
    private String recordssid;//笔录ssid

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }
}
