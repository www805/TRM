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
public class Arraignment extends Model<Arraignment> {

    private static final long serialVersionUID = 1L;

    /**
     * 提讯表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 询问人id
     */
    private Integer adminid;

    /**
     * 记录人id
     */
    private Integer recordadminid;

    /**
     * 问话地点
     */
    private String recordplace;

    /**
     * 询问对象:例如：犯罪嫌疑人，被害人
     */
    private String askobj;

    /**
     * 询问次数:默认1
     */
    private Integer asknum;

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
    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }
    public Integer getRecordadminid() {
        return recordadminid;
    }

    public void setRecordadminid(Integer recordadminid) {
        this.recordadminid = recordadminid;
    }
    public String getRecordplace() {
        return recordplace;
    }

    public void setRecordplace(String recordplace) {
        this.recordplace = recordplace;
    }
    public String getAskobj() {
        return askobj;
    }

    public void setAskobj(String askobj) {
        this.askobj = askobj;
    }
    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
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
        return "Arraignment{" +
        "id=" + id +
        ", adminid=" + adminid +
        ", recordadminid=" + recordadminid +
        ", recordplace=" + recordplace +
        ", askobj=" + askobj +
        ", asknum=" + asknum +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
