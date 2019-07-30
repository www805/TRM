package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

import com.avst.trm.v1.feignclient.mc.vo.StartMCVO;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckStartRecordVO;

public class StartRercordVO {
    private boolean recordbool=false;//是否开过笔录了 false:m没有 true：开过了

    private CheckStartRecordVO checkStartRecordVO;

    private StartMCVO startMCVO;//笔录开始返回结果

    public boolean isRecordbool() {
        return recordbool;
    }

    public void setRecordbool(boolean recordbool) {
        this.recordbool = recordbool;
    }

    public CheckStartRecordVO getCheckStartRecordVO() {
        return checkStartRecordVO;
    }

    public void setCheckStartRecordVO(CheckStartRecordVO checkStartRecordVO) {
        this.checkStartRecordVO = checkStartRecordVO;
    }

    public StartMCVO getStartMCVO() {
        return startMCVO;
    }

    public void setStartMCVO(StartMCVO startMCVO) {
        this.startMCVO = startMCVO;
    }
}
