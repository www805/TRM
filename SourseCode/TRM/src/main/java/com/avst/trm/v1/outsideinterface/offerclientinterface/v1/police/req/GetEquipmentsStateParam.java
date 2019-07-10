package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req;

public class GetEquipmentsStateParam {
    private String mtssid;//获取ssid
    private Integer fdrecord;//是否需要录像，1使用，-1 不使用
    private Integer usepolygraph;//是否使用测谎仪，1使用，-1 不使用
    private Integer useasr;//是否使用语言识别，1使用，-1 不使用
    private Integer asrRun;//语音识别服务是否启动 1开启 -1不开起

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

    public Integer getAsrRun() {
        return asrRun;
    }

    public void setAsrRun(Integer asrRun) {
        this.asrRun = asrRun;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }
}
