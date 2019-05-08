package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;

import java.util.List;

public class AddTemplateParam extends Police_template {
    private List<Integer> templatetoproblemids;//模板题目关联  题目ids

    private Integer templatetypeid;//模板类型ID

    public List<Integer> getTemplatetoproblemids() {
        return templatetoproblemids;
    }

    public void setTemplatetoproblemids(List<Integer> templatetoproblemids) {
        this.templatetoproblemids = templatetoproblemids;
    }

    public Integer getTemplatetypeid() {
        return templatetypeid;
    }

    public void setTemplatetypeid(Integer templatetypeid) {
        this.templatetypeid = templatetypeid;
    }
}
