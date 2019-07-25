package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

import com.avst.trm.v1.feignclient.ec.req.PtdjParam_out;
import com.avst.trm.v1.feignclient.mc.req.StartMCParam_out;

public class StartRercordParam extends StartMCParam_out {
    private String recordssid;//笔录ssid

    private PtdjParam_out ptdjParam_out;//片头数据

    public PtdjParam_out getPtdjParam_out() {
        return ptdjParam_out;
    }

    public void setPtdjParam_out(PtdjParam_out ptdjParam_out) {
        this.ptdjParam_out = ptdjParam_out;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }
}
