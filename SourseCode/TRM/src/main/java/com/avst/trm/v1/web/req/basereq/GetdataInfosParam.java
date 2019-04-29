package com.avst.trm.v1.web.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetdataInfosParam extends Page {
    private String dataname_cn;//表单中文名称

    public String getDataname_cn() {
        return dataname_cn;
    }

    public void setDataname_cn(String dataname_cn) {
        this.dataname_cn = dataname_cn;
    }
}
