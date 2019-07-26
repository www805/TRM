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
    private Integer recordCount;

    /**
     * 语音笔录总量
     */
    private Integer recordrealCount;

    /**
     * 笔录总时长
     */
    private Integer recordtimeCount;

    /**
     * 录音时长
     */
    private Integer timeCount;

    /**
     * 笔录字数
     */
    private Integer translatextCount;

    /**
     * 登录次数
     */
    private Integer loginCount;

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getRecordrealCount() {
        return recordrealCount;
    }

    public void setRecordrealCount(Integer recordrealCount) {
        this.recordrealCount = recordrealCount;
    }

    public Integer getRecordtimeCount() {
        return recordtimeCount;
    }

    public void setRecordtimeCount(Integer recordtimeCount) {
        this.recordtimeCount = recordtimeCount;
    }

    public Integer getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Integer timeCount) {
        this.timeCount = timeCount;
    }

    public Integer getTranslatextCount() {
        return translatextCount;
    }

    public void setTranslatextCount(Integer translatextCount) {
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
                ", translatextCount=" + translatextCount +
                ", loginCount=" + loginCount +
                '}';
    }
}
