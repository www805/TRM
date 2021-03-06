package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-24
 */
public class Police_arraignment extends Model<Police_arraignment> {

    private static final long serialVersionUID = 1L;

    /**
     * 提讯表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 询问人id
     */
    private String adminssid;

    /**
     * 询问人2id
     */
    private String otheradminssid;



    /**
     * 记录人id
     */
    private String recordadminssid;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    /**
     * 关联笔录
     */
    private String recordssid;

    /**
     * 会议ssid
     */
    private String mtssid;

    /**
     *会议模板ssid
     */
    private String mtmodelssid;

    /**
     * 被提讯人
     */
    private String userssid;


    /**多功能状态
     * 主要用于控制审讯
     * 1：单功能（一键审讯只有视频）
     * 2：第二多功能（开启审讯视频和笔录）
     * 3：多功能（普通笔录都有）
     */
    private Integer multifunctionbool;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getMultifunctionbool() {
        return multifunctionbool;
    }

    public void setMultifunctionbool(Integer multifunctionbool) {
        this.multifunctionbool = multifunctionbool;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getMtmodelssid() {
        return mtmodelssid;
    }

    public void setMtmodelssid(String mtmodelssid) {
        this.mtmodelssid = mtmodelssid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAdminssid() {
        return adminssid;
    }

    public void setAdminssid(String adminssid) {
        this.adminssid = adminssid;
    }
    public String getRecordadminssid() {
        return recordadminssid;
    }

    public void setRecordadminssid(String recordadminssid) {
        this.recordadminssid = recordadminssid;
    }
    public String getRecordplace() {
        return recordplace;
    }

    public void setRecordplace(String recordplace) {
        this.recordplace = recordplace;
    }

    public String getOtheradminssid() {
        return otheradminssid;
    }

    public void setOtheradminssid(String otheradminssid) {
        this.otheradminssid = otheradminssid;
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
    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
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

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }


    @Override
    public String toString() {
        return "Police_arraignment{" +
                "id=" + id +
                ", adminssid='" + adminssid + '\'' +
                ", otheradminssid='" + otheradminssid + '\'' +
                ", recordadminssid='" + recordadminssid + '\'' +
                ", recordplace='" + recordplace + '\'' +
                ", askobj=" + askobj +
                ", asknum=" + asknum +
                ", createtime=" + createtime +
                ", recordssid='" + recordssid + '\'' +
                ", mtssid='" + mtssid + '\'' +
                ", ssid='" + ssid + '\'' +
                ", string1='" + string1 + '\'' +
                ", string2='" + string2 + '\'' +
                ", integer1=" + integer1 +
                ", integer2=" + integer2 +
                '}';
    }
}
