package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;
import com.google.gson.annotations.Expose;

public class AddCaseToArraignmentVO {
    private boolean recordingbool=false;//是否存在笔录进行中或者未开始 true 存在笔录中 false不存在默认false
    private CheckStartRecordVO checkStartRecordVO;

    private  String  recordssid;//笔录ssid

    private Case case_;//案件信息返回回显
    private boolean caseingbool=false;//案件是否需要在暂停中 true 暂停中false正常


    private boolean casenumingbool=false;//案号已存在是否需要使用最后的案件提讯信息
    private String casessid;//返回案件sssid

    private Integer custommsgbool;//是否使用自定义信息 //用于显示使用哪个方法

    private Integer multifunctionbool;//功能类型 1 单功能 2双功能 3 多功能//控制跳转

    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }

    public boolean isCasenumingbool() {
        return casenumingbool;
    }

    public void setCasenumingbool(boolean casenumingbool) {
        this.casenumingbool = casenumingbool;
    }

    public Integer getCustommsgbool() {
        return custommsgbool;
    }

    public void setCustommsgbool(Integer custommsgbool) {
        this.custommsgbool = custommsgbool;
    }

    public Integer getMultifunctionbool() {
        return multifunctionbool;
    }

    public void setMultifunctionbool(Integer multifunctionbool) {
        this.multifunctionbool = multifunctionbool;
    }

    public Case getCase_() {
        return case_;
    }

    public boolean isRecordingbool() {
        return recordingbool;
    }

    public void setRecordingbool(boolean recordingbool) {
        this.recordingbool = recordingbool;
    }

    public CheckStartRecordVO getCheckStartRecordVO() {
        return checkStartRecordVO;
    }

    public void setCheckStartRecordVO(CheckStartRecordVO checkStartRecordVO) {
        this.checkStartRecordVO = checkStartRecordVO;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public void setCase_(Case case_) {
        this.case_ = case_;
    }

    public boolean isCaseingbool() {
        return caseingbool;
    }

    public void setCaseingbool(boolean caseingbool) {
        this.caseingbool = caseingbool;
    }



}
