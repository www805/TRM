package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordreal;

public class Recordreal extends Police_recordreal {
    private String recorddownurl;//录音实时下载地址

    private String recordrealurl;//录音实时真实地址

    private String askusername;//询问人名称

    private String askedusername;//被询问人名称


    public String getAskusername() {
        return askusername;
    }

    public void setAskusername(String askusername) {
        this.askusername = askusername;
    }

    public String getAskedusername() {
        return askedusername;
    }

    public void setAskedusername(String askedusername) {
        this.askedusername = askedusername;
    }

    public String getRecorddownurl() {
        return recorddownurl;
    }

    public void setRecorddownurl(String recorddownurl) {
        this.recorddownurl = recorddownurl;
    }

    public String getRecordrealurl() {
        return recordrealurl;
    }

    public void setRecordrealurl(String recordrealurl) {
        this.recordrealurl = recordrealurl;
    }
}
