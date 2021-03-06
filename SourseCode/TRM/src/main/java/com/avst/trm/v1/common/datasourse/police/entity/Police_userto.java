package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 89088 kB
 * </p>
 *
 * @author Admin
 * @since 2019-05-15
 */
public class Police_userto extends Model<Police_userto> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 被询问人id
     */
    private String userssid;

    /**
     * 其他关系人id
     */
    private String otheruserssid;

    /**
     * 所属关系 例：父子，母女
     */
    private String relation;

    /**
     * 语种
     */
    private String language;

    /**
     * 人员类型:1监护人；2翻译人员；3手语人员
     */
    private Integer usertype;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 人员类型标题
     */
    private String usertitle;

    /**
     * 提讯ssid
     */
    private String arraignmentssid;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }
    public String getOtheruserssid() {
        return otheruserssid;
    }

    public void setOtheruserssid(String otheruserssid) {
        this.otheruserssid = otheruserssid;
    }
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }
    public String getArraignmentssid() {
        return arraignmentssid;
    }

    public void setArraignmentssid(String arraignmentssid) {
        this.arraignmentssid = arraignmentssid;
    }
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }
    public Integer getInteger1() {
        return integer1;
    }

    public void setInteger1(Integer integer1) {
        this.integer1 = integer1;
    }
    public Integer getInteger2() {
        return integer2;
    }

    public void setInteger2(Integer integer2) {
        this.integer2 = integer2;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Police_userto{" +
        "id=" + id +
        ", userssid=" + userssid +
        ", otheruserssid=" + otheruserssid +
        ", relation=" + relation +
        ", language=" + language +
        ", usertype=" + usertype +
        ", createtime=" + createtime +
        ", usertitle=" + usertitle +
        ", arraignmentssid=" + arraignmentssid +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
