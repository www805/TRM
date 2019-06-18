package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;

public class DownloadNotificationVO {
    private Base_filesave base_filesave;
    private String recorddownurl_html;//下载html地址

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
