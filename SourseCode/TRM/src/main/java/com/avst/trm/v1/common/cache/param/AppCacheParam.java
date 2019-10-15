package com.avst.trm.v1.common.cache.param;

import java.util.Map;

/**
 * avst外部配置文件缓存
 */
public class AppCacheParam {

    private String title;/**客户端登录页左上角标题（授权公司的名字）**/
    private Map<String,Object> data;/**客户端授权其他数据**/
    private String syslogoimage;/**服务端logo图片**/
    private String clientimage;/**客户端logo图片**/
    private String guidepageUrl;/**引导页地址**/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getSyslogoimage() {
        return syslogoimage;
    }

    public void setSyslogoimage(String syslogoimage) {
        this.syslogoimage = syslogoimage;
    }

    public String getClientimage() {
        return clientimage;
    }

    public void setClientimage(String clientimage) {
        this.clientimage = clientimage;
    }

    public String getGuidepageUrl() {
        return guidepageUrl;
    }

    public void setGuidepageUrl(String guidepageUrl) {
        this.guidepageUrl = guidepageUrl;
    }

    @Override
    public String toString() {
        return "AppCacheParam{" +
                "title='" + title + '\'' +
                ", data=" + data +
                ", syslogoimage='" + syslogoimage + '\'' +
                ", clientimage='" + clientimage + '\'' +
                ", guidepageUrl='" + guidepageUrl + '\'' +
                '}';
    }
}
