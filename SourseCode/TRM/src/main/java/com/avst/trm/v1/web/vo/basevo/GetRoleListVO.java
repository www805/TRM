package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.web.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;

import java.util.List;

public class GetRoleListVO {

    private List<Base_role> pagelist;

    private GetRoleListParam pageparam;

    public List<Base_role> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_role> pagelist) {
        this.pagelist = pagelist;
    }

    public GetRoleListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetRoleListParam pageparam) {
        this.pageparam = pageparam;
    }
}
