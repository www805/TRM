package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.Police_template;

import java.util.List;

public class AddTemplateParam extends Police_template {
    private List<Police_problem> templatetoproblemids;//模板题目关联  题目ids

    private Integer templatetypeid;//模板类型ID

    public List<Police_problem> getTemplatetoproblemids() {
        return templatetoproblemids;
    }

    public void setTemplatetoproblemids(List<Police_problem> templatetoproblemids) {
        this.templatetoproblemids = templatetoproblemids;
    }

    public Integer getTemplatetypeid() {
        return templatetypeid;
    }

    public void setTemplatetypeid(Integer templatetypeid) {
        this.templatetypeid = templatetypeid;
    }
}
