package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.ArraignmentAndRecord;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordUserInfos;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Usergrade;

import java.util.List;

public class GetCasesByCasenumVO {
    private Case case_;

    private ArraignmentAndRecord arraignmentAndRecord;

    private RecordUserInfos recordUserInfos;

    private List<Usergrade> usergrades;

    public List<Usergrade> getUsergrades() {
        return usergrades;
    }

    public void setUsergrades(List<Usergrade> usergrades) {
        this.usergrades = usergrades;
    }

    public RecordUserInfos getRecordUserInfos() {
        return recordUserInfos;
    }

    public void setRecordUserInfos(RecordUserInfos recordUserInfos) {
        this.recordUserInfos = recordUserInfos;
    }

    public ArraignmentAndRecord getArraignmentAndRecord() {
        return arraignmentAndRecord;
    }

    public void setArraignmentAndRecord(ArraignmentAndRecord arraignmentAndRecord) {
        this.arraignmentAndRecord = arraignmentAndRecord;
    }

    public Case getCase_() {
        return case_;
    }

    public void setCase_(Case case_) {
        this.case_ = case_;
    }
}
