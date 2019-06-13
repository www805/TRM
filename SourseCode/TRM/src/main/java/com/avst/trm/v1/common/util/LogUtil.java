package com.avst.trm.v1.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录
 */
public class LogUtil {

    private static Logger log= LoggerFactory.getLogger(LogUtil.class);

    /**
     * 写入日志
     * 1 info,2 debug,3 warn,4 error
     */
    public static void intoLog(int loggrade, Class aClass,String logtxt ){

        if(null!=aClass){
            log= LoggerFactory.getLogger(aClass);
        }
        if(loggrade==1){
            log.info(logtxt);
        }else if(loggrade==2){
            log.debug(logtxt);
        }else if(loggrade==3){
            log.warn(logtxt);
        }else if(loggrade==4){
            log.error(logtxt);
        }
    }


    /**
     * 写入日志
     * 没有等级的通用日志
     */
    public static void intoLog( String logtxt){
        log.info(logtxt);
    }

    public static void intoLog( Object logtxt){
        try {
            log.info(logtxt.toString()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入某一个类的日志
     * @param aClass
     * @param logtxt
     */
    public static void intoLog(Class aClass, Object logtxt){
        try {
            if(null!=aClass){
                log= LoggerFactory.getLogger(aClass);
            }
            log.info(logtxt.toString()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
