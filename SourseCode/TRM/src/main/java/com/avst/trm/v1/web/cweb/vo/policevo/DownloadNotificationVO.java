package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;

public class DownloadNotificationVO {
    private Base_filesave base_filesave;
    private String recorddownurl_html;//下载html地址
    private String recorddownurl_htmlread;//下载地址的内容朗读

    public String getRecorddownurl_htmlread() {
        return recorddownurl_htmlread;
    }

    public void setRecorddownurl_htmlread(String recorddownurl_htmlread) {
        this.recorddownurl_htmlread = recorddownurl_htmlread;
    }

    public Base_filesave getBase_filesave() {
        return base_filesave;
    }

    public void setBase_filesave(Base_filesave base_filesave) {
        this.base_filesave = base_filesave;
    }

    public String getRecorddownurl_html() {
        return recorddownurl_html;
    }

    public void setRecorddownurl_html(String recorddownurl_html) {
        this.recorddownurl_html = recorddownurl_html;
    }
}
