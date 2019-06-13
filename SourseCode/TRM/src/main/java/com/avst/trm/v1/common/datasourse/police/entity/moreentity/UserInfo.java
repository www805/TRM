package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;

public class UserInfo extends Police_userinfo {
    private String cardtypename;//证件名称

    private String cardtypessid;//证件类型ssid

    private String cardnum;//证件号码

    public String getCardtypessid() {
        return cardtypessid;
    }

    public void setCardtypessid(String cardtypessid) {
        this.cardtypessid = cardtypessid;
    }

    public String getCardtypename() {
        return cardtypename;
    }

    public void setCardtypename(String cardtypename) {
        this.cardtypename = cardtypename;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }
}
