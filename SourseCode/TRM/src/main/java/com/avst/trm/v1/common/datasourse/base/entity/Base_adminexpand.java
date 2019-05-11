package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 89088 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-26
 */
public class Base_adminexpand extends Model<Base_adminexpand> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户拓展表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 拓展名称
     */
    private String expandname;

    /**
     * 拓展值
     */
    private String expandvalue;

    private String adminssid;

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
    public String getExpandname() {
        return expandname;
    }

    public void setExpandname(String expandname) {
        this.expandname = expandname;
    }
    public String getExpandvalue() {
        return expandvalue;
    }

    public void setExpandvalue(String expandvalue) {
        this.expandvalue = expandvalue;
    }
    public String getAdminssid() {
        return adminssid;
    }

    public void setAdminssid(String adminssid) {
        this.adminssid = adminssid;
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
        return "Base_adminexpand{" +
        "id=" + id +
        ", expandname=" + expandname +
        ", expandvalue=" + expandvalue +
        ", adminssid=" + adminssid +
        ", id=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
