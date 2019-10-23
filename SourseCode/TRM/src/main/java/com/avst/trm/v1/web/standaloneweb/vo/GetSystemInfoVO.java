package com.avst.trm.v1.web.standaloneweb.vo;

import com.avst.trm.v1.web.standaloneweb.vo.param.HardwareParam;
import com.avst.trm.v1.web.standaloneweb.vo.param.SQParam;
import com.avst.trm.v1.web.standaloneweb.vo.param.SoftwareParam;

public class GetSystemInfoVO {

    private SQParam sqParam;//授权信息

    private SoftwareParam softwareParam;//软件信息

    private HardwareParam hardwareParam;//硬件信息

    public SQParam getSqParam() {
        return sqParam;
    }

    public void setSqParam(SQParam sqParam) {
        this.sqParam = sqParam;
    }

    public SoftwareParam getSoftwareParam() {
        return softwareParam;
    }

    public void setSoftwareParam(SoftwareParam softwareParam) {
        this.softwareParam = softwareParam;
    }

    public HardwareParam getHardwareParam() {
        return hardwareParam;
    }

    public void setHardwareParam(HardwareParam hardwareParam) {
        this.hardwareParam = hardwareParam;
    }
}
