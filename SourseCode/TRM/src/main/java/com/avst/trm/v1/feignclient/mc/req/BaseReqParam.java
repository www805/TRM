package com.avst.trm.v1.feignclient.mc.req;

/**
 * 一般放的都是权限验证的东西
 * 暂时没用
 */
public class BaseReqParam {

    private String asrEquipmentssid;//asr设备服务ssid，不定是设备ssid

    private String asrtype;//采用的是哪一种语音识别服务，avst公司自制的语音服务

    public String getAsrtype() {
        return asrtype;
    }

    public void setAsrtype(String asrtype) {
        this.asrtype = asrtype;
    }

    public String getAsrEquipmentssid() {
        return asrEquipmentssid;
    }

    public void setAsrEquipmentssid(String asrEquipmentssid) {
        this.asrEquipmentssid = asrEquipmentssid;
    }


}
