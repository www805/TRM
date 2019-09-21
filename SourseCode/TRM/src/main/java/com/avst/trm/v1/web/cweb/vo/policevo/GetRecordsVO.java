package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordsParam;

import java.util.List;

public class GetRecordsVO {
    private List<Record> pagelist;//全部笔录

    private GetRecordsParam pageparam;

    public List<Record> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Record> pagelist) {
        this.pagelist = pagelist;
    }

    public GetRecordsParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetRecordsParam pageparam) {
        this.pageparam = pageparam;
    }
}
