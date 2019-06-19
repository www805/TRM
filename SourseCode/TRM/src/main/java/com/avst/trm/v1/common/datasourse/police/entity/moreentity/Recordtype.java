package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;

import java.util.List;

public class Recordtype extends Police_recordtype {
    private List<Police_recordtype> recordtypes;

    public List<Police_recordtype> getRecordtypes() {
        return recordtypes;
    }

    public void setRecordtypes(List<Police_recordtype> recordtypes) {
        this.recordtypes = recordtypes;
    }
}
