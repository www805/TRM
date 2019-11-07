package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.common.util.baseaction.Page;

public class GetworkunitListParam extends Page {
    private String workname;

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }
}
