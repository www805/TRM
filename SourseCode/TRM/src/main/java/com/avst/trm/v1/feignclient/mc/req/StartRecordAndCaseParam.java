package com.avst.trm.v1.feignclient.mc.req;

/**
 * 开始笔录传递的笔录案件参数
 */
public class StartRecordAndCaseParam {
    /**
     * 案件
     */
    //案件编号
    private String casenum;
    //案件名称
    private String casename;
    //案   由
    private String cause;
    //办案部门
    private String department;
    //被询(讯)问人
    private String askedname;
    //询(讯)问人
    private String askname;
    //录制(记录)问人
    private String recordername;
    //询(讯)问地址
    private String askplace;
    //案件类型
    private String casetypename;


    /**
     * 笔录
     */
    //笔录类型
    private String recordtypename;
    //笔录名称
    private String recordname;

    public String getCasenum() {
        return casenum;
    }

    public void setCasenum(String casenum) {
        this.casenum = casenum;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAskedname() {
        return askedname;
    }

    public void setAskedname(String askedname) {
        this.askedname = askedname;
    }

    public String getAskname() {
        return askname;
    }

    public void setAskname(String askname) {
        this.askname = askname;
    }

    public String getRecordername() {
        return recordername;
    }

    public void setRecordername(String recordername) {
        this.recordername = recordername;
    }

    public String getAskplace() {
        return askplace;
    }

    public void setAskplace(String askplace) {
        this.askplace = askplace;
    }

    public String getCasetypename() {
        return casetypename;
    }

    public void setCasetypename(String casetypename) {
        this.casetypename = casetypename;
    }

    public String getRecordtypename() {
        return recordtypename;
    }

    public void setRecordtypename(String recordtypename) {
        this.recordtypename = recordtypename;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }
}
