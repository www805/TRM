package com.avst.trm.v1.feignclient.ec.req;

public class GetFDStateParam extends BaseAvstParam{
    /**
     * 状态类型，需要什么状态的返回，设备状态返回数据太多，需要去选择一下
     */
    private int stateType=0;//0返回全部

    public int getStateType() {
        return stateType;
    }

    public void setStateType(int stateType) {
        this.stateType = stateType;
    }

}
