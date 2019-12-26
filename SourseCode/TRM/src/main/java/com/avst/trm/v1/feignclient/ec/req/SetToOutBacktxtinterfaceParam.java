package com.avst.trm.v1.feignclient.ec.req;

public class SetToOutBacktxtinterfaceParam extends FDBaseParam {

    private Integer port;
    private Integer ftpport;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getFtpport() {
        return ftpport;
    }

    public void setFtpport(Integer ftpport) {
        this.ftpport = ftpport;
    }
}