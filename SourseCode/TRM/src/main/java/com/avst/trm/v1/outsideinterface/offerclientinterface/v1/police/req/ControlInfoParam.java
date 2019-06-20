package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

import java.util.Date;

public class ControlInfoParam {

    //服务器名字
    private String name;

    //服务器IP
    private String ip;

    //服务状态
    private Integer status;

    //服务类型
    private String type;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "ControlInfoParam{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", createtime=" + createtime +
                ", status=" + status +
                '}';
    }
}
