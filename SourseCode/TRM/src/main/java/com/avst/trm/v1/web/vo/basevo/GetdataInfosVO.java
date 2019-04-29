package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DatainfoAndType;
import com.avst.trm.v1.web.req.basereq.GetdataInfosParam;

import java.util.List;

public class GetdataInfosVO {
    private List<DatainfoAndType> pagelist;

    private GetdataInfosParam pageparam;

    public List<DatainfoAndType> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<DatainfoAndType> pagelist) {
        this.pagelist = pagelist;
    }

    public GetdataInfosParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetdataInfosParam pageparam) {
        this.pageparam = pageparam;
    }
}
