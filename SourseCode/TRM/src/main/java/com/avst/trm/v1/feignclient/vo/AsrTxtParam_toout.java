package com.avst.trm.v1.feignclient.vo;

/**
 * 服务返回文本的集合
 */
public class AsrTxtParam_toout {

    private String txt;

    private String starttime;//本句话的开始时间，基于本次语音识别开始的ms数值

    private int asrnum=0;//本句话识别的次数

    private String asrtime;//识别的返回时间,long类型的ms，用于记录和标注结束的时间节点

    private int asrsort;//本次语音识别，这一句是第几句

    private String userssid;//会议用户ssid

    public void setAsrnum(int asrnum) {
        this.asrnum = asrnum;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public int getAsrsort() {
        return asrsort;
    }

    public void setAsrsort(int asrsort) {
        this.asrsort = asrsort;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getAsrnum() {
        return asrnum;
    }

    public String getAsrtime() {
        return asrtime;
    }

    public void setAsrtime(String asrtime) {
        this.asrtime = asrtime;
    }
}
