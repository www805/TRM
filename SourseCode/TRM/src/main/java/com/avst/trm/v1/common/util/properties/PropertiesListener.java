package com.avst.trm.v1.common.util.properties;


import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.io.File;


/**
 * 配置文件监听器，用来加载自定义配置文件
 *
 * */
public class PropertiesListener implements ApplicationListener<ApplicationStartedEvent> {

    private String propertyFileName;

    private String outProName;

    public PropertiesListener(String propertyFileName,String outProName) {
        this.propertyFileName = propertyFileName;
        this.outProName = outProName;
    }

    public PropertiesListener(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        String propath= OpenUtil.getXMSoursePath();
        if(propath.indexOf("/") > -1){//路径获取可能有2种
            propath=propath.endsWith("/") ? propath : (propath+"/")+outProName;
        }else{
            propath=propath.endsWith("\\") ? propath : (propath+"\\")+outProName;
        }
        File file=new File(propath);
        if (file.exists()){
            LogUtil.intoLog(this.getClass(),"调用外部的部分参数配置，propath："+propath);
            PropertiesListenerConfig.loadAllPropertiesWithOutSide(propath,propertyFileName);
        }else{
            LogUtil.intoLog(this.getClass(),"调用内部的参数配置，propath："+propath);
            PropertiesListenerConfig.loadAllProperties(propertyFileName);
        }
    }
}
