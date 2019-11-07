package com.avst.trm.v1.web.sweb.vo.basevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.web.sweb.req.basereq.GetworkunitListParam;

import java.util.List;

public class GetworkunitListVO {
    private List<Police_workunit> pagelist;

    private GetworkunitListParam pageparam;

    public List<Police_workunit> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Police_workunit> pagelist) {
        this.pagelist = pagelist;
    }

    public GetworkunitListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetworkunitListParam pageparam) {
        this.pageparam = pageparam;
    }
}
