package com.avst.trm.v1.web.cweb.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.web.cweb.req.basereq.GetPackdownListParam;

import java.util.List;

public class GetPackdownListVO {
    private List<Base_filesave> pagelist;

    private GetPackdownListParam pageparam;

    public List<Base_filesave> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_filesave> pagelist) {
        this.pagelist = pagelist;
    }

    public GetPackdownListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetPackdownListParam pageparam) {
        this.pageparam = pageparam;
    }
}
