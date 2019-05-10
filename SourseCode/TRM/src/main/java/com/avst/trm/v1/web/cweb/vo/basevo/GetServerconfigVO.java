package com.avst.trm.v1.web.cweb.vo.basevo;


import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;

public class GetServerconfigVO {
    private ServerconfigAndFilesave serverconfigAndFilesave;

    public ServerconfigAndFilesave getServerconfigAndFilesave() {
        return serverconfigAndFilesave;
    }

    public void setServerconfigAndFilesave(ServerconfigAndFilesave serverconfigAndFilesave) {
        this.serverconfigAndFilesave = serverconfigAndFilesave;
    }
}
