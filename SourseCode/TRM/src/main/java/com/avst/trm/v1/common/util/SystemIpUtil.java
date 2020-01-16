package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.*;
import java.util.*;
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
            String myIP = ServerIpCache.getServerIp();
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

    /**
     *  修改ip、子网掩码、网关
     * @param name  网卡名
     * @param newip  ip
     * @param zwm  子网掩码
     * @param wg  默认网关
     */
    public static void setLocalIP(String name, String newip, String zwm, String wg) {
        String cmd = "netsh    interface    ip    set    addr    " + name + "    static    "
                + newip + "    " + zwm + "     " + wg + "     1";

//        System.out.println(cmd);
        LogUtil.intoLog(1, SystemIpUtil.class, "正在修改本机ip：" + cmd);
//        NetTool.executeCMD(cmd);

        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            printMessage(process.getInputStream(),process.getErrorStream(),process.getOutputStream());
            int value = process.waitFor();
            System.out.println(value);
            LogUtil.intoLog(1, SystemIpUtil.class, "执行cmd修改本机ip返回结果：" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取一个可用的IP
     * localhost he 127.0.0.1 不用
     * @return
     */
    public static String getOneUseableIp(){

        Map<String, List<GetNetworkConfigureVO>> map=getLocalMachineInfo();
        if(null!=map&&null!=map.keySet()&&map.keySet().size() > 0){
            Set<String> keyset=map.keySet();
            for(String key:keyset){
                List<GetNetworkConfigureVO> list=map.get(key);
                if(null!=list&&list.size() > 0){
                    for(GetNetworkConfigureVO vo:list){
                        String ip=vo.getIp();
                        try {
                            if(!ip.trim().equals("localhost")&&!ip.trim().equals("127.0.0.1")){
//                                LogUtil.intoLog(ip+"---ip获取正常 getOneUseableIp");
                                return ip.trim();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        LogUtil.intoLog(4,SystemIpUtil.class,"---ip获取异常，只能给一个localhost getOneUseableIp");
        return "localhost";
    }

    /**
     * 区分Linux和win
     * @return
     */
    public static Map<String, List<GetNetworkConfigureVO>> getLocalMachineInfo(){
        int osType = NetTool.osType();
        if(osType==1){
            return getLocalMachineInfo_win();
        }else{
            return getLocalMachineInfo_linux();
        }
    }


    public static Map<String, List<GetNetworkConfigureVO>> getLocalMachineInfo_linux(){

        try {
            Map<String, List<GetNetworkConfigureVO>> ipmap=new HashMap<String, List<GetNetworkConfigureVO>>();
            // 获得本机的所有网络接口
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                List<GetNetworkConfigureVO> iplist=new ArrayList<GetNetworkConfigureVO>();
                // 获得与该网络接口绑定的 IP 地址，一般只有一个
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
                        GetNetworkConfigureVO networkConfigureVO=new GetNetworkConfigureVO();
                        networkConfigureVO.setIp(addr.getHostAddress());
                        networkConfigureVO.setName(nif.getName());
                        iplist.add(networkConfigureVO);
                    }
                }
                if(null!=iplist&&iplist.size()>0){
                    ipmap.put(nif.getName(),iplist);
                }
            }
            LogUtil.intoLog(1,SystemIpUtil.class,"获取系统IP成功"+JacksonUtil.objebtToString(ipmap));
            return ipmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.intoLog(4,SystemIpUtil.class,"获取系统IP失败，严重错误");
        return null;
    }

    public static Map<String, List<GetNetworkConfigureVO>> getLocalMachineInfo_win(){
        String line ="";
        String lineAll ="";
        int n;
        Map<String, List<GetNetworkConfigureVO>> map = new HashMap<String, List<GetNetworkConfigureVO>>();
        String osName = NetTool.getOsName();

        String name = "以太网适配器";
        String ip = "IPv4 地址";
        String subnetMask = "子网掩码";
        String gateway = "默认网关. . . . . . . . . . . . . :";

        if(osName.equals("Windows XP")){
            name = "Host Name";
            ip = "IP Address";
            subnetMask = "Subnet Mask";
            gateway = "Default Gateway . . . . . . . . . :";
        }

        BufferedReader br = null;
        Process ps=null;
        try {
//            Process ps = Runtime.getRuntime().exec("cmd /c ipconfig /all");
            ps = Runtime.getRuntime().exec("ipconfig");
            br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "GB2312"));
            while(null != (line = br.readLine())) {
                lineAll += line + "\n";
            }
            ps.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != br){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(null!=ps){
                    ps.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] split = lineAll.split(name);



        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            List<GetNetworkConfigureVO> list = new ArrayList<>();
            GetNetworkConfigureVO vo = null;

            String[] strings = str.split("\n");
            for (int j = 0; j < strings.length; j++) {

                String string = strings[j];

                if(null == vo){
                    vo = new GetNetworkConfigureVO();
                }


                if(string.indexOf(ip) != -1){
                    Matcher mc = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)").matcher(string);
                    if(mc.find()){
                        vo.setName(strings[0].trim().replace(":", ""));
                        vo.setIp(mc.group());
                        map.put(vo.getName(), list);
                    }
                }else if(string.indexOf(subnetMask) != -1){
                    Matcher mc = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)").matcher(string);
                    if(mc.find()){
                        vo.setSubnetMask(mc.group());
                    }
                }
                if(StringUtils.isNotBlank(vo.getIp()) && StringUtils.isNotBlank(vo.getSubnetMask())){
                    list.add(vo);
                    vo = null;
                }

            }


            if (str.indexOf(gateway) != -1) {
                //加入默认网关
                String gatewayAll = str.substring(str.indexOf(gateway),str.length()).replace(gateway,"").trim();
                for (int u = 0; u < list.size(); u++) {
                    //遍历该网卡下的所有ip集合
                    GetNetworkConfigureVO vv = list.get(u);

                    if (StringUtils.isNotBlank(gatewayAll)) {
                        //默认网管换行
                        String[] gatewayAlls = gatewayAll.split("\n");
                        if (gatewayAlls.length > 0) {
                            for (int w = 0; w < gatewayAlls.length; w++) {
                                String s = gatewayAlls[w];
                                if (w == u) {
                                    //判断如果是ip才放进去
                                    if(OpenUtil.isIp(s.trim())){
                                        vv.setGateway(s.trim());
                                    }
                                }
                            }
                        }else{
                            //判断如果是ip才放进去
                            if(OpenUtil.isIp(gatewayAll)){
                                vv.setGateway(gatewayAll);
                            }
                        }

                    }
                }
            }


        }

        return map;
    }

    //清除防止阻塞Runtime.getRuntime().exec(cmd);
    private static void printMessage(final InputStream input,final InputStream errors, final OutputStream output) {

        try {
            input.close();
            errors.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        getLocalMachineInfo_linux();

    }


}
