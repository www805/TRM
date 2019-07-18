package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;

import java.util.List;

public class GetRecordByIdVO {
    private Record record;//笔录信息 //有Word/PDF地址

    private CaseAndUserInfo caseAndUserInfo;//案件信息 //没有文件和地址



    //以下对接外部的返回
    private GetMCVO getMCVO;//asr识别回放sql  //没有文件和地址

    private GetPlayUrlVO getPlayUrlVO;//点播地址 //有文件地址

    private List<PHDataBackVoParam> phDataBackVoParams;//审讯检测数据 //没有文件和地址

    public GetPlayUrlVO getGetPlayUrlVO() {
        return getPlayUrlVO;
    }

    public void setGetPlayUrlVO(GetPlayUrlVO getPlayUrlVO) {
        this.getPlayUrlVO = getPlayUrlVO;
    }

    public List<PHDataBackVoParam> getPhDataBackVoParams() {
        return phDataBackVoParams;
    }

    public void setPhDataBackVoParams(List<PHDataBackVoParam> phDataBackVoParams) {
        this.phDataBackVoParams = phDataBackVoParams;
    }

    public GetMCVO getGetMCVO() {
        return getMCVO;
    }

    public void setGetMCVO(GetMCVO getMCVO) {
        this.getMCVO = getMCVO;
    }

    public CaseAndUserInfo getCaseAndUserInfo() {
        return caseAndUserInfo;
    }

    public void setCaseAndUserInfo(CaseAndUserInfo caseAndUserInfo) {
        this.caseAndUserInfo = caseAndUserInfo;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
