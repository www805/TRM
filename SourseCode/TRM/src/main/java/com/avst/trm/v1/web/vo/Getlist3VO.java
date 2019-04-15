package com.avst.trm.v1.web.vo;

import com.avst.trm.v1.web.req.Getlist3Param;
import com.avst.trm.v1.web.vo.param.Getlist3VOParam;

import java.util.List;

public class Getlist3VO {

    private List<Getlist3VOParam> pagelist;

    private Getlist3Param pageparam;

    public List<Getlist3VOParam> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Getlist3VOParam> pagelist) {
        this.pagelist = pagelist;
    }

    public Getlist3Param getPageparam() {
        return pageparam;
    }

    public void setPageparam(Getlist3Param pageparam) {
        this.pageparam = pageparam;
    }
}
