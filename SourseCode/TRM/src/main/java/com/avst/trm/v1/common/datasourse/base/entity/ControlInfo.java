package com.avst.trm.v1.common.datasourse.base.entity;

public class ControlInfo {

    //服务器名字
    private String name;

    //服务器IP
    private String ip;

    //服务状态
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}