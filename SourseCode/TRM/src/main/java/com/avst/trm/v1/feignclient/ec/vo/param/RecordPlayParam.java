package com.avst.trm.v1.feignclient.ec.vo.param;

public class RecordPlayParam {

    private String xyType;//点播地址的协议

    private String playUrl;//点播地址

    private String filename;//文件名

    private String datatype;//文件类型

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
