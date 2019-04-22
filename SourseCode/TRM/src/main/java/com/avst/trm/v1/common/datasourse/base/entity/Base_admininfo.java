package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-22
 */
public class Base_admininfo extends Model<Base_admininfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 系统用户表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
    private String workunitssid;

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
    private Date lastlogintime;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
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
    public String getWorkunitssid() {
        return workunitssid;
    }

    public void setWorkunitssid(String workunitssid) {
        this.workunitssid = workunitssid;
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
    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }
    public Integer getInteger1() {
        return integer1;
    }

    public void setInteger1(Integer integer1) {
        this.integer1 = integer1;
    }
    public Integer getInteger2() {
        return integer2;
    }

    public void setInteger2(Integer integer2) {
        this.integer2 = integer2;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Base_admininfo{" +
        "id=" + id +
        ", loginaccount=" + loginaccount +
        ", username=" + username +
        ", password=" + password +
        ", adminbool=" + adminbool +
        ", workunitssid=" + workunitssid +
        ", unitsort=" + unitsort +
        ", registertime=" + registertime +
        ", updatetime=" + updatetime +
        ", lastlogintime=" + lastlogintime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
