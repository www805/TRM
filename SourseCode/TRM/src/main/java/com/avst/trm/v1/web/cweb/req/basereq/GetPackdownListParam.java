package com.avst.trm.v1.web.cweb.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetPackdownListParam extends Page {
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
