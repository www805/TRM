package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;

import java.util.List;

public class GetCaseByIdVO {
    private List<Case> cases;

    private List<Case> othercases;//除开自自己以外全部的案件


    public List<Case> getOthercases() {
        return othercases;
    }

    public void setOthercases(List<Case> othercases) {
        this.othercases = othercases;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
}
