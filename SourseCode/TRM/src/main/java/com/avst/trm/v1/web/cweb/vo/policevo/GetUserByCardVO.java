package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;

import java.util.List;

public class GetUserByCardVO {
    private Police_userinfo userinfo;//人员信息

    private List<Police_case> cases;//人员相关的案件信息

    private List<AdminAndWorkunit> otheruserinfos;//询问人员等

    private Integer asknum;//提讯次数；


    public Integer getAsknum() {
        return asknum;
    }

    public void setAsknum(Integer asknum) {
        this.asknum = asknum;
    }

    public List<AdminAndWorkunit> getOtheruserinfos() {
        return otheruserinfos;
    }

    public void setOtheruserinfos(List<AdminAndWorkunit> otheruserinfos) {
        this.otheruserinfos = otheruserinfos;
    }

    public Police_userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Police_userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<Police_case> getCases() {
        return cases;
    }

    public void setCases(List<Police_case> cases) {
        this.cases = cases;
    }
}
