package com.avst.trm.v1.outsideinterface.serverinterface.common.vo.Param;

/**
 * 返回需要同步的数据的集合
 */
public class StartSynchronizedataVOParam {

    private String dataname;//同步的表名

    private String filenameOrDatassid;//文件的话就是文件名,数据库的话就是ssid

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getFilenameOrDatassid() {
        return filenameOrDatassid;
    }

    public void setFilenameOrDatassid(String filenameOrDatassid) {
        this.filenameOrDatassid = filenameOrDatassid;
    }
}
