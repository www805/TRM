package com.avst.trm.v1.web.vo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * session缓存的用户信息
 */
public class AdminManage_session  {

    /**
     * 登陆账号
     */
    private String loginaccount;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态：1正常; 2禁用;默认1
     */
    private Integer adminbool;

    /**
     * 工作单位id
     */
    private Integer workunitid;

    /**
     * 单位中的排序,用于客户端的使用者（办案人员）
     */
    private Integer unitsort;

    /**
     * 注册时间
     */
    private Date registertime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 最后一次登陆时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastlogintime;

    private String ssid;

    public String getLoginaccount() {
        return loginaccount;
    }

    public void setLoginaccount(String loginaccount) {
        this.loginaccount = loginaccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdminbool() {
        return adminbool;
    }

    public void setAdminbool(Integer adminbool) {
        this.adminbool = adminbool;
    }

    public Integer getWorkunitid() {
        return workunitid;
    }

    public void setWorkunitid(Integer workunitid) {
        this.workunitid = workunitid;
    }

    public Integer getUnitsort() {
        return unitsort;
    }

    public void setUnitsort(Integer unitsort) {
        this.unitsort = unitsort;
    }

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
