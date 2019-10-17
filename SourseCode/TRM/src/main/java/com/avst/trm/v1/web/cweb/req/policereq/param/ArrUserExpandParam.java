package com.avst.trm.v1.web.cweb.req.policereq.param;

/**
 * 添加人员案件信息参数:多角色(针对已存在的用户)
 */
public class ArrUserExpandParam {
    //多角色使用
    private String userinfogradessid;//人员级别表ssid
    private String userssid;//级别名称

    public String getUserinfogradessid() {
        return userinfogradessid;
    }

    public void setUserinfogradessid(String userinfogradessid) {
        this.userinfogradessid = userinfogradessid;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }
}
