package com.avst.trm.v1.web.standaloneweb.req;

import com.avst.trm.v1.feignclient.ec.req.BaseAvstParam;

public class SetNTPParam extends BaseAvstParam {

    private String ntpip;
    private String timeInterval;
    private String ntpprot;

    public String getNtpip() {
        return ntpip;
    }

    public void setNtpip(String ntpip) {
        this.ntpip = ntpip;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getNtpprot() {
        return ntpprot;
    }

    public void setNtpprot(String ntpprot) {
        this.ntpprot = ntpprot;
    }
}
