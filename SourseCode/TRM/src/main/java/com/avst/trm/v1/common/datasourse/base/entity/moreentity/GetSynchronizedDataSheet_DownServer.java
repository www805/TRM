package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_downserver;

import java.util.Date;

public class GetSynchronizedDataSheet_DownServer extends Base_datasheet_downserver {

    private Date lastuploadtime;//最后同步时间

    public Date getLastuploadtime() {
        return lastuploadtime;
    }

    public void setLastuploadtime(Date lastuploadtime) {
        this.lastuploadtime = lastuploadtime;
    }

    public String getUpserverip() {
        return upserverip;
    }

    public void setUpserverip(String upserverip) {
        this.upserverip = upserverip;
    }

    public Integer getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(Integer uploadcount) {
        this.uploadcount = uploadcount;
    }

    private String upserverip;//同步的上级服务器的IP

    private Integer uploadcount;//同步次数
}
