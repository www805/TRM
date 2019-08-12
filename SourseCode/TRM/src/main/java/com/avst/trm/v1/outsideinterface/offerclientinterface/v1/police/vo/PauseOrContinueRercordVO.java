package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

import com.avst.trm.v1.feignclient.mc.vo.PauseOrContinueMCVO;

public class PauseOrContinueRercordVO extends PauseOrContinueMCVO {
    private int pauseOrContinue;//1请求暂停，2请求继续

    public int getPauseOrContinue() {
        return pauseOrContinue;
    }

    public void setPauseOrContinue(int pauseOrContinue) {
        this.pauseOrContinue = pauseOrContinue;
    }
}
