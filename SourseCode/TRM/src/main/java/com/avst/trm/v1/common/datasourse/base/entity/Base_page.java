package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-22
 */
public class Base_page extends Model<Base_page> {

    private static final long serialVersionUID = 1L;

    /**
     * 页面表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 页面编号
     */
    private String pageid;

    /**
     * 页面名称
     */
    private String pagename;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 页面说明
     */
    private String description;

    /**
     * 类型id
     */
    private String typessid;

    /**
     * 第一个页面:
1是/-1不是（一种类型只有一个首页）
     */
    private Integer firstpage;

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
    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }
    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTypessid() {
        return typessid;
    }

    public void setTypessid(String typessid) {
        this.typessid = typessid;
    }
    public Integer getFirstpage() {
        return firstpage;
    }

    public void setFirstpage(Integer firstpage) {
        this.firstpage = firstpage;
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
        return "Base_page{" +
        "id=" + id +
        ", pageid=" + pageid +
        ", pagename=" + pagename +
        ", createtime=" + createtime +
        ", description=" + description +
        ", typessid=" + typessid +
        ", firstpage=" + firstpage +
        ", id=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base_page base_page = (Base_page) o;
        return  Objects.equals(pageid, base_page.pageid) || Objects.equals(ssid, base_page.ssid) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageid, pagename, createtime, description, typessid, firstpage, ssid, string1, string2, integer1, integer2);
    }
}
