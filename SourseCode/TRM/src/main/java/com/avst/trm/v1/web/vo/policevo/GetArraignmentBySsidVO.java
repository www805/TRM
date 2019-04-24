package com.avst.trm.v1.web.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtemplate;

import java.util.List;

public class GetArraignmentBySsidVO {
    private List<Police_recordtemplate> recordtemplates;//全部模板


    public List<Police_recordtemplate> getRecordtemplates() {
        return recordtemplates;
    }

    public void setRecordtemplates(List<Police_recordtemplate> recordtemplates) {
        this.recordtemplates = recordtemplates;
    }
}
