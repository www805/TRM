package com.avst.trm.v1.web.standaloneweb.req;

import com.avst.trm.v1.feignclient.ec.req.BaseAvstParam;

public class SetEcSystemTimeParam extends BaseAvstParam {

    private String systemTime;

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }
}
