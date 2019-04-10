package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.Null;

/**
 * 一些常用的公共的缓存
 */
public class CommonCache {

    private static String clientkey=null;
    /**
     * 授权是否正常
     * 定时器里面要每天要进行检测
     */
    private static boolean clientSQbool=true;

     public static String getClientKey(){
         if(!clientSQbool){
             return null;
         }
         if(StringUtils.isEmpty(clientkey)){
             clientkey = AnalysisSQ.getClientKey();
         }
         return clientkey;
     }


    /**
     * 当前服务类型
     */
    private static String currentServerType= "court";

    public static String getCurrentServerType(){
        return currentServerType;
    }

    public static void setCurrentServerType(String serverType){
        if(StringUtils.isEmpty(serverType)){
            return ;
        }
        currentServerType=serverType;
    }

}
