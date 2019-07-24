package com.avst.trm.v1.feignclient.ec.req;

public class DvdOutOrInParam_out extends BaseAvstParam {

    private int dx=0;//现阶段这个参数只能是0，不区分单独的光驱

    private int inOrOut=1;//1进仓，2出仓

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }
}