package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DownserverAndDatainfo;
import com.avst.trm.v1.web.req.basereq.GetdownServersParam;

import java.util.List;

public class GetdownServersVO {
    private List<DownserverAndDatainfo> pagelist;

    private GetdownServersParam pageparam;

    private String lastIP;//最后一次同步的IP

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public List<DownserverAndDatainfo> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<DownserverAndDatainfo> pagelist) {
        this.pagelist = pagelist;
    }

    public GetdownServersParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetdownServersParam pageparam) {
        this.pageparam = pageparam;
    }
}
