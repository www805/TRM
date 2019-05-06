package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.datasourse.base.entity.Base_permissions;

import java.util.List;

public class UpdateRoleToPermissionsParam  {
    private List<Base_permissions> permissions;//权限集合

    private String rolessid;//角色ID

    public List<Base_permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Base_permissions> permissions) {
        this.permissions = permissions;
    }

    public String getRolessid() {
        return rolessid;
    }

    public void setRolessid(String rolessid) {
        this.rolessid = rolessid;
    }
}
