package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.web.req.basereq.KeywordParam;

import java.util.List;

public class KeywordListVO {

    private List<Base_keyword> pagelist;

    private KeywordParam pageparam;

    public List<Base_keyword> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_keyword> pagelist) {
        this.pagelist = pagelist;
    }

    public KeywordParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(KeywordParam pageparam) {
        this.pageparam = pageparam;
    }
}
