package com.avst.trm.v1.web.sweb.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.web.sweb.req.basereq.GetUserListParam;

import java.util.List;

public class GetUserListVO {
    private List<AdminAndWorkunit> pagelist;

    private GetUserListParam pageparam;

    public List<AdminAndWorkunit> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<AdminAndWorkunit> pagelist) {
        this.pagelist = pagelist;
    }

    public GetUserListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetUserListParam pageparam) {
        this.pageparam = pageparam;
    }
}
