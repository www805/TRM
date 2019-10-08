package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-23
 */
public class Base_serverconfig extends Model<Base_serverconfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器配置表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id=1;

    /**
     * 系统名称
     */
    private String sysname;

    /**
     * 系统logo地址
     */
    private String syslogo_filesavessid;

    /**
     * 客户端名称
     */
    private String clientname;

    /**
     * 客户端logo地址
     */
    private String client_filesavessid;

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

    private String sysmsg;

    private String companymsg;

    private String runbook_filesavessid;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public String getSysmsg() {
        return sysmsg;
    }

    public void setSysmsg(String sysmsg) {
        this.sysmsg = sysmsg;
    }

    public String getCompanymsg() {
        return companymsg;
    }

    public void setCompanymsg(String companymsg) {
        this.companymsg = companymsg;
    }

    public String getRunbook_filesavessid() {
        return runbook_filesavessid;
    }

    public void setRunbook_filesavessid(String runbook_filesavessid) {
        this.runbook_filesavessid = runbook_filesavessid;
    }

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
    public String getSyslogo_filesavessid() {
        return syslogo_filesavessid;
    }

    public void setSyslogo_filesavessid(String syslogo_filesavessid) {
        this.syslogo_filesavessid = syslogo_filesavessid;
    }
    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }
    public String getClient_filesavessid() {
        return client_filesavessid;
    }

    public void setClient_filesavessid(String client_filesavessid) {
        this.client_filesavessid = client_filesavessid;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
        ", syslogo_filesavessid=" + syslogo_filesavessid +
        ", clientname=" + clientname +
        ", client_filesavessid=" + client_filesavessid +
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
