package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;

import java.util.List;

public class GetCaseByIdVO {
    private List<CaseAndUserInfo> cases;

    public List<CaseAndUserInfo> getCases() {
        return cases;
    }

    public void setCases(List<CaseAndUserInfo> cases) {
        this.cases = cases;
    }
}
