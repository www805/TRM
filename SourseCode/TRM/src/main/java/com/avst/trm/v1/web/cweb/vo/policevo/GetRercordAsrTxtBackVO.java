package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;

public class GetRercordAsrTxtBackVO  {
    private String txt;

    private String starttime;//本句话的开始时间，基于本次语音识别开始的ms数值

    private int asrnum=0;//本句话识别的次数

    private String asrtime;//识别的返回时间,long类型的ms，用于记录和标注结束的时间节点

    private int asrsort;//本次语音识别，这一句是第几句

    private String userssid;//会议用户ssid

    private String username;//会议用户名称

    private Integer usertype;//1、询问人2被询问人

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

    public void setAsrnum(int asrnum) {
        this.asrnum = asrnum;
    }

    public String getAsrtime() {
        return asrtime;
    }

    public void setAsrtime(String asrtime) {
        this.asrtime = asrtime;
    }

    public int getAsrsort() {
        return asrsort;
    }

    public void setAsrsort(int asrsort) {
        this.asrsort = asrsort;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    @Override
    public String toString() {
        return "GetRercordAsrTxtBackVO{" +
                "txt='" + txt + '\'' +
                ", starttime='" + starttime + '\'' +
                ", asrnum=" + asrnum +
                ", asrtime='" + asrtime + '\'' +
                ", asrsort=" + asrsort +
                ", userssid='" + userssid + '\'' +
                ", username='" + username + '\'' +
                ", usertype=" + usertype +
                '}';
    }
}
