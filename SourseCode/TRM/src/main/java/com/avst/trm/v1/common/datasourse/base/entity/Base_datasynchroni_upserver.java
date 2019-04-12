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
 * @since 2019-04-11
 */
public class Base_datasynchroni_upserver extends Model<Base_datasynchroni_upserver> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器数据同步表(上层服务器使用)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表单名称
     */
    private String cnname;

    /**
     * 可供同步的表单id
     */
    private Integer datasheetid;

    /**
     * 最后同步时间
     */
    private Date lastuploadtime;

    /**
     * 最后同步id
     */
    private Integer dataid;

    /**
     * 同步次数
     */
    private Integer uploadcount;

    /**
     * 单位中的排序
     */
    private Integer unitsort;

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
    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }
    public Integer getDatasheetid() {
        return datasheetid;
    }

    public void setDatasheetid(Integer datasheetid) {
        this.datasheetid = datasheetid;
    }
    public Date getLastuploadtime() {
        return lastuploadtime;
    }

    public void setLastuploadtime(Date lastuploadtime) {
        this.lastuploadtime = lastuploadtime;
    }
    public Integer getDataid() {
        return dataid;
    }

    public void setDataid(Integer dataid) {
        this.dataid = dataid;
    }
    public Integer getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(Integer uploadcount) {
        this.uploadcount = uploadcount;
    }
    public Integer getUnitsort() {
        return unitsort;
    }

    public void setUnitsort(Integer unitsort) {
        this.unitsort = unitsort;
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
        return "Base_datasynchroni_upserver{" +
        "id=" + id +
        ", cnname=" + cnname +
        ", datasheetid=" + datasheetid +
        ", lastuploadtime=" + lastuploadtime +
        ", dataid=" + dataid +
        ", uploadcount=" + uploadcount +
        ", unitsort=" + unitsort +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
