package com.avst.trm.v1.feignclient.mc.vo;

/**
 * 服务返回文本的集合
 */
public class AsrTxtParam_toout {

    private String userssid;//会议用户ssid

    private String txt;

    private String starttime;//本句话的开始时间，基于本次语音识别开始的ms数值

    private int asrnum=0;//本句话识别的次数

    private String asrtime;//识别的返回时间,long类型的ms，用于记录和标注结束的时间节点

    private int asrsort;//本次语音识别，这一句是第几句

    private String asrstartime;//asr开始识别开始时间

    private long subtractime;//时间差



    private String keyword_txt;//关键字检测之后的文本:trm加的

    public String getKeyword_txt() {
        return keyword_txt;
    }

    public void setKeyword_txt(String keyword_txt) {
        this.keyword_txt = keyword_txt;
    }

    public long getSubtractime() {
        return subtractime;
    }

    public void setSubtractime(long subtractime) {
        this.subtractime = subtractime;
    }

    public String getAsrstartime() {
        return asrstartime;
    }

    public void setAsrstartime(String asrstartime) {
        this.asrstartime = asrstartime;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public void setAsrnum(int asrnum) {
        this.asrnum = asrnum;
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

    @Override
    public String toString() {
        return "AsrTxtParam_toout{" +
                "userssid='" + userssid + '\'' +
                ", txt='" + txt + '\'' +
                ", starttime='" + starttime + '\'' +
                ", asrnum=" + asrnum +
                ", asrtime='" + asrtime + '\'' +
                ", asrsort=" + asrsort +
                ", asrstartime='" + asrstartime + '\'' +
                '}';
    }
}
