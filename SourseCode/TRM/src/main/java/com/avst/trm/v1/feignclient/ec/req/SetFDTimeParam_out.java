package com.avst.trm.v1.feignclient.ec.req;

public class SetFDTimeParam_out extends BaseAvstParam {

    private String timeZone="UTC+08:00";//时区

    private String dateTime;//2019-11-12 12:12:12

    //这里以后要做请求时间控制，上一次的改设备的请求时间跟这一次的请求时间要有一定的间隔

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
