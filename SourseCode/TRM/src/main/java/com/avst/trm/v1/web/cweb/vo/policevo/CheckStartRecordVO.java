package com.avst.trm.v1.web.cweb.vo.policevo;

import java.util.List;

public class CheckStartRecordVO {
    private String mtmodel_ssid;//被占用的会议模板

    private List<String> admininfos_ssid;//被占用的询问人集合

    private String userinfo_ssid;//被占用的被询问人

    private String msg;//检测返回提示

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
