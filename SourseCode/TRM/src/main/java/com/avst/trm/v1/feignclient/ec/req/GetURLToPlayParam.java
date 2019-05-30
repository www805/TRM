package com.avst.trm.v1.feignclient.ec.req;

public class GetURLToPlayParam {

    private String ssType;//请求的是哪一个存储服务，avst ss_avst

    private String iid;//文件在源设备中的唯一识别码

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}
