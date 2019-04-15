package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import java.util.Date;

public class Problem {
    private Integer id;

    /**
     * 问题内容
     */
    private String problem;

    /**
     * 参考答案
     */
    private String referanswer;

    /**
     * 排序
     */
    private Integer ordernum;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getReferanswer() {
        return referanswer;
    }

    public void setReferanswer(String referanswer) {
        this.referanswer = referanswer;
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
}
