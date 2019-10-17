package com.avst.trm.v1.feignclient.ec.req;

public class SetFDAudioVolumeParam_out extends BaseAvstParam {

    private int ch=0;//设置哪一个通道

    private int volume;//音频音量

    private int save=0;//是否保存，1保存

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }
}
