package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetRecordsParam extends Page {
    private String recordname;//笔录名

    private String recordtypessid;//笔录类型

    private Integer recordbool;//笔录状态

    private boolean creatorbool=false;//是否只看创建人的

    public boolean isCreatorbool() {
        return creatorbool;
    }

    public void setCreatorbool(boolean creatorbool) {
        this.creatorbool = creatorbool;
    }

    public Integer getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(Integer recordbool) {
        this.recordbool = recordbool;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }
}
