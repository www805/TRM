package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;

import java.util.List;

public class AddRecordParam {
    private String recordssid;//笔录ssid

    private List<RecordToProblem> recordToProblems;

    private Integer recordbool;//笔录状态  1进行中 2已结束 0未开始

    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public List<RecordToProblem> getRecordToProblems() {
        return recordToProblems;
    }

    public void setRecordToProblems(List<RecordToProblem> recordToProblems) {
        this.recordToProblems = recordToProblems;
    }
}
