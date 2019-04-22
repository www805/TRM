package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;

import java.util.List;

public class KeywordListVO {

    private List<Base_keyword> pagelist;

    private Getlist3Param pageparam;

    public List<Base_keyword> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_keyword> pagelist) {
        this.pagelist = pagelist;
    }

    public Getlist3Param getPageparam() {
        return pageparam;
    }

    public void setPageparam(Getlist3Param pageparam) {
        this.pageparam = pageparam;
    }
}
