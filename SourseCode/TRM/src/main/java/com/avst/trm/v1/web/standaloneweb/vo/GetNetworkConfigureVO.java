package com.avst.trm.v1.web.standaloneweb.vo;

public class GetNetworkConfigureVO {

    private String name;//网卡名
    private String ip;//本机ip
    private String subnetMask;//子网掩码
    private String gateway;//网关

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GetNetworkConfigureVO{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", subnetMask='" + subnetMask + '\'' +
                ", gateway='" + gateway + '\'' +
                '}';
    }
}
