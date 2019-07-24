package com.avst.trm.v1.feignclient.mc.vo;

import com.avst.trm.v1.feignclient.mc.vo.param.Avstmt_modeltd;

import java.util.List;

public class GetTdByModelSsidVO {
    private List<Avstmt_modeltd> modeltds;

    public List<Avstmt_modeltd> getModeltds() {
        return modeltds;
    }

    public void setModeltds(List<Avstmt_modeltd> modeltds) {
        this.modeltds = modeltds;
    }
}
