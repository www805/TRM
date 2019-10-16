package com.avst.trm.v1.common.datasourse.police.entity;

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
 * @since 2019-05-10
 */
public class Police_userinfo extends Model<Police_userinfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String username;

    /**
     * 曾用名
     */
    private String beforename;

    /**
     * 绰号
     */
    private String nickname;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别:1男2女-1未知
     */
    private Integer sex;

    /**
     * 出生日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date both;

    /**
     * 民族
     */
    private String nationalssid;

    /**
     * 国籍
     */
    private String nationalityssid;

    /**
     * 职业
     */
    private String professional;

    /**
     * 文化程度
     */
    private String educationlevel;

    /**
     * 政治面貌
     */
    private String politicsstatus;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 户籍地
     */
    private String domicile;

    /**
     * 现住地
     */
    private String residence;

    /**
     * 工作单位
     */
    private String workunits;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 特殊人员说明：
1、无犯罪史
2、毒
3、逃
4、违
     */
    private Integer specialtype;

    /**
     * 签证机关
     */
    private String issuingauthority;

    /**
     * 身份证有效期
     */
    private String validity;


    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBeforename() {
        return beforename;
    }

    public void setBeforename(String beforename) {
        this.beforename = beforename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBoth() {
        return both;
    }

    public void setBoth(Date both) {
        this.both = both;
    }

    public String getNationalssid() {
        return nationalssid;
    }

    public void setNationalssid(String nationalssid) {
        this.nationalssid = nationalssid;
    }

    public String getNationalityssid() {
        return nationalityssid;
    }

    public void setNationalityssid(String nationalityssid) {
        this.nationalityssid = nationalityssid;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String getPoliticsstatus() {
        return politicsstatus;
    }

    public void setPoliticsstatus(String politicsstatus) {
        this.politicsstatus = politicsstatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getWorkunits() {
        return workunits;
    }

    public void setWorkunits(String workunits) {
        this.workunits = workunits;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getSpecialtype() {
        return specialtype;
    }

    public void setSpecialtype(Integer specialtype) {
        this.specialtype = specialtype;
    }

    public String getIssuingauthority() {
        return issuingauthority;
    }

    public void setIssuingauthority(String issuingauthority) {
        this.issuingauthority = issuingauthority;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
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

    @Override
    public String toString() {
        return "Police_userinfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", beforename='" + beforename + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", both=" + both +
                ", nationalssid='" + nationalssid + '\'' +
                ", nationalityssid='" + nationalityssid + '\'' +
                ", professional='" + professional + '\'' +
                ", educationlevel='" + educationlevel + '\'' +
                ", politicsstatus='" + politicsstatus + '\'' +
                ", phone='" + phone + '\'' +
                ", domicile='" + domicile + '\'' +
                ", residence='" + residence + '\'' +
                ", workunits='" + workunits + '\'' +
                ", createtime=" + createtime +
                ", specialtype=" + specialtype +
                ", issuingauthority='" + issuingauthority + '\'' +
                ", validity='" + validity + '\'' +
                ", ssid='" + ssid + '\'' +
                ", string1='" + string1 + '\'' +
                ", string2='" + string2 + '\'' +
                ", integer1=" + integer1 +
                '}';
    }
}
