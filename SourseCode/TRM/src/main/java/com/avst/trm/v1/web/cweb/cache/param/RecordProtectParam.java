package com.avst.trm.v1.web.cweb.cache.param;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;

import java.util.List;

public class RecordProtectParam {
    private String recordssid;//笔录ssid

    private List<RecordToProblem> recordToProblems;//笔录问答：从实时缓存获取即可

    private String mtssid;//会议ssid;用于获取问答对话以及身心检测数据

    private String iid;//直播iid：用于获取直播地址

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public List<RecordToProblem> getRecordToProblems() {
        return recordToProblems;
    }

    public void setRecordToProblems(List<RecordToProblem> recordToProblems) {
        this.recordToProblems = recordToProblems;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    @Override
    public String toString() {
        return "RecordProtectParam{" +
                "recordssid='" + recordssid + '\'' +
                ", recordToProblems=" + recordToProblems +
                ", mtssid='" + mtssid + '\'' +
                ", iid='" + iid + '\'' +
                '}';
    }
}
