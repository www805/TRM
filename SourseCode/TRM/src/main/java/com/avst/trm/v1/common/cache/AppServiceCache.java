package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.AppCacheParam;

public class AppServiceCache {

    private static AppCacheParam appCacheParam;

    public static synchronized AppCacheParam getAppServiceCache() {

        if(null == appCacheParam){
            appCacheParam = new AppCacheParam();
        }
        return appCacheParam;
    }

    public static synchronized void setAppServiceCache(AppCacheParam appCacheParam) {
        AppServiceCache.appCacheParam = appCacheParam;
    }

    public static synchronized void delAppServiceCache(){
        appCacheParam = null;
    }
}
