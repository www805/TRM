package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problemtype;
import com.avst.trm.v1.web.cweb.req.policereq.ProblemtypeParam;

import java.util.List;

public class GetProblemTypesVO {

    private List<Problemtype> pagelist;

    private ProblemtypeParam pageparam;

    public List<Problemtype> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Problemtype> problems) {
        this.pagelist = problems;
    }

    public ProblemtypeParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(ProblemtypeParam problemsParam) {
        this.pageparam = problemsParam;
    }
}
