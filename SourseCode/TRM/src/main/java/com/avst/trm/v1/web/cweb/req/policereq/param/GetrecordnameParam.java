package com.avst.trm.v1.web.cweb.req.policereq.param;

public class GetrecordnameParam {
    private String username;//嫌疑人
    private String cardnum;//证件号
    private String recordtypename;//类型名称
    private String casename;//案件名称
    private String asknum;//提讯次数
    private String recordplace;//办案地点
    private String times;//证件号17位时间
    private String modelname;//模板名称 会议名称


    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getRecordtypename() {
        return recordtypename;
    }

    public void setRecordtypename(String recordtypename) {
        this.recordtypename = recordtypename;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getAsknum() {
        return asknum;
    }

    public void setAsknum(String asknum) {
        this.asknum = asknum;
    }

    public String getRecordplace() {
        return recordplace;
    }

    public void setRecordplace(String recordplace) {
        this.recordplace = recordplace;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
