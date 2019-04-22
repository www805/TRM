package com.avst.trm.v1.outsideinterface.conf;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部请求保存的缓存
 */
public class OutSideIntegerfaceCache {

    private static Map<String , String>  toupserverTBTokenMap;//一个下级服务器只能有一个同步任务（下级服务器的序号为key键）

    public static String getToupserverTBToken(String key){
        if(null!=toupserverTBTokenMap&&toupserverTBTokenMap.containsKey(key)){
            return toupserverTBTokenMap.get(key);
        }
        return null;
    }

    /**
     * 要检测返回的boolean值，true才是可以下一个同步任务
     * @param token
     * @return
     */
    public static boolean setToupserverTBToken(String key,String token){

        String toupserverTBToken=null;
        if(null==toupserverTBTokenMap){
            toupserverTBTokenMap=new HashMap<String , String>();
        }
        if(toupserverTBTokenMap.containsKey(key)){
            toupserverTBToken=toupserverTBTokenMap.get(key);
        }
        if(null!=toupserverTBToken){
            return false;
        }
        toupserverTBTokenMap.put(key,token);

        return true;
    }

    /**
     * 关闭本次同步token认证
     * @return
     */
    public static boolean delToupserverTBToken(String key){

        if(null!=toupserverTBTokenMap&&toupserverTBTokenMap.containsKey(key)){
            toupserverTBTokenMap.remove(key);
        }
        return true;
    }


}
