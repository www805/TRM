package com.avst.trm.v1.feignclient.ec.vo;

import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;

import java.util.List;

public class CheckRecordFileStateVO {

    private String iid;

    private List<RecordFileParam> recordList;

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public List<RecordFileParam> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordFileParam> recordList) {
        this.recordList = recordList;
    }
}
