package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-23
 */
public class Police_recordreal extends Model<Police_recordreal> {

    private static final long serialVersionUID = 1L;

    /**
     * 笔录实时表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 笔录id
     */
    private String recordssid;

    /**
     * 翻译文字
     */
    private String translatext;

    /**
     * 实时录音地址
     */
    private String recordreal_filesavessid;

    /**
     * 录音时长ms
     */
    private Long time;

    /**
     * 录音人id
     */
    private String userssid;

    /**
     * 录音人类型：1询问人（系统用户admin）
2被询问人user
     */
    private Integer recordtype;

    /**
     * 录音开始时间 ms
     */
    private Long starttime;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }
    public String getTranslatext() {
        return translatext;
    }

    public void setTranslatext(String translatext) {
        this.translatext = translatext;
    }
    public String getRecordreal_filesavessid() {
        return recordreal_filesavessid;
    }

    public void setRecordreal_filesavessid(String recordreal_filesavessid) {
        this.recordreal_filesavessid = recordreal_filesavessid;
    }
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }
    public Integer getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(Integer recordtype) {
        this.recordtype = recordtype;
    }
    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Police_recordreal{" +
        "id=" + id +
        ", recordssid=" + recordssid +
        ", translatext=" + translatext +
        ", recordreal_filesavessid=" + recordreal_filesavessid +
        ", time=" + time +
        ", userssid=" + userssid +
        ", recordtype=" + recordtype +
        ", starttime=" + starttime +
        ", ordernum=" + ordernum +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
