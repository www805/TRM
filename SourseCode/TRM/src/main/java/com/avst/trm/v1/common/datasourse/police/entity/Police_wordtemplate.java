package com.avst.trm.v1.common.datasourse.police.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Admin
 * @since 2019-06-27
 */
public class Police_wordtemplate extends Model<Police_wordtemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * 笔录word模板表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Word模板名称
     */
    private String wordtemplatename;

    /**
     * Word模板地址
     */
    private String wordtemplate_filesavessid;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    private Integer defaultbool;//是否为默认word模板1是 -1不是

    private Integer wordtype;//word笔录模板类型1笔录模板2笔录制作说明只允许一个笔录模板制作说明存在

    private Integer wordtemplatebool;//模板状态默认1正常 -1删除

    public Integer getWordtemplatebool() {
        return wordtemplatebool;
    }

    public void setWordtemplatebool(Integer wordtemplatebool) {
        this.wordtemplatebool = wordtemplatebool;
    }

    public Integer getWordtype() {
        return wordtype;
    }

    public void setWordtype(Integer wordtype) {
        this.wordtype = wordtype;
    }

    public Integer getDefaultbool() {
        return defaultbool;
    }

    public void setDefaultbool(Integer defaultbool) {
        this.defaultbool = defaultbool;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getWordtemplatename() {
        return wordtemplatename;
    }

    public void setWordtemplatename(String wordtemplatename) {
        this.wordtemplatename = wordtemplatename;
    }
    public String getWordtemplate_filesavessid() {
        return wordtemplate_filesavessid;
    }

    public void setWordtemplate_filesavessid(String wordtemplate_filesavessid) {
        this.wordtemplate_filesavessid = wordtemplate_filesavessid;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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


}
