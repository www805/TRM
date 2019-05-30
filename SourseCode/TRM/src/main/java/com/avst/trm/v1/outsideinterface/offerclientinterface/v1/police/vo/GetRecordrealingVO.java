package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

import com.avst.trm.v1.feignclient.ec.vo.param.FDCacheParam;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;

import java.util.List;

public class GetRecordrealingVO {
    private List<AsrTxtParam_toout> list;

    private List<FDCacheParam> fdCacheParams;

    public List<AsrTxtParam_toout> getList() {
        return list;
    }

    public void setList(List<AsrTxtParam_toout> list) {
        this.list = list;
    }

    public List<FDCacheParam> getFdCacheParams() {
        return fdCacheParams;
    }

    public void setFdCacheParams(List<FDCacheParam> fdCacheParams) {
        this.fdCacheParams = fdCacheParams;
    }
}
