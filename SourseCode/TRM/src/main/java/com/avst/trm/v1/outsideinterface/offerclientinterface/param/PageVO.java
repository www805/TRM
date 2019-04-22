package com.avst.trm.v1.outsideinterface.offerclientinterface.param;

import java.util.List;

/**
 * 页面动作集合
 */
public class PageVO {

    private String pageid;//页面id

    private List<ActionVO> actionList;//该页面对应的所有请求动作集合

    public  String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public List<ActionVO> getActionList() {
        return actionList;
    }

    public void setActionList(List<ActionVO> actionList) {
        this.actionList = actionList;
    }
}
