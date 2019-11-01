package com.avst.trm.v1.common.util.sq;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.*;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.wb.deencode.DeCodeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 客户端服务器有的类
 * 解密数据
 * 加密使用时间
 */
public class AnalysisSQ {



    /*简单加密*/
    /*
     * 16进制数字字符集
     * webmac
     */
    private static String hexString="123456789ABCDEF0";
    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode_uid(String str)
    {
        //根据默认编码获取字节数组
        byte[] bytes=str.getBytes();
        StringBuilder sb=new StringBuilder(bytes.length*2);
        //将字节数组中每个字节拆解成2位16进制整数
        for(int i=0;i<bytes.length;i++)
        {
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }

    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode_uid(String bytes)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
        //将每2位16进制整数组装成一个字节
        for(int i=0;i<bytes.length();i+=2)
            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
        return new String(baos.toByteArray());
    }


        private static String inifilename= PropertiesListenerConfig.getProperty("pro.javakeyname");//我们发出去的授权文件和运行的工程文件放在同一个目录下的
    private static String inifilename_yingcang="java.ini";//隐藏的授权文件名
    /**
     *  初始化授权文件的路径
     */
    private static String keypath=OpenUtil.getXMSoursePath()+"\\"+inifilename;

    /**
     * 隐藏记录授权运行的文件
     */
    private static String inipath= OpenUtil.getXMSoursePath()+"\\"+ inifilename_yingcang;

