package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 笔录统计
 */
public class Base_arraignmentCount extends Base_admininfo{

//    /**
//     * id
//     */
//    private Integer id;
//
//    /**
//     * 人员名称
//     */
//    private String username;

    /**
     * 笔录总量
     */
    private String recordCount;

    /**
     * 语音笔录总量
     */
    private String recordrealCount;

    /**
     * 笔录总时长
     */
    private Date recordtimeCount;

    /**
     * 录音时长
     */
    private Date timeCount;

    /**
     * 笔录字数
     */
    private String translatextCount;

    /**
     * 登录次数
     */
    private Integer loginCount;

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }

    public String getRecordrealCount() {
        return recordrealCount;
    }

    public void setRecordrealCount(String recordrealCount) {
        this.recordrealCount = recordrealCount;
    }

    public Date getRecordtimeCount() {
        return recordtimeCount;
    }

    public void setRecordtimeCount(Date recordtimeCount) {
        this.recordtimeCount = recordtimeCount;
    }

    public Date getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Date timeCount) {
        this.timeCount = timeCount;
    }

    public String getTranslatextCount() {
        return translatextCount;
    }

    public void setTranslatextCount(String translatextCount) {
        this.translatextCount = translatextCount;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Override
    public String toString() {
        return "Base_arraignmentCount{" +
                "recordCount='" + recordCount + '\'' +
                ", recordrealCount='" + recordrealCount + '\'' +
                ", recordtimeCount=" + recordtimeCount +
                ", timeCount=" + timeCount +
                ", translatextCount='" + translatextCount + '\'' +
                ", loginCount=" + loginCount +
                '}';
    }
}
