package com.avst.trm.v1.feignclient.mc.vo;

/**
 * 服务返回测谎数据的集合
 */
public class PhDataParam_toout<T> {

    private long reqTime;//测谎数据请求时间 s秒，相对开始记录测谎的时间,,可以有重复的秒数，这1秒请求2次了，也没关系，

    private T t;//真正的测谎数据集合

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
