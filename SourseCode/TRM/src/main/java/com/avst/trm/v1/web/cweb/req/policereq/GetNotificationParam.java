package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetNotificationParam extends Page {

    private String notificationname;//告知书名称

    private String updatetime; //上传时间

    private String ssid;

    public String getNotificationname() {
        return notificationname;
    }

    public void setNotificationname(String notificationname) {
        this.notificationname = notificationname;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public String toString() {
        return "GetNotificationParam{" +
                "notificationname='" + notificationname + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", ssid='" + ssid + '\'' +
                '}';
    }
}
