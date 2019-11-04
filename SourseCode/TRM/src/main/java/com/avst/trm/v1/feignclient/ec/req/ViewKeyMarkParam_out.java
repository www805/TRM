package com.avst.trm.v1.feignclient.ec.req;

public class ViewKeyMarkParam_out extends BaseAvstParam {

    private String keyText;//重点标记的文件说明

    public String getKeyText() {
        return keyText;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }
}
