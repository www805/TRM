package com.avst.trm.v1.feignclient.mc.req;

import java.util.List;

public class StartMCParam_out extends BaseReqParam{

    private int meetingtype;//会议类型，1视频/2音频

    private String mcType;//会议采用版本，现阶段只有AVST

    private int modelbool;//是否需要会议模板，1需要/-1不需要

    private String mtmodelssid;//会议模板ssid

    private List<TdAndUserAndOtherParam> tdList;

    private String ywSystemType;//业务系统类型，TRM_AVST AVst版本的笔录系统

    private StartRecordAndCaseParam startRecordAndCaseParam;//案件笔录参数

    public StartRecordAndCaseParam getStartRecordAndCaseParam() {
        return startRecordAndCaseParam;
    }

    public void setStartRecordAndCaseParam(StartRecordAndCaseParam startRecordAndCaseParam) {
        this.startRecordAndCaseParam = startRecordAndCaseParam;
    }

    public String getYwSystemType() {
        return ywSystemType;
    }

    public void setYwSystemType(String ywSystemType) {
        this.ywSystemType = ywSystemType;
    }

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }

    public List<TdAndUserAndOtherParam> getTdList() {
        return tdList;
    }

    public void setTdList(List<TdAndUserAndOtherParam> tdList) {
        this.tdList = tdList;
    }

    public int getMeetingtype() {
        return meetingtype;
    }

    public void setMeetingtype(int meetingtype) {
        this.meetingtype = meetingtype;
    }

    public int getModelbool() {
        return modelbool;
    }

    public void setModelbool(int modelbool) {
        this.modelbool = modelbool;
    }

    public String getMtmodelssid() {
        return mtmodelssid;
    }

    public void setMtmodelssid(String mtmodelssid) {
        this.mtmodelssid = mtmodelssid;
    }
}
