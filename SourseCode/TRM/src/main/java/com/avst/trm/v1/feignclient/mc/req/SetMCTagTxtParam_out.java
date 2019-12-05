package com.avst.trm.v1.feignclient.mc.req;

public class SetMCTagTxtParam_out {
    private  String mtssid;//会议ssid

    private String mcType;//会议采用版本，现阶段只有AVST

    private String userssid;//用户ssid

    private String tagtxt;//打点标记文本

    private String starttime;//语音识别时间标识

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getTagtxt() {
        return tagtxt;
    }

    public void setTagtxt(String tagtxt) {
        this.tagtxt = tagtxt;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}
