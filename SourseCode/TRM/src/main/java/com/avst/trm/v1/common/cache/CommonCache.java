package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.datasourse.base.entity.Base_action;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndType;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.Null;

/**
 * 一些常用的公共的缓存
 */
public class CommonCache {

    /**
     * 客户端访问时使用的key
     */
    private static String clientkey=null;
    /**
     * 授权是否正常
     * 定时器里面要每天要进行检测
     */
    public static boolean clientSQbool=true;

     public static String getClientKey(){
         if(!clientSQbool){
             return null;
         }
         if(StringUtils.isEmpty(clientkey)){
             clientkey = AnalysisSQ.getClientKey();
         }
         return clientkey;
     }

     private static String clientbaseurl;

    public static String getClientBaseurl(){
        if(StringUtils.isEmpty(clientbaseurl)){

            initServerConfigAndType();
        }
        return clientbaseurl;
    }


    /**
     * 当前服务类型
     */
    private static String currentServerType;

    public static String getCurrentServerType(){


        if(StringUtils.isEmpty(currentServerType)){
            initServerConfigAndType();
        }

        return currentServerType;
    }

    public static int getCurrentServerTypeid(){

        if(null==serverconfig){
            ServerconfigAndType serverconfigAndType=getServerconfig();
            if(null!=serverconfigAndType){
                return serverconfigAndType.getTypeid();
            }
        }else{
            return serverconfig.getTypeid();
        }

        return 0;
    }

    /**
     * 服务器系统配置缓存
     */
    private static ServerconfigAndType serverconfig=null;

    public static ServerconfigAndType getServerconfig(){

        if(null==serverconfig){
            initServerConfigAndType();
        }
        return serverconfig;
    }

    private static void initServerConfigAndType(){
        Base_serverconfigMapper base_serverconfigMapper= SpringUtil.getBean(Base_serverconfigMapper.class);

        EntityWrapper ew=new EntityWrapper();
        ew.eq("id",1);
        ServerconfigAndType serverconfigAndType= base_serverconfigMapper.getServerconfigAndType(ew);
        serverconfig=serverconfigAndType;
        String serverip=serverconfigAndType.getServerip();
        String serverport=serverconfigAndType.getServerport();
        if(StringUtils.isNotEmpty(serverip)&&StringUtils.isNotEmpty(serverport)){
            clientbaseurl = "http://"+serverip+":"+serverport+ PropertiesListenerConfig.getProperty("pro.baseurl");
        }
        String type=serverconfigAndType.getType();
        if(StringUtils.isNotEmpty(type)){
            currentServerType=type;
        }
    }




    public static void setCurrentServerType(String serverType){
        if(StringUtils.isEmpty(serverType)){
            return ;
        }
        currentServerType=serverType;
    }


    /**
     * 获取缓存中对应的动作对象
     * @param url
     * @return
     */
    public static Base_action getBase_action(String url,String type){


        return null;
    }

}
