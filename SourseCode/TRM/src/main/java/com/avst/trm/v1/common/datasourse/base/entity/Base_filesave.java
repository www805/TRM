package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
public class Base_filesave extends Model<Base_filesave> {

    private static final long serialVersionUID = 1L;

    /**
     * 文件存储表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 真实路径文件名：32加密的名字，真实路径的名字（没有中文和特殊符号）
     */
    private String realfilename;

    /**
     * 文件本身的文件名
     */
    private String uploadfilename;

    /**
     * 从属表的ssid
     */
    private String zhname;

    /**
     * 下载地址
     */
    private String recorddownurl;

    /**
     * 真实存储地址
     */
    private String recordrealurl;

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
    public String getRealfilename() {
        return realfilename;
    }

    public void setRealfilename(String realfilename) {
        this.realfilename = realfilename;
    }
    public String getUploadfilename() {
        return uploadfilename;
    }

    public void setUploadfilename(String uploadfilename) {
        this.uploadfilename = uploadfilename;
    }
    public String getZhname() {
        return zhname;
    }

    public void setZhname(String zhname) {
        this.zhname = zhname;
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
        return "Base_filesave{" +
        "id=" + id +
        ", realfilename=" + realfilename +
        ", uploadfilename=" + uploadfilename +
        ", zhname=" + zhname +
        ", recorddownurl=" + recorddownurl +
        ", recordrealurl=" + recordrealurl +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
