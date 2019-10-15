package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetProblemsParam extends Page {
    private String keyword;//关键字

    private String problemtypeid;//问题类型ID

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProblemtypeid() {
        return problemtypeid;
    }

    public void setProblemtypeid(String problemtypeid) {
        this.problemtypeid = problemtypeid;
    }
}
