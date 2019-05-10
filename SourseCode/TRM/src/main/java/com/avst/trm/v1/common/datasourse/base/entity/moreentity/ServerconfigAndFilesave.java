package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;

public class ServerconfigAndFilesave extends Base_serverconfig {
    private String client_downurl;//客户端logo下载地址

    private String client_realurl;//客户端logo真实地址

    private String syslogo_downurl;//服务端logo下载地址

    private String syslogo_realurl;//服务端logo真实地址

    public String getClient_downurl() {
        return client_downurl;
    }

    public void setClient_downurl(String client_downurl) {
        this.client_downurl = client_downurl;
    }

    public String getClient_realurl() {
        return client_realurl;
    }

    public void setClient_realurl(String client_realurl) {
        this.client_realurl = client_realurl;
    }

    public String getSyslogo_downurl() {
        return syslogo_downurl;
    }

    public void setSyslogo_downurl(String syslogo_downurl) {
        this.syslogo_downurl = syslogo_downurl;
    }

    public String getSyslogo_realurl() {
        return syslogo_realurl;
    }

    public void setSyslogo_realurl(String syslogo_realurl) {
        this.syslogo_realurl = syslogo_realurl;
    }
}
