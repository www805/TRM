package com.avst.trm.v1.web.cweb.req.courtreq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetUserinfogradePageParam extends Page {
    private String gradename;//等级名称

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }
}
