package com.avst.trm.v1.feignclient.ec.vo.ph;

/**
 * 实时心理状态返回
 */
public class GetPolygraphAnalysisVO<T> {

    private T t;//不同的测谎仪返回的测试数据可能不一样，而我们返回给上层业务平台的也可能不一样

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
