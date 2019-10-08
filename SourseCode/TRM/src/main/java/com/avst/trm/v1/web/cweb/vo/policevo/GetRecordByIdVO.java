package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_national;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
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

    private String record_pausebool;//笔录是否允许暂停1允许 -1 不允许 默认不允许

    private String record_adjournbool;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1


    /**
     * 用于编辑人员笔录信息
     */
    private  List<AdminAndWorkunit> adminList;//全部用户，
    private  List<Base_nationality> nationalityList;//全部国籍
    private  List<Base_national> nationalList;//全部民族


    public List<AdminAndWorkunit> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<AdminAndWorkunit> adminList) {
        this.adminList = adminList;
    }

    public List<Base_nationality> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<Base_nationality> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<Base_national> getNationalList() {
        return nationalList;
    }

    public void setNationalList(List<Base_national> nationalList) {
        this.nationalList = nationalList;
    }

    public String getRecord_adjournbool() {
        return record_adjournbool;
    }

    public void setRecord_adjournbool(String record_adjournbool) {
        this.record_adjournbool = record_adjournbool;
    }

    public String getRecord_pausebool() {
        return record_pausebool;
    }

    public void setRecord_pausebool(String record_pausebool) {
        this.record_pausebool = record_pausebool;
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
