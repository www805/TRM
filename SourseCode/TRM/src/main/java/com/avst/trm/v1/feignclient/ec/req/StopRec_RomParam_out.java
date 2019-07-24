package com.avst.trm.v1.feignclient.ec.req;

public class StopRec_RomParam_out extends BaseAvstParam {

    private int dx;//dx 取值定义0：全部光驱 1：光驱一 2：光驱二

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }
}
