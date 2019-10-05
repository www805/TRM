package com.avst.trm.v1.common.util.log;

import java.util.List;

public class LogVO {

    private LogPageParam logPageParam;

    private List<LogParam> logList;

    public LogPageParam getLogPageParam() {
        return logPageParam;
    }

    public void setLogPageParam(LogPageParam logPageParam) {
        this.logPageParam = logPageParam;
    }

    public List<LogParam> getLogList() {
        return logList;
    }

    public void setLogList(List<LogParam> logList) {
        this.logList = logList;
    }
}
