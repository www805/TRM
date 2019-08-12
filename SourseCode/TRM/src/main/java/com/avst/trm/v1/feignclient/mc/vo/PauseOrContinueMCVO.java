package com.avst.trm.v1.feignclient.mc.vo;

public class PauseOrContinueMCVO {

    private int recordnum;//录音设备暂停/停止个数

    private int asrnum;//语音识别服务暂停/停止个数

    private int polygraphnum;//测谎仪服务暂停/停止个数

    public int getRecordnum() {
        return recordnum;
    }

    public void setRecordnum(int recordnum) {
        this.recordnum = recordnum;
    }

    public int getAsrnum() {
        return asrnum;
    }

    public void setAsrnum(int asrnum) {
        this.asrnum = asrnum;
    }

    public int getPolygraphnum() {
        return polygraphnum;
    }

    public void setPolygraphnum(int polygraphnum) {
        this.polygraphnum = polygraphnum;
    }
}
