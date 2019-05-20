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
public class Base_type extends Model<Base_type> {

    private static final long serialVersionUID = 1L;

    /**
     * 类型表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型名称：法院、检察院、公安、会议、sweb
     */
    private String type;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 会议模板ssid
     */
    private String mtmodelssid;

    /**
     * 类型的中文名称
     */
    private String typename;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;



    public String getMtmodelssid() {
        return mtmodelssid;
    }

    public void setMtmodelssid(String mtmodelssid) {
        this.mtmodelssid = mtmodelssid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Base_type{" +
        "id=" + id +
        ", type=" + type +
        ", ordernum=" + ordernum +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        ", typename=" + typename +
        "}";
    }
}
