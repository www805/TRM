package com.avst.trm.v1.web.cweb.req.courtreq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;
import com.avst.trm.v1.web.cweb.req.policereq.param.ArrUserExpandParam;

import java.util.List;

public class UpdateCaseToUserParam {

    private Police_case case_;//案件信息

    private Police_arraignment arraignment;//提讯信息

    private String recordssid;//笔录ssid

    //**暂时针对法院版人员
    private List<UserInfo> arraignmentexpand;//拓展表数据：针对未存在用户，主要用于外来人员

    private List<ArrUserExpandParam> arrUserExpandParams;//拓展表数据;针对已存在用户：主要用于内部管理员

    public Police_case getCase_() {
        return case_;
    }

    public void setCase_(Police_case case_) {
        this.case_ = case_;
    }

    public Police_arraignment getArraignment() {
        return arraignment;
    }

    public void setArraignment(Police_arraignment arraignment) {
        this.arraignment = arraignment;
    }

    public String getRecordssid() {
        return recordssid;
    }

    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public List<UserInfo> getArraignmentexpand() {
        return arraignmentexpand;
    }

    public void setArraignmentexpand(List<UserInfo> arraignmentexpand) {
        this.arraignmentexpand = arraignmentexpand;
    }

    public List<ArrUserExpandParam> getArrUserExpandParams() {
        return arrUserExpandParams;
    }

    public void setArrUserExpandParams(List<ArrUserExpandParam> arrUserExpandParams) {
        this.arrUserExpandParams = arrUserExpandParams;
    }
}
