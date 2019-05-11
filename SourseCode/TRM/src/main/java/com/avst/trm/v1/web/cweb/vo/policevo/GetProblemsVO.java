package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.web.cweb.req.policereq.GetProblemsParam;

import java.util.List;

public class GetProblemsVO {
    private List<Problem> pagelist;

    private GetProblemsParam pageparam;

    public List<Problem> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Problem> problems) {
        this.pagelist = problems;
    }

    public GetProblemsParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetProblemsParam problemsParam) {
        this.pageparam = problemsParam;
    }
}
