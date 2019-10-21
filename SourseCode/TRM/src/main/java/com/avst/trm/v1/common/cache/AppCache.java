package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.cache.param.SysYmlParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppCache {

    private static AppCacheParam appCacheParam;

    private static Map<String,Object> ymlCache;

    public static synchronized AppCacheParam getAppCacheParam() {

        if(null == appCacheParam){
            initAppCache();//获取数据放进
        }
        return appCacheParam;
    }

    /**
     * 设置一个缓存
     * @param appCacheParam
     */
    public static synchronized void setAppCacheParam(AppCacheParam appCacheParam) {
        AppCache.appCacheParam = appCacheParam;
    }

    /**
     * 清除缓存
     */
    public static synchronized void delAppCacheParam(){
        appCacheParam = null;
        ymlCache = null;
    }

    /**
     * 获取外部yml文件中的数据
     * @return
     */
    private static synchronized Map<String,Object> getYmlMapCache() {

        if(null == ymlCache){

            String nav_file_name= PropertiesListenerConfig.getProperty("nav.file.name");
            String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";

            FileInputStream fis = null;
            Map<String,Object> map = null;

            try {
                fis = new FileInputStream(path);

                Yaml yaml = new Yaml();
                map = yaml.load(fis);
                ymlCache = map;
            } catch (IOException e) {
                LogUtil.intoLog(4, AppCache.class, "没找到外部配置文件：" + path);
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
        return ymlCache;
    }

    /**
     * 把yml数据解析放进缓存
     */
    private static synchronized void initAppCache(){
        MainService mainService= SpringUtil.getBean(MainService.class);

        Base_serverconfigMapper base_serverconfigMapper = SpringUtil.getBean(Base_serverconfigMapper.class);
        Base_filesaveMapper base_filesaveMapper = SpringUtil.getBean(Base_filesaveMapper.class);


        AppCacheParam cacheParam = new AppCacheParam();

        String myIP = NetTool.getMyIP();
        Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);

        if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
            Base_filesave filesaveSyslogo = new Base_filesave();
            filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
            Base_filesave syslogo = base_filesaveMapper.selectOne(filesaveSyslogo);
            if (null!=syslogo){
                cacheParam.setSyslogoimage("http://" + myIP + syslogo.getRecorddownurl());
            }
        }

        if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
            Base_filesave filesaveClientlogo = new Base_filesave();
            filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
            Base_filesave clientlogo = base_filesaveMapper.selectOne(filesaveClientlogo);
            if (null!=clientlogo){
                cacheParam.setClientimage("http://" + myIP + clientlogo.getRecorddownurl());
            }
        }

        //从缓存中获取
        Map<String,Object> map = getYmlMapCache();

        //获取授权信息
        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
        String gnlist = getSQEntity.getGnlist();

        String cwebFile=PropertiesListenerConfig.getProperty("nav.file.client");

        String application_name=PropertiesListenerConfig.getProperty("spring.application.name");

        Map<String, Object> avstYml = (Map<String, Object>) map.get(application_name);
        String oem = "common_o";
        //取出公司分类
        Map<String, Object> oemYml = (Map<String, Object>) avstYml.get("oem");//取出通用版
        Map<String, Object> commonYml = (Map<String, Object>) oemYml.get(oem);

        //判断如果是通用版，就获取通用版的菜单栏，如果不是通用版就从avst里面匹配出当前公司指定的菜单栏
        if(gnlist.indexOf(oem) == -1){
            String oemListStr = "";

            for(Map.Entry<String, Object> entry: oemYml.entrySet()){
//                    System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
                if("".equals(oemListStr)){
                    oemListStr += entry.getKey();
                }else {
                    oemListStr += "|" + entry.getKey();
                }
            }
            if (StringUtils.isNotBlank(oemListStr)) {
                String[] split = gnlist.split("\\|");
                String[] gnlit = oemListStr.split("\\|");
                oemListStr = "";
                for (int i = 0; i < split.length; i++) {
                    for (int j = 0; j < gnlit.length; j++) {
                        if (split[i].equals(gnlit[j])) {
                            oemListStr = gnlit[j];
                            break;
                        }
                    }
                    if(!"".equals(oemListStr)){
                        cwebFile = oemListStr;
                        break;
                    }
                }
            }
        }


        Map<String, Object> fileYml = null;
        if (gnlist.indexOf("common_o") == -1) {
            fileYml = (Map<String, Object>) oemYml.get(cwebFile);
        } else {
            fileYml = (Map<String, Object>) commonYml.get(cwebFile);
        }
//                Map<String,Object> zkYml = (Map<String, Object>) map.get("zk");
//                String guidepageUrl = (String) zkYml.get("home-url");

//                fileYml.put("bottom", commonYml.get("bottom"));
//                fileYml.put("login", commonYml.get("login"));
        fileYml.put("gnlist", gnlist);
//                String hostAddress = NetTool.getMyIP();


        Map<String, Object> branchYml = (Map<String, Object>) avstYml.get("branch");//分支
        Map<String, Object> logoYml = null;//分支特性
        //判断是公安、纪委、监察委那个版本，取出该版本的logo
        if(gnlist.indexOf(SQVersion.GA_T) != -1){
            logoYml = (Map<String, Object>) branchYml.get(SQVersion.GA_T);
        }else if(gnlist.indexOf(SQVersion.JW_T) != -1){
            logoYml = (Map<String, Object>) branchYml.get(SQVersion.JW_T);
        }else if(gnlist.indexOf(SQVersion.JCW_T) != -1){
            logoYml = (Map<String, Object>) branchYml.get(SQVersion.JCW_T);
        }else if(gnlist.indexOf(SQVersion.FY_T) != -1){
            logoYml = (Map<String, Object>) branchYml.get(SQVersion.FY_T);
        }

        fileYml.put("logotitle", logoYml.get("logo"));

        cacheParam.setGuidepageUrl("");//先给个空字符串

        //请求总控获取提供过来的地址
        RResult zkResult = new RResult();
        mainService.getServerStatus(zkResult, null);
        if(null != zkResult.getData()){
            List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) zkResult.getData();
            for (LinkedHashMap<String, Object> hashMap : data) {
                if("zk".equals(hashMap.get("servername"))){
                    cacheParam.setGuidepageUrl("http://" + (String) hashMap.get("url"));//进入总控
                }
            }
        }

        //海康的就进去
        if (gnlist.indexOf("hk_o") != -1) {
            //把获取到的总控地址，放到菜单栏里面
            List<Map<String, Object>> navList  = (List<Map<String, Object>>) fileYml.get("nav");

            for (Map<String, Object> stringObjectMap : navList) {
                String name = (String) stringObjectMap.get("name");
                if (name.indexOf("系统配置") != -1) {
                    List<Map<String, Object>> itemListMap = (List<Map<String, Object>>) stringObjectMap.get("list");
                    for (Map<String, Object> itemlist : itemListMap) {
                        String itemName = (String) itemlist.get("name");
                        if (itemName.indexOf("总控") != -1) {
                            itemlist.put("url", cacheParam.getGuidepageUrl());
                            break;
                        }
                    }
                }
            }
        }

        Map<String, Object> ptdj = (Map<String, Object>) avstYml.get("ptdj");
        fileYml.put("ptdj", ptdj);

        cacheParam.setData(fileYml);

//                cacheParam.setGuidepageUrl("http://" + hostAddress + guidepageUrl);
        LogUtil.intoLog(1, mainService.getClass(), "外部配置文件，获取菜单栏成功");

        //更新缓存
        SysYmlParam sysYmlParam=new SysYmlParam();
        sysYmlParam.setAvstYml(avstYml);
        sysYmlParam.setBranchYml(logoYml);
        sysYmlParam.setOemYml(fileYml);
        sysYmlParam.setGnlist(gnlist);
        SysYmlCache.setSysYmlParam(sysYmlParam);

        appCacheParam = cacheParam;
    }
}
