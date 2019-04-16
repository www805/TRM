package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;

import java.util.List;

public class AddTemplateParam extends Police_template {
    private List<Integer> templatetoproblemids;//模板题目关联  题目ids

    public List<Integer> getTemplatetoproblemids() {
        return templatetoproblemids;
    }

    public void setTemplatetoproblemids(List<Integer> templatetoproblemids) {
        this.templatetoproblemids = templatetoproblemids;
    }
}
