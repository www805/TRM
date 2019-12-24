package com.avst.trm.v1.feignclient.mc.vo.param;

public class Base_modeltype {
    private Integer id;

    /**
     * 模板类型名称
     */
    private String modeltypename;

    /**
     * 模板类型编号,0/1/2/3/4(一般0是默认)
     */
    private Integer modeltypenum;

    /**
     * 分支类型
     */
    private String branchtype;

    /**
     * 分支名称
     */
    private String branchname;

    /**
     * 默认模板类型,1默认，0一般
     */
    private Integer defaultmodeltype;

    /**
     * 模板类型状态,1正常、0暂停、-1删除
     */
    private Integer modeltypestate;

    private String ssid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModeltypename() {
        return modeltypename;
    }

    public void setModeltypename(String modeltypename) {
        this.modeltypename = modeltypename;
    }

    public Integer getModeltypenum() {
        return modeltypenum;
    }

    public void setModeltypenum(Integer modeltypenum) {
        this.modeltypenum = modeltypenum;
    }

    public String getBranchtype() {
        return branchtype;
    }

    public void setBranchtype(String branchtype) {
        this.branchtype = branchtype;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public Integer getDefaultmodeltype() {
        return defaultmodeltype;
    }

    public void setDefaultmodeltype(Integer defaultmodeltype) {
        this.defaultmodeltype = defaultmodeltype;
    }

    public Integer getModeltypestate() {
        return modeltypestate;
    }

    public void setModeltypestate(Integer modeltypestate) {
        this.modeltypestate = modeltypestate;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
