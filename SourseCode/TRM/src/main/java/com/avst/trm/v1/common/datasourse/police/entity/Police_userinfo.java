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
 * @since 2019-04-15
 */
public class Police_userinfo extends Model<Police_userinfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户角色表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 证件类型
     */
    private Integer cardtypeid;

    /**
     * 证件号码
     */
    private String cardid;

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
    private Date both;

    /**
     * 民族
     */
    private String national;

    /**
     * 国籍
     */
    private String nationality;

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

    private String ssid;

    private String string1;

    private String string2;

    /**
     * 特殊人员
     */
    private Integer integer1;

    private Integer integer2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCardtypeid() {
        return cardtypeid;
    }

    public void setCardtypeid(Integer cardtypeid) {
        this.cardtypeid = cardtypeid;
    }
    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
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
    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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
        return "Police_userinfo{" +
        "id=" + id +
        ", cardtypeid=" + cardtypeid +
        ", cardid=" + cardid +
        ", username=" + username +
        ", beforename=" + beforename +
        ", nickname=" + nickname +
        ", age=" + age +
        ", sex=" + sex +
        ", both=" + both +
        ", national=" + national +
        ", nationality=" + nationality +
        ", professional=" + professional +
        ", educationlevel=" + educationlevel +
        ", politicsstatus=" + politicsstatus +
        ", phone=" + phone +
        ", domicile=" + domicile +
        ", residence=" + residence +
        ", workunits=" + workunits +
        ", createtime=" + createtime +
        ", specialtype=" + specialtype +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
