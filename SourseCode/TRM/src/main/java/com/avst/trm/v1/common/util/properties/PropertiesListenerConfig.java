package com.avst.trm.v1.common.util.properties;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import org.springframework.beans.BeansException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesListenerConfig {
    public static Map<String, String> propertiesMap = new HashMap<>();

    private static void processProperties(Properties props) throws BeansException {
        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            try {
                // PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
                propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载jar内部资源文件
     * @param propertyFileName
     */
    public static void loadAllProperties(String propertyFileName) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
            processProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载外部资源文件
     * @param propath 外部加载的属性文件
     */
    public static void loadAllPropertiesWithOutSide(String propath,String propertyFileName) {
        try {
            propertiesMap = new HashMap<String, String>();
           List<String> prolist=ReadWriteFile.readTxtFileToList(propath,"utf8");
           if(null!=prolist&&prolist.size() > 0){
                for(String pro:prolist){
                    try {
                        if(pro.trim().equals("")||pro.indexOf("#") > -1){
                            continue;
                        }
                        String[] arr=pro.split("=");
                        if(null!=arr&&arr.length==2){
//                            LogUtil.intoLog(PropertiesListenerConfig.class,arr[0]+":key----val:"+arr[1]);
                            propertiesMap.put(arr[0].trim(),arr[1].trim());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
           }else{
               LogUtil.intoLog(PropertiesListenerConfig.class,"还是要用内部的属性文件，propath："+propath);
               loadAllProperties(propertyFileName);
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {

//        if(null==propertiesMap||null==propertiesMap.entrySet()||propertiesMap.entrySet().size()==0){
//            loadAllProperties("application.properties");
//        }

        if(null==propertiesMap||null==propertiesMap.entrySet()||propertiesMap.entrySet().size()==0){
            new PropertiesListener("application.properties","trm.properties");
        }

        return propertiesMap.get(name);
    }

    public static void setProperty(String name,String val) {

        propertiesMap.put(name, val);

    }

    public static Map<String, String> getAllProperty() {
        return propertiesMap;
    }
}
