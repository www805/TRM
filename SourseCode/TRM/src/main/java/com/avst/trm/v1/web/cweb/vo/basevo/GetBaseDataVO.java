package com.avst.trm.v1.web.cweb.vo.basevo;

import com.avst.trm.v1.common.datasourse.base.entity.Base_national;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.police.entity.Police_cardtype;
import com.avst.trm.v1.common.datasourse.police.entity.Police_namingrule;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfograde;
import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;

import java.util.List;

public class GetBaseDataVO {
    private  List<AdminAndWorkunit> adminList;//全部用户，
    private  List<Base_nationality> nationalityList;//全部国籍
    private  List<Base_national> nationalList;//全部民族
    private  List<Police_workunit> workunitList;//全部工作单位
    private  List<Police_cardtype> cardtypeList;//全部证件类型
    private  List<Police_userinfograde> userinfogradeList;//人员级别类型
    private  List<Police_namingrule> namingruleList;//笔录命名规则

    public List<AdminAndWorkunit> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<AdminAndWorkunit> adminList) {
        this.adminList = adminList;
    }

    public List<Base_nationality> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<Base_nationality> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<Base_national> getNationalList() {
        return nationalList;
    }

    public void setNationalList(List<Base_national> nationalList) {
        this.nationalList = nationalList;
    }

    public List<Police_workunit> getWorkunitList() {
        return workunitList;
    }

    public void setWorkunitList(List<Police_workunit> workunitList) {
        this.workunitList = workunitList;
    }

    public List<Police_cardtype> getCardtypeList() {
        return cardtypeList;
    }

    public void setCardtypeList(List<Police_cardtype> cardtypeList) {
        this.cardtypeList = cardtypeList;
    }

    public List<Police_userinfograde> getUserinfogradeList() {
        return userinfogradeList;
    }

    public void setUserinfogradeList(List<Police_userinfograde> userinfogradeList) {
        this.userinfogradeList = userinfogradeList;
    }

    public List<Police_namingrule> getNamingruleList() {
        return namingruleList;
    }

    public void setNamingruleList(List<Police_namingrule> namingruleList) {
        this.namingruleList = namingruleList;
    }
}
