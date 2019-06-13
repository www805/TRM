package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userto;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;

import java.util.List;

public class AddCaseToArraignmentParam {
    private String userssid;
    private String casessid;
    private String adminssid;
    private String otheradminssid;
    private String recordadminssid;
    private String recordtypessid;
    private String recordplace;
    private String recordname;
    private Integer askobj;
    private Integer asknum;
    private List<Police_userto> usertos;//其他在场人员信息

    private Integer adduser_bool;//是否需要新增人员 1需要 -1不需要
    private UserInfo addUserInfo;//新增人员的信息

    private Integer addcase_bool;//是否需要新增案件 1需要 -1不需要
    private Police_case addPolice_case;//新增案件的信息

    public Integer getAdduser_bool() {
        return adduser_bool;
    }

    public void setAdduser_bool(Integer adduser_bool) {
        this.adduser_bool = adduser_bool;
    }

    public UserInfo getAddUserInfo() {
        return addUserInfo;
    }

    public void setAddUserInfo(UserInfo addUserInfo) {
        this.addUserInfo = addUserInfo;
    }

    public Integer getAddcase_bool() {
        return addcase_bool;
    }

    public void setAddcase_bool(Integer addcase_bool) {
        this.addcase_bool = addcase_bool;
    }

    public Police_case getAddPolice_case() {
        return addPolice_case;
    }

    public void setAddPolice_case(Police_case addPolice_case) {
        this.addPolice_case = addPolice_case;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }

    public List<Police_userto> getUsertos() {
        return usertos;
    }

    public void setUsertos(List<Police_userto> usertos) {
        this.usertos = usertos;
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

    public String getOtheradminssid() {
        return otheradminssid;
    }

    public void setOtheradminssid(String otheradminssid) {
        this.otheradminssid = otheradminssid;
    }

    public String getRecordadminssid() {
        return recordadminssid;
    }

    public void setRecordadminssid(String recordadminssid) {
        this.recordadminssid = recordadminssid;
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

    public Integer getAskobj() {
        return askobj;
    }

    public void setAskobj(Integer askobj) {
        this.askobj = askobj;
    }

    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
    }
}
