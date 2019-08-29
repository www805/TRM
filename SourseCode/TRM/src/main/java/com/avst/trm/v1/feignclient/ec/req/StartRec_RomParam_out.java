package com.avst.trm.v1.feignclient.ec.req;

public class StartRec_RomParam_out extends BaseAvstParam {

    private int dx=0;//0：全部光驱 1：光驱一 2：光驱二

    private String iid;//视频文件的唯一标识

    private String bmode="bmode";//仅当 dx 为 0 时有效，exchange 表示为使用接力刻录模式，当没有值或其他值是表示使用双光驱刻录

    private int burntime;//刻录时间，以小时为单位1-24

    public int getBurntime() {

        if(burntime!=0){
            if(burntime < 0){
                burntime=1;
            }else if(burntime > 24){
                burntime=24;
            }
        }
        return burntime;
    }

    public void setBurntime(int burntime) {
        this.burntime = burntime;
    }

    public String getBmode() {
        return bmode;
    }

    public void setBmode(String bmode) {
        this.bmode = bmode;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}