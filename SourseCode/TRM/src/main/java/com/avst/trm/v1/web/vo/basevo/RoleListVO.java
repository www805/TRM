package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;

import java.util.List;

public class RoleListVO {

    private List<Base_role> pagelist;

    private Getlist3Param pageparam;

    public List<Base_role> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_role> pagelist) {
        this.pagelist = pagelist;
    }

    public Getlist3Param getPageparam() {
        return pageparam;
    }

    public void setPageparam(Getlist3Param pageparam) {
        this.pageparam = pageparam;
    }
}
