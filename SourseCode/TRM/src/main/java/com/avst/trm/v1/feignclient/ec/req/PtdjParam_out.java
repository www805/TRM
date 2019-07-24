package com.avst.trm.v1.feignclient.ec.req;

import java.util.List;

public class PtdjParam_out extends BaseAvstParam{

    private int ct=15;//1-120 单位（秒）

    private List<String> lineList;//片头叠加的值，是有顺序的，即使中间有的没有填，也要给一个空字符串，以防错位

    public List<String> getLineList() {
        return lineList;
    }

    public void setLineList(List<String> lineList) {
        this.lineList = lineList;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }
}