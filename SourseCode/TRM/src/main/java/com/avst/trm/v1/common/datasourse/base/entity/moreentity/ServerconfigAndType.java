package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;

public class ServerconfigAndType extends Base_serverconfig {

    /**
     * 类型名称：法院、检察院、公安、会议、publicweb
     */
    private String type;

    /**
     * 类型名称
     */
    private String typename;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
