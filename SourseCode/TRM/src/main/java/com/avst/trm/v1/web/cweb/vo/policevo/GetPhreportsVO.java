package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Phreport;
import com.avst.trm.v1.web.cweb.req.policereq.GetPhreportsParam;

import java.util.List;

public class GetPhreportsVO {
    private List<Phreport> pagelist;

    private GetPhreportsParam pageparam;

    public List<Phreport> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Phreport> pagelist) {
        this.pagelist = pagelist;
    }

    public GetPhreportsParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetPhreportsParam pageparam) {
        this.pageparam = pageparam;
    }
}
