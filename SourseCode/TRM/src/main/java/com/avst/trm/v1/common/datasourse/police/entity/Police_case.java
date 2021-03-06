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
 * @since 2019-04-22
 */
public class Police_case extends Model<Police_case> {

    private static final long serialVersionUID = 1L;

    /**
     * 案件表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 案件名称
     */
    private String casename;

    /**
     * 当前案由
     */
    private String cause;

    /**
     * 案件编号
     */
    private String casenum;

    /**
     * 案件发生时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date occurrencetime;

    /**
     * 案发开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    /**
     * 案发结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    /**
     * 到案方式
     */
    private String caseway;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    private Date createtime;


    /**
     * 创建人
     */
    private String creator;

    /**
     * 案件状态;0未开始1进行中 2已归档（已完成）默认0
     */
    private Integer casebool;

    private String department;//办案部门

    /**
     * 是否需要重新打包：1需要-1不需要
     */
    private Integer repackbool;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getRepackbool() {
        return repackbool;
    }

    public void setRepackbool(Integer repackbool) {
        this.repackbool = repackbool;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCasebool() {
        return casebool;
    }

    public void setCasebool(Integer casebool) {
        this.casebool = casebool;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }
    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
    public String getCasenum() {
        return casenum;
    }

    public void setCasenum(String casenum) {
        this.casenum = casenum;
    }
    public Date getOccurrencetime() {
        return occurrencetime;
    }

    public void setOccurrencetime(Date occurrencetime) {
        this.occurrencetime = occurrencetime;
    }
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
    public String getCaseway() {
        return caseway;
    }

    public void setCaseway(String caseway) {
        this.caseway = caseway;
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
        return "Police_case{" +
                "id=" + id +
                ", casename='" + casename + '\'' +
                ", cause='" + cause + '\'' +
                ", casenum='" + casenum + '\'' +
                ", occurrencetime=" + occurrencetime +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", caseway='" + caseway + '\'' +
                ", ordernum=" + ordernum +
                ", createtime=" + createtime +
                ", creator='" + creator + '\'' +
                ", casebool=" + casebool +
                ", department='" + department + '\'' +
                ", ssid='" + ssid + '\'' +
                ", string1='" + string1 + '\'' +
                ", string2='" + string2 + '\'' +
                ", integer1=" + integer1 +
                ", integer2=" + integer2 +
                '}';
    }
}
