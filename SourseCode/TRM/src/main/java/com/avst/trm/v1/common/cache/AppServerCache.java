package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.SQEntity;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class AppServerCache {

    private static AppCacheParam appCacheParam;

    public static synchronized AppCacheParam getAppServerCache() {

        if(null == appCacheParam || null == appCacheParam.getData()){
            init();
        }
        return appCacheParam;
    }

    public static synchronized void setAppServerCache(AppCacheParam appCacheParam) {
        AppServerCache.appCacheParam = appCacheParam;
    }

    public static synchronized void delAppServerCache(){
        appCacheParam = null;
    }



    private static synchronized void init(){

        if(null == appCacheParam){
            appCacheParam = new AppCacheParam();
        }

        Base_serverconfigMapper base_serverconfigMapper = SpringUtil.getBean(Base_serverconfigMapper.class);
        Base_filesaveMapper filesaveMapper = SpringUtil.getBean(Base_filesaveMapper.class);

        String nav_file_name= PropertiesListenerConfig.getProperty("nav.file.name");
        String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";
        FileInputStream fis = null;
        String myIP = ServerIpCache.getServerIp();
        try {

            Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);
            String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");//地址请勿使用获取ip拼接的方式！！！
            if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
                Base_filesave filesaveSyslogo = new Base_filesave();
                filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
                Base_filesave syslogo = filesaveMapper.selectOne(filesaveSyslogo);
                if (null!=syslogo){
                    appCacheParam.setSyslogoimage(uploadbasepath+ syslogo.getRecorddownurl());
                }
            }

            if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
                Base_filesave filesaveClientlogo = new Base_filesave();
                filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
                Base_filesave clientlogo = filesaveMapper.selectOne(filesaveClientlogo);
                if (null!=clientlogo){
                    appCacheParam.setClientimage(uploadbasepath+ clientlogo.getRecorddownurl());
                }
            }

            fis = new FileInputStream(path);

            Yaml yaml = new Yaml();
            Map<String,Object> map = yaml.load(fis);

            String application_name= PropertiesListenerConfig.getProperty("spring.application.name");
            String client_name= PropertiesListenerConfig.getProperty("nav.file.client");
            String swebFile=PropertiesListenerConfig.getProperty("nav.file.service");

            //获取授权信息
            CommonCache.gnlist();
            SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
            String gnlist = getSQEntity.getGnlist();

            Map<String,Object> avstYml = (Map<String, Object>) map.get(application_name);
            Map<String, Object> oemYml = (Map<String, Object>) avstYml.get("oem");//oem
            Map<String, Object> commonYml = (Map<String, Object>) oemYml.get("common_o");//取出通用版
            Map<String,Object> fileYml = (Map<String, Object>) commonYml.get(swebFile);

            Map<String,Object> clientYml = (Map<String, Object>) commonYml.get(client_name);//取出客户端
            String guidepageUrl = (String) clientYml.get("home-url");//获取客户端的首页
            fileYml.put("bottom", commonYml.get("bottom"));
            fileYml.put("gnlist", gnlist);
            String hostAddress = ServerIpCache.getServerIp();

            appCacheParam.setData(fileYml);

            appCacheParam.setGuidepageUrl("http://" + hostAddress + guidepageUrl);

        } catch (IOException e) {
            LogUtil.intoLog(4, AppServerCache.class, "没找到外部配置文件：" + path);
        }finally {
            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }





}
