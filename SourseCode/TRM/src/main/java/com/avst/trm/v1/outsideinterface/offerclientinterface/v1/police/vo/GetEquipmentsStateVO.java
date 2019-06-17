package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

public class GetEquipmentsStateVO {
    //状态： -1异常  0未启动 1正常
    private Integer AsrState;//语音识别状态

    private Integer PolygraphState;//身心检测状态

    private Integer LiveState;//直播状态

    private Integer MtState;//会议状态

    private Integer PlayState;//点播文件状态

    public Integer getAsrState() {
        return AsrState;
    }

    public void setAsrState(Integer asrState) {
        AsrState = asrState;
    }

    public Integer getPolygraphState() {
        return PolygraphState;
    }

    public void setPolygraphState(Integer polygraphState) {
        PolygraphState = polygraphState;
    }

    public Integer getLiveState() {
        return LiveState;
    }

    public void setLiveState(Integer liveState) {
        LiveState = liveState;
    }

    public Integer getMtState() {
        return MtState;
    }

    public void setMtState(Integer mtState) {
        MtState = mtState;
    }

    public Integer getPlayState() {
        return PlayState;
    }

    public void setPlayState(Integer playState) {
        PlayState = playState;
    }
}
