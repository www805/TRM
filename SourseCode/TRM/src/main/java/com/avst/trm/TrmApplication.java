package com.avst.trm;

import com.avst.trm.v1.common.util.properties.PropertiesListener;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@MapperScan({"com.avst.trm.v1.common.datasourse.base.mapper","com.avst.trm.v1.common.datasourse.police.mapper"})
@EnableScheduling
public class TrmApplication {

    /**
     *
     * 第四种方式：通过注册监听器(`Listeners`) + `PropertiesLoaderUtils`的方式
     *
     * 注册配置文件监听器
     */
    @RequestMapping("/listener")
    public Map<String, Object> listener() {
        Map<String, Object> map = new HashMap<>();
        map.putAll(PropertiesListenerConfig.getAllProperty());
        return map;
    }



    public static void main(String[] args) {

        //注册监听器
        SpringApplication application = new SpringApplication(TrmApplication.class);
        application.addListeners(new PropertiesListener("application.properties","trm.properties"));
        application.run( args);

    }

}
