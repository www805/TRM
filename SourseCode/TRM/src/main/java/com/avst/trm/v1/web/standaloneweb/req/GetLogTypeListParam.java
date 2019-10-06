package com.avst.trm.v1.web.standaloneweb.req;

public class GetLogTypeListParam {

    private int sysOrFd=1;//查询哪一个系统的日志，1是笔录系统，2是设备嵌入式系统

    public int getSysOrFd() {
        return sysOrFd;
    }

    public void setSysOrFd(int sysOrFd) {
        this.sysOrFd = sysOrFd;
    }
}
