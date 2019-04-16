package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

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
