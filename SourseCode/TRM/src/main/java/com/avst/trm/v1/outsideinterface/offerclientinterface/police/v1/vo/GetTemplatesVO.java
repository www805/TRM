package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplatesParam;

import java.util.List;

public class GetTemplatesVO {
    private List<Template> templates;
    private GetTemplatesParam templatesParam;

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public GetTemplatesParam getTemplatesParam() {
        return templatesParam;
    }

    public void setTemplatesParam(GetTemplatesParam templatesParam) {
        this.templatesParam = templatesParam;
    }
}
