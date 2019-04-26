package com.avst.trm.v1.report.toupserver.common.reqparam;

/**
 * 同步数据的类型和参数
 */
public class SynchronizeDataTypeParam {

    private int type;//同步的类型，0同步全部数据，1同步一个表的数据，2同步一条数据

    private String datatablename;//同步数据的表的名称（英文）

    private String sdIP;//同步的上级服务器的IP

    //同步数据的ssid，如果是同步表的话就同步表数据库的ssid（这时候就没什么用），如果是同步一条数据的话就是该数据的ssid
    private String datassid;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatatablename() {
        return datatablename;
    }

    public void setDatatablename(String datatablename) {
        this.datatablename = datatablename;
    }

    public String getDatassid() {
        return datassid;
    }

    public void setDatassid(String datassid) {
        this.datassid = datassid;
    }

    public String getSdIP() {
        return sdIP;
    }

    public void setSdIP(String sdIP) {
        this.sdIP = sdIP;
    }
}
