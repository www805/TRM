package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;

public class AdminAndAdmintorole extends Base_admininfo {

    private Integer admintoroleid;

    /**
     * 管理员编号
     */
    private Integer adminid;

    /**
     * 角色编号
     */
    private Integer roleid;

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }
    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Integer getAdmintoroleid() {
        return admintoroleid;
    }

    public void setAdmintoroleid(Integer admintoroleid) {
        this.admintoroleid = admintoroleid;
    }

}
