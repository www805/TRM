package com.avst.trm.v1.web.cweb.req.policereq;

public class GetCaseByIdParam {
    private String userssid;//人员ssid
    private String creatoruuid;//创建人ssid

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getCreatoruuid() {
        return creatoruuid;
    }

    public void setCreatoruuid(String creatoruuid) {
        this.creatoruuid = creatoruuid;
    }
}


