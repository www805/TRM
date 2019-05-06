package com.avst.trm.v1.web.sweb.req;

public class BaseReq {

    private String actionid;

    private String pageid;

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getActionid() {
        return actionid;
    }

    public void setActionid(String actionid) {
        this.actionid = actionid;
    }
}
