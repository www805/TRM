package com.avst.trm.v1.web.cweb.vo.courtvo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Userinfograde;
import com.avst.trm.v1.web.cweb.req.courtreq.GetUserinfogradePageParam;

import java.util.List;

public class GetUserinfogradePageVO {
    private List<Userinfograde> pagelist;//全部笔录

    private GetUserinfogradePageParam pageparam;

    public List<Userinfograde> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Userinfograde> pagelist) {
        this.pagelist = pagelist;
    }

    public GetUserinfogradePageParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetUserinfogradePageParam pageparam) {
        this.pageparam = pageparam;
    }
}
