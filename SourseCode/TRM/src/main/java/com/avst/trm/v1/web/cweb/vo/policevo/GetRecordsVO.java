package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordsParam;

import java.util.List;

public class GetRecordsVO {
    private List<Record> pagelist;//全部笔录

    private GetRecordsParam pageparam;

    private String recordtype_conversation1;//默认谈话笔录ssid:一键笔录

    private String recordtype_conversation2;//默认谈话笔录ssid:开启笔录

    public String getRecordtype_conversation1() {
        return recordtype_conversation1;
    }

    public void setRecordtype_conversation1(String recordtype_conversation1) {
        this.recordtype_conversation1 = recordtype_conversation1;
    }

    public String getRecordtype_conversation2() {
        return recordtype_conversation2;
    }

    public void setRecordtype_conversation2(String recordtype_conversation2) {
        this.recordtype_conversation2 = recordtype_conversation2;
    }

    public List<Record> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Record> pagelist) {
        this.pagelist = pagelist;
    }

    public GetRecordsParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetRecordsParam pageparam) {
        this.pageparam = pageparam;
    }
}
