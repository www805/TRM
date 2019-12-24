package com.avst.trm.v1.common.util.ipandport;

public class UpdatePortParam {

    //==-1代表不修改

    private int zkserverport=-1;//总控zk的对外开放端口

    private int trmserverport=-1;//trm的对外开放端口

    private int mcserverport=-1;//mc的对外开放端口

    private int ecserverport=-1;//ec的对外开放端口

    private int socketioserverport=-1;//socketio的对外开放端口

    private int ftpserverport=-1;//ftp的对外开放端口

    private int nginxserverport=-1;//nginx的对外开放端口

    private String anzhuangpath;//程序安装路径

    public String getAnzhuangpath() {
        return anzhuangpath;
    }

    public void setAnzhuangpath(String anzhuangpath) {
        this.anzhuangpath = anzhuangpath;
    }

    public int getZkserverport() {
        return zkserverport;
    }

    public void setZkserverport(int zkserverport) {
        this.zkserverport = zkserverport;
    }

    public int getTrmserverport() {
        return trmserverport;
    }

    public void setTrmserverport(int trmserverport) {
        this.trmserverport = trmserverport;
    }

    public int getMcserverport() {
        return mcserverport;
    }

    public void setMcserverport(int mcserverport) {
        this.mcserverport = mcserverport;
    }

    public int getEcserverport() {
        return ecserverport;
    }

    public void setEcserverport(int ecserverport) {
        this.ecserverport = ecserverport;
    }

    public int getSocketioserverport() {
        return socketioserverport;
    }

    public void setSocketioserverport(int socketioserverport) {
        this.socketioserverport = socketioserverport;
    }

    public int getFtpserverport() {
        return ftpserverport;
    }

    public void setFtpserverport(int ftpserverport) {
        this.ftpserverport = ftpserverport;
    }

    public int getNginxserverport() {
        return nginxserverport;
    }

    public void setNginxserverport(int nginxserverport) {
        this.nginxserverport = nginxserverport;
    }
}
