package com.avst.trm.v1.web.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetdownServersParam extends Page {
    private String upserverip;//上级服务器IP

    public String getUpserverip() {
        return upserverip;
    }

    public void setUpserverip(String upserverip) {
        this.upserverip = upserverip;
    }
}
