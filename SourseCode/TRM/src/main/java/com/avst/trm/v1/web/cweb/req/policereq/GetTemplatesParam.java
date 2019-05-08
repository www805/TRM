package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetTemplatesParam extends Page {
    private String keyword;//关键字

    private Integer templatetypeid;//模板类型ID

    private String token;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getTemplatetypeid() {
        return templatetypeid;
    }

    public void setTemplatetypeid(Integer templatetypeid) {
        this.templatetypeid = templatetypeid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
