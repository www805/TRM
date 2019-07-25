package com.avst.trm.v1.common.cache;

import java.util.Map;

public class PtdjmapCache {

    private static Map<String, Object> propertie;

    public static Map<String, Object> getPtdjmapCache() {
        return PtdjmapCache.propertie;
    }

    public static void setPtdjmapCache(Map<String, Object> param) {
        propertie = param;
    }

    public static void delPtdjmapCache(){
        propertie = null;
    }
}
