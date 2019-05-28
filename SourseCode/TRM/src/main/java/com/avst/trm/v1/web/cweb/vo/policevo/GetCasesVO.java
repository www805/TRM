package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.web.cweb.req.policereq.GetCasesParam;

import java.util.List;

public class GetCasesVO {
    private GetCasesParam pageparam;

    private List<CaseAndUserInfo> pagelist;

    public GetCasesParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetCasesParam pageparam) {
        this.pageparam = pageparam;
    }

    public List<CaseAndUserInfo> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<CaseAndUserInfo> pagelist) {
        this.pagelist = pagelist;
    }
}
