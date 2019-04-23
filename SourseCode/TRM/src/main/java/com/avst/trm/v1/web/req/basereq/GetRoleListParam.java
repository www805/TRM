package com.avst.trm.v1.web.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.util.baseaction.Page;

public class GetRoleListParam extends Page {
    private String rolename;

    private Integer rolebool;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Integer getRolebool() {
        return rolebool;
    }

    public void setRolebool(Integer rolebool) {
        this.rolebool = rolebool;
    }
}
