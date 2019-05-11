package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
import com.avst.trm.v1.web.cweb.req.policereq.GetTemplatesParam;

import java.util.List;

public class GetTemplatesVO {
    private List<Template> pagelist;
    private GetTemplatesParam pageparam;

    public List<Template> getPagelist() {
        return pagelist;
    }

    public void setPagelist(List<Template> templates) {
        this.pagelist = templates;
    }

    public GetTemplatesParam getPageparam() {
        return pageparam;
    }

    public void setPageparam(GetTemplatesParam pageparam) {
        this.pageparam = pageparam;
    }
}
