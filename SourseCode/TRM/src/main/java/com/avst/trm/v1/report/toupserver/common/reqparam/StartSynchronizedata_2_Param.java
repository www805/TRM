package com.avst.trm.v1.report.toupserver.common.reqparam;

/**
 * 同步前检测下级服务器所有同步的数据是否在上级服务器中存在的实体类
 * 所有类型都可以共用
 */
public class StartSynchronizedata_2_Param {
    private String dataname;//同步的表名

    private Integer type=1;//文件还是数据库，1是数据库，2是文件

    private String datassid;//数据库的话就是ssid,文件在数据库中也是有ssid的

    private String filename;//如果是文件的话还需要写出文件名

    private boolean overwork=false;//是否已经同步完成


    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isOverwork() {
        return overwork;
    }

    public void setOverwork(boolean overwork) {
        this.overwork = overwork;
    }


    public StartSynchronizedata_2_Param(){

    }

    public String getDatassid() {
        return datassid;
    }

    public void setDatassid(String datassid) {
        this.datassid = datassid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public StartSynchronizedata_2_Param(String dataname, Integer type, String datassid) {
        this.dataname = dataname;
        this.type = type;
        this.datassid = datassid;
    }

    public StartSynchronizedata_2_Param(String dataname, Integer type, String datassid, String filename) {
        this.dataname = dataname;
        this.type = type;
        this.datassid = datassid;
        this.filename = filename;
    }
}
