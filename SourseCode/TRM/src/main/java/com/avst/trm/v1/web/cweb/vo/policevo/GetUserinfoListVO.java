package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;

import java.util.List;

public class GetUserinfoListVO {
    private List<Police_userinfo> userinfos;

    public List<Police_userinfo> getUserinfos() {
        return userinfos;
    }

    public void setUserinfos(List<Police_userinfo> userinfos) {
        this.userinfos = userinfos;
    }
}
