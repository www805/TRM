package com.avst.trm.v1.feignclient.ec.req;

public class GetptdjconstParam_out extends BaseAvstParam {


    private boolean mustUpdateBool=false;//是否强制修改数据库中保存的片头

    public boolean isMustUpdateBool() {
        return mustUpdateBool;
    }

    public void setMustUpdateBool(boolean mustUpdateBool) {
        this.mustUpdateBool = mustUpdateBool;
    }
}
