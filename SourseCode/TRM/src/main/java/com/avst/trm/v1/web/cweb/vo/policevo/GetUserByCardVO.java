package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;

import java.util.List;

public class GetUserByCardVO {
    private UserInfo userinfo;//人员信息

    private List<CaseAndUserInfo> cases;//人员相关的案件信息

    private List<AdminAndWorkunit> otheruserinfos;//询问人员等

    public List<AdminAndWorkunit> getOtheruserinfos() {
        return otheruserinfos;
    }

    public void setOtheruserinfos(List<AdminAndWorkunit> otheruserinfos) {
        this.otheruserinfos = otheruserinfos;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<CaseAndUserInfo> getCases() {
        return cases;
    }

    public void setCases(List<CaseAndUserInfo> cases) {
        this.cases = cases;
    }
}
