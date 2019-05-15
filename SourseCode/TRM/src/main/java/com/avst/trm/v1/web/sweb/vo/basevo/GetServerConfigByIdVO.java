package com.avst.trm.v1.web.sweb.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;

public class GetServerConfigByIdVO {

    private Base_serverconfig serverconfig;

    private String syslogoimage;

    private String clientimage;

    public String getSyslogoimage() {
        return syslogoimage;
    }

    public void setSyslogoimage(String syslogoimage) {
        this.syslogoimage = syslogoimage;
    }

    public String getClientimage() {
        return clientimage;
    }

    public void setClientimage(String clientimage) {
        this.clientimage = clientimage;
    }

    public Base_serverconfig getServerconfig() {
        return serverconfig;
    }

    public void setServerconfig(Base_serverconfig serverconfig) {
        this.serverconfig = serverconfig;
    }
}
