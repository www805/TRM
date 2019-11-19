package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;

public class LoginParam extends Base_admininfo {
    private boolean rememberpassword;//记住密码

    public boolean isRememberpassword() {
        return rememberpassword;
    }

    public void setRememberpassword(boolean rememberpassword) {
        this.rememberpassword = rememberpassword;
    }

}
