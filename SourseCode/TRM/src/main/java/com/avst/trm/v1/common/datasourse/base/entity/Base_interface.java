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
public class Base_interface extends Model<Base_interface> {

    private static final long serialVersionUID = 1L;

    /**
     * 接口表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 接口名称
     */
    private String interfacename;

    /**
     * 接口地址
     */
    private String interfaceurl;

    /**
     * 接口说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 类型id
     */
    private String typessid;

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
    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }
    public String getInterfaceurl() {
        return interfaceurl;
    }

    public void setInterfaceurl(String interfaceurl) {
        this.interfaceurl = interfaceurl;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getTypessid() {
        return typessid;
    }

    public void setTypessid(String typessid) {
        this.typessid = typessid;
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
        return "Base_interface{" +
        "id=" + id +
        ", interfacename=" + interfacename +
        ", interfaceurl=" + interfaceurl +
        ", description=" + description +
        ", createtime=" + createtime +
        ", typessid=" + typessid +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
