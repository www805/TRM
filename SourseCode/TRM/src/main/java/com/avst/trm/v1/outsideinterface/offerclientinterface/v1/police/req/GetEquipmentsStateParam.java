package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

public class GetEquipmentsStateParam {
    private String mtssid;//获取ssid
    private Integer fdrecord;//是否需要录像，1使用，-1 不使用
    private Integer usepolygraph;//是否使用测谎仪，1使用，-1 不使用
    private Integer useasr;//是否使用语言识别，1使用，-1 不使用

    private int recordnum=0;//本次会议开启的录音/像个数
    private int asrnum=0;//本次会议开启的语音识别个数
    private int polygraphnum=0;//本次会议开启的测谎仪个数

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

    public Integer getFdrecord() {
        return fdrecord;
    }

    public void setFdrecord(Integer fdrecord) {
        this.fdrecord = fdrecord;
    }

    public Integer getUsepolygraph() {
        return usepolygraph;
    }

    public void setUsepolygraph(Integer usepolygraph) {
        this.usepolygraph = usepolygraph;
    }

    public Integer getUseasr() {
        return useasr;
    }

    public void setUseasr(Integer useasr) {
        this.useasr = useasr;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }
}
