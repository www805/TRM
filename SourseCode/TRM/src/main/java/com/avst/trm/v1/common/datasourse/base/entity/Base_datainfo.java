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
 * @since 2019-04-24
 */
public class Base_datainfo extends Model<Base_datainfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 可以同步的表单(客户端和服务器都有)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表单名
     */
    private String dataname;

    /**
     * 表单中文名称
     */
    private String dataname_cn;

    /**
     * 对应服务器中的mapper对象名
     */
    private String mappername;

    /**
     * 类型的ssid
     */
    private String typessid;

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
    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }
    public String getDataname_cn() {
        return dataname_cn;
    }

    public void setDataname_cn(String dataname_cn) {
        this.dataname_cn = dataname_cn;
    }
    public String getMappername() {
        return mappername;
    }

    public void setMappername(String mappername) {
        this.mappername = mappername;
    }
    public String getTypessid() {
        return typessid;
    }

    public void setTypessid(String typessid) {
        this.typessid = typessid;
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
        return "Base_datainfo{" +
        "id=" + id +
        ", dataname=" + dataname +
        ", dataname_cn=" + dataname_cn +
        ", mappername=" + mappername +
        ", typessid=" + typessid +
        ", id=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
