package com.avst.trm.v1.common.util.sq;

import com.avst.trm.v1.common.util.DateUtil;

public class SQEntity {
    /**
     *  授权服务类型
     */
    private String serverType="court";
    /**
     * 是否是永久授权
     */
    private boolean foreverBool=false;

    /**
     * 授权开始时间
     */
    private String startTime= DateUtil.getDateAndMinute();

    /**
     * 绑定CPU编码
     * 不使用网卡，使用CPU序列号
     */
//    private String cpuCode=NetTool.getLocalMac();
    private String cpuCode=NetTool.getSQCode();

    /**
     * 授权总天数
     */
    private int sqDay=1;

    /**
     * 单位名称
     */
    private String clientName="测试单位";
    /**
     * 单位代码
     */
    private String unitCode="0000";
    /**
     * 排序，多台客户端的时候
     */
    private int sortNum=1;

    /**
     * 客户端的功能列表，暂时只有：record、asr、tts、fd、ph(笔录管理、语音识别、语音播报、设备控制、测谎仪)
     * 用 | 隔开
     */
    private String gnlist;//功能列表

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public boolean isForeverBool() {
        return foreverBool;
    }

    public void setForeverBool(boolean foreverBool) {
        this.foreverBool = foreverBool;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCpuCode() {
        return cpuCode;
    }

    public void setCpuCode(String cpuCode) {
        this.cpuCode = cpuCode;
    }

    public int getSqDay() {
        return sqDay;
    }

    public void setSqDay(int sqDay) {
        this.sqDay = sqDay;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getGnlist() {
        return gnlist;
    }

    public void setGnlist(String gnlist) {
        this.gnlist = gnlist;
    }

    @Override
    public String toString() {
        return  serverType + ";" +
                foreverBool +";"+
                startTime + ";" +
                cpuCode + ";" +
                sqDay +";"+
                clientName + ";" +
                unitCode + ";" +
                sortNum  + ";" +
                gnlist ;
    }



}
