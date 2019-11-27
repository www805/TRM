package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;

public class ProblemByIdVO {

    private Problem problem;

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
