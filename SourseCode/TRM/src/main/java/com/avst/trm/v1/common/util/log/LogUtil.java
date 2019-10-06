package com.avst.trm.v1.common.util.log;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

    private static String logFileFormat=".log";//日志文件的后缀名
    public static String logname_info="trm-info";//一般日志的名称（最新一天的）

    public static String logname_warn="trm-warn";//警告日志的名称（最新一天的）

    public static String logname_error="trm-error";//错误日志的名称（最新一天的）

    //历史日期的一般日志的名称的格式，@time的格式2019-09-06，当哪一天的日志超过了10M（设置），就会存成第二个文件1，一直往上加
    private static String sptime="@time";//@time参数需要替换，按实际的时间替换
    public static String logname_info_old=logname_info+"-"+sptime;
    public static String logname_warn_old=logname_warn+"-"+sptime;
    public static String logname_error_old=logname_error+"-"+sptime;

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

        //先判断日志文件夹下面有没有日志文件
        List<String> filelist=FileUtil.getAllFilePath(logbasepath,1);
        if(null==filelist||filelist.size()==0){
            intoLog(4,LogUtil.class,"服务器日志所在位置没有找到日志文件，logbasepath："+logbasepath );
            return null;
        }

        int Currentpag=logPageParam.getCurrentpage();
        int Pagesize=logPageParam.getPagesize();
        int Totalpage=logPageParam.getTotalpage();
        String time=logPageParam.getTime();
        String type=logPageParam.getType();

        if((StringUtils.isEmpty(currenttype)||StringUtils.isEmpty(currenttime)||null==loglist||loglist.size()==0)
            ||(!currenttype.equals(type)||!currenttime.equals(time))){//需要重新查询日志

            //判断是不是今天的日志
            String newdate=DateUtil.getDate();//当天的日期
            if(StringUtils.isEmpty(time)){//没有时间就是用今天的时间
                time=newdate;
            }else{
                try {
                    time=DateUtil.strToDateToFormatYMD(time);
                } catch (Exception e) {
                    e.printStackTrace();
                    intoLog(4,LogUtil.class,"查询的时间参数异常，time="+time );
                    return null;
                }
            }
            if(StringUtils.isEmpty(type)){//没有类型没有就用一般日志类型
                type="info";
            }else{
                if(!type.equals("info")&&!type.equals("error")&&!type.equals("warn")){
                    intoLog(4,LogUtil.class,"查询的日志类型参数异常，type="+type );
                    return null;
                }
            }

            //组合日志的名字
            String logname="";
            if(type.equals("info")){
                if(time.equals(newdate)){//当天日志
                    logname=logname_info+logFileFormat;
                }else{
                    logname=logname_info_old;
                    logname=logname.replace(sptime,time);
                }
            }else if(type.equals("error")){
                if(time.equals(newdate)){//当天日志
                    logname=logname_error+logFileFormat;
                }else{
                    logname=logname_error_old;
                    logname=logname.replace(sptime,time);
                }
            }else{
                if(time.equals(newdate)){//当天日志
                    logname=logname_warn+logFileFormat;
                }else{
                    logname=logname_warn_old;
                    logname=logname.replace(sptime,time);
                }
            }

            //获取日志文件的内容
            List<LogParam> needLogList=new ArrayList<LogParam>();
            for(String filename:filelist){
                if(filename.indexOf(logname) > -1){//这里用包含去查找是因为日志文件可能会有多个
                    needLogList.addAll(jxLog(filename,type));
                }
            }

            //筛除logutil输出的日志，并缓存
            if(null!=needLogList&&needLogList.size() > 0){
                loglist=needLogList;
                currenttype=type;
                currenttime=time;
            }else{
                intoLog(3,LogUtil.class,"没有找到任何一条查询条件下的日志，type："+type +",time:"+time);
                return new LogVO();
            }
        }

        if(null==loglist||loglist.size()==0){

            intoLog(4,LogUtil.class,"没有日志可供查询,日志类型："+type+",日志查询的日期："+time );
            return null;
        }
        //开始计算返回的数量
        int logsize=loglist.size();
        int totallogpage=logsize/Pagesize+(logsize%Pagesize==0?0:1);//整除不了就要加1页
        if(Currentpag >totallogpage){

            intoLog(4,LogUtil.class,"日志查询已经超出了实际页数，要查询的页数："+Currentpag+",实际的总页数："+totallogpage );
            return null;
        }
        logPageParam.setTotalpage(totallogpage);
        logPageParam.setTime(currenttime);
        logPageParam.setType(currenttype);

        List<LogParam> logvolist=new ArrayList<LogParam>();
        for(int i=0;i<loglist.size();i++){
            if(i < (Currentpag-1)*Pagesize ) continue;
            if(i >= (Currentpag*Pagesize) ) break;
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

    private static List<LogParam> jxLog(String path,String type){

        InputStream input = null;
        InputStreamReader inp=null;
        BufferedReader bufread=null;
        try {
            List<LogParam> readList=new ArrayList<LogParam>();
            LogUtil.intoLog(ReadWriteFile.class,"读取文件文本,文件路径为："+path);


            input = new FileInputStream(path);
            inp=new InputStreamReader(input,"utf8");//字节流字符流转化的桥梁

            bufread = new BufferedReader(inp);//以字符流方式读入

            try {
                String read="";
                while ((read = bufread.readLine()) != null) {
                    read=read.trim();

                    //判断是否合符规格@time@user
                    if(read.indexOf("@time")>-1&&read.indexOf("@user")>-1){
                        LogParam logPageParam=new LogParam();
                        String[] arr=read.split("@user");
                        read=arr[0];
                        logPageParam.setUser(arr[1].trim());
                        arr=read.split("@msg");
                        read=arr[0];
                        logPageParam.setMsg(arr[1].trim());
                        arr=read.split("@line");
                        read=arr[0];
                        arr=read.split("@class");
                        read=arr[0];
                        arr=read.split("@time");
                        logPageParam.setTime(arr[1].trim());

                        logPageParam.setType(type);
                        readList.add(logPageParam);
                    }
                }

                return readList;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                bufread.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                inp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    public static void main(String[] args) {
        LogPageParam logPageParam=new LogPageParam();
        logPageParam.setType("info");
        logPageParam.setTime("2019-10-05");
        logPageParam.setPagesize(10);
        getlog(logPageParam);
    }

}
