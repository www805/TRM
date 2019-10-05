package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Case extends Police_case {

    private String creatorname;//创建人名称

    private List<UserInfo> userInfos;//多个案件人信息

    private List<ArraignmentAndRecord> arraignments;//多次提讯数据

    @JsonFormat(pattern="yyyy年MM月dd日HH时mm分ss秒",timezone="GMT+8")
    private Date occurrencetime_format;//案发时间换格式

    private String record_pausebool;//案件是否允许继续暂停，用于是否显示列表的继续按钮

    public String getRecord_pausebool() {
        return record_pausebool;
    }

    public void setRecord_pausebool(String record_pausebool) {
        this.record_pausebool = record_pausebool;
    }

    public Date getOccurrencetime_format() {
        return occurrencetime_format;
    }

    public void setOccurrencetime_format(Date occurrencetime_format) {
        this.occurrencetime_format = occurrencetime_format;
    }

    public List<ArraignmentAndRecord> getArraignments() {
        return arraignments;
    }

    public void setArraignments(List<ArraignmentAndRecord> arraignments) {
        this.arraignments = arraignments;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }
}
