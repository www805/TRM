package com.avst.trm.v1.feignclient.mc.req;

public class PauseOrContinueMCParam_out extends BaseReqParam {

    private int pauseOrContinue;//1请求暂停，2请求继续

    private String mtssid;

    private String mcType;

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }

    public int getPauseOrContinue() {
        return pauseOrContinue;
    }

    public void setPauseOrContinue(int pauseOrContinue) {
        this.pauseOrContinue = pauseOrContinue;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }
}
