package com.avst.trm.v1.web.req.basereq;


import com.avst.trm.v1.common.util.baseaction.Page;

/**
 * 管理员用户列表查询条件
 */
public class GetUserListParam extends Page {

    private String username;//用户名称

    private String loginaccount;//登陆账号

    private String workunitssid;//工作单位ssid

    private String rolessid; //用户角色ssid

    private Integer adminbool;//用户状态;1正常; 2禁用

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginaccount() {
        return loginaccount;
    }

    public void setLoginaccount(String loginaccount) {
        this.loginaccount = loginaccount;
    }

    public String getWorkunitssid() {
        return workunitssid;
    }

    public void setWorkunitssid(String workunitssid) {
        this.workunitssid = workunitssid;
    }

    public String getRolessid() {
        return rolessid;
    }

    public void setRolessid(String rolessid) {
        this.rolessid = rolessid;
    }

    public Integer getAdminbool() {
        return adminbool;
    }

    public void setAdminbool(Integer adminbool) {
        this.adminbool = adminbool;
    }

    @Override
    public String toString() {
        return "GetUserListParam{" +
                "username='" + username + '\'' +
                ", loginaccount='" + loginaccount + '\'' +
                ", workunitssid='" + workunitssid + '\'' +
                ", rolessid='" + rolessid + '\'' +
                ", adminbool=" + adminbool +
                '}';
    }
}
