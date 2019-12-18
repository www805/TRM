package com.avst.trm.v1.feignclient.ec.vo;

public class GetDefaultSaveInfoVO {

    private Integer id;

    /**
     * 设备ssid
     */
    private String mtssid;

    private String sstype;

    private Integer port;

    /**
     * 存储总容量,MB为单位
     */
    private Integer totalcapacity;

    /**
     * 存储已用容量,MB为单位
     */
    private Integer usedcapacity;

    /**
     * 存储本地文件夹base路径
     */
    private String datasavebasepath;

    /**
     * 存储服务的协议类型
     */
    private String xytype;
    /**
     * 默认用户密码
     */
    private String passwd;
    /**
     * 默认用户名
     */
    private String user;

    /**
     * 存储服务器的状态
     */
    private Integer ssstate;

    /**
     * 割切存储路径
     */
    private String ssstatic;


    /**
     * 是否是默认的存储设备
     */
    private Integer defaultsavebool;


    /**
     * 服务中文说明
     */
    private String explain;

    /**
     * http请求的开头
     */
    private String httpbasestaticpath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMtssid() {
        return mtssid;
    }

    public void setMtssid(String mtssid) {
        this.mtssid = mtssid;
    }

    public String getSstype() {
        return sstype;
    }

    public void setSstype(String sstype) {
        this.sstype = sstype;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTotalcapacity() {
        return totalcapacity;
    }

    public void setTotalcapacity(Integer totalcapacity) {
        this.totalcapacity = totalcapacity;
    }

    public Integer getUsedcapacity() {
        return usedcapacity;
    }

    public void setUsedcapacity(Integer usedcapacity) {
        this.usedcapacity = usedcapacity;
    }

    public String getDatasavebasepath() {
        return datasavebasepath;
    }

    public void setDatasavebasepath(String datasavebasepath) {
        this.datasavebasepath = datasavebasepath;
    }

    public String getXytype() {
        return xytype;
    }

    public void setXytype(String xytype) {
        this.xytype = xytype;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getSsstate() {
        return ssstate;
    }

    public void setSsstate(Integer ssstate) {
        this.ssstate = ssstate;
    }

    public String getSsstatic() {
        return ssstatic;
    }

    public void setSsstatic(String ssstatic) {
        this.ssstatic = ssstatic;
    }

    public Integer getDefaultsavebool() {
        return defaultsavebool;
    }

    public void setDefaultsavebool(Integer defaultsavebool) {
        this.defaultsavebool = defaultsavebool;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getHttpbasestaticpath() {
        return httpbasestaticpath;
    }

    public void setHttpbasestaticpath(String httpbasestaticpath) {
        this.httpbasestaticpath = httpbasestaticpath;
    }
}
