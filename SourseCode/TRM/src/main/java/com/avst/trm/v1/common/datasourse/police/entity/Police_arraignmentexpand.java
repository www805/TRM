package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Admin
 * @since 2019-10-11
 */
public class Police_arraignmentexpand extends Model<Police_arraignmentexpand> {

    private static final long serialVersionUID = 1L;

    /**
     * 提讯拓展表 
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

    private String arraignmentssid;

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
    public String getArraignmentssid() {
        return arraignmentssid;
    }

    public void setArraignmentssid(String arraignmentssid) {
        this.arraignmentssid = arraignmentssid;
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
        return "Police_arraignmentexpand{" +
        "id=" + id +
        ", expandname=" + expandname +
        ", expandvalue=" + expandvalue +
        ", arraignmentssid=" + arraignmentssid +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
