package com.avst.trm.v1.web.cweb.req.policereq;

public class ExportWordParam {
    private String recordssid;//笔录ssid

    private boolean wordheadbool=true;//是否导出笔录头文件，默认true

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public boolean isWordheadbool() {
        return wordheadbool;
    }

    public void setWordheadbool(boolean wordheadbool) {
        this.wordheadbool = wordheadbool;
    }
}
