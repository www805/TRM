package com.avst.trm.v1.web.sweb.vo.basevo;

import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;

import java.util.List;
import java.util.Map;

public class GetServerIpVO {

    private Map<String, List<GetNetworkConfigureVO>> trmipMap;
    private Object modeltds;

    public Map<String, List<GetNetworkConfigureVO>> getTrmipMap() {
        return trmipMap;
    }

    public void setTrmipMap(Map<String, List<GetNetworkConfigureVO>> trmipMap) {
        this.trmipMap = trmipMap;
    }

    public Object getModeltds() {
        return modeltds;
    }

    public void setModeltds(Object modeltds) {
        this.modeltds = modeltds;
    }
}
