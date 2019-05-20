package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

public class RecordUserInfos {
    private String userssid;//被询问用户ssid

    private String username;//被询问用户名称

    private String adminssid;

    private String adminname;

    private String otheradminssid;

    private String otheradminname;

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
