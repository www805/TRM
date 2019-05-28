package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 案件笔录数据
 */
public class CaseAndUserInfo extends Police_case {
    private String userssid;//用户ssid
    private String username;

    private Integer asknum;//询问次数

    @JsonFormat(pattern="yyyy年MM月dd日HH时mm分ss秒",timezone="GMT+8")
    private Date occurrencetime_format;//案发时间换格式

    private List<ArraignmentAndRecord> arraignments;//多次提讯数据

    @Override
    public String getUserssid() {
        return userssid;
    }

    @Override
    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public Date getOccurrencetime_format() {
        return occurrencetime_format;
    }

    public void setOccurrencetime_format(Date occurrencetime_format) {
        this.occurrencetime_format = occurrencetime_format;
    }

    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ArraignmentAndRecord> getArraignments() {
        return arraignments;
    }

    public void setArraignments(List<ArraignmentAndRecord> arraignments) {
        this.arraignments = arraignments;
    }
}
