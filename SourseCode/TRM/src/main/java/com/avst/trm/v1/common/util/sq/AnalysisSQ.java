package com.avst.trm.v1.common.util.sq;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.wb.deencode.DeCodeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

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


//    private static String inifilename= PropertiesListenerConfig.getProperty("pro.javakeyname");//我们发出去的授权文件和运行的工程文件放在同一个目录下的
    private static String inifilename="javatrm.ini";//main测试使用
    /**
     *  初始化授权文件的路径
     */
    private static String keypath=OpenUtil.getXMSoursePath()+"\\"+inifilename;

    /**
     * 隐藏记录授权运行的文件
     */
    private static String inipath= OpenUtil.getJDKorJREPath()+ inifilename;

    /**
     * 生成客户端授权的隐秘文件
     * 修改数据库
     * @return
     */
    public static boolean createClientini(Base_serverconfigMapper base_serverconfigMapper, Base_serverconfig serverconfig){

        try {
            if(StringUtils.isEmpty(inifilename)){
                inifilename="javatrm.ini";
            }

            String rr=ReadWriteFile.readTxtFileToStr(keypath,"utf8");
            rr+=";0";//再加上一个服务器使用时间
            String encode=encode_uid(rr.trim());
            System.out.println("--encode:"+encode);


            ReadWriteFile.writeTxtFile(encode,inipath,"utf8");

//            OpenUtil.setFileHide(inipath);
            String code=rr.split(";")[0];
            String[] sqcodearr= DeCodeUtil.decoderByDES(code).split(";");
            String servertype=sqcodearr[0];
            String clientName=sqcodearr[5];
            String startTime=sqcodearr[2];//授权开始时间

            if(StringUtils.isEmpty(servertype)){
                servertype="0";
            }
            try {
                serverconfig.setType(servertype);//授权基本类型
                if(StringUtils.isNotEmpty(clientName)){
                    serverconfig.setClientname(clientName);
                }
                serverconfig.setAuthorizebool(1);
                serverconfig.setWorkstarttime(DateUtil.getNowTime());
                serverconfig.setWorkdays(1);
                int updatebool=base_serverconfigMapper.updateById(serverconfig);
                System.out.println(updatebool+":updatebool");
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
            System.out.println("--decode:"+decode);

            String[] arr=decode.split(";");
            int useday=Integer.parseInt(arr[1]);
            useday=day;
            String newcode=arr[0]+";"+useday;
            System.out.println(newcode+":newcode--");
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
            System.out.println("未找到使用的授权文件---");
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
            String localcpuCode=NetTool.getLocalMac();
            if(!localcpuCode.equals(cpuCode)){
                System.out.println(localcpuCode+":localcpuCode------cpuCode:"+cpuCode);
                System.out.println("授权机器码不一致");
                return -1;
            }

            if(foreverBool.equals("true")){
                return 1001;
            }else{
                int sqDay=Integer.parseInt(sqcodearr[4]);
//                System.out.println(sqDay+":sqDay usetime:"+usetime );
                return (sqDay-usetime);
            }

        }catch (Exception e){
            e.printStackTrace();
            return -100002;
        }

    }



    public static SQEntity getSQEntity(){

        File file=new File(inipath);
        if(!file.exists()){
            System.out.println("未找到使用的授权文件---");
            return null;
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
            SQEntity sqEntity=new SQEntity();
            sqEntity.setClientName(clientName);
            sqEntity.setCpuCode(cpuCode);
            sqEntity.setForeverBool(Boolean.valueOf(foreverBool));
            sqEntity.setServerType(serverType);
            sqEntity.setSortNum(Integer.parseInt(sortNum));
            sqEntity.setSqDay(Integer.parseInt(sqDay));
            sqEntity.setUnitCode(unitCode);
            return sqEntity;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据隐藏的ini授权记录文件获取
     * @return
     */
    public static String getClientKey(){

        try {
            File file=new File(inipath);
            if(!file.exists()){
                Base_serverconfigMapper base_serverconfigMapper= SpringUtil.getBean(Base_serverconfigMapper.class);
                boolean bool=createClientini(base_serverconfigMapper, new Base_serverconfig() );
                if(!bool){
                    System.out.println("createClientini 初始化失败--");
                    return null;
                }
            }
            String code=ReadWriteFile.readTxtFileToStr(inipath,"utf8");
            String key=code.substring(0,10)+ DateUtil.getSeconds();
            return encode_uid(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    public static void main(String[] args) {

//        System.out.println(CommonCache.getClientKey());

//        updateClientini();

        System.out.println(checkUseTime());

    }




}
