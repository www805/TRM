package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

public class GetRecordParam {
    private  String mtssid;//会议的ssid

    private String mcType;

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }
}
