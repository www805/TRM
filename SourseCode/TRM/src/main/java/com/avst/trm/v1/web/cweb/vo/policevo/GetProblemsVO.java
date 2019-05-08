package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.web.cweb.req.policereq.GetProblemsParam;

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
