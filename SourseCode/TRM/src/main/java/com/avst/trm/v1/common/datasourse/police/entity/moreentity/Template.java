package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Template {

    private Integer id;
    /**
     * 模板标题
     */
    private String title;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatetime;

    /**
     * ssid唯一值
     */
    private String ssid;

    private String templatetype;

    private String templatetypessid;

    private String typename;

    public Template() {
    }

    public Template(Police_template template) {
        this.id = template.getId();
        this.title = template.getTitle();
        this.ordernum = template.getOrdernum();
        this.createtime = template.getCreatetime();
        this.updatetime = template.getUpdatetime();
        this.ssid = template.getSsid();
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTemplatetypessid() {
        return templatetypessid;
    }

    public void setTemplatetypessid(String templatetypessid) {
        this.templatetypessid = templatetypessid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getTemplatetype() {
        return templatetype;
    }

    public void setTemplatetype(String templatetype) {
        this.templatetype = templatetype;
    }

    /**
     * 题目列表
     */
    private List<TemplateToProblem> templateToProblems=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public List<TemplateToProblem> getTemplateToProblems() {
        return templateToProblems;
    }

    public void setTemplateToProblems(List<TemplateToProblem> templateToProblems) {
        this.templateToProblems = templateToProblems;
    }
}
