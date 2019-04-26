package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_upserver;

import java.util.Date;

public class GetSynchronizedDataSheet_UpServer extends Base_datasheet_upserver {

    private Date lastuploadtime;//最后同步时间

    public Date getLastuploadtime() {
        return lastuploadtime;
    }

    public void setLastuploadtime(Date lastuploadtime) {
        this.lastuploadtime = lastuploadtime;
    }

    public Integer getUnitsort() {
        return unitsort;
    }

    public void setUnitsort(Integer unitsort) {
        this.unitsort = unitsort;
    }

    public Integer getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(Integer uploadcount) {
        this.uploadcount = uploadcount;
    }

    private Integer unitsort;//同步的下级服务器的序号

    private Integer uploadcount;//同步次数

}
