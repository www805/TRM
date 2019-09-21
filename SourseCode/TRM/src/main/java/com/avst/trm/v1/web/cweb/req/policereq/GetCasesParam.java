package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetCasesParam extends Page {
    private String casename;//案件名称

    private String username;//案件人员名称

    private String occurrencetime_start;//案发时间开始

    private String occurrencetime_end;//案发结束时间

    private String starttime_start;//谈话时间开始

    private String starttime_end;//谈话结束时间

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
