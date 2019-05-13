package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

import java.io.Serializable;
import java.util.Date;

public class GetProblemtypeParam extends Page {

    /**
     * 题目类型表
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String typename;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    private Date createtime;

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
    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
    public String toString() {
        return "Police_problemtype{" +
                "id=" + id +
                ", typename=" + typename +
                ", ordernum=" + ordernum +
                ", createtime=" + createtime +
                ", id=" + ssid +
                ", string1=" + string1 +
                ", string2=" + string2 +
                ", integer1=" + integer1 +
                ", integer2=" + integer2 +
                "}";
    }

}
