package com.avst.trm.v1.web.standaloneweb.vo.param;

public class SoftwareParam {

    private String workTime;//本次系统开启到现在总共运行多长时间

    private String sysVersion;//软件版本

    private String sysStartTime;//系统开始使用的时间

    private String companyname;//所属公司

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getSysStartTime() {
        return sysStartTime;
    }

    public void setSysStartTime(String sysStartTime) {
        this.sysStartTime = sysStartTime;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
}
