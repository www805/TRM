package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfograde;

/**
 * 多角色
 */
public class Usergrade extends Police_userinfograde {
    private String username;//用户名称
    private String userssid;//用户ssid

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

}
