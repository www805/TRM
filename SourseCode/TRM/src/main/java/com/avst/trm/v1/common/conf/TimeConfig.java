package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.outsideinterface.conf.ZkTimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//服务器启动的时候会执行一次
@Component
@Order(value = 1)
public class TimeConfig implements ApplicationRunner {

    @Autowired
    private ZkTimeConfig zkTimeConfig;

    @Override
    public void run(ApplicationArguments args){

        try {
            //获取总控时间，同步时间
            zkTimeConfig.compare();
        } catch (Exception e) {
            LogUtil.intoLog(4,this.getClass(),"TimeConfig.run is error, 获取获取总控时间，同步时间 失败","请求总连接池的定时器");
        }

    }
}
