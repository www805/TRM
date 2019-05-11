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
 * @since 2019-04-26
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
     * 总录音地址
     */
    private String record_filesavessid;

    /**
     * 总录音时长
     */
    private Integer recordtime;

    /**
     * 笔录开始时间 ms
     */
    private Long recordstarttime;

    /**
     * 笔录状态：1进行中2未开始
     */
    private Integer recordbool;

    /**
     * PDF地址
     */
    private String pdf_filesavessid;

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
    public String getRecord_filesavessid() {
        return record_filesavessid;
    }

    public void setRecord_filesavessid(String record_filesavessid) {
        this.record_filesavessid = record_filesavessid;
    }
    public Integer getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Integer recordtime) {
        this.recordtime = recordtime;
    }
    public Long getRecordstarttime() {
        return recordstarttime;
    }

    public void setRecordstarttime(Long recordstarttime) {
        this.recordstarttime = recordstarttime;
    }
    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
    }
    public String getPdf_filesavessid() {
        return pdf_filesavessid;
    }

    public void setPdf_filesavessid(String pdf_filesavessid) {
        this.pdf_filesavessid = pdf_filesavessid;
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
        ", record_filesavessid=" + record_filesavessid +
        ", recordtime=" + recordtime +
        ", recordstarttime=" + recordstarttime +
        ", recordbool=" + recordbool +
        ", pdf_filesavessid=" + pdf_filesavessid +
        ", createtime=" + createtime +
        ", id=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }


    public static void main(String[] args) {
        long a=1556245697000L;
        long b=1556246444000L;

        System.out.println((b-a)/ 1000);
    }
}
