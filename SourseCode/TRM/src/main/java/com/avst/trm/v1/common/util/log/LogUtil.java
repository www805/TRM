package com.avst.trm.v1.common.util.log;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录
 */
public class LogUtil {

    private static Logger log= LoggerFactory.getLogger(LogUtil.class);

    public static String userSeparator= getUserSeparator();//操作人的名称分隔符

    private static String getUserSeparator(){
        if(StringUtils.isEmpty(userSeparator)){
            userSeparator= PropertiesListenerConfig.getProperty("userSeparator");
            if(StringUtils.isEmpty(userSeparator)) userSeparator="@user";
        }
        return userSeparator;
    }

    //跟logback。xml是对应的
    public static String logbasepath="D:\\log\\trm";//日志存在位置，这个也是写死的。不可以随便改，需要跟logback一致

    public static String logname_info="trm-info.log";//一般日志的名称（最新一天的）

    public static String logname_warn="trm-warn.log";//警告日志的名称（最新一天的）

    public static String logname_error="trm-error.log";//错误日志的名称（最新一天的）

    //历史日期的一般日志的名称的格式，@time的格式2019-09-06，@num一般情况下为0，当哪一天的日志超过了10M（设置），就会存成第二个文件1，一直往上加
    public static String logname_info_old="trm-info-@time.@num.log";
    public static String logname_warn_old="trm-warn-@time.@num.log";
    public static String logname_error_old="trm-error-@time.@num.log";//@time参数需要替换，按实际的时间替换，@num就要看它到底有几个地址文件了

    /**
     * 写入日志
     * loggrade 1 info,2 debug,3 warn,4 error
     */
    public static void intoLog(int loggrade, Class aClass,String logtxt ){

        AdminManage_session adminManage_session=CommonCache.getAdminManage_session();
        String username=null;
        if(null!=adminManage_session&&StringUtils.isNotEmpty(adminManage_session.getUsername())){
            username=adminManage_session.getUsername();
        }
        intoLog(loggrade, aClass,logtxt,username );
    }

    /**
     * 写入日志（全的，可以用于外部展示）
     * @param loggrade  1 info,2 debug,3 warn,4 error
     * @param aClass
     * @param logtxt
     * @param user
     */
    public static void intoLog(int loggrade, Class aClass,String logtxt,String user ){

        if(null!=aClass){
            log= LoggerFactory.getLogger(aClass);
        }
        if(StringUtils.isEmpty(user)){
            user="system";//系统自己执行的，比如定时器
        }
        logtxt+=userSeparator+user;
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
        intoLog(LogUtil.class,logtxt);
    }

    public static void intoLog( Object logtxt){
        try {
            intoLog(LogUtil.class,logtxt);
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
            intoLog(1,aClass,logtxt.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String currenttype;//当前缓存的日志的类型

    private static String currenttime;//2019-08-05 这种格式,当前缓存的日志的查询时间

    private static List<LogParam> loglist;//当前缓存的日志

    public static LogVO getlog(LogPageParam logPageParam){

        int Currentpag=logPageParam.getCurrentpage();
        int Pagesize=logPageParam.getPagesize();
        int Totalpage=logPageParam.getTotalpage();
        String time=logPageParam.getTime();
        String type=logPageParam.getType();

        if((StringUtils.isEmpty(currenttype)||StringUtils.isEmpty(currenttime)||null==loglist||loglist.size()==0)
            ||(!currenttype.equals(type)||!time.equals(time))){//需要重新查询日志

        }

        if(null==loglist||loglist.size()==0){
            AdminManage_session adminManage_session=CommonCache.getAdminManage_session();
            String username="未知";
            if(null!=adminManage_session&&StringUtils.isNotEmpty(adminManage_session.getUsername())){
                username=adminManage_session.getUsername();
            }
            intoLog(4,LogUtil.class,"没有日志可供查询,日志类型："+type+",日志查询的日期："+time,username );
            return null;
        }
        //开始计算返回的数量
        int logsize=loglist.size();
        int totallogpage=logsize/Pagesize+(logsize%Pagesize==0?0:1);//整除不了就要加1页
        if(Currentpag >totallogpage){
            AdminManage_session adminManage_session=CommonCache.getAdminManage_session();
            String username="未知";
            if(null!=adminManage_session&&StringUtils.isNotEmpty(adminManage_session.getUsername())){
                username=adminManage_session.getUsername();
            }
            intoLog(4,LogUtil.class,"日志查询已经超出了实际页数，要查询的页数："+Currentpag+",实际的总页数："+totallogpage,username );
            return null;
        }
        logPageParam.setTotalpage(totallogpage);
        logPageParam.setTime(currenttime);
        logPageParam.setType(currenttype);

        List<LogParam> logvolist=new ArrayList<LogParam>();
        for(int i=0;i<loglist.size();i++){
            if(i < Currentpag*Pagesize ) continue;
            if(i >= Currentpag*(Pagesize+1) ) break;
            logvolist.add(loglist.get(i));
        }
        if(null!=logvolist&&logvolist.size() > 0){
            LogVO logVO=new LogVO();
            logVO.setLogPageParam(logPageParam);
            logVO.setLogList(logvolist);
            return logVO;
        }

        return null;
    }
}
