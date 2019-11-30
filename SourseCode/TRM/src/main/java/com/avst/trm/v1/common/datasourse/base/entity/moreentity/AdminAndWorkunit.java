package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admintorole;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class AdminAndWorkunit extends Base_admininfo {
    private String workname;//单位名称

    private Integer superrolebool=-1;//角色中是否为包含超管:-1否1 是

    private List<Base_role> roles;//角色集合

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public List<Base_role> getRoles() {
        return roles;
    }

    public void setRoles(List<Base_role> roles) {
        this.roles = roles;
    }


    public Integer getSuperrolebool() {
        return superrolebool;
    }

    public void setSuperrolebool(Integer superrolebool) {
        this.superrolebool = superrolebool;
    }
}
