package com.avst.trm.v1.report.toupserver.common.vo;


import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;

import java.util.List;

public class StartSynchronizedataVO {

    private List<StartSynchronizedata_2_Param> datalist;

    private int total;

    public List<StartSynchronizedata_2_Param> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<StartSynchronizedata_2_Param> datalist) {
        this.datalist = datalist;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
