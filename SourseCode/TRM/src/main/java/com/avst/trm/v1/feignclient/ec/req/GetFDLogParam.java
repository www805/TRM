package com.avst.trm.v1.feignclient.ec.req;

public class GetFDLogParam extends FDBaseParam {

    private int page=-1;//查询的页，每页最大 15 行信息

    private int logtype=0;//0 -> ALL,暂时不分类，只查0

    private int fy=-1;//查询某年份（1990 - 2050）

    private int fm=-1;//查询某月份（1-12）

    private int fd=-1;//查询某日期（1-31 根据月份而定义的有效）

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLogtype() {
        return logtype;
    }

    public void setLogtype(int logtype) {
        this.logtype = logtype;
    }

    public int getFy() {
        return fy;
    }

    public void setFy(int fy) {
        this.fy = fy;
    }

    public int getFm() {
        return fm;
    }

    public void setFm(int fm) {
        this.fm = fm;
    }

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }
}
