package com.avst.trm.v1.feignclient.mc.req;

public class GetTDCacheParamByMTssidParam_out {
    private  String mtssid;//会议ssid

    private String mcType;//会议采用版本，现阶段只有AVST

    private String userssid;

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

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
