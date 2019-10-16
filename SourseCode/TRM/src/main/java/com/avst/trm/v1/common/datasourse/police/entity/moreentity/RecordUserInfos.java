package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import java.util.List;

public class RecordUserInfos {
    private String arraignmentssid;//本次提讯ssid

    private String userssid;//被询问用户ssid

    private String username;//被询问用户名称

    private String adminssid;

    private String adminname;

    private String otheradminssid;

    private String otheradminname;

    private String recordadminssid;//记录人ssid

    private String recordadminname;//记录人

    private String workunitssid1;

    private String workunitname1;

    private String workunitssid2;

    private String workunitname2;

    private String workunitssid3;

    private String workunitname3;

    private UserInfo userInfo;//嫌疑人详细信息

    private List<Usergrade> usergrades;//其他角色 提讯拓展表

    public String getArraignmentssid() {
        return arraignmentssid;
    }

    public void setArraignmentssid(String arraignmentssid) {
        this.arraignmentssid = arraignmentssid;
    }

    public List<Usergrade> getUsergrades() {
        return usergrades;
    }

    public void setUsergrades(List<Usergrade> usergrades) {
        this.usergrades = usergrades;
    }

    public String getWorkunitname1() {
        return workunitname1;
    }

    public void setWorkunitname1(String workunitname1) {
        this.workunitname1 = workunitname1;
    }

    public String getWorkunitname2() {
        return workunitname2;
    }

    public void setWorkunitname2(String workunitname2) {
        this.workunitname2 = workunitname2;
    }

    public String getWorkunitname3() {
        return workunitname3;
    }

    public void setWorkunitname3(String workunitname3) {
        this.workunitname3 = workunitname3;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getWorkunitssid1() {
        return workunitssid1;
    }

    public void setWorkunitssid1(String workunitssid1) {
        this.workunitssid1 = workunitssid1;
    }

    public String getWorkunitssid2() {
        return workunitssid2;
    }

    public void setWorkunitssid2(String workunitssid2) {
        this.workunitssid2 = workunitssid2;
    }

    public String getWorkunitssid3() {
        return workunitssid3;
    }

    public void setWorkunitssid3(String workunitssid3) {
        this.workunitssid3 = workunitssid3;
    }

    public String getRecordadminssid() {
        return recordadminssid;
    }

    public void setRecordadminssid(String recordadminssid) {
        this.recordadminssid = recordadminssid;
    }

    public String getRecordadminname() {
        return recordadminname;
    }

    public void setRecordadminname(String recordadminname) {
        this.recordadminname = recordadminname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getAdminssid() {
        return adminssid;
    }

    public void setAdminssid(String adminssid) {
        this.adminssid = adminssid;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getOtheradminssid() {
        return otheradminssid;
    }

    public void setOtheradminssid(String otheradminssid) {
        this.otheradminssid = otheradminssid;
    }

    public String getOtheradminname() {
        return otheradminname;
    }

    public void setOtheradminname(String otheradminname) {
        this.otheradminname = otheradminname;
    }
}
