package com.avst.trm.v1.common.util.sq;

import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${Javakeyname}")
    private static String inifilename;

    /**
     * 生成客户端授权的隐秘文件
     * @return
     */
    public static boolean createClientini(String keypath){

        try {
            if(StringUtils.isEmpty(inifilename)){
                inifilename="javatrm.ini";
            }

            String rr=ReadWriteFile.readTxtFileToStr(keypath,"utf8");
            rr+=";0";//再加上一个服务器使用时间
            String encode=encode_uid(rr);
            System.out.println("--encode:"+encode);

            String inipath= OpenUtil.getJDKorJREPath()+ inifilename;
            ReadWriteFile.writeTxtFile(inipath,rr);

//            OpenUtil.setFileHide(inipath);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false ;
    }


    /**
     * 更新客户端授权的使用时间
     * @return
     */
    public static boolean updateClientini(){

        try {
            String inipath= OpenUtil.getJDKorJREPath()+ inifilename;

            String rr=ReadWriteFile.readTxtFileToStr(inipath,"utf8");

            String encode=encode_uid(rr);
            System.out.println("--encode:"+encode);

            ReadWriteFile.writeTxtFile(inipath,rr);

//            OpenUtil.setFileHide(inipath);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false ;
    }


    /**
     * 检测使用时间是否已经超时，超时就会是负数
     * @return
     */
    public static int checkUseTime(){

        String inipath= OpenUtil.getJDKorJREPath()+ inifilename;
        File file=new File(inipath);
        if(!file.exists()){
            System.out.println("未找到使用的授权文件---");
            return -1;
        }
        try {

            String rr=ReadWriteFile.readTxtFileToStr(inipath,"utf8");
            String sqcode=rr.split(";")[0];
            int usetime=Integer.parseInt(rr.split(";")[1]);
            String[] sqcodearr=decode_uid(sqcode).split(";");
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
                return (usetime-sqDay);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }










    public static void main(String[] args) {

        String uuid="123evghtbsr53fty";
        String en=encode_uid(uuid);
        System.out.println(en);
        String de=decode_uid(en);
        System.out.println(de);

    }




}
