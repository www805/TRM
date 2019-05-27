package com.avst.trm.v1.feignclient.req;

public class GetMCaLLUserAsrTxtListParam_out {
    private  String mtssid;//会议ssid

    private String mcType;//会议采用版本，现阶段只有AVST

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
