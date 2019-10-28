package com.avst.trm.v1.feignclient.ec.req;

public class SetFDNTPParam_out extends BaseAvstParam {


    private int ntp_enable=1;//NTP是否启用，1启用，0不启用

    private String ntp_host;//NTP服务器的IP

    private String ntp_port;//ntp服务器的开放端口

    private String ntp_intervaltime;//ntp同步时间

    public int getNtp_enable() {
        return ntp_enable;
    }

    public void setNtp_enable(int ntp_enable) {
        this.ntp_enable = ntp_enable;
    }

    public String getNtp_host() {
        return ntp_host;
    }

    public void setNtp_host(String ntp_host) {
        this.ntp_host = ntp_host;
    }

    public String getNtp_port() {
        return ntp_port;
    }

    public void setNtp_port(String ntp_port) {
        this.ntp_port = ntp_port;
    }

    public String getNtp_intervaltime() {
        return ntp_intervaltime;
    }

    public void setNtp_intervaltime(String ntp_intervaltime) {
        this.ntp_intervaltime = ntp_intervaltime;
    }
}
