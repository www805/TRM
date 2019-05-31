package com.avst.trm.v1.feignclient.ec.req.ph;

public class BaseParam {

    private String phType;//测谎仪请求类型 avst PH_AVST

    private String polygraphssid;//测谎仪服务ssid;

    public String getPolygraphssid() {
        return polygraphssid;
    }

    public void setPolygraphssid(String polygraphssid) {
        this.polygraphssid = polygraphssid;
    }

    public String getPhType() {
        return phType;
    }

    public void setPhType(String phType) {
        this.phType = phType;
    }
}
