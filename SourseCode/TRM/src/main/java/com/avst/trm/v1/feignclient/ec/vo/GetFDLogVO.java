package com.avst.trm.v1.feignclient.ec.vo;


import com.avst.trm.v1.feignclient.ec.vo.param.FDLogItem;

import java.util.List;

public class GetFDLogVO {

    private String totalpage;//总共有多少页

    private String currpage;//读取的是第几页

    private List<FDLogItem> fdLogItemlist;//日志列表

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public String getCurrpage() {
        return currpage;
    }

    public void setCurrpage(String currpage) {
        this.currpage = currpage;
    }

    public List<FDLogItem> getFdLogItemlist() {
        return fdLogItemlist;
    }

    public void setFdLogItemlist(List<FDLogItem> fdLogItemlist) {
        this.fdLogItemlist = fdLogItemlist;
    }
}
