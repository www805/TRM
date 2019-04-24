package com.avst.trm.v1.web.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.web.req.policereq.GetArraignmentListParam;

import java.util.List;

public class GetArraignmentListVO {
    private GetArraignmentListParam pageparam;

    private List<CaseAndUserInfo> pagelist;


    public GetArraignmentListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetArraignmentListParam pageparam) {
        this.pageparam = pageparam;
    }

    public List<CaseAndUserInfo> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<CaseAndUserInfo> pagelist) {
        this.pagelist = pagelist;
    }
}
