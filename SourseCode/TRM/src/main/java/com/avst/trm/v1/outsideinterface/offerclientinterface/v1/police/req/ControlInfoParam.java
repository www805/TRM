package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

import java.util.Date;

public class ControlInfoParam {

    //服务器数量
    private Integer count;

    //服务状态
    private Integer status;

    //服务类型
    private String type;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
                "type='" + type + '\'' +
                ", createtime=" + createtime +
                ", count='" + count + '\'' +
                ", status=" + status +
                '}';
    }
}
