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

        //清理以往的打包缓存
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    delGZIP();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //赋值服务器的开始时间
        CommonCache.sysStartTime= DateUtil.getSeconds();

        //开启socketio
        SocketIOConfig.StartSocketio();

    }

    /**
     * 开启的时候自动删除打包的文件
     */
    private void delGZIP(){

        LogUtil.intoLog(1,this.getClass(),"准备清理上一次开启产生的zip压缩包","监测自身的定时器");
        //先获取要删除文件的格式
        String gztype= PropertiesListenerConfig.getProperty("gztype");
        if(StringUtils.isEmpty(gztype)){
            gztype=".zip";
        }

        //获取所有等待检测的文件
        String ftpsavebasepath= PropertiesListenerConfig.getProperty("ftpsavebasepath");
        if(StringUtils.isEmpty(ftpsavebasepath)){
            ftpsavebasepath="d:/ftpdata/";
        }
        List<String> filelist=FileUtil.getAllFilePath(ftpsavebasepath,2);
        if(null!=filelist&&filelist.size() > 0){
            for(String path:filelist){
                //对比删除
                if(path.endsWith(gztype)){//删除GZIP的文件
                    File file=new File(path);
                    file.delete();
                    file=null;
                }
            }
        }

    }
}
