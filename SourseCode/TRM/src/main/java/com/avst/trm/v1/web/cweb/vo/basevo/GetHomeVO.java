package com.avst.trm.v1.web.cweb.vo.basevo;

import java.util.List;

public class GetHomeVO {
    private Integer record_num;//笔录总数

    private Integer case_num;//案件总数

    private Integer template_num;//模板总数

    private Integer userinfo_num;//人员总数

    private Integer case_startnum_y;//案件开始提讯数量

    private Integer case_endnum_y;//案件未开始提讯数量

    private Integer record_finishnum_y;//已完成笔录数量

    private Integer record_unfinishnum_y;//进行中的笔录数量

    private Integer record_num_y;//笔录总数

    private Integer case_num_y;//案件总数

    private Integer template_num_y;//模板总数

    private Integer userinfo_num_y;//人员总数

    private List<Integer> case_monthnum_y;//12月案件

    private List<Integer> record_monthnum_y;//12月笔录

    public Integer getRecord_num() {
        return record_num;
    }

    public void setRecord_num(Integer record_num) {
        this.record_num = record_num;
    }

    public Integer getCase_num() {
        return case_num;
    }

    public void setCase_num(Integer case_num) {
        this.case_num = case_num;
    }

    public Integer getTemplate_num() {
        return template_num;
    }

    public void setTemplate_num(Integer template_num) {
        this.template_num = template_num;
    }

    public Integer getUserinfo_num() {
        return userinfo_num;
    }

    public void setUserinfo_num(Integer userinfo_num) {
        this.userinfo_num = userinfo_num;
    }

    public Integer getCase_startnum_y() {
        return case_startnum_y;
    }

    public void setCase_startnum_y(Integer case_startnum_y) {
        this.case_startnum_y = case_startnum_y;
    }

    public Integer getCase_endnum_y() {
        return case_endnum_y;
    }

    public void setCase_endnum_y(Integer case_endnum_y) {
        this.case_endnum_y = case_endnum_y;
    }

    public Integer getRecord_finishnum_y() {
        return record_finishnum_y;
    }

    public void setRecord_finishnum_y(Integer record_finishnum_y) {
        this.record_finishnum_y = record_finishnum_y;
    }

    public Integer getRecord_unfinishnum_y() {
        return record_unfinishnum_y;
    }

    public void setRecord_unfinishnum_y(Integer record_unfinishnum_y) {
        this.record_unfinishnum_y = record_unfinishnum_y;
    }

    public Integer getRecord_num_y() {
        return record_num_y;
    }

    public void setRecord_num_y(Integer record_num_y) {
        this.record_num_y = record_num_y;
    }

    public Integer getCase_num_y() {
        return case_num_y;
    }

    public void setCase_num_y(Integer case_num_y) {
        this.case_num_y = case_num_y;
    }

    public Integer getTemplate_num_y() {
        return template_num_y;
    }

    public void setTemplate_num_y(Integer template_num_y) {
        this.template_num_y = template_num_y;
    }

    public Integer getUserinfo_num_y() {
        return userinfo_num_y;
    }

    public void setUserinfo_num_y(Integer userinfo_num_y) {
        this.userinfo_num_y = userinfo_num_y;
    }

    public List<Integer> getCase_monthnum_y() {
        return case_monthnum_y;
    }

    public void setCase_monthnum_y(List<Integer> case_monthnum_y) {
        this.case_monthnum_y = case_monthnum_y;
    }

    public List<Integer> getRecord_monthnum_y() {
        return record_monthnum_y;
    }

    public void setRecord_monthnum_y(List<Integer> record_monthnum_y) {
        this.record_monthnum_y = record_monthnum_y;
    }
}
