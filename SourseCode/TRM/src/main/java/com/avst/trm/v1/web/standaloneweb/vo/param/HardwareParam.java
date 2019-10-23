package com.avst.trm.v1.web.standaloneweb.vo.param;

public class HardwareParam {

    private String version;//设备版本

    private String serialnumber;//设备序列号

    private String fdid;;//设备ID

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getFdid() {
        return fdid;
    }

    public void setFdid(String fdid) {
        this.fdid = fdid;
    }
}
