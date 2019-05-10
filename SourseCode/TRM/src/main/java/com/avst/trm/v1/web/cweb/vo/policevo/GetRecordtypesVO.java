package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.avst.trm.v1.web.sweb.vo.basevo.param.GetPermissionsVOParam;

import java.util.List;

public class GetRecordtypesVO {
    private List<GetRecordtypesVOParam> getRecordtypesVOParamList;

    public List<GetRecordtypesVOParam> getGetRecordtypesVOParamList() {

        return getRecordtypesVOParamList;
    }

    public void setGetRecordtypesVOParamList(List<GetRecordtypesVOParam> getRecordtypesVOParamList) {
        this.getRecordtypesVOParamList = getRecordtypesVOParamList;
    }
}
