package com.avst.trm.v1.feignclient.mc.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Avstmt_modelAll {

    private Integer id;

    private Integer meetingtype;

    private Integer usernum;

    private Integer opened;

    private Integer userecord;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    private String explain;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    private String ssid;


    private List<Avstmt_modeltdAll> avstmt_modeltdAlls;//全部模板通道

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMeetingtype() {
        return meetingtype;
    }

    public void setMeetingtype(Integer meetingtype) {
        this.meetingtype = meetingtype;
    }

    public Integer getUsernum() {
        return usernum;
    }

    public void setUsernum(Integer usernum) {
        this.usernum = usernum;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    public Integer getUserecord() {
        return userecord;
    }

    public void setUserecord(Integer userecord) {
        this.userecord = userecord;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
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

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public List<Avstmt_modeltdAll> getAvstmt_modeltdAlls() {
        return avstmt_modeltdAlls;
    }

    public void setAvstmt_modeltdAlls(List<Avstmt_modeltdAll> avstmt_modeltdAlls) {
        this.avstmt_modeltdAlls = avstmt_modeltdAlls;
    }
}
