package com.avst.trm.v1.feignclient.mc.vo;


import com.avst.trm.v1.feignclient.mc.vo.param.UserETParam;

import java.util.List;

public class StartMCVO {

    private String mtssid;//会议ssid

    private List<UserETParam> useretlist; //用户设备直播信息

    private int recordnum;//录音设备开启个数

    private int asrnum;//语音识别服务开启个数

    private int polygraphnum;//测谎仪服务开启个数

    public List<UserETParam> getUseretlist() {
        return useretlist;
    }

    public void setUseretlist(List<UserETParam> useretlist) {
        this.useretlist = useretlist;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public int getRecordnum() {
        return recordnum;
    }

    public void setRecordnum(int recordnum) {
        this.recordnum = recordnum;
    }

    public int getAsrnum() {
        return asrnum;
    }


    public void setAsrnum(int asrnum) {
        this.asrnum = asrnum;
    }

    public int getPolygraphnum() {
        return polygraphnum;
    }

    public void setPolygraphnum(int polygraphnum) {
        this.polygraphnum = polygraphnum;
    }
}