    /**
     * 生成客户端授权的隐秘文件
     * 修改数据库
     * @return
     */
    public static boolean createClientini( Base_serverconfig serverconfig){

        try {
            if(StringUtils.isEmpty(inifilename)){
                inifilename="javatrm.ini";
            }

            String rr=ReadWriteFile.readTxtFileToStr(keypath,"utf8");
            rr+=";0";//再加上一个服务器使用时间
            String encode=encode_uid(rr.trim());
            LogUtil.intoLog(AnalysisSQ.class,"--encode:"+encode);

            ReadWriteFile.writeTxtFile(encode,inipath,"utf8");

//            OpenUtil.setFileHide(inipath);//暂时不用隐藏，写文件的时候有问题

            String code=rr.split(";")[0];
            String[] sqcodearr= DeCodeUtil.decoderByDES(code).split(";");
            String servertype=sqcodearr[0];
            String clientName=sqcodearr[5];
            String startTime=sqcodearr[2];//授权开始时间
            String sortnum=sqcodearr[7];
            String gnlist=sqcodearr[8];

            if(StringUtils.isEmpty(servertype)){
                servertype="0";
            }
            try {
                serverconfig.setType(servertype);//授权基本类型
                if(StringUtils.isNotEmpty(clientName)){
                    serverconfig.setClientname(clientName);
                }
                serverconfig.setAuthorizebool(1);
                if(StringUtils.isNotEmpty(sortnum)){
                    serverconfig.setAuthorizesortnum(Integer.parseInt(sortnum));
                }
                serverconfig.setWorkstarttime(DateUtil.strToDate(startTime));//授权开始的时间
                serverconfig.setWorkdays(1);
                if(StringUtils.isEmpty(serverconfig.getSsid())){
                    serverconfig.setSsid(OpenUtil.getUUID_32());
                }
                boolean bool=updateServiceConfig(serverconfig,1);
                if(bool){//成功的话刷新一遍客户端页面
                    CommonCache.getinit_CLIENT(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false ;
    }


    /**
     * 更新客户端授权的使用时间
     * 更新天数
     * @return
     */
    public static boolean updateClientini(int day){

        try {

            String rr=ReadWriteFile.readTxtFileToStr(inipath,"utf8");

            String decode=decode_uid(rr);
            LogUtil.intoLog(AnalysisSQ.class,"--decode:"+decode);

            String[] arr=decode.split(";");
            int useday=Integer.parseInt(arr[1]);
            if(useday==day){//时间一样的话就不需要写入授权文件
                return true;
            }
            useday=day;
            String newcode=arr[0]+";"+useday;
            LogUtil.intoLog(AnalysisSQ.class,newcode+":newcode--");
            ReadWriteFile.writeTxtFile(encode_uid(newcode),inipath);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false ;
    }


    /**
     * 检测使用时间是否已经超时，超时就会是负数
     * inipath 文件授权加了一次密，在客户端存成隐藏的文件又简单加密了一次
     * @return
     */
    public static int checkUseTime(){



        File file=new File(inipath);
        if(!file.exists()){
            LogUtil.intoLog(4,AnalysisSQ.class,"未找到使用的授权文件---");
            updateServiceConfig(null,-1);
            return -100001;
        }
        try {

            String rr=ReadWriteFile.readTxtFileToStr(inipath,"utf8");
            rr=decode_uid(rr);
            String sqcode=rr.split(";")[0];
            int usetime=Integer.parseInt(rr.split(";")[1]);
            String[] sqcodearr= DeCodeUtil.decoderByDES(sqcode).split(";");
            String foreverBool=sqcodearr[1];
            String cpuCode=sqcodearr[3];
//            String localcpuCode=NetTool.getLocalMac();
            String localcpuCode=NetTool.getSQCode_win();//使用CPU序列号
            if(!localcpuCode.equals(cpuCode)){
//                LogUtil.intoLog(AnalysisSQ.class,localcpuCode+":localcpuCode------cpuCode:"+cpuCode);
                LogUtil.intoLog(4,AnalysisSQ.class,"授权机器码不一致");

                updateServiceConfig(null,-1);
                return -1;
            }

            if(foreverBool.equals("true")){
                return 1001;
            }else{
                int sqDay=Integer.parseInt(sqcodearr[4]);
//                LogUtil.intoLog(AnalysisSQ.class,sqDay+":sqDay usetime:"+usetime );
                return (sqDay-usetime);
            }

        }catch (Exception e){
            e.printStackTrace();
            return -100002;
        }

    }

    /**
     * 修改系统配置表
     * @return
     */
    public static boolean  updateServiceConfig(Base_serverconfig serverconfig,int authorizebool){

        try {
            Base_serverconfigMapper base_serverconfigMapper=SpringUtil.getBean(Base_serverconfigMapper.class);
            if(null==serverconfig){
                serverconfig=new Base_serverconfig();
                serverconfig.setId(1);
                serverconfig=base_serverconfigMapper.selectById(1);
            }
            int updatebool=-1;
            if(null!=serverconfig&&serverconfig.getId() >0){
                serverconfig.setAuthorizebool(authorizebool);
                updatebool=base_serverconfigMapper.updateById(serverconfig);
            }else{
                updatebool=base_serverconfigMapper.insert(serverconfig);
            }
            if(updatebool >= 0){
                LogUtil.intoLog(1,AnalysisSQ.class,"修改系统配置成功，authorizebool："+authorizebool);
                return true;
            }else{
                LogUtil.intoLog(1,AnalysisSQ.class,"修改系统配置失败，authorizebool："+authorizebool);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.intoLog(4,AnalysisSQ.class,"修改系统配置数据库抛错，大错------");
        }
        return false;
    }

    /**
     * 本地授权信息获取
     * @return
     */
    public static SQEntity getSQEntity(){

        File file=new File(inipath);
        if(!file.exists()){
            LogUtil.intoLog(AnalysisSQ.class,"未找到使用的授权文件---重新授权");
            Base_serverconfigMapper base_serverconfigMapper=SpringUtil.getBean(Base_serverconfigMapper.class);
            Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
            boolean bool=createClientini(serverconfig);
            if(bool){
                file=new File(inipath);
                if(!file.exists()){
                    return null;
                }
            }else{
                return null;
            }
        }
        try {

            String rr=ReadWriteFile.readTxtFileToStr(inipath,"utf8");
            rr=decode_uid(rr);
            String sqcode=rr.split(";")[0];
            int usetime=Integer.parseInt(rr.split(";")[1]);
            String[] sqcodearr= DeCodeUtil.decoderByDES(sqcode).split(";");
            String serverType=sqcodearr[0];
            String foreverBool=sqcodearr[1];
            String startTime=sqcodearr[2];
            String cpuCode=sqcodearr[3];
            String sqDay=sqcodearr[4];
            String clientName=sqcodearr[5];
            String unitCode=sqcodearr[6];
            String sortNum=sqcodearr[7];
            String gnlist=sqcodearr[8];

            SQEntity sqEntity=new SQEntity();
            sqEntity.setClientName(clientName);
            sqEntity.setCpuCode(cpuCode);
            sqEntity.setForeverBool(Boolean.valueOf(foreverBool));
            sqEntity.setServerType(serverType);
            sqEntity.setSortNum(Integer.parseInt(sortNum));
            sqEntity.setSqDay(Integer.parseInt(sqDay));
            sqEntity.setUnitCode(unitCode);
            sqEntity.setStartTime(startTime);
            sqEntity.setGnlist(gnlist);
            return sqEntity;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析其他人的授权信息
     * @param sq
     * @return
     */
    public static SQEntity getSQEntity(String sq){

        try {

            sq=decode_uid(sq);
            String sqcode=sq.split(";")[0];
            int usetime=Integer.parseInt(sq.split(";")[1]);
            String[] sqcodearr= DeCodeUtil.decoderByDES(sqcode).split(";");
            String serverType=sqcodearr[0];
            String foreverBool=sqcodearr[1];
            String startTime=sqcodearr[2];
            String cpuCode=sqcodearr[3];
            String sqDay=sqcodearr[4];
            String clientName=sqcodearr[5];
            String unitCode=sqcodearr[6];
            String sortNum=sqcodearr[7];
            String gnlist=sqcodearr[8];

            if(foreverBool.equals("true")){//永久授权不用检查使用剩余时间

            }else{
                int sqDay_int=Integer.parseInt(sqDay);
                if((sqDay_int-usetime) < 0){ //还剩多少使用时间
                    return null;
                }
            }

            SQEntity sqEntity=new SQEntity();
            sqEntity.setClientName(clientName);
            sqEntity.setCpuCode(cpuCode);
            sqEntity.setForeverBool(Boolean.valueOf(foreverBool));
            sqEntity.setServerType(serverType);
            sqEntity.setSortNum(Integer.parseInt(sortNum));
            sqEntity.setSqDay(Integer.parseInt(sqDay));
            sqEntity.setUnitCode(unitCode);
            sqEntity.setGnlist(gnlist);
            sqEntity.setStartTime(startTime);
            return sqEntity;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据隐藏的ini授权记录文件获取
     * 也可以生产唯一的token
     * @return
     */
    public static String getClientKey(){

        try {
            String code=getServerSQCode();
            String key=code.substring(0,10)+ DateUtil.getSeconds();
            return encode_uid(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据隐藏的ini授权记录获取授权的code
     * @return
     */
    public static String getServerSQCode(){

        try {
            File file=new File(inipath);
            if(!file.exists()){
                Base_serverconfigMapper base_serverconfigMapper= SpringUtil.getBean(Base_serverconfigMapper.class);
                boolean bool=createClientini(new Base_serverconfig() );
                if(!bool){
                    LogUtil.intoLog(AnalysisSQ.class,"createClientini 初始化失败--");
                    return null;
                }
            }
            String code=ReadWriteFile.readTxtFileToStr(inipath,"utf8");
            return code;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {

        SQEntity sqEntity=AnalysisSQ.getSQEntity();
        LogUtil.intoLog(AnalysisSQ.class,sqEntity.getStartTime());

    }




}
