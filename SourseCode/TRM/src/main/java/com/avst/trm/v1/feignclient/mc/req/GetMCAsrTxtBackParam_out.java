package com.avst.trm.v1.feignclient.mc.req;

public class GetMCAsrTxtBackParam_out extends BaseReqParam {

    private String mtssid;//会议ssid

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
