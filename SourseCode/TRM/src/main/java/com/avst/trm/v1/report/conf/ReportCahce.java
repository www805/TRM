package com.avst.trm.v1.report.conf;

/**
 * 请求服务器过程中的缓存
 */
public class ReportCahce {

    private static String  toupserverTBToken;//一次只能有一个同步任务

    public static String getToupserverTBToken(){
        return toupserverTBToken;
    }

    /**
     * 要检测返回的boolean值，true才是可以下一个同步任务
     * @param token
     * @return
     */
    public static boolean setToupserverTBToken(String token){

        if(null!=toupserverTBToken){
            return false;
        }
        toupserverTBToken=token;
        return true;
    }

    /**
     * 关闭本次同步token认证
     * @return
     */
    public static boolean delToupserverTBToken(){

        toupserverTBToken=null;
        return true;
    }


}
