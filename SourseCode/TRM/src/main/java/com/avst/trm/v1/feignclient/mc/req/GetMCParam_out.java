package com.avst.trm.v1.feignclient.mc.req;

public class GetMCParam_out {

    private String mtssid;

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
