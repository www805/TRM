package com.avst.trm.v1.web.cweb.vo.policevo.param;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordtypeToWord;

import java.util.ArrayList;
import java.util.List;

public class GetRecordtypesVOParam extends RecordtypeToWord {
    private List<RecordtypeToWord> police_recordtypes=new ArrayList<RecordtypeToWord>();

    public List<RecordtypeToWord> getPolice_recordtypes() {
        return police_recordtypes;
    }

    public void setPolice_recordtypes(List<RecordtypeToWord> police_recordtypes) {
        this.police_recordtypes = police_recordtypes;
    }
}
