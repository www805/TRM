package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;

public class GetRecordByIdVO {
    private Record record;//笔录信息

    private CaseAndUserInfo caseAndUserInfo;//案件信息

    public CaseAndUserInfo getCaseAndUserInfo() {
        return caseAndUserInfo;
    }

    public void setCaseAndUserInfo(CaseAndUserInfo caseAndUserInfo) {
        this.caseAndUserInfo = caseAndUserInfo;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
