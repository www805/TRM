package com.avst.trm.v1.web.sweb.req.basereq;

public class StartdownServerParam {
    private String upserverip;//同步服务器的ip

    private String datainfossid;//表单ssid

    private Integer type;//同步的类型，0同步全部数据，1同步一个表的数据，2同步一条数据

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUpserverip() {
        return upserverip;
    }

    public void setUpserverip(String upserverip) {
        this.upserverip = upserverip;
    }

    public String getDatainfossid() {
        return datainfossid;
    }

    public void setDatainfossid(String datainfossid) {
        this.datainfossid = datainfossid;
    }
}
