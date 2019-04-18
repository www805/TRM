package com.avst.trm.v1.common.cache.param;

public class CheckSQParam {

    private boolean checkbool=false;

    private String code;

    private String msg;

    public boolean isCheckbool() {
        return checkbool;
    }

    public void setCheckbool(boolean checkbool) {
        this.checkbool = checkbool;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
