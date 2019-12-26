package com.avst.trm.v1.web.sweb.req.basereq;

public class GetServerPortALLParam {

    private Integer trmport;
    private Integer zkport;
    private Integer mcport;
    private Integer ecport;
    private Integer saveinfoport;
    private Integer ftpport;
    private Integer socketioport;
    private Integer nginxport;
    private String anzhuangpath;

    public Integer getTrmport() {
        return trmport;
    }

    public void setTrmport(Integer trmport) {
        this.trmport = trmport;
    }

    public Integer getZkport() {
        return zkport;
    }

    public void setZkport(Integer zkport) {
        this.zkport = zkport;
    }

    public Integer getMcport() {
        return mcport;
    }

    public void setMcport(Integer mcport) {
        this.mcport = mcport;
    }

    public Integer getEcport() {
        return ecport;
    }

    public void setEcport(Integer ecport) {
        this.ecport = ecport;
    }

    public Integer getSaveinfoport() {
        return saveinfoport;
    }

    public void setSaveinfoport(Integer saveinfoport) {
        this.saveinfoport = saveinfoport;
    }

    public Integer getFtpport() {
        return ftpport;
    }

    public void setFtpport(Integer ftpport) {
        this.ftpport = ftpport;
    }

    public Integer getSocketioport() {
        return socketioport;
    }

    public void setSocketioport(Integer socketioport) {
        this.socketioport = socketioport;
    }

    public Integer getNginxport() {
        return nginxport;
    }

    public void setNginxport(Integer nginxport) {
        this.nginxport = nginxport;
    }

    public String getAnzhuangpath() {
        return anzhuangpath;
    }

    public void setAnzhuangpath(String anzhuangpath) {
        this.anzhuangpath = anzhuangpath;
    }
}
