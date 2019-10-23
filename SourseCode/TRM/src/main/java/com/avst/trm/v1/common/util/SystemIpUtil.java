package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemIpUtil {


    /**
     * 获取ip、子网掩码、网关
     * @return
     */
    public static GetNetworkConfigureVO getIpInfo(){

        GetNetworkConfigureVO configureVO = new GetNetworkConfigureVO();

        OutputStream os = null;
        InputStream in=null;
        InputStream in2=null;
        Process process=null;
        BufferedReader br = null;

        try {
            String myIP = NetTool.getMyIP();
            configureVO.setIp(myIP);//本机IP

            String osName = NetTool.getOsName();   //得到操作系统 xp 为"Windows XP"  其他的的楼主自己去试试

            long start = System.currentTimeMillis();
            process = Runtime.getRuntime().exec("ipconfig");
            os=process.getOutputStream();
            in=process.getInputStream();
            in2=process.getErrorStream();

            br = new BufferedReader(new InputStreamReader(in,"GBK"));
            List<String> rowList = new ArrayList();
            String temp;
            while((temp = br.readLine()) != null){
                rowList.add(temp );
            }
            for (String string : rowList) {
                String sm=  osName.equals("Windows XP") ? "Subnet Mask" : "子网掩码" ; //这里只判断了win7个xp
                if(string.indexOf(sm) != -1){
                    Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
                    if(mc.find()){
//                        System.out.println("子掩码：" + mc.group());
                        configureVO.setSubnetMask(mc.group());
                    }
                };
                String dg =  osName.equals("Windows XP") ? "Default Gateway" : "默认网关" ; //这里只判断了win7个xp
                if(string.indexOf(dg) != -1){
                    Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
                    if(mc.find()){
//                        System.out.println("默认网关：" + mc.group());
                        configureVO.setGateway(mc.group());
                    }
                    break;
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if(null!=br){
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if(null!=os){
                    os.flush();
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if(null!=process){
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if(null!=in){
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return configureVO;
    }


    /**
     *  修改ip、子网掩码、网关
     * @param newip  ip
     * @param zwm   子网掩码
     * @param wg  默认网关
     * @throws Exception
     */
    public static void setIP(String newip, String zwm,String wg){
        String cmd = "netsh    interface    ip    set    addr    \"本地连接\"    static    "
                + newip + "    " + zwm + "     " + wg + "     1";

        NetTool.executeCMD(cmd);
    }


    public static void main(String[] args) throws IOException{

        String osName = NetTool.getOsName();
        System.out.println(osName);

//        soutIp();
//
//        try {
//            setIP("192.168.17.173", "255.255.255.0", "192.168.17.254");
////            setIP("192.168.17.170", "255.0.0.0", "10.0.0.1");
//
//            Thread.currentThread().sleep(5 * 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //修改中请稍后，因修改IP、子掩码、网关系统可能会有3-5秒的延迟
//
//        soutIp();


//        Process pro = Runtime.getRuntime().exec("ipconfig");
//        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream(), "GBK"));
//        List<String> rowList = new ArrayList();
//        String temp;
//        while((temp = br.readLine()) != null){
//            rowList.add(temp );
//        }
//        for (String string : rowList) {
//            if(string.indexOf("Subnet Mask") != -1){
//                Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
//                if(mc.find()){
//                    System.out.println("子掩码：" + mc.group());
//                }else{
//                    System.out.println("子掩码为空");
//                }
//            };
//            if(string.indexOf("Default Gateway") != -1){
//                Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
//                if(mc.find()){
//                    System.out.println("默认网关：" + mc.group());
//                }else{
//                    System.out.println("默认网关为空");
//                }
//                return;
//            };
//        }



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

}
