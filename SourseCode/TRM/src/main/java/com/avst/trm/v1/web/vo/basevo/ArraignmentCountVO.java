package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.web.req.basereq.Arraignment_countParam;

import java.util.List;

public class ArraignmentCountVO {

    private List<Base_arraignmentCount> pagelist;

    private Arraignment_countParam pageparam;

    public List<Base_arraignmentCount> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_arraignmentCount> pagelist) {
        this.pagelist = pagelist;
    }

    public Arraignment_countParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(Arraignment_countParam pageparam) {
        this.pageparam = pageparam;
    }
}
