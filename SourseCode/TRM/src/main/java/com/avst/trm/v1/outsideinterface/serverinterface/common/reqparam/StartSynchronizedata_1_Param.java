package com.avst.trm.v1.outsideinterface.serverinterface.common.reqparam;

import java.util.List;

/**
 * 同步前检测下级服务器所有同步的数据是否在上级服务器中存在的实体类
 * StartSynchronizedata_1_Param 本次同步的总计
 * StartSynchronizedata_2_Param 本次同步的list数据的基本对象
 */
public class StartSynchronizedata_1_Param {

    private int totalnum;//总计有多少条需要同步的数据

    private int finishednum;//已完成的数量

    private List<StartSynchronizedata_2_Param> datalist;//待同步的数据集合

    private String sdTableSsid;//数据同步表的ssid，用于更新同步数据

    public String getSdTableSsid() {
        return sdTableSsid;
    }

    public void setSdTableSsid(String sdTableSsid) {
        this.sdTableSsid = sdTableSsid;
    }

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public int getFinishednum() {
        return finishednum;
    }

    public void setFinishednum(int finishednum) {
        this.finishednum = finishednum;
    }

    public List<StartSynchronizedata_2_Param> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<StartSynchronizedata_2_Param> datalist) {

        this.totalnum=datalist.size();
        if(totalnum > 0){
            int finishednum_=0;
            for(StartSynchronizedata_2_Param data:datalist){
                if(data.isOverwork()){
                    finishednum_++;
                }
            }
            this.finishednum=finishednum_;
        }
        this.datalist = datalist;
    }
}
