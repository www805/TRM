package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Notification;
import com.avst.trm.v1.web.cweb.req.policereq.GetNotificationParam;

import java.util.List;

public class GetNotificationVO {

    private List<Notification> pagelist;
    private GetNotificationParam pageparam;

    public List<Notification> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Notification> pagelist) {
        this.pagelist = pagelist;
    }

    public GetNotificationParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetNotificationParam pageparam) {
        this.pageparam = pageparam;
    }
}
