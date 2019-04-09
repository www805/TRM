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
public class Record extends Model<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * 笔录表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 笔录名称
     */
    private String recordname;

    /**
     *   模板id
     */
    private Integer templateid;

    /**
     * 案件id
     */
    private Integer caseid;

    /**
     * 总录音下载地址
     */
    private String recorddownurl;

    /**
     * 总录音真实地址
     */
    private String recordrealurl;

    /**
     * 总录音时长
     */
    private Integer recordtime;

    /**
     * 笔录进行时间
     */
    private Date recordingtime;

    /**
     * 笔录状态：1进行中2未开始
     */
    private Integer recordbool;

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
    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }
    public Integer getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Integer templateid) {
        this.templateid = templateid;
    }
    public Integer getCaseid() {
        return caseid;
    }

    public void setCaseid(Integer caseid) {
        this.caseid = caseid;
    }
    public String getRecorddownurl() {
        return recorddownurl;
    }

    public void setRecorddownurl(String recorddownurl) {
        this.recorddownurl = recorddownurl;
    }
    public String getRecordrealurl() {
        return recordrealurl;
    }

    public void setRecordrealurl(String recordrealurl) {
        this.recordrealurl = recordrealurl;
    }
    public Integer getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Integer recordtime) {
        this.recordtime = recordtime;
    }
    public Date getRecordingtime() {
        return recordingtime;
    }

    public void setRecordingtime(Date recordingtime) {
        this.recordingtime = recordingtime;
    }
    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
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
        return "Record{" +
        "id=" + id +
        ", recordname=" + recordname +
        ", templateid=" + templateid +
        ", caseid=" + caseid +
        ", recorddownurl=" + recorddownurl +
        ", recordrealurl=" + recordrealurl +
        ", recordtime=" + recordtime +
        ", recordingtime=" + recordingtime +
        ", recordbool=" + recordbool +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
