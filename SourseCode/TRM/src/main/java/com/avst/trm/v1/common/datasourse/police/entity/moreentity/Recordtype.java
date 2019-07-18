package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;

import java.util.List;

public class Recordtype extends RecordtypeToWord {
    private List<RecordtypeToWord> recordtypes;

    public List<RecordtypeToWord> getRecordtypes() {
        return recordtypes;
    }

    public void setRecordtypes(List<RecordtypeToWord> recordtypes) {
        this.recordtypes = recordtypes;
    }
}
