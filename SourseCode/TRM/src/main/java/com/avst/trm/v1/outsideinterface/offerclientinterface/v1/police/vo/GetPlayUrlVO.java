package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

import com.avst.trm.v1.feignclient.ec.vo.CheckRecordFileStateVO;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;

import java.util.List;

public class GetPlayUrlVO {
    private String iid;

    private List<RecordFileParam> recordFileParams;//文件状态集合

    private List<RecordPlayParam> recordPlayParams;//播放文件集合

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public List<RecordFileParam> getRecordFileParams() {
        return recordFileParams;
    }

    public void setRecordFileParams(List<RecordFileParam> recordFileParams) {
        this.recordFileParams = recordFileParams;
    }

    public List<RecordPlayParam> getRecordPlayParams() {
        return recordPlayParams;
    }

    public void setRecordPlayParams(List<RecordPlayParam> recordPlayParams) {
        this.recordPlayParams = recordPlayParams;
    }
}
