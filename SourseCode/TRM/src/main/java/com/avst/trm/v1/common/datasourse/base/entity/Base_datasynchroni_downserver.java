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
 * @since 2019-04-19
 */
public class Base_datasynchroni_downserver extends Model<Base_datasynchroni_downserver> {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端数据同步表(下级服务器使用)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 最后同步时间：客户端的时间
     */
    private Date lastuploadtime;

    /**
     * 同步次数:一次同步按钮算一次（2张表同步次数可能不一样）
     */
    private Integer uploadcount;

    /**
     * 上级服务器的IP
     */
    private String upserverip;

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
    public Date getLastuploadtime() {
        return lastuploadtime;
    }

    public void setLastuploadtime(Date lastuploadtime) {
        this.lastuploadtime = lastuploadtime;
    }
    public Integer getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(Integer uploadcount) {
        this.uploadcount = uploadcount;
    }
    public String getUpserverip() {
        return upserverip;
    }

    public void setUpserverip(String upserverip) {
        this.upserverip = upserverip;
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
        return "Base_datasynchroni_downserver{" +
        "id=" + id +
        ", lastuploadtime=" + lastuploadtime +
        ", uploadcount=" + uploadcount +
        ", upserverip=" + upserverip +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
