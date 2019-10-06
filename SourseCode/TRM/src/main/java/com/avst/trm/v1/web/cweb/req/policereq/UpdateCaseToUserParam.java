package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;

public class UpdateCaseToUserParam {
    private UserInfo userInfo;//被询问人信息

    private Police_case case_;//案件信息

    private Police_arraignment arraignment;//提讯信息

    private String recordssid;//笔录ssid

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

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
}
