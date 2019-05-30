package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;

import java.util.List;

public class GetMCVO {
    private List<AsrTxtParam_toout> list;

    private String iid;//直播数据存储iid

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public List<AsrTxtParam_toout> getList() {
        return list;
    }

    public void setList(List<AsrTxtParam_toout> list) {
        this.list = list;
    }
}
