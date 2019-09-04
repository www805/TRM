package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        scheduler.testTasks2();

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
    }

    /**
     * 开启的时候自动删除打包的文件
     */
    private void delGZIP(){

        LogUtil.intoLog(1,this.getClass(),"准备清理上一次开启产生的zip压缩包");
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
                if(path.indexOf("a08a1f4d944b489fa10dfc3eb5212b48_sxsba2") > 0){

                    if(path.endsWith(gztype)){//删除GZIP的文件
                        File file=new File(path);
                        file.delete();
                        file=null;
                    }
                }
            }
        }

    }
}
