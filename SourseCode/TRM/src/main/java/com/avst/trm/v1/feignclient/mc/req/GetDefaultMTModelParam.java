package com.avst.trm.v1.feignclient.mc.req;

public class GetDefaultMTModelParam {

    private String mcType;//会议采用版本，现阶段只有AVST

    private int onlinebool=1;//是否需要连接,1连接，2单机

    public int getOnlinebool() {
        return onlinebool;
    }

    public void setOnlinebool(int onlinebool) {
        this.onlinebool = onlinebool;
    }

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }
}
