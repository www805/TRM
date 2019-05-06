package com.avst.trm.v1.web.sweb.req.basereq;

import java.util.List;

public class CloseddownServerParam {
    private String downserverssid;//同步服务器的ssid

    private List<String> datainfossids;//表单ssid集合

    public String getDownserverssid() {
        return downserverssid;
    }

    public void setDownserverssid(String downserverssid) {
        this.downserverssid = downserverssid;
    }

    public List<String> getDatainfossids() {
        return datainfossids;
    }

    public void setDatainfossids(List<String> datainfossids) {
        this.datainfossids = datainfossids;
    }
}
