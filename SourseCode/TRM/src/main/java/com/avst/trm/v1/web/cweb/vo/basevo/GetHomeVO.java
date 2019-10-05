package com.avst.trm.v1.web.cweb.vo.basevo;

import com.avst.trm.v1.common.util.sq.SQEntity;

import java.util.List;

public class GetHomeVO {
    private Integer case_startnum;//案件开始提讯数量

    private Integer case_endnum;//案件未开始提讯数量

    private Integer record_finishnum;//已完成笔录数量

    private Integer record_unfinishnum;//进行中的笔录数量

    private Integer record_waitnum;//未开始笔录数量

    private Integer record_num;//笔录总数

    private Integer case_num;//案件总数


    private Integer case_startnum_y;//案件开始提讯数量

    private Integer case_endnum_y;//案件未开始提讯数量

    private Integer record_finishnum_y;//已完成笔录数量

    private Integer record_unfinishnum_y;//进行中的笔录数量

    private Integer record_waitnum_y;//未开始笔录数量

    private Integer record_num_y;//笔录总数

    private Integer case_num_y;//案件总数



    private List<Integer> case_monthnum_y;//12月案件

    private List<Integer> record_monthnum_y;//12月笔录

    private String dq_y;//当前年份

    private String clientname;//客户端名称


    private SQEntity sqEntity; //系统授权信息

    private List<String> sqgnList; //授权功能集合

    private Integer workdays; //授权信息，同步工作天数


    private Integer stateSQ;//授权状态 1正常 -1不正常

    private String liveurl;//首页直播地址

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public Integer getStateSQ() {
        return stateSQ;
    }

    public void setStateSQ(Integer stateSQ) {
        this.stateSQ = stateSQ;
    }

    public Integer getCase_startnum() {
        return case_startnum;
    }

    public void setCase_startnum(Integer case_startnum) {
        this.case_startnum = case_startnum;
    }

    public Integer getCase_endnum() {
        return case_endnum;
    }

    public void setCase_endnum(Integer case_endnum) {
        this.case_endnum = case_endnum;
    }

    public Integer getRecord_finishnum() {
        return record_finishnum;
    }

    public void setRecord_finishnum(Integer record_finishnum) {
        this.record_finishnum = record_finishnum;
    }

    public Integer getRecord_unfinishnum() {
        return record_unfinishnum;
    }

    public void setRecord_unfinishnum(Integer record_unfinishnum) {
        this.record_unfinishnum = record_unfinishnum;
    }

    public Integer getRecord_waitnum() {
        return record_waitnum;
    }

    public void setRecord_waitnum(Integer record_waitnum) {
        this.record_waitnum = record_waitnum;
    }

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

    public Integer getRecord_waitnum_y() {
        return record_waitnum_y;
    }

    public void setRecord_waitnum_y(Integer record_waitnum_y) {
        this.record_waitnum_y = record_waitnum_y;
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

    public String getDq_y() {
        return dq_y;
    }

    public void setDq_y(String dq_y) {
        this.dq_y = dq_y;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public SQEntity getSqEntity() {
        return sqEntity;
    }

    public void setSqEntity(SQEntity sqEntity) {
        this.sqEntity = sqEntity;
    }

    public List<String> getSqgnList() {
        return sqgnList;
    }

    public void setSqgnList(List<String> sqgnList) {
        this.sqgnList = sqgnList;
    }

    public Integer getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Integer workdays) {
        this.workdays = workdays;
    }
}
