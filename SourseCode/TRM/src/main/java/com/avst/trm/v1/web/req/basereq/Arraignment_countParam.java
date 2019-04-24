package com.avst.trm.v1.web.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

import java.util.Date;

public class Arraignment_countParam extends Page {

    private Date starttime;

    private Date endtime;

    private String times;

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
