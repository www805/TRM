package com.avst.trm.v1.web.sweb.vo.policevo;

import com.avst.trm.v1.web.sweb.req.basereq.Arraignment_countParam;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentCountParam;

import java.util.List;

public class GetArraignmentCountVO {
    private Arraignment_countParam pageparam;

    private List<GetArraignmentCountParam> pagelist;

    public Arraignment_countParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(Arraignment_countParam pageparam) {
        this.pageparam = pageparam;
    }

    public List<GetArraignmentCountParam> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<GetArraignmentCountParam> pagelist) {
        this.pagelist = pagelist;
    }

    @Override
    public String toString() {
        return "GetArraignmentCountVO{" +
                "pageparam=" + pageparam +
                ", pagelist=" + pagelist +
                '}';
    }
}
