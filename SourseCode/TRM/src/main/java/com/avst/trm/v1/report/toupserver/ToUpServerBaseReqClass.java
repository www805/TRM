package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.report.cache.ReportCahce;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataCache;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataThreadCache;
import com.avst.trm.v1.report.toupserver.common.conf.SynchronizedataThread;
import com.avst.trm.v1.report.toupserver.common.reqparam.GotoSynchronizedataParam;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizedataParam;
import com.avst.trm.v1.report.toupserver.police.v1.ToUpServerDeal.ToUpServerBaseDeal_police;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_1_Param;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizeDataTypeParam;
import org.apache.commons.lang.StringUtils;

/**
 * 请求上级服务器
 */
public class ToUpServerBaseReqClass {

    private static String baseurl= PropertiesListenerConfig.getProperty("re.basepath");

    /**
     * 请求上级服务器的验证钥匙
     */
    public static String token;

    private static ToUpServerBaseDealClass toUpServerBaseDealClass=getToUpServerBaseDealClass();

    private static ToUpServerBaseDealClass getToUpServerBaseDealClass(){
        if(null==toUpServerBaseDealClass){
            toUpServerBaseDealClass= SpringUtil.getBean(ToUpServerBaseDealClass.class);
        }
        return  toUpServerBaseDealClass;
    }

    public static boolean initsynchronizeddata(){


        String token=ReportCahce.getToupserverTBToken();
        if(StringUtils.isNotEmpty(token) ){//暂时同步在客户端只能存在一个
            StartSynchronizedata_1_Param startSynchronizedata_1_param=SynchronizedataCache.getSynchronizeData(token);
            if(null!=startSynchronizedata_1_param
                    ||null!=startSynchronizedata_1_param.getDatalist()){
                //查看同步的thread的状态，不对,重新启动
                if(null==SynchronizedataThreadCache.getSynchronizedataThread()){
                    String doing_toupurl= baseurl +PropertiesListenerConfig.getProperty("re.doing_toup");
                    String doing_toup_file= baseurl +PropertiesListenerConfig.getProperty("re.doing_toup_file");
                    SynchronizedataThread thread=new SynchronizedataThread(doing_toupurl,doing_toup_file) ;
                    thread.start();
                }
                return false;
            }else{//说明是有残留的token直接干掉
                ReportCahce.delToupserverTBToken();
            }
        }

        String url= baseurl +PropertiesListenerConfig.getProperty("re.init_toup");
        toUpServerBaseDealClass.initsynchronizeddata(url);
        return true;
    };

    public static void startSynchronizedata(SynchronizeDataTypeParam synchronizeDataTypeParam){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.start_toup");
        toUpServerBaseDealClass.startSynchronizedata(url,synchronizeDataTypeParam);
    };

    public static void synchronizedata(GotoSynchronizedataParam synchronizedataParam){
        String doing_toupurl= baseurl +PropertiesListenerConfig.getProperty("re.doing_toup");
        String doing_toup_file= baseurl +PropertiesListenerConfig.getProperty("re.doing_toup_file");
        toUpServerBaseDealClass.gotosynchronizedata(doing_toupurl,doing_toup_file,synchronizedataParam);
    };

    /**
     * 结束本次同步
     * 这个接口只会在结束同步overSynchronizedata之后出现未同步完成的时候调用，
     * 如果多次同步不成功，可以强制要求本次结束同步，也可以不强制，就是走一半的结束同步overSynchronizedata
     * @param mustOver 是否强制结束
     */
    public static void overSynchronizedata_must(boolean mustOver){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.mustover_toup");
        toUpServerBaseDealClass.overSynchronizedata_must(url,mustOver);
    };

    /**
     * 结束本次同步
     */
    public static void overSynchronizedata(){

        String url= baseurl +PropertiesListenerConfig.getProperty("re.over_toup");
        toUpServerBaseDealClass.overSynchronizedata(url);
    };


}
