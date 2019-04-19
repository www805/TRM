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
 * @since 2019-04-19
 */
public class Base_datasheet_downserver extends Model<Base_datasheet_downserver> {

    private static final long serialVersionUID = 1L;

    /**
     * 同步表单对应的数据(下级服务器)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据ssid
     */
    private String datassid;

    /**
     * 表单名
     */
    private String dataname;

    /**
     * 客户端数据同步id
     */
    private Integer downserverid;

    /**
     * 是否同步文件:1同步文件，-1不是
     */
    private Integer filetype;

    /**
     * 文件名
     */
    private String filename;

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
    public String getDatassid() {
        return datassid;
    }

    public void setDatassid(String datassid) {
        this.datassid = datassid;
    }
    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }
    public Integer getDownserverid() {
        return downserverid;
    }

    public void setDownserverid(Integer downserverid) {
        this.downserverid = downserverid;
    }
    public Integer getFiletype() {
        return filetype;
    }

    public void setFiletype(Integer filetype) {
        this.filetype = filetype;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
        return "Base_datasheet_downserver{" +
        "id=" + id +
        ", datassid=" + datassid +
        ", dataname=" + dataname +
        ", downserverid=" + downserverid +
        ", filetype=" + filetype +
        ", filename=" + filename +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
