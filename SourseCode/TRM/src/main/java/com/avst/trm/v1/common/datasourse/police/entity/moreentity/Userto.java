package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userto;

/**
 * 其他在场人人员信息
 */
public class Userto extends Police_userto {
    //可能需要新增其他人员
    private String cardtypessid;//当前证件类型ssid
    private String cardnum;//当前证件号码
    private String username;//姓名
    private Integer sex;//性别
    private String phone;//联系电话

    public String getCardtypessid() {
        return cardtypessid;
    }

    public void setCardtypessid(String cardtypessid) {
        this.cardtypessid = cardtypessid;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

