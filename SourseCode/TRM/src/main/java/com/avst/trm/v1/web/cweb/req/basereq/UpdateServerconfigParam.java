package com.avst.trm.v1.web.cweb.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;

public class UpdateServerconfigParam extends Base_serverconfig {
    private String client_url;

    public String getClient_url() {
        return client_url;
    }

    public void setClient_url(String client_url) {
        this.client_url = client_url;
    }
}
