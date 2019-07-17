package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.AppCacheParam;

public class AppCache {

    private static AppCacheParam appCacheParam;

    public static AppCacheParam getAppCacheParam() {

        if(null == appCacheParam){
            appCacheParam = new AppCacheParam();
        }
        return appCacheParam;
    }

    public static void setAppCacheParam(AppCacheParam appCacheParam) {
        AppCache.appCacheParam = appCacheParam;
    }

    public static void delAppCacheParam(){
        appCacheParam = null;
    }
}
