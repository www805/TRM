package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Admin
 * @since 2019-08-28
 */
public class Police_casetouserinfo extends Model<Police_casetouserinfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 案件人员表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 案件ssid
     */
    private String casessid;

    /**
     * 人员ssid
     */
    private String userssid;

    /**
     * 人员证件对应ssid
     */
    private String usertotypessid;

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

    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getUsertotypessid() {
        return usertotypessid;
    }

    public void setUsertotypessid(String usertotypessid) {
        this.usertotypessid = usertotypessid;
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
}
