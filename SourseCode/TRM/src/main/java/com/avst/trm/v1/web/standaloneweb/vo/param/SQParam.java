package com.avst.trm.v1.web.standaloneweb.vo.param;

import com.avst.trm.v1.common.util.sq.SQEntity;

import java.util.List;
import java.util.Map;

public class SQParam {

    private SQEntity sqEntity;
    private List<String> gnlist;
    private Map<String, Object> qtlist;

    public SQEntity getSqEntity() {
        return sqEntity;
    }

    public void setSqEntity(SQEntity sqEntity) {
        this.sqEntity = sqEntity;
    }

    public List<String> getGnlist() {
        return gnlist;
    }

    public void setGnlist(List<String> gnlist) {
        this.gnlist = gnlist;
    }

    public Map<String, Object> getQtlistp() {
        return qtlist;
    }

    public void setQtlistp(Map<String, Object> map) {
        this.qtlist = map;
    }
}
