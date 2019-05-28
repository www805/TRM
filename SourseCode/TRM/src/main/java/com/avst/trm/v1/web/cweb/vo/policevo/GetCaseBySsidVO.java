package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;

public class GetCaseBySsidVO {
    private CaseAndUserInfo caseAndUserInfo;

    public CaseAndUserInfo getCaseAndUserInfo() {
        return caseAndUserInfo;
    }

    public void setCaseAndUserInfo(CaseAndUserInfo caseAndUserInfo) {
        this.caseAndUserInfo = caseAndUserInfo;
    }
}
