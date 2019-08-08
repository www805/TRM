package com.avst.trm.v1.feignclient.ec.vo.param;

public class RecordPlayParam {

    private String xyType;//点播地址的协议

    private String playUrl;//点播地址

    private String filename;//文件名

    private String datatype;//文件类型

    private int filenum;//第几个播放文件

    /**
     * 如果存储的是视频，并且几个视频是相关联的，就需要确认2个视频的重叠时间，S（秒数）
     */
    private Integer repeattime=0;
    /**
     * 这个视频文件录制的服务器开始时间
     */
    private long recordstarttime;

    public long getRecordstarttime() {
        return recordstarttime;
    }

    public void setRecordstarttime(long recordstarttime) {
        this.recordstarttime = recordstarttime;
    }

    public int getFilenum() {
        return filenum;
    }

    public void setFilenum(int filenum) {
        this.filenum = filenum;
    }

    public Integer getRepeattime() {
        return repeattime;
    }

    public void setRepeattime(Integer repeattime) {
        this.repeattime = repeattime;
    }

    public String getXyType() {
        return xyType;
    }

    public void setXyType(String xyType) {
        this.xyType = xyType;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
}
