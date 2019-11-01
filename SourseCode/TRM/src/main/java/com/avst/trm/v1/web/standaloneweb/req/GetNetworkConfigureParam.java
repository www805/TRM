package com.avst.trm.v1.web.standaloneweb.req;

public class GetNetworkConfigureParam {

    private String ip_new;//本机ip
    private String netmask;//子网掩码
    private String gateway;//网关
    private String dev="eth0";//eth0 | eth1 网口序号
    private String flushbonadingetinfossid;//嵌入式设备ssid
    private String fdType;//嵌入式设备选择的类型，avst设备使用FD_AVST

    public String getIp_new() {
        return ip_new;
    }

    public void setIp_new(String ip_new) {
        this.ip_new = ip_new;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getFlushbonadingetinfossid() {
        return flushbonadingetinfossid;
    }

    public void setFlushbonadingetinfossid(String flushbonadingetinfossid) {
        this.flushbonadingetinfossid = flushbonadingetinfossid;
    }

    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }
}
