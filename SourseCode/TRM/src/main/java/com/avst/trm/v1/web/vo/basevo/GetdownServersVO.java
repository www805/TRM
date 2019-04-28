package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;
import com.avst.trm.v1.web.req.basereq.GetdownServersParam;

import java.util.List;

public class GetdownServersVO {
    private List<Base_datasynchroni_downserver> pagelist;

    private List<Base_datainfo> datainfos;

    private GetdownServersParam pageparam;

    public List<Base_datasynchroni_downserver> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Base_datasynchroni_downserver> pagelist) {
        this.pagelist = pagelist;
    }

    public List<Base_datainfo> getDatainfos() {
        return datainfos;
    }

    public void setDatainfos(List<Base_datainfo> datainfos) {
        this.datainfos = datainfos;
    }

    public GetdownServersParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetdownServersParam pageparam) {
        this.pageparam = pageparam;
    }
}
