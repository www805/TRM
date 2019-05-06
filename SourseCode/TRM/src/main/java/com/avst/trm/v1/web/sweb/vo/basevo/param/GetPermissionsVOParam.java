package com.avst.trm.v1.web.sweb.vo.basevo.param;

import com.avst.trm.v1.common.datasourse.base.entity.Base_permissions;

import java.util.ArrayList;
import java.util.List;

public class GetPermissionsVOParam extends Base_permissions{
    private List<Base_permissions> permissionsList=new ArrayList<Base_permissions>();

    public List<Base_permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Base_permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }
}
