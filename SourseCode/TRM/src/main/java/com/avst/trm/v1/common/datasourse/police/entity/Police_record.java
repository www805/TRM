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
 * @since 2019-04-23
 */
public class Police_record extends Model<Police_record> {

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
     *  笔录模板id
     */
    private String recordtemplatessid;

    /**
     * 案件id
     */
    private String casessid;

    /**
     * 总录音下载地址
     */
    private String recorddown__filesavessid;

    /**
     * 总录音真实地址
     */
    private String recordreal_filesavessid;

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
     * PDF下载地址
     */
    private String pdfdown_filesavessid;

    /**
     * PDF真实地址
     */
    private String pdfreal_filesavessid;

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
    public String getRecordtemplatessid() {
        return recordtemplatessid;
    }

    public void setRecordtemplatessid(String recordtemplatessid) {
        this.recordtemplatessid = recordtemplatessid;
    }
    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }
    public String getRecorddown__filesavessid() {
        return recorddown__filesavessid;
    }

    public void setRecorddown__filesavessid(String recorddown__filesavessid) {
        this.recorddown__filesavessid = recorddown__filesavessid;
    }
    public String getRecordreal_filesavessid() {
        return recordreal_filesavessid;
    }

    public void setRecordreal_filesavessid(String recordreal_filesavessid) {
        this.recordreal_filesavessid = recordreal_filesavessid;
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
    public String getPdfdown_filesavessid() {
        return pdfdown_filesavessid;
    }

    public void setPdfdown_filesavessid(String pdfdown_filesavessid) {
        this.pdfdown_filesavessid = pdfdown_filesavessid;
    }
    public String getPdfreal_filesavessid() {
        return pdfreal_filesavessid;
    }

    public void setPdfreal_filesavessid(String pdfreal_filesavessid) {
        this.pdfreal_filesavessid = pdfreal_filesavessid;
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
        return "Police_record{" +
        "id=" + id +
        ", recordname=" + recordname +
        ", recordtemplatessid=" + recordtemplatessid +
        ", casessid=" + casessid +
        ", recorddown__filesavessid=" + recorddown__filesavessid +
        ", recordreal_filesavessid=" + recordreal_filesavessid +
        ", recordtime=" + recordtime +
        ", recordingtime=" + recordingtime +
        ", recordbool=" + recordbool +
        ", pdfdown_filesavessid=" + pdfdown_filesavessid +
        ", pdfreal_filesavessid=" + pdfreal_filesavessid +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
