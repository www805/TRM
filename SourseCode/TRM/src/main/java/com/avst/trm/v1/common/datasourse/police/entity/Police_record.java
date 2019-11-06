package com.avst.trm.v1.common.datasourse.police.entity;

import com.avst.trm.v1.common.util.log.LogUtil;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 笔录状态：1进行中2已完成
     */
    private Integer recordbool;

    /**
     * PDF地址
     */
    private String pdf_filesavessid;

    /**
     * word地址
     */
    private String word_filesavessid;


    /**
     * 点播文件存储地址的iid
     */
    private String gz_iid;


    /**
     * 笔录类型
     */
    private  String recordtypessid;

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    private String wordtemplatessid;//笔录模板ssid

    private String wordhead_filesavessid;//word文件头

    /**
     * 是否需要重新打包：1需要-1不需要
     */
    private Integer repackbool;


    /**
     * 用于控制视频语音定位，时间提前(正数)或者者后退（负数）。毫秒值 默认0
     */
    private long positiontime;

    public long getPositiontime() {
        return positiontime;
    }

    public void setPositiontime(long positiontime) {
        this.positiontime = positiontime;
    }

    public Integer getRepackbool() {
        return repackbool;
    }

    public void setRepackbool(Integer repackbool) {
        this.repackbool = repackbool;
    }

    public String getWordhead_filesavessid() {
        return wordhead_filesavessid;
    }

    public void setWordhead_filesavessid(String wordhead_filesavessid) {
        this.wordhead_filesavessid = wordhead_filesavessid;
    }

    public String getWordtemplatessid() {
        return wordtemplatessid;
    }

    public void setWordtemplatessid(String wordtemplatessid) {
        this.wordtemplatessid = wordtemplatessid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGz_iid() {
        return gz_iid;
    }

    public void setGz_iid(String gz_iid) {
        this.gz_iid = gz_iid;
    }

    public String getWord_filesavessid() {
        return word_filesavessid;
    }

    public void setWord_filesavessid(String word_filesavessid) {
        this.word_filesavessid = word_filesavessid;
    }

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
                ", recordname='" + recordname + '\'' +
                ", recordbool=" + recordbool +
                ", pdf_filesavessid='" + pdf_filesavessid + '\'' +
                ", word_filesavessid='" + word_filesavessid + '\'' +
                ", gz_iid='" + gz_iid + '\'' +
                ", recordtypessid='" + recordtypessid + '\'' +
                ", createtime=" + createtime +
                ", ssid='" + ssid + '\'' +
                ", string1='" + string1 + '\'' +
                ", string2='" + string2 + '\'' +
                ", integer1=" + integer1 +
                ", integer2=" + integer2 +
                ", wordtemplatessid='" + wordtemplatessid + '\'' +
                '}';
    }

    public static void main(String[] args) {
        long a=1556245697000L;
        long b=1556246444000L;

        LogUtil.intoLog(Police_record.class,(b-a)/ 1000);
    }
}
