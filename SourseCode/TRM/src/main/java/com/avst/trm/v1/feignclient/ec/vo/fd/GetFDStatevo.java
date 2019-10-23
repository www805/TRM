package com.avst.trm.v1.feignclient.ec.vo.fd;

/**
 * 设备状态的参数映射
 * 有需要就直接加
 */
public class GetFDStatevo {

    private String dev_version;
    private String hw;
    private String sw;

    private String devmid_id;

    //设备序列号
    private String serialnumber;

    public String getDev_version() {
        return dev_version;
    }

    public void setDev_version(String dev_version) {
        this.dev_version = dev_version;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getDevmid_id() {
        return devmid_id;
    }

    public void setDevmid_id(String devmid_id) {
        this.devmid_id = devmid_id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }
}
