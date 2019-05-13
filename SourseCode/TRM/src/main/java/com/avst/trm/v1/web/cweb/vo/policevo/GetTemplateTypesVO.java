package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Templatetype;
import com.avst.trm.v1.web.cweb.req.policereq.ProblemtypeParam;

import java.util.List;

public class GetTemplateTypesVO {

    private List<Templatetype> pagelist;

    private ProblemtypeParam pageparam;

    public List<Templatetype> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Templatetype> pagelist) {
        this.pagelist = pagelist;
    }

    public ProblemtypeParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(ProblemtypeParam pageparam) {
        this.pageparam = pageparam;
    }
}
