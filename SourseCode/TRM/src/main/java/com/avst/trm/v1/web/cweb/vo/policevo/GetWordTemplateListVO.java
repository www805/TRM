package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.WordTemplate;
import com.avst.trm.v1.web.cweb.req.policereq.GetWordTemplateListParam;

import java.util.List;

public class GetWordTemplateListVO {
    private List<WordTemplate> pagelist;
    private GetWordTemplateListParam pageparam;

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
