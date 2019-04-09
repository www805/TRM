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
 * @since 2019-04-09
 */
public class Baseaction extends Model<Baseaction> {

    private static final long serialVersionUID = 1L;

    /**
     * 动作表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动作编号
     */
    private String actionid;

    /**
     * 页面id
     */
    private String pageid;

    /**
     * 动作名称
     */
    private String actionname;

    /**
     * 动作地址
     */
    private String actionurl;

    /**
     * 是否有页面跳转:1有-1没有
     */
    private Integer topagebool;

    /**
     * 下一个页面
     */
    private String nextpageid;

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
    public String getActionid() {
        return actionid;
    }

    public void setActionid(String actionid) {
        this.actionid = actionid;
    }
    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }
    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }
    public String getActionurl() {
        return actionurl;
    }

    public void setActionurl(String actionurl) {
        this.actionurl = actionurl;
    }
    public Integer getTopagebool() {
        return topagebool;
    }

    public void setTopagebool(Integer topagebool) {
        this.topagebool = topagebool;
    }
    public String getNextpageid() {
        return nextpageid;
    }

    public void setNextpageid(String nextpageid) {
        this.nextpageid = nextpageid;
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
        return "Baseaction{" +
        "id=" + id +
        ", actionid=" + actionid +
        ", pageid=" + pageid +
        ", actionname=" + actionname +
        ", actionurl=" + actionurl +
        ", topagebool=" + topagebool +
        ", nextpageid=" + nextpageid +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
