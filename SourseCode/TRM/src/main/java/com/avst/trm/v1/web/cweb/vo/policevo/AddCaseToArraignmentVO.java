package com.avst.trm.v1.web.cweb.vo.policevo;

public class AddCaseToArraignmentVO {
    private boolean recordingbool=false;//是否存在笔录进行中或者未开始 true 存在笔录中 false不存在默认false

    private CheckStartRecordVO checkStartRecordVO;

    private String recordssid;//笔录ssid



    private String recordtypessid;

    private String recordtype_conversation1;//默认谈话笔录ssid:一键笔录

    private String recordtype_conversation2;//默认谈话笔录ssid:开启笔录

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }

    public String getRecordtype_conversation1() {
        return recordtype_conversation1;
    }

    public void setRecordtype_conversation1(String recordtype_conversation1) {
        this.recordtype_conversation1 = recordtype_conversation1;
    }

    public String getRecordtype_conversation2() {
        return recordtype_conversation2;
    }

    public void setRecordtype_conversation2(String recordtype_conversation2) {
        this.recordtype_conversation2 = recordtype_conversation2;
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

}
