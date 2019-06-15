package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 89088 kB
 * </p>
 *
 * @author Admin
 * @since 2019-05-17
 */
public class Police_recordtoproblem extends Model<Police_recordtoproblem> {

    private static final long serialVersionUID = 1L;

    /**
     * 笔录题目表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 笔录ssid
     */
    private String recordssid;

    /**
     * 题目
     */
    private String problem;

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

    /**
     * 录音开始时间：默认-1，-1代表该题目为自添
     */
    private Long starttime;

    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }

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
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
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
        return "Police_recordtoproblem{" +
        "id=" + id +
        ", recordssid=" + recordssid +
        ", problem=" + problem +
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
