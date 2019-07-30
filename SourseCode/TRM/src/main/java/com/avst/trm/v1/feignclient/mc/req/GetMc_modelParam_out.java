package com.avst.trm.v1.feignclient.mc.req;

public class GetMc_modelParam_out {
    private String mcType;//会议采用版本，现阶段只有AVST

    private String modelssid;//模板ssid

    public String getModelssid() {
        return modelssid;
    }

    public void setModelssid(String modelssid) {
        this.modelssid = modelssid;
    }

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }
}
