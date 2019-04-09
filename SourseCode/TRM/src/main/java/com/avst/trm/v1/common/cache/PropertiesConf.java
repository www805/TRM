package com.avst.trm.v1.common.cache;

import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 对应application属性文件
 */
@ConfigurationProperties(prefix = "pro")
public class PropertiesConf {

    private String javakeyname;

    private String baseurl;


    public String getJavakeyname() {
        return javakeyname;
    }

    public void setJavakeyname(String javakeyname) {
        this.javakeyname = javakeyname;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
