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
 * @since 2019-04-22
 */
public class Base_authorize extends Model<Base_authorize> {

    private static final long serialVersionUID = 1L;

    /**
     * 授权表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 授权服务类型
     */
    private String servertype;

    /**
     * 是否是永久授权
     */
    private Integer foreverbool;

    /**
     * 授权开始时间
     */
    private Date starttime;

    /**
     * 绑定CPU编码
     */
    private String cpucode;

    /**
     * 授权总天数
     */
    private Integer sqday;

    /**
     * 单位名称
     */
    private String clientname;

    /**
     * 单位代码
     */
    private String unitcode;

    /**
     * 排序:一个单位有一台主机，short=0；若干个办公客户端sort从1开始继增
     */
    private Integer sortnum;

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
    public String getServertype() {
        return servertype;
    }

    public void setServertype(String servertype) {
        this.servertype = servertype;
    }
    public Integer getForeverbool() {
        return foreverbool;
    }

    public void setForeverbool(Integer foreverbool) {
        this.foreverbool = foreverbool;
    }
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
    public String getCpucode() {
        return cpucode;
    }

    public void setCpucode(String cpucode) {
        this.cpucode = cpucode;
    }
    public Integer getSqday() {
        return sqday;
    }

    public void setSqday(Integer sqday) {
        this.sqday = sqday;
    }
    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }
    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }
    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
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
        return "Base_authorize{" +
        "id=" + id +
        ", servertype=" + servertype +
        ", foreverbool=" + foreverbool +
        ", starttime=" + starttime +
        ", cpucode=" + cpucode +
        ", sqday=" + sqday +
        ", clientname=" + clientname +
        ", unitcode=" + unitcode +
        ", sortnum=" + sortnum +
        ", id=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
