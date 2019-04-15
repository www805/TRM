package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetProblemsParam;

import java.util.List;

public class GetProblemsVO {
    private List<Problem> problems;

    private GetProblemsParam problemsParam;

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public GetProblemsParam getProblemsParam() {
        return problemsParam;
    }

    public void setProblemsParam(GetProblemsParam problemsParam) {
        this.problemsParam = problemsParam;
    }
}
