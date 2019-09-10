package com.avst.trm.v1.web.sweb.req.policereq;

public class GetArraignmentCountParam {

    private String username; //用户名
    private String workname; //用户名
    private Integer arraignmentcount;   //询问总次数
    private Integer recordadmincount;  //记录总次数
    private Integer recordcount;   //笔录总次数

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public Integer getArraignmentcount() {
        return arraignmentcount;
    }

    public void setArraignmentcount(Integer arraignmentcount) {
        this.arraignmentcount = arraignmentcount;
    }

    public Integer getRecordadmincount() {
        return recordadmincount;
    }

    public void setRecordadmincount(Integer recordadmincount) {
        this.recordadmincount = recordadmincount;
    }

    public Integer getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(Integer recordcount) {
        this.recordcount = recordcount;
    }

    @Override
    public String toString() {
        return "GetArraignmentCountParam{" +
                "username='" + username + '\'' +
                ", workname='" + workname + '\'' +
                ", arraignmentcount=" + arraignmentcount +
                ", recordadmincount=" + recordadmincount +
                ", recordcount=" + recordcount +
                '}';
    }
}
