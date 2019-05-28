package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetCasesParam extends Page {
    private String casename;//案件名称

    private String casenum;//案件编号

    private String username;//案件人员名称


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
