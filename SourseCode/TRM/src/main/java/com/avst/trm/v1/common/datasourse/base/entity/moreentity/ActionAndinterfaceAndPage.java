package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_action;

public class ActionAndinterfaceAndPage extends Base_action {

    private String typename;

    private String type;

    private String interfaceurl;

    private Integer firstpage;//是否是这一分类的首页，1是，-1不是

    private Integer page_id_c;//当前页面的id

    private Integer page_id_n;//下一个页面的id

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getInterfaceurl() {
        return interfaceurl;
    }

    public void setInterfaceurl(String interfaceurl) {
        this.interfaceurl = interfaceurl;
    }

    public Integer getPage_id_c() {
        return page_id_c;
    }

    public void setPage_id_c(Integer page_id_c) {
        this.page_id_c = page_id_c;
    }

    public Integer getPage_id_n() {
        return page_id_n;
    }

    public void setPage_id_n(Integer page_id_n) {
        this.page_id_n = page_id_n;
    }

    public Integer getFirstpage() {
        return firstpage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFirstpage(Integer firstpage) {
        this.firstpage = firstpage;
    }
}
