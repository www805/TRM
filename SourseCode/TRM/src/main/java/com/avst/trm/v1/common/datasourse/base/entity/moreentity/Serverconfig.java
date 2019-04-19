package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Serverconfig {
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
     * 工作开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date workstarttime;

    /**
     * 同步工作天数
     */
    private Integer workdays;

    /**
     * 1已授权，-1未授权
     */
    private Integer authorizebool;

    /**
     * 最基本的类型（如：court，说明要使用court_client和court_web 2类type）
     */
    private String type;

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
}
