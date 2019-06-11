package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;

import java.util.List;

public class GetUserinfoListVO {
    private List<UserInfo> userinfos;

    public List<UserInfo> getUserinfos() {
        return userinfos;
    }

    public void setUserinfos(List<UserInfo> userinfos) {
        this.userinfos = userinfos;
    }
}
