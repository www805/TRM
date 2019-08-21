package com.avst.trm.v1.feignclient.ec.req;

public class GetBurnTimeParam extends BaseAvstParam {

    private Integer burntime;

    public Integer getBurntime() {
        return burntime;
    }

    public void setBurntime(Integer burntime) {
        this.burntime = burntime;
    }
}
