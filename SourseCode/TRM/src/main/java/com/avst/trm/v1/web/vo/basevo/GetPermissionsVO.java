package com.avst.trm.v1.web.vo.basevo;

import com.avst.trm.v1.web.vo.basevo.param.GetPermissionsVOParam;

import java.util.List;

public class GetPermissionsVO {
    private List<GetPermissionsVOParam> permissionsVOParams;

    public List<GetPermissionsVOParam> getPermissionsVOParams() {
        return permissionsVOParams;
    }

    public void setPermissionsVOParams(List<GetPermissionsVOParam> permissionsVOParams) {
        this.permissionsVOParams = permissionsVOParams;
    }
}
