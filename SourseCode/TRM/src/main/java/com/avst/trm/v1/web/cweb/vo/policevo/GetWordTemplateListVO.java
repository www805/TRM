package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.WordTemplate;
import com.avst.trm.v1.web.cweb.req.policereq.GetWordTemplateListParam;

import java.util.List;

public class GetWordTemplateListVO {
    private List<WordTemplate> pagelist;
    private GetWordTemplateListParam pageparam;



    private String wordtemplate_explaindownurl;//word模板说明制作下载地址
    private String wordtemplate_explaindownurl_html;//word模板说明制作下载地址转html地址
    private String wordtemplate_explaindownssid;

    public String getWordtemplate_explaindownssid() {
        return wordtemplate_explaindownssid;
    }

    public void setWordtemplate_explaindownssid(String wordtemplate_explaindownssid) {
        this.wordtemplate_explaindownssid = wordtemplate_explaindownssid;
    }

    public String getWordtemplate_explaindownurl() {
        return wordtemplate_explaindownurl;
    }

    public void setWordtemplate_explaindownurl(String wordtemplate_explaindownurl) {
        this.wordtemplate_explaindownurl = wordtemplate_explaindownurl;
    }

    public String getWordtemplate_explaindownurl_html() {
        return wordtemplate_explaindownurl_html;
    }

    public void setWordtemplate_explaindownurl_html(String wordtemplate_explaindownurl_html) {
        this.wordtemplate_explaindownurl_html = wordtemplate_explaindownurl_html;
    }

    public List<WordTemplate> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<WordTemplate> pagelist) {
        this.pagelist = pagelist;
    }

    public GetWordTemplateListParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetWordTemplateListParam pageparam) {
        this.pageparam = pageparam;
    }
}
