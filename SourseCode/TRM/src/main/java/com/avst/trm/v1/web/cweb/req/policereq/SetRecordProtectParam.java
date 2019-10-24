package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;

import java.util.List;

public class SetRecordProtectParam {
    private String recordssid;

    private String mtssid;//会议ssid

    private String iid;//视频iid

    private List<RecordToProblem> recordToProblems;

    public List<RecordToProblem> getRecordToProblems() {
        return recordToProblems;
    }

    public void setRecordToProblems(List<RecordToProblem> recordToProblems) {
        this.recordToProblems = recordToProblems;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }
}
