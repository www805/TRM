package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import org.apache.commons.lang.StringUtils;

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


}
