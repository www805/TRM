package com.avst.trm.v1.feignclient.req;

/**
 * 用户通道关联其他的参数的集合，用于开启会议时用的
 * 暂时只有asr和测谎仪，有其他的也写在这里，这里是对每一个会议人员的处理的参数集合
 */
public class TdAndUserAndOtherParam {

    private String tdssid;//设备通道ssid

    private String username;//会议用户名

    private String userssid;//会议用户的ssid

    private int grade;//1主麦，2副麦，有时需要一些特殊的处理(主麦只有一个)

    private int usepolygraph;//是否使用测谎仪

    private int useasr;//是否使用语言识别，1使用，-1 不使用

    private String polygraphssid;//测谎仪ssid

    private String asrssid;//语言识别ssid

    private String asrtype;//语音服务类型，AVST

    public String getAsrtype() {
        return asrtype;
    }

    public void setAsrtype(String asrtype) {
        this.asrtype = asrtype;
    }

    public String getTdssid() {
        return tdssid;
    }

    public void setTdssid(String tdssid) {
        this.tdssid = tdssid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getUsepolygraph() {
        return usepolygraph;
    }

    public void setUsepolygraph(int usepolygraph) {
        this.usepolygraph = usepolygraph;
    }

    public int getUseasr() {
        return useasr;
    }

    public void setUseasr(int useasr) {
        this.useasr = useasr;
    }

    public String getPolygraphssid() {
        return polygraphssid;
    }

    public void setPolygraphssid(String polygraphssid) {
        this.polygraphssid = polygraphssid;
    }

    public String getAsrssid() {
        return asrssid;
    }

    public void setAsrssid(String asrssid) {
        this.asrssid = asrssid;
    }
}
