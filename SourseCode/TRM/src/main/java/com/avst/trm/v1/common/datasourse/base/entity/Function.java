package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 39936 kB
 * </p>
 *
 * @author Mht
 * @since 2019-04-03
 */
public class Function extends Model<Function> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 功能编号
     */
    private String functionnum;

    /**
     * 功能名称
     */
    private String functionname;

    /**
     * 功能对应的请求地址
     */
    private String funurl;

    /**
     * 功能说明
     */
    private String functionexplain;

    /**
     * 是否启用该权限,0为启用，1为禁用
     */
    private Integer isenable;

    /**
     * 功能类型，1常规2特殊
     */
    private Integer funtype;

    /**
     * 分组
     */
    private Integer fungroup;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastupdatetime;

    private String ssid;

    private Integer integer2;

    private Integer integer1;

    private String string1;

    private String string2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getFunctionnum() {
        return functionnum;
    }

    public void setFunctionnum(String functionnum) {
        this.functionnum = functionnum;
    }
    public String getFunctionname() {
        return functionname;
    }

    public void setFunctionname(String functionname) {
        this.functionname = functionname;
    }
    public String getFunurl() {
        return funurl;
    }

    public void setFunurl(String funurl) {
        this.funurl = funurl;
    }
    public String getFunctionexplain() {
        return functionexplain;
    }

    public void setFunctionexplain(String functionexplain) {
        this.functionexplain = functionexplain;
    }
    public Integer getIsenable() {
        return isenable;
    }

    public void setIsenable(Integer isenable) {
        this.isenable = isenable;
    }
    public Integer getFuntype() {
        return funtype;
    }

    public void setFuntype(Integer funtype) {
        this.funtype = funtype;
    }
    public Integer getFungroup() {
        return fungroup;
    }

    public void setFungroup(Integer fungroup) {
        this.fungroup = fungroup;
    }
    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
    public Date getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    public Integer getInteger2() {
        return integer2;
    }

    public void setInteger2(Integer integer2) {
        this.integer2 = integer2;
    }
    public Integer getInteger1() {
        return integer1;
    }

    public void setInteger1(Integer integer1) {
        this.integer1 = integer1;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Function{" +
        "id=" + id +
        ", functionnum=" + functionnum +
        ", functionname=" + functionname +
        ", funurl=" + funurl +
        ", functionexplain=" + functionexplain +
        ", isenable=" + isenable +
        ", funtype=" + funtype +
        ", fungroup=" + fungroup +
        ", addtime=" + addtime +
        ", lastupdatetime=" + lastupdatetime +
        ", ssid=" + ssid +
        ", integer2=" + integer2 +
        ", integer1=" + integer1 +
        ", string1=" + string1 +
        ", string2=" + string2 +
        "}";
    }
}
