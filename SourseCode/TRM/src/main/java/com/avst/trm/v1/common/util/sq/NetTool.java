package com.avst.trm.v1.common.util.sq;

import com.avst.trm.v1.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
public class NetTool{  
  
public static void main( String[] args){  
 
	try {

		LogUtil.intoLog(NetTool.class,getLocalMac());
	} catch (Exception e) {
		
		e.printStackTrace();
	}

  
}  
  
//取得LOCALHOST的IP地址  
public static InetAddress getMyIP() {  
	InetAddress myIPaddress=null;
try { 
	myIPaddress=InetAddress.getLocalHost();
	}  
catch (UnknownHostException e) {}  
return (myIPaddress);  
}  
//取得 www.baidu.com 的IP地址  
public static String getServerIP(String url){ 
	InetAddress myServer=null; 
	String hostname=null;
try {
	myServer=InetAddress.getByName(url);
	hostname=myServer.getHostName();
}  
catch (UnknownHostException e) {}  
return (hostname);  
}  




private static String getLocalMac_win() throws SocketException {


	StringBuffer sb=new StringBuffer("");
	try {
		//获取网卡，获取地址
		InetAddress ia = InetAddress.getLocalHost();

		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

//		LogUtil.intoLog(NetTool.class,"mac数组长度："+mac.length);


		for(int i=0; i<mac.length; i++) {

			if(i!=0) {

				sb.append("");

			}

			//字节转换为整数

			int temp = mac[i]&0xff;

			String str = Integer.toHexString(temp);

//			LogUtil.intoLog(NetTool.class,"每8位:"+str);

			if(str.length()==1) {

				sb.append("0"+str);

			}else {

				sb.append(str);

			}

		}
//		LogUtil.intoLog(NetTool.class,"本机MAC地址:"+sb.toString().toUpperCase());
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}

	return sb.toString();
}



/**
 * Return Opertaion System Name;
 * 
 * @return os name.
 */
public static String getOsName() {
    String os = "";
    os = System.getProperty("os.name");
    return os;
}

/**
 * Returns the MAC address of the computer.
 * 
 * @return the MAC address
 */
public static String getLocalMac() {
    String address = "";
    String os = getOsName();
    if (os.startsWith("Win")) {
        try {
			address=getLocalMac_win();
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
    } else if (os.startsWith("Linux")) {
        String command = "/bin/sh -c ifconfig -a";
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("HWaddr") > 0) {
                    int index = line.indexOf("HWaddr") + "HWaddr".length();
                    address = line.substring(index);
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
        }
    }
    address = address.trim();

	address=AnalysisSQ.encode_uid(address);

    return address;
}

}  