package com.avst.trm.v1.feignclient.ec.vo.param;

public class FDCacheParam {

    private String fdSsid;//嵌入式设备的ssid

    private String fdType;//那种设备，avst的是FD_AVST

    private String livingUrl;//该设备的直播地址；

    private String previewurl;//该设备的直播预览地址；

    private int port;//开放接口的端口

    private String ip;

    private String user;

    private String passwd;

    private int useRecord;//录音状态，0未开始，1录音中，2录音结束，3录音异常

    private long recordStartTime;//录音开始时间 long ms

    private String recordFileiid;//录音文件的唯一识别码

    public String getRecordFileiid() {
        return recordFileiid;
    }

    public void setRecordFileiid(String recordFileiid) {
        this.recordFileiid = recordFileiid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFdSsid() {
        return fdSsid;
    }

    public void setFdSsid(String fdSsid) {
        this.fdSsid = fdSsid;
    }

    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }

    public String getLivingUrl() {
        return livingUrl;
    }

    public void setLivingUrl(String livingUrl) {
        this.livingUrl = livingUrl;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUseRecord() {
        return useRecord;
    }

    public void setUseRecord(int useRecord) {
        this.useRecord = useRecord;
    }

    public long getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(long recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public String getPreviewurl() {
        return previewurl;
    }

    public void setPreviewurl(String previewurl) {
        this.previewurl = previewurl;
    }
}
