package com.avst.trm.v1.web.cweb.vo.policevo.param;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;

import java.util.ArrayList;
import java.util.List;

public class GetRecordtypesVOParam extends Police_recordtype{
    private List<Police_recordtype> police_recordtypes=new ArrayList<Police_recordtype>();

    public List<Police_recordtype> getPolice_recordtypes() {
        return police_recordtypes;
    }

    public void setPolice_recordtypes(List<Police_recordtype> police_recordtypes) {
        this.police_recordtypes = police_recordtypes;
    }
}
