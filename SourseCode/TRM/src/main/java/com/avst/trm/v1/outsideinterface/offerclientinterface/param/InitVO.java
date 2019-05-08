package com.avst.trm.v1.outsideinterface.offerclientinterface.param;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
/**
 * 初始化客户端
 */
public class InitVO {

    private String code;//授权说明，0：代表正常 其他数值代表错误

    private String msg;//返回的文件说明

    private String serviceType;//服务器是那种类型（公安、纪委、检察院、法院、会议）

    private String baseUrl;//客户端请求服务器的前缀地址

    private String key;//客户端以后每次请求服务器的秘钥

    private String firstpageid;//客户端显示的第一个页面

    private List<PageVO> pageList;//客户端需要显示的页面以及对应请求的处理

    //还有一个类用于显示客户端系统配置，客户端名称、logo地址、配色方案等


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<PageVO> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageVO> pageList) {
        this.pageList = pageList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFirstpageid() {
        return firstpageid;
    }

    public void setFirstpageid(String firstpageid) {
        this.firstpageid = firstpageid;
    }
}
