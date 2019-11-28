package com.avst.trm.v1.web.cweb.req.courtreq;

public class Exporttemplate_ueParam {
    private String recordssid;

    private Integer exporttype;//导出类型：1word 2pdf

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public Integer getExporttype() {
        return exporttype;
    }

    public void setExporttype(Integer exporttype) {
        this.exporttype = exporttype;
    }
}
