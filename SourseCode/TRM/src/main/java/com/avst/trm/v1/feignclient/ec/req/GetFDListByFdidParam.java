package com.avst.trm.v1.feignclient.ec.req;

public class GetFDListByFdidParam {
    private String fdid;//

    private String fdType;

    public String getFdid() {
        return fdid;
    }

    public void setFdid(String fdid) {
        this.fdid = fdid;
    }

    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }
}
