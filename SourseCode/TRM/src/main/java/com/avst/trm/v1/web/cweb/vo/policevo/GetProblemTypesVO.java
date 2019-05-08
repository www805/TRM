package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.Police_problemtype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problemtype;

import java.util.List;

public class GetProblemTypesVO {
    private List<Problemtype> problemtypes;

    public List<Problemtype> getProblemtypes() {
        return problemtypes;
    }

    public void setProblemtypes(List<Problemtype> problemtypes) {
        this.problemtypes = problemtypes;
    }
}
