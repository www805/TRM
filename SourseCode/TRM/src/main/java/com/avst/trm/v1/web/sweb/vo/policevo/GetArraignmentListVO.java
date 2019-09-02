package com.avst.trm.v1.web.sweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentListParam;

import java.util.List;

public class GetArraignmentListVO {
    private GetArraignmentListParam pageparam;

    private List<Case> pagelist;


    public GetArraignmentListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetArraignmentListParam pageparam) {
        this.pageparam = pageparam;
    }

    public List<Case> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Case> pagelist) {
        this.pagelist = pagelist;
    }
}
