package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;

import java.util.List;

public class UserInfo extends Police_userinfo {
    private String cardtypename;//当前证件名称

    private String cardtypessid;//当前证件类型ssid

    private String cardnum;//当前证件号码

    private String usertotypessid;//当前证件人员关系的ssid


    //多角色使用
    private String userinfogradessid;//人员级别表ssid
    private Integer grade;//人员级别
    private String gradename;//级别名称


    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    private List<UserInfoAndCard> cards;//多证件证件

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getUserinfogradessid() {
        return userinfogradessid;
    }

    public void setUserinfogradessid(String userinfogradessid) {
        this.userinfogradessid = userinfogradessid;
    }

    public String getUsertotypessid() {
        return usertotypessid;
    }

    public void setUsertotypessid(String usertotypessid) {
        this.usertotypessid = usertotypessid;
    }

    public List<UserInfoAndCard> getCards() {
        return cards;
    }

    public void setCards(List<UserInfoAndCard> cards) {
        this.cards = cards;
    }

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
