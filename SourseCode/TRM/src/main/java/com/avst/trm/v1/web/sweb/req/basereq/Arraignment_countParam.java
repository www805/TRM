package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class Arraignment_countParam extends Page {

    private String starttime;

    private String endtime;

    private String username;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Arraignment_countParam{" +
                "starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
