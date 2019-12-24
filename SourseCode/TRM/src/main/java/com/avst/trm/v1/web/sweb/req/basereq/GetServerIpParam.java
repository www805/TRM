package com.avst.trm.v1.web.sweb.req.basereq;

public class GetServerIpParam {

    private String baseType;
    private String trmip;
    private ServerIpssidParam flushbonadingip;
    private ServerIpssidParam asrip;
    private ServerIpssidParam polygraphip;
    private ServerIpssidParam storageip;
    private ServerIpssidParam ttsetinfoip;

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public String getTrmip() {
        return trmip;
    }

    public void setTrmip(String trmip) {
        this.trmip = trmip;
    }

    public ServerIpssidParam getFlushbonadingip() {
        return flushbonadingip;
    }

    public void setFlushbonadingip(ServerIpssidParam flushbonadingip) {
        this.flushbonadingip = flushbonadingip;
    }

    public ServerIpssidParam getAsrip() {
        return asrip;
    }

    public void setAsrip(ServerIpssidParam asrip) {
        this.asrip = asrip;
    }

    public ServerIpssidParam getPolygraphip() {
        return polygraphip;
    }

    public void setPolygraphip(ServerIpssidParam polygraphip) {
        this.polygraphip = polygraphip;
    }

    public ServerIpssidParam getStorageip() {
        return storageip;
    }

    public void setStorageip(ServerIpssidParam storageip) {
        this.storageip = storageip;
    }

    public ServerIpssidParam getTtsetinfoip() {
        return ttsetinfoip;
    }

    public void setTtsetinfoip(ServerIpssidParam ttsetinfoip) {
        this.ttsetinfoip = ttsetinfoip;
    }

}
