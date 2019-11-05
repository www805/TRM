package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetPhreportsParam extends Page {
    private String recordssid;//关联的笔录ssid

    private String phreportname;//报告名称

    private String starttime_start;//报告时间开始

    private String starttime_end;//报告结束时间

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public String getPhreportname() {
        return phreportname;
    }

    public void setPhreportname(String phreportname) {
        this.phreportname = phreportname;
    }

    public String getStarttime_start() {
        return starttime_start;
    }

    public void setStarttime_start(String starttime_start) {
        this.starttime_start = starttime_start;
    }

    public String getStarttime_end() {
        return starttime_end;
    }

    public void setStarttime_end(String starttime_end) {
        this.starttime_end = starttime_end;
    }
}
