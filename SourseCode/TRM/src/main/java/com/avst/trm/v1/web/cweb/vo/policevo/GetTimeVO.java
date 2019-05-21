package com.avst.trm.v1.web.cweb.vo.policevo;

public class GetTimeVO {
    private  String currenttime;//当前时间

    private String yesterdaytime;//昨日时间

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getYesterdaytime() {
        return yesterdaytime;
    }

    public void setYesterdaytime(String yesterdaytime) {
        this.yesterdaytime = yesterdaytime;
    }
}
