package com.avst.trm.v1.web.standaloneweb.vo;

public class GetAboutVO {
    private String sysmsg;//系统信息

    private String companymsg;//公司信息

    private String runbookdownurl;//操作说明书下载地址

    private String runbookdownurl_html;//操作说明书预览地址

    public String getSysmsg() {
        return sysmsg;
    }

    public void setSysmsg(String sysmsg) {
        this.sysmsg = sysmsg;
    }

    public String getCompanymsg() {
        return companymsg;
    }

    public void setCompanymsg(String companymsg) {
        this.companymsg = companymsg;
    }

    public String getRunbookdownurl() {
        return runbookdownurl;
    }

    public void setRunbookdownurl(String runbookdownurl) {
        this.runbookdownurl = runbookdownurl;
    }

    public String getRunbookdownurl_html() {
        return runbookdownurl_html;
    }

    public void setRunbookdownurl_html(String runbookdownurl_html) {
        this.runbookdownurl_html = runbookdownurl_html;
    }
}
