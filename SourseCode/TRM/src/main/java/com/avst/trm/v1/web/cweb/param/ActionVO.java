package com.avst.trm.v1.web.cweb.param;

/**
 * 客户端请求动作的集合
 */
public class ActionVO {

    private String actionId;//该请求动作的唯一编号，一个请求一个编号

    private String reqURL;//请求后台的路径（相对的）

    private int gotopageOrRefresh=1;//1是跳转页面,2是局部刷新

    private String nextPageId;//跳转页面的编号

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getReqURL() {
        return reqURL;
    }

    public void setReqURL(String reqURL) {
        this.reqURL = reqURL;
    }

    public String getNextPageId() {
        return nextPageId;
    }

    public void setNextPageId(String nextPageId) {
        this.nextPageId = nextPageId;
    }

    public int getGotopageOrRefresh() {
        return gotopageOrRefresh;
    }

    public void setGotopageOrRefresh(int gotopageOrRefresh) {
        this.gotopageOrRefresh = gotopageOrRefresh;
    }
}
