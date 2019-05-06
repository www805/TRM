package com.avst.trm.v1.web.cweb.req;

import com.avst.trm.v1.common.util.baseaction.Page;

public class GetProblemsParam extends Page {
    private String keyword;//关键字

    private Integer problemtypeid;//问题类型ID

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getProblemtypeid() {
        return problemtypeid;
    }

    public void setProblemtypeid(Integer problemtypeid) {
        this.problemtypeid = problemtypeid;
    }
}
