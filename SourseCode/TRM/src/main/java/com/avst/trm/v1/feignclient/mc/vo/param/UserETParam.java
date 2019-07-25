package com.avst.trm.v1.feignclient.mc.vo.param;

public class UserETParam {

    private String userssid;//会议用户ssid

    private String fdssid;//嵌入式设备ssid

    private String polygraphssid;//测谎仪ssid，不一定有值，一般只有副麦才会有

    private String livingurl;//设备直播地址

    private String previewurl;//设备直播预览地址（标清）

    private String iid;//设备录音/像的唯一标识，用于保存录音/像

    public String getPreviewurl() {
        return previewurl;
    }

    public void setPreviewurl(String previewurl) {
        this.previewurl = previewurl;
    }

    public String getPolygraphssid() {
        return polygraphssid;
    }

    public void setPolygraphssid(String polygraphssid) {
        this.polygraphssid = polygraphssid;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getUserssid() {
        return userssid;
    }

    public void setUserssid(String userssid) {
        this.userssid = userssid;
    }

    public String getFdssid() {
        return fdssid;
    }

    public void setFdssid(String fdssid) {
        this.fdssid = fdssid;
    }

    public String getLivingurl() {
        return livingurl;
    }

    public void setLivingurl(String livingurl) {
        this.livingurl = livingurl;
    }
}
