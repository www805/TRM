package com.avst.trm.v1.feignclient.mc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class GetDefaultMTModelVO {

    /**
     * avst会议模板
     */
    private Integer id;

    /**
     * 会议类型(1视频/2音频)
     */
    private Integer meetingtype;

    /**
     * 人员数量
     */
    private Integer usernum;

    /**
     * 是否公开
     */
    private Integer opened;

    /**
     * 是否录制
     */
    private Integer userecord;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    /**
     * 1对单单语音识别，2单对多语音识别
     */
    private Integer asrservermodel;

    /**
     * 单对多语音识别模式下才有效，用于标记，开启几路语音识别给这个会话
     */
    private Integer asrnum;


    /**
     * 模板中文说明
     */
    private String explain;

    private String ssid;

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

    public Integer getAsrservermodel() {
        return asrservermodel;
    }

    public void setAsrservermodel(Integer asrservermodel) {
        this.asrservermodel = asrservermodel;
    }

    public Integer getAsrnum() {
        return asrnum;
    }

    public void setAsrnum(Integer asrnum) {
        this.asrnum = asrnum;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
