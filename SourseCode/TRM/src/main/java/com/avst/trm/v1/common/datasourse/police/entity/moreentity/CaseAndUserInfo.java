package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;

import java.util.List;

/**
 * 案件笔录数据
 */
public class CaseAndUserInfo extends Police_case {
    private String username;

    private List<ArraignmentAndRecord> arraignments;//多次提讯数据


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
