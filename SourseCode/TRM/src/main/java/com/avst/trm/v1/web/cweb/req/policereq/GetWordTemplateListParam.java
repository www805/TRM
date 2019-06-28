package com.avst.trm.v1.web.cweb.req.policereq;


import com.avst.trm.v1.common.util.baseaction.Page;

public class GetWordTemplateListParam extends Page {
    private String wordtemplatename;//word模板名称
    private String recordtypessid;//笔录类型

    public String getWordtemplatename() {
        return wordtemplatename;
    }

    public void setWordtemplatename(String wordtemplatename) {
        this.wordtemplatename = wordtemplatename;
    }

    public String getRecordtypessid() {
        return recordtypessid;
    }

    public void setRecordtypessid(String recordtypessid) {
        this.recordtypessid = recordtypessid;
    }
}
