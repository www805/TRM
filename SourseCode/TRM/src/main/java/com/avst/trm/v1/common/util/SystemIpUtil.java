package com.avst.trm.v1.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemIpUtil {


    public static void main(String[] args) throws IOException{

        Process pro = Runtime.getRuntime().exec("ipconfig");
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream(), "GBK"));
        List<String> rowList = new ArrayList();
        String temp;
        while((temp = br.readLine()) != null){
            rowList.add(temp );
        }
        for (String string : rowList) {
            if(string.indexOf("Subnet Mask") != -1){
                Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
                if(mc.find()){
                    System.out.println("子掩码：" + mc.group());
                }else{
                    System.out.println("子掩码为空");
                }
            };
            if(string.indexOf("Default Gateway") != -1){
                Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
                if(mc.find()){
                    System.out.println("默认网关：" + mc.group());
                }else{
                    System.out.println("默认网关为空");
                }
                return;
            };
        }



//        InetAddress ip = null;
//        NetworkInterface ni = null;
//        try {
//            ip = InetAddress.getLocalHost();
//            ni = NetworkInterface.getByInetAddress(ip);// 搜索绑定了指定IP地址的网络接口
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<InterfaceAddress> list = ni.getInterfaceAddresses();// 获取此网络接口的全部或部分
//        // InterfaceAddresses
//        // 所组成的列表
//        if (list.size() > 0) {
//            int mask = list.get(0).getNetworkPrefixLength(); // 子网掩码的二进制1的个数
//            StringBuilder maskStr = new StringBuilder();
//            int[] maskIp = new int[4];
//            for (int i = 0; i < maskIp.length; i++) {
//                maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (mask & 0xff) : 0);
//                mask -= 8;
//                maskStr.append(maskIp[i]);
//                if (i < maskIp.length - 1) {
//                    maskStr.append(".");
//                }
//            }
//            System.out.println("SubnetMask:" + maskStr);
//        }
//
//
//
//
//
//
//
//        try {
//            setIP("192.168.17.173", "255.255.255.0", "192.168.17.254");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        aa();

    }

    public static String execCommand(String command)
    {
        String line = "";
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Runtime.getRuntime().exec(command).getInputStream()));)
        {
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line + "\r\n");
        } catch (IOException e) { return "Invalid command."; }

        return sb.toString();
    }



    public static void aa(){
        InetAddress ip = null;
        try {
            //这里作为测试直接取IP地址，LZ可以根据需要自己调整调用相应的方法
            ip = InetAddress.getLocalHost();
//            ip = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
            NetworkInterface ni = NetworkInterface.getByInetAddress(ip);
            List<InterfaceAddress> list = ni.getInterfaceAddresses();
            if (list.size() > 0) {
                int mask = list.get(0).getNetworkPrefixLength(); //子网掩码的二进制1的个数
                StringBuilder maskStr = new StringBuilder();
                int[] maskIp = new int[4];
                for (int i=0; i<maskIp.length; i++) {
                    maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (mask & 0xff) : 0);
                    mask -= 8;
                    maskStr.append(maskIp[i]);
                    if (i < maskIp.length-1) {maskStr.append(".");}
                }
                System.out.println(maskStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setIP(String newip, String zwm,String wg) throws Exception {
        Runtime.getRuntime().exec("netsh    interface    ip    set    addr    \"本地连接\"    static    "
                + newip + "    " + zwm + "     " + wg + "     1");
    }



    public static String getIP(){
        String Ip = null;
        try {
            Ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return Ip;
    }


}
