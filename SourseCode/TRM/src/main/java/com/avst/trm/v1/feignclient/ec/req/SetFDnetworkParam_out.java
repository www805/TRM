package com.avst.trm.v1.feignclient.ec.req;

public class SetFDnetworkParam_out extends BaseAvstParam {

    private int ajust=1;//1|0 是否立即生效

    private String dev="eth0";//eth0 | eth1 网口序号

    private String ip_new;//设备 IP

    private String netmask;//设备子网掩码

    private String gateway;//设备网关

    public int getAjust() {
        return ajust;
    }

    public void setAjust(int ajust) {
        this.ajust = ajust;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

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
}
