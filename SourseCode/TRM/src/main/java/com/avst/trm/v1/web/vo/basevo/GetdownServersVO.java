package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;

import java.util.List;

public class GetdownServersVO {
    private List<Base_datasynchroni_downserver> datasynchroni_downservers;

    private List<Base_datainfo> datainfos;

    public List<Base_datasynchroni_downserver> getDatasynchroni_downservers() {
        return datasynchroni_downservers;
    }

    public void setDatasynchroni_downservers(List<Base_datasynchroni_downserver> datasynchroni_downservers) {
        this.datasynchroni_downservers = datasynchroni_downservers;
    }

    public List<Base_datainfo> getDatainfos() {
        return datainfos;
    }

    public void setDatainfos(List<Base_datainfo> datainfos) {
        this.datainfos = datainfos;
    }
}
