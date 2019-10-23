package com.avst.trm.v1.web.standaloneweb.req;

public class GetNetworkConfigureParam {

    private String ip;//本机ip
    private String netmask;//子网掩码
    private String gateway;//网关

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
