package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;

import java.util.List;

public class DownloadNotificationVO {
    private Base_filesave base_filesave;
    private String recorddownurl_html;//下载html地址

    private List<String> recorddownurl_htmlreads;//需要朗读的文本

    public List<String> getRecorddownurl_htmlreads() {
        return recorddownurl_htmlreads;
    }

    public void setRecorddownurl_htmlreads(List<String> recorddownurl_htmlreads) {
        this.recorddownurl_htmlreads = recorddownurl_htmlreads;
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
