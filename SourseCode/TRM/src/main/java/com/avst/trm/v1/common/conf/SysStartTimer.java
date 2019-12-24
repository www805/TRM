package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.socketio.SocketIOConfig;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 当springboot启动的时候，会自动执行该类
 */
@Component
@Order(value = 1)
public class SysStartTimer implements ApplicationRunner {



    @Autowired
    private Scheduler scheduler;

    //获取服务器时间进行比对
    @Override
    public void run(ApplicationArguments args) {

        //开机的时候检测一遍授权
        scheduler.checkSQTasks2();

        //赋值服务器的开始时间
        CommonCache.sysStartTime= DateUtil.getSeconds();

        //开启socketio
        SocketIOConfig.StartSocketio();

    }


}
