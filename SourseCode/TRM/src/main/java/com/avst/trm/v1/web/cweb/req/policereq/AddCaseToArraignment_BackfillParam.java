package com.avst.trm.v1.web.cweb.req.policereq;

/**
 * 提讯回填参数
 */
public class AddCaseToArraignment_BackfillParam {
    private String casessid;
    private Integer recordbool;//根据笔录状态：休庭

    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }

    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
    }


}
