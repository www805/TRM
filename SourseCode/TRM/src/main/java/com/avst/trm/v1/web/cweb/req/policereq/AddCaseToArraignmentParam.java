package com.avst.trm.v1.web.cweb.req.policereq;

public class AddCaseToArraignmentParam {
    private String casessid;
    private String adminssid;
    private String otheradminssid;
    private String recordadminssid;
    private String recordtypessid;
    private String recordplace;
    private Integer askobj;
    private Integer asknum;

    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }

    public String getAdminssid() {
        return adminssid;
    }

    public void setAdminssid(String adminssid) {
        this.adminssid = adminssid;
    }

    public String getOtheradminssid() {
        return otheradminssid;
    }

    public void setOtheradminssid(String otheradminssid) {
        this.otheradminssid = otheradminssid;
    }

    public String getRecordadminssid() {
        return recordadminssid;
    }

    public void setRecordadminssid(String recordadminssid) {
        this.recordadminssid = recordadminssid;
    }

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }

    public String getRecordplace() {
        return recordplace;
    }

    public void setRecordplace(String recordplace) {
        this.recordplace = recordplace;
    }

    public Integer getAskobj() {
        return askobj;
    }

    public void setAskobj(Integer askobj) {
        this.askobj = askobj;
    }

    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
    }
}
