package com.avst.trm.v1.report.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.report.cache.ReportCahce;
import org.apache.commons.lang.StringUtils;

public class ReportConf {

    /**
     * 拼装get请求上级服务器的请求参数
     * @param data 实际参数的Jackson的对象转string
     * @return
     */
    public static String getParam(String data,boolean initbool){
        Integer shortnum= CommonCache.getServerconfig().getAuthorizesortnum();
        if(initbool){
            String sqCode=CommonCache.getServerSQCode();
            return "sqNum="+shortnum+"&sqCode="+sqCode ;
        }else{
            String token= ReportCahce.getToupserverTBToken();
            String rr="sqNum="+shortnum+"&token="+token;
            if(StringUtils.isEmpty(data)){
                rr="data="+data+"&"+rr;
            }
            return rr;
        }
    }


}
