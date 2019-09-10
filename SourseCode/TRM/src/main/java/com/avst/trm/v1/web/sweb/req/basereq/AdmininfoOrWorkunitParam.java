package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;

public class AdmininfoOrWorkunitParam extends Base_admininfo {

    private String workname;

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }
}
