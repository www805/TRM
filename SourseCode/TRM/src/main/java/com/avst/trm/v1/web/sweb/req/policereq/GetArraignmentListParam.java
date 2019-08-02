package com.avst.trm.v1.web.sweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetArraignmentListParam extends Page {
    private String casename;//案件名称

    private String casenum;//案件编号

    private String username;//案件人员名称

    private String occurrencetime_start;//案发时间开始

    private String occurrencetime_end;//案发结束时间

    public String getOccurrencetime_start() {
        return occurrencetime_start;
    }

    public void setOccurrencetime_start(String occurrencetime_start) {
        this.occurrencetime_start = occurrencetime_start;
    }

    public String getOccurrencetime_end() {
        return occurrencetime_end;
    }

    public void setOccurrencetime_end(String occurrencetime_end) {
        this.occurrencetime_end = occurrencetime_end;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getCasenum() {
        return casenum;
    }

    public void setCasenum(String casenum) {
        this.casenum = casenum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
