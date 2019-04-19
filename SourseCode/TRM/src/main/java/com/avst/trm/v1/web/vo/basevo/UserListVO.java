package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;

import java.util.List;

public class UserListVO {

    private List<AdminAndAdminRole> pagelist;

    private Getlist3Param pageparam;

    public List<AdminAndAdminRole> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<AdminAndAdminRole> pagelist) {
        this.pagelist = pagelist;
    }

    public Getlist3Param getPageparam() {
        return pageparam;
    }

    public void setPageparam(Getlist3Param pageparam) {
        this.pageparam = pageparam;
    }
}
