package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.report.toupserver.police.v1.ToUpServerDeal.ToUpServerBaseDeal_police;

/**
 * 请求上级服务器
 */
public class ToUpServerBaseReqClass {

    private static String baseurl= PropertiesListenerConfig.getProperty("re.basepath");

    /**
     * 请求上级服务器的验证钥匙
     */
    public static String token;

    public static void initsynchronizeddata(){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.init_toup");

        getToUpServerBaseReqInterface().initsynchronizeddata(url);

    };

    public static void startSynchronizedata(){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.start_toup");
        getToUpServerBaseReqInterface().startSynchronizedata(url);
    };

    public static void synchronizedata(){
        String url= baseurl +PropertiesListenerConfig.getProperty("re.doing_toup");
        getToUpServerBaseReqInterface().synchronizedata(url);

    };

    public static void overSynchronizedata(){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.over_toup");
        getToUpServerBaseReqInterface().overSynchronizedata(url);
    };


    public static ToUpServerBaseReqInterface getToUpServerBaseReqInterface(){

        String serverType= CommonCache.getCurrentServerType();

        if(serverType.startsWith("court")){

        }else if(serverType.startsWith("police")){
           return new ToUpServerBaseDeal_police();
        }else if(serverType.startsWith("meeting")){

        }else if(serverType.startsWith("dis")){

        }

        return null;
    }

}
