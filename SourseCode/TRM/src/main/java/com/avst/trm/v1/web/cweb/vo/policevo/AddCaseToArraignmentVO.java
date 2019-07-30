package com.avst.trm.v1.web.cweb.vo.policevo;

public class AddCaseToArraignmentVO {
    private boolean recordingbool=false;//是否存在笔录进行中或者未开始 true 存在笔录中 false不存在默认false

    private CheckStartRecordVO checkStartRecordVO;

    private String recordssid;//笔录ssid

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
