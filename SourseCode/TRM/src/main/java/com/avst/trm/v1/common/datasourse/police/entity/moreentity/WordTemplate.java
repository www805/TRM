package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_wordtemplate;

public class WordTemplate extends Police_wordtemplate {

    private String wordtemplate_downurl;//word下载地址

    private String wordtemplate_realurl;//word真实地址

    private String wordtemplate_downurl_html;//word下载地址转html地址

    public String getWordtemplate_downurl_html() {
        return wordtemplate_downurl_html;
    }

    public void setWordtemplate_downurl_html(String wordtemplate_downurl_html) {
        this.wordtemplate_downurl_html = wordtemplate_downurl_html;
    }

    public String getWordtemplate_downurl() {
        return wordtemplate_downurl;
    }

    public void setWordtemplate_downurl(String wordtemplate_downurl) {
        this.wordtemplate_downurl = wordtemplate_downurl;
    }

    public String getWordtemplate_realurl() {
        return wordtemplate_realurl;
    }

    public void setWordtemplate_realurl(String wordtemplate_realurl) {
        this.wordtemplate_realurl = wordtemplate_realurl;
    }
}
