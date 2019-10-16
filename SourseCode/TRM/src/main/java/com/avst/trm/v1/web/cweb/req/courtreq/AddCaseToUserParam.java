package com.avst.trm.v1.web.cweb.req.courtreq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;

import java.util.List;
import java.util.Map;

public class AddCaseToUserParam {
    private String casessid;//案件ssid
    private String adminssid;//询问人一的ssid
    private String recordtypessid;//笔录类型ssid
    private String recordplace;//问话地点
    private Integer asknum;//询问次数
    private String mtmodelssid;//会议模板ssid
    private String wordtemplatessid;//笔录模板ssid

    private Police_case addPolice_case;//新增案件的信息

    private Integer skipCheckbool=-1;//是否跳过检测1跳过-1不跳过
    private Integer skipCheckCasebool=-1;//是否跳过检测案件状态 1跳过-1不跳过
    private Integer multifunctionbool;//功能类型 1 单功能 2双功能 3 多功能

    private List<Map<String,UserInfo>> arraignmentexpand;//拓展表数据

    public List<Map<String, UserInfo>> getArraignmentexpand() {
        return arraignmentexpand;
    }

    public void setArraignmentexpand(List<Map<String, UserInfo>> arraignmentexpand) {
        this.arraignmentexpand = arraignmentexpand;
    }

    public String getCasessid() {
        return casessid;
    }

    public void setCasessid(String casessid) {
        this.casessid = casessid;
    }

    public String getAdminssid() {
        return adminssid;
    }

    public void setAdminssid(String adminssid) {
        this.adminssid = adminssid;
    }

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }

    public String getRecordplace() {
        return recordplace;
    }

    public void setRecordplace(String recordplace) {
        this.recordplace = recordplace;
    }

    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
    }

    public String getMtmodelssid() {
        return mtmodelssid;
    }

    public void setMtmodelssid(String mtmodelssid) {
        this.mtmodelssid = mtmodelssid;
    }

    public String getWordtemplatessid() {
        return wordtemplatessid;
    }

    public void setWordtemplatessid(String wordtemplatessid) {
        this.wordtemplatessid = wordtemplatessid;
    }

    public Police_case getAddPolice_case() {
        return addPolice_case;
    }

    public void setAddPolice_case(Police_case addPolice_case) {
        this.addPolice_case = addPolice_case;
    }

    public Integer getSkipCheckbool() {
        return skipCheckbool;
    }

    public void setSkipCheckbool(Integer skipCheckbool) {
        this.skipCheckbool = skipCheckbool;
    }

    public Integer getSkipCheckCasebool() {
        return skipCheckCasebool;
    }

    public void setSkipCheckCasebool(Integer skipCheckCasebool) {
        this.skipCheckCasebool = skipCheckCasebool;
    }

    public Integer getMultifunctionbool() {
        return multifunctionbool;
    }

    public void setMultifunctionbool(Integer multifunctionbool) {
        this.multifunctionbool = multifunctionbool;
    }
}
