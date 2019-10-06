package com.avst.trm.v1.feignclient.ec.vo.param;

public class FDLogItem {

    private String date;//日志时间

    private String tp;//ftp_oper,ftp日志类型，sys_oper常规日志类型

    private String message;//日志信息

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
