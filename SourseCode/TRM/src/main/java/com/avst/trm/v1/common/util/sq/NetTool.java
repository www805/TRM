package com.avst.trm.v1.common.util.sq;
import java.io.*;

import com.avst.trm.v1.common.util.LogUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetTool{
  
public static void main( String[] args){  
 
	try {

//		LogUtil.intoLog(NetTool.class,getLocalMac());
		LogUtil.intoLog(NetTool.class,getCPUCode());


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


	/**
	 * 获取CPU序列号
	 * @return
	 */
	public static String getCPUCode(){

		String cpuCode="";
		String os = getOsName();
		if (os.startsWith("Win")) {
			try {
				cpuCode= getCPUCode_win();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (os.startsWith("Linux")) {
			cpuCode= getCPUID_linux();
		}else{
			LogUtil.intoLog(4,NetTool.class,"getCPUID is error ，os："+os);
		}
		if(null!=cpuCode&&!cpuCode.trim().equals("")){
			cpuCode=AnalysisSQ.encode_uid(cpuCode);
		}
		return cpuCode;
	}


	/**
	 * Linux系统获取CPU序列号，没有验证过
	 * @return
	 * @throws InterruptedException
	 */
	private static String getCPUID_linux()  {
		String result = "";
		String CPU_ID_CMD = "dmidecode";
		BufferedReader bufferedReader = null;
		Process p = null;
		InputStream in2=null;
		InputStream in=null;
		try {
			p = Runtime.getRuntime().exec(new String[]{ "sh", "-c", CPU_ID_CMD });// 管道
			in=p.getInputStream();
			in2=p.getErrorStream();
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				// 寻找标示字符串[hwaddr]
				index = line.toLowerCase().indexOf("uuid");
				if (index >= 0) {// 找到了
					// 取出mac地址并去除2边空格
					result = line.substring(index + "uuid".length() + 1).trim();
					break;
				}
			}

			int exitvalue=p.waitFor();
			if(exitvalue!=0){
				throw new Exception("exitvalue is not 0, 说明代码有错");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {

			printMessage(in2);

			try {
				if(null!=bufferedReader){
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if(null!=in){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if(null!=p){
					p.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result.trim();
	}

	/**
	 * 获取win CPU序列号
	 * @return
	 */
	private static String getCPUCode_win(){

		OutputStream os = null;
		InputStream in=null;
		InputStream in2=null;
		Process process=null;
		Scanner sc=null;
		try {
			long start = System.currentTimeMillis();
			process = Runtime.getRuntime().exec(
					new String[] { "wmic", "cpu", "get", "ProcessorId" });
			os=process.getOutputStream();
			in=process.getInputStream();
			in2=process.getErrorStream();
			sc = new Scanner(in);
			String property = sc.next();
			String serial = sc.next();

			int exitvalue=process.waitFor();
			if(exitvalue!=0){
				throw new Exception("exitvalue is not 0, 说明代码有错");
			}

			System.out.println(property + "::::::: " + serial);
			return serial;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

			printMessage(in2);

			try {
				if(null!=os){
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if(null!=sc){
					sc.close();
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

		return null;

	}


	private static void printMessage(final InputStream input) {
		new Thread(new Runnable() {
       	public void run() {
        	Reader reader = new InputStreamReader(input);
			BufferedReader bf = new BufferedReader(reader);
        	String line = null;
        	  try {
					while((line=bf.readLine())!=null) {
						System.out.println(line);
					}
         		} catch (IOException e) {
            		e.printStackTrace();
         	 	}finally {
				  try {
					  if(null!=bf){
						bf.close();
					}
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				  try {
					  if(null!=reader){
						  reader.close();
					  }
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
			  }
        	}
    	}).start();
	}

}  