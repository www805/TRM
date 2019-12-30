package com.avst.trm.v1.common.datasourse.police.entity;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Admin
 * @since 2019-12-30
 */
public class Police_namingrule extends Model<Police_namingrule> {

    private static final long serialVersionUID = 1L;

    /**
     * 笔录命名规则表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 命名规则说明
     */
    private String description;

    /**
     * 规则内容
     */
    private String rule;

    /**
     * 命名类型：1、快速 2、正常
     */
    private Integer namingruletype;

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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
    public Integer getNamingruletype() {
        return namingruletype;
    }

    public void setNamingruletype(Integer namingruletype) {
        this.namingruletype = namingruletype;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Police_namingrule{" +
        "id=" + id +
        ", description=" + description +
        ", rule=" + rule +
        ", namingruletype=" + namingruletype +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
