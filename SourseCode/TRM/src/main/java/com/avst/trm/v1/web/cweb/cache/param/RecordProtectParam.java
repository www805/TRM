package com.avst.trm.v1.web.cweb.cache.param;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;

import java.util.List;

public class RecordProtectParam {
    private String recordssid;//笔录ssid

    private String mtssid;//会议ssid;用于获取问答对话以及身心检测数据

    private List<RecordToProblem> recordToProblems;//笔录问答：从实时缓存获取即可

    private  List<AsrTxtParam_toout> asrTxtParamToouts;//asr识别回放

    private List<PHDataBackVoParam> phDataBackVoParams;//身心监测数据

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

    public List<AsrTxtParam_toout> getAsrTxtParamToouts() {
        return asrTxtParamToouts;
    }

    public void setAsrTxtParamToouts(List<AsrTxtParam_toout> asrTxtParamToouts) {
        this.asrTxtParamToouts = asrTxtParamToouts;
    }

    public List<PHDataBackVoParam> getPhDataBackVoParams() {
        return phDataBackVoParams;
    }

    public void setPhDataBackVoParams(List<PHDataBackVoParam> phDataBackVoParams) {
        this.phDataBackVoParams = phDataBackVoParams;
    }

    @Override
    public String toString() {
        return "RecordProtectParam{" +
                "recordssid='" + recordssid + '\'' +
                ", recordToProblems=" + recordToProblems +
                ", mtssid='" + mtssid + '\'' +
                ", asrTxtParamToouts=" + asrTxtParamToouts +
                ", phDataBackVoParams=" + phDataBackVoParams +
                '}';
    }
}
