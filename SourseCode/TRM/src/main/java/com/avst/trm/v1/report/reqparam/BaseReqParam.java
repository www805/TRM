package com.avst.trm.v1.report.reqparam;

public class BaseReqParam {

    /**
     * 同步过程中的验证key
     */
    private String token;

    /**
     * 该服务器在单位中的排序的序号
     */
    private String sqNum;

    private String data;//用Jackjson转对象为字符串的结果集

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSqNum() {
        return sqNum;
    }

    public void setSqNum(String sqNum) {
        this.sqNum = sqNum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
