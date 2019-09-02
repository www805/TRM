package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.feignclient.mc.vo.param.Avstmt_modeltd;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;

import java.util.List;

public class GetRecordByIdVO {
    private Record record;//笔录信息 //有Word/PDF地址


    //以下对接外部的返回
    private GetMCVO getMCVO;//asr识别回放sql  //没有文件和地址

    private GetPlayUrlVO getPlayUrlVO;//点播地址 //有文件地址

    private List<PHDataBackVoParam> phDataBackVoParams;//审讯检测数据 //没有文件和地址

    private List<Avstmt_modeltd> modeltds;//会议模板通道list


    private String recordtype_conversation1;//默认谈话笔录ssid:一键笔录

    private String recordtype_conversation2;//默认谈话笔录ssid:开启笔录

    public String getRecordtype_conversation1() {
        return recordtype_conversation1;
    }

    public void setRecordtype_conversation1(String recordtype_conversation1) {
        this.recordtype_conversation1 = recordtype_conversation1;
    }

    public String getRecordtype_conversation2() {
        return recordtype_conversation2;
    }

    public void setRecordtype_conversation2(String recordtype_conversation2) {
        this.recordtype_conversation2 = recordtype_conversation2;
    }

    public List<Avstmt_modeltd> getModeltds() {
        return modeltds;
    }

    public void setModeltds(List<Avstmt_modeltd> modeltds) {
        this.modeltds = modeltds;
    }

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

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
