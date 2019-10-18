package com.avst.trm.v1.feignclient.ec.vo.fd;

/**
 * 默认本机审讯设备数据接收
 */
public class Flushbonadinginfo {
    /**
     * 设备编号
     */
    private String etnum;

    /**
     * 设备IP
     */
    private String etip;

    /**
     * 设备类型ssid
     */
    private String etypessid;

    /**
     * 设备类型
     */
    private String ettypenum;

    private Integer id;

    /**
     * 设备ssid
     */
    private String equipmentssid;

    /**
     * 直播地址
     */
    private String livingurl;

    /**
     * 直播预览地址（标清）
     */
    private String previewurl;


    /**
     * 默认直播地址
     */
    private Integer defaulturlbool;

    /**
     * 开放接口的端口
     */
    private Integer port;

    private String user;

    private String passwd;

    /**
     * ftp上传存储备设路径,只是一级路径，其实就是集中管理里面的本机设备ID，就是用来ftp上传时加以及路径，方便区分
     */
    private String uploadbasepath;

    /**
     * 中文解释
     */
    private String explain;

    /**
     * 刻录选时时长,小时为单位（1-24）
     */
    private Integer burntime;

    /**
     * 是否需要光盘同刻,1需要/0不需要
     */
    private Integer burnbool;

    /**
     * 片头列表,设备刻录的片头,片头的名称，用,分割
     */
    private String ptjson;

    /**
     * 片头显示时间,秒（5-60）
     */
    private Integer ptshowtime;

    /**
     * 是否需要硬盘录像,
     * 如果程序要求录，这里录像与否都会录像，当程序不要求录像，但是这里要录像也会录像1需要/0不需要
     */
    private Integer diskrecbool;

    /**
     * 分盘重复时间
     * 设备录像，视频文件超出录制时长，重新录制一个新文件，2个文件之间的重复的视频的时间s(秒)
     */
    private Integer repeattime;

    private String ssid;

    public String getEtnum() {
        return etnum;
    }

    public void setEtnum(String etnum) {
        this.etnum = etnum;
    }

    public String getEtip() {
        return etip;
    }

    public void setEtip(String etip) {
        this.etip = etip;
    }

    public String getEtypessid() {
        return etypessid;
    }

    public void setEtypessid(String etypessid) {
        this.etypessid = etypessid;
    }

    public String getEttypenum() {
        return ettypenum;
    }

    public void setEttypenum(String ettypenum) {
        this.ettypenum = ettypenum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEquipmentssid() {
        return equipmentssid;
    }

    public void setEquipmentssid(String equipmentssid) {
        this.equipmentssid = equipmentssid;
    }

    public String getLivingurl() {
        return livingurl;
    }

    public void setLivingurl(String livingurl) {
        this.livingurl = livingurl;
    }

    public String getPreviewurl() {
        return previewurl;
    }

    public void setPreviewurl(String previewurl) {
        this.previewurl = previewurl;
    }

    public Integer getDefaulturlbool() {
        return defaulturlbool;
    }

    public void setDefaulturlbool(Integer defaulturlbool) {
        this.defaulturlbool = defaulturlbool;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUploadbasepath() {
        return uploadbasepath;
    }

    public void setUploadbasepath(String uploadbasepath) {
        this.uploadbasepath = uploadbasepath;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Integer getBurntime() {
        return burntime;
    }

    public void setBurntime(Integer burntime) {
        this.burntime = burntime;
    }

    public Integer getBurnbool() {
        return burnbool;
    }

    public void setBurnbool(Integer burnbool) {
        this.burnbool = burnbool;
    }

    public String getPtjson() {
        return ptjson;
    }

    public void setPtjson(String ptjson) {
        this.ptjson = ptjson;
    }

    public Integer getPtshowtime() {
        return ptshowtime;
    }

    public void setPtshowtime(Integer ptshowtime) {
        this.ptshowtime = ptshowtime;
    }

    public Integer getDiskrecbool() {
        return diskrecbool;
    }

    public void setDiskrecbool(Integer diskrecbool) {
        this.diskrecbool = diskrecbool;
    }

    public Integer getRepeattime() {
        return repeattime;
    }

    public void setRepeattime(Integer repeattime) {
        this.repeattime = repeattime;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
