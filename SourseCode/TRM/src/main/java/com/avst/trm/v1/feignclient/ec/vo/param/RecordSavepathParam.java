package com.avst.trm.v1.feignclient.ec.vo.param;

public class RecordSavepathParam {

    private String xyType;//地址的协议

    private String savepath;//存储地址

    private String soursedatapath;//源存储路径，从哪来的

    private String filename;//文件名

    private String datatype;//文件类型

    private int filenum;//第几个播放文件

    /**
     * 如果存储的是视频，并且几个视频是相关联的，就需要确认2个视频的重叠时间，S（秒数）
     */
    private Integer repeattime=0;

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

    public String getSoursedatapath() {
        return soursedatapath;
    }

    public void setSoursedatapath(String soursedatapath) {
        this.soursedatapath = soursedatapath;
    }

    public String getXyType() {
        return xyType;
    }

    public void setXyType(String xyType) {
        this.xyType = xyType;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
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
