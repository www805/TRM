package com.avst.trm.v1.common.datasourse.police.entity;

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
public class Police_templatetotype extends Model<Police_templatetotype> {

    private static final long serialVersionUID = 1L;

    /**
     * 模板类型对应表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板id
     */
    private String templatessid;

    /**
     * 是否为默认模板：1默认/-1非默认
     */
    private Integer templatebool;

    /**
     * 模板类型id
     */
    private String templatetypessid;

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
    public String getTemplatessid() {
        return templatessid;
    }

    public void setTemplatessid(String templatessid) {
        this.templatessid = templatessid;
    }
    public Integer getTemplatebool() {
        return templatebool;
    }

    public void setTemplatebool(Integer templatebool) {
        this.templatebool = templatebool;
    }
    public String getTemplatetypessid() {
        return templatetypessid;
    }

    public void setTemplatetypessid(String templatetypessid) {
        this.templatetypessid = templatetypessid;
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
        return "Police_templatetotype{" +
        "id=" + id +
        ", templatessid=" + templatessid +
        ", templatebool=" + templatebool +
        ", templatetypessid=" + templatetypessid +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
