package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.web.cweb.req.policereq.param.Phreportdata;

import java.util.List;

public class UploadPhreportParam {
    private String phreportname;//报告名称

    private String recordssid;//关联的笔录ssid

   /* private List<Phreportdata> phreportdataList;*///选择的情绪报告的数据

    private List<String> phreportdataList;//


    public String getPhreportname() {
        return phreportname;
    }

    public void setPhreportname(String phreportname) {
        this.phreportname = phreportname;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public List<String> getPhreportdataList() {
        return phreportdataList;
    }

    public void setPhreportdataList(List<String> phreportdataList) {
        this.phreportdataList = phreportdataList;
    }
}
