package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;

import java.util.List;

public class CheckStartRecordParam {
    private String mtmodel_ssid;//会议模板

    private List<String> admininfos_ssid;//询问人集合

    private String userinfo_ssid;//被询问人

    public String getMtmodel_ssid() {
        return mtmodel_ssid;
    }

    public void setMtmodel_ssid(String mtmodel_ssid) {
        this.mtmodel_ssid = mtmodel_ssid;
    }

    public List<String> getAdmininfos_ssid() {
        return admininfos_ssid;
    }

    public void setAdmininfos_ssid(List<String> admininfos_ssid) {
        this.admininfos_ssid = admininfos_ssid;
    }

    public String getUserinfo_ssid() {
        return userinfo_ssid;
    }

    public void setUserinfo_ssid(String userinfo_ssid) {
        this.userinfo_ssid = userinfo_ssid;
    }
}
