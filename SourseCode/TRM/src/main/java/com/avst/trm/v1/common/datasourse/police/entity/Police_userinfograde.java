package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Admin
 * @since 2019-10-11
 */
public class Police_userinfograde extends Model<Police_userinfograde> {

    private static final long serialVersionUID = 1L;

    /**
     * 人员类型级别表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 级别名称
     */
    private String gradename;

    /**
     * 级别值
     */
    private Integer grade;

    /**
     * 级别类型
     */
    private Integer gradetype;

    /**
     * 类型ssid
     */
    private String typessid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 用于标记这个人物角色在系统中的简介
     */
    private String gradeintroduce;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getId() {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGradeintroduce() {
        return gradeintroduce;
    }

    public void setGradeintroduce(String gradeintroduce) {
        this.gradeintroduce = gradeintroduce;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public Integer getGradetype() {
        return gradetype;
    }

    public void setGradetype(Integer gradetype) {
        this.gradetype = gradetype;
    }
    public String getTypessid() {
        return typessid;
    }

    public void setTypessid(String typessid) {
        this.typessid = typessid;
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
        return "Police_userinfograde{" +
        "id=" + id +
        ", gradename=" + gradename +
        ", grade=" + grade +
        ", gradetype=" + gradetype +
        ", typessid=" + typessid +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
