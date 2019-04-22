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
public class Base_serverconfig extends Model<Base_serverconfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器配置表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统名称
     */
    private String sysname;

    /**
     * 系统logo地址
     */
    private String syslogourl;

    /**
     * 客户端名称
     */
    private String clientname;

    /**
     * 客户端logo地址
     */
    private String clienturl;

    /**
     * 服务器IP
     */
    private String serverip;

    /**
     * 服务器端口
     */
    private String serverport;

    /**
     * 1已授权，-1未授权
     */
    private Integer authorizebool;

    /**
     * 最基本的类型（如：court，说明要使用court_client和court_web 2类type）
     */
    private String type;

    /**
     * 工作开始时间
     */
    private Date workstarttime;

    /**
     * 同步工作天数
     */
    private Integer workdays;

    /**
     * 授权的单位编号:一个单位有可能有多个客户端服务器，所以需要对客户端服务器做标记
     */
    private Integer authorizesortnum;

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
    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }
    public String getSyslogourl() {
        return syslogourl;
    }

    public void setSyslogourl(String syslogourl) {
        this.syslogourl = syslogourl;
    }
    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }
    public String getClienturl() {
        return clienturl;
    }

    public void setClienturl(String clienturl) {
        this.clienturl = clienturl;
    }
    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }
    public String getServerport() {
        return serverport;
    }

    public void setServerport(String serverport) {
        this.serverport = serverport;
    }
    public Integer getAuthorizebool() {
        return authorizebool;
    }

    public void setAuthorizebool(Integer authorizebool) {
        this.authorizebool = authorizebool;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Date getWorkstarttime() {
        return workstarttime;
    }

    public void setWorkstarttime(Date workstarttime) {
        this.workstarttime = workstarttime;
    }
    public Integer getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Integer workdays) {
        this.workdays = workdays;
    }
    public Integer getAuthorizesortnum() {
        return authorizesortnum;
    }

    public void setAuthorizesortnum(Integer authorizesortnum) {
        this.authorizesortnum = authorizesortnum;
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
        return "Base_serverconfig{" +
        "id=" + id +
        ", sysname=" + sysname +
        ", syslogourl=" + syslogourl +
        ", clientname=" + clientname +
        ", clienturl=" + clienturl +
        ", serverip=" + serverip +
        ", serverport=" + serverport +
        ", authorizebool=" + authorizebool +
        ", type=" + type +
        ", workstarttime=" + workstarttime +
        ", workdays=" + workdays +
        ", authorizesortnum=" + authorizesortnum +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
