package com.avst.trm.v1.common.cache;

import java.util.Properties;

public class PtdjiniCache {

    private static Properties propertie;

    public static Properties getPtdjiniCache() {
        return PtdjiniCache.propertie;
    }

    public static void setPtdjiniCache(Properties param) {
        propertie = param;
    }

    public static void delPtdjiniCache(){
        propertie = null;
    }
}
