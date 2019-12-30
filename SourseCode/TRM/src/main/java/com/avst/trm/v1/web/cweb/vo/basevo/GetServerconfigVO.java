package com.avst.trm.v1.web.cweb.vo.basevo;


import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;
import com.avst.trm.v1.common.datasourse.police.entity.Police_namingrule;

import java.util.List;

public class GetServerconfigVO {
    private ServerconfigAndFilesave serverconfigAndFilesave;

    private List<Police_namingrule> namingrules;//命名规则

    public List<Police_namingrule> getNamingrules() {
        return namingrules;
    }

    public void setNamingrules(List<Police_namingrule> namingrules) {
        this.namingrules = namingrules;
    }

    public ServerconfigAndFilesave getServerconfigAndFilesave() {
        return serverconfigAndFilesave;
    }

    public void setServerconfigAndFilesave(ServerconfigAndFilesave serverconfigAndFilesave) {
        this.serverconfigAndFilesave = serverconfigAndFilesave;
    }
}
