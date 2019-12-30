package com.avst.trm.v1.web.cweb.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.police.entity.Police_namingrule;

import java.util.List;

public class UpdateServerconfigParam extends Base_serverconfig {
    private String client_url;

    private List<Police_namingrule> namingrules;//命名规则

    public List<Police_namingrule> getNamingrules() {
        return namingrules;
    }

    public void setNamingrules(List<Police_namingrule> namingrules) {
        this.namingrules = namingrules;
    }

    public String getClient_url() {
        return client_url;
    }

    public void setClient_url(String client_url) {
        this.client_url = client_url;
    }
}
