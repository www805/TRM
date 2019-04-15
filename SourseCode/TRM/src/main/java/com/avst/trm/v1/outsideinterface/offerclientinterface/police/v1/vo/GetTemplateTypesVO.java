package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Templatetype;

import java.util.List;

public class GetTemplateTypesVO {
    private  List<Templatetype> templatetypes;

    public List<Templatetype> getTemplatetypes() {
        return templatetypes;
    }

    public void setTemplatetypes(List<Templatetype> templatetypes) {
        this.templatetypes = templatetypes;
    }
}
