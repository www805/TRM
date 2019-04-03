package com.avst.trm.v1.common.util;

import java.io.BufferedReader; 
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.io.OutputStream;
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder; 
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;




public class HttpRequest {
	
       
       public static String readContentFromGet(String url,String param) throws IOException { 

           
    	   
    	    HttpURLConnection connection=null;
    	    HttpsURLConnection connections=null;
    		BufferedReader reader=null;
    		String lines=null;
    		DataOutputStream out = null;
    		try {
    			   // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码 

				String getURL = url;
    			 
    			System.out.println(DateUtil.getDateAndMinute()+" getURL:"+getURL);
    			if(getURL.startsWith("https://")){
    				URL getUrl = new URL(getURL); 
    				connections =    (HttpsURLConnection) getUrl.openConnection();
                    TrustManager[] trustAllCerts = new TrustManager[]{
                            new X509TrustManager() {
								
								@Override
								public X509Certificate[] getAcceptedIssuers() {
									return null;
								}
								
								@Override
								public void checkServerTrusted(X509Certificate[] arg0, String arg1)
										throws CertificateException {
									
								}
								
								@Override
								public void checkClientTrusted(X509Certificate[] arg0, String arg1)
										throws CertificateException {
									
								}

							}
                        };
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    connections.setSSLSocketFactory(sc.getSocketFactory());
                    HostnameVerifier hv = new HostnameVerifier() {
						
						@Override
						public boolean verify(String arg0, SSLSession arg1) {
							return true;
						}
					}; 
                    connections.setHostnameVerifier(hv);

                    connections.setRequestProperty("Accept-Charset", "utf-8");
                    connections.setRequestProperty("User-Agent","java HttpsURLConnection");
                   
                    connections.setRequestMethod("POST");
                    connections.setUseCaches(false);
                    connections.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8"); 
                    connections.setConnectTimeout(8000);
                    connections.setReadTimeout(8000);
                    connections.setDoInput(true);
                    connections.setInstanceFollowRedirects(true); 
                    if(param !=null){
                        connections.setDoOutput(true);
                        // 获取URLConnection对象对应的输出流
                        out = new DataOutputStream(connections.getOutputStream());
                        // 发送请求参数
                        out.write(param.getBytes("utf-8"));
                        // flush输出流的缓冲
                        out.flush();
                        out.close();
                    }else{
                    	
                    }
                    // 取得该连接的输入流，以读取响应内容
                    reader = new BufferedReader(new InputStreamReader(connections.getInputStream(),"utf-8"));
                    int code = connections.getResponseCode();
                    lines = reader.readLine();
                    System.out.println("----https协议请求的返回code："+code);
    			}else{
    				getURL=getURL+"?"+ URLEncoder.encode(param, "utf-8");
    				URL getUrl = new URL(getURL);
     			   // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection 
     			
     			   connection = (HttpURLConnection) getUrl.openConnection(); 
     			
     			   // 建立与服务器的连接，并未发送数据 
     			   connection.setReadTimeout(4000);
     			   connection.setConnectTimeout(4000);
     			   connection.connect(); 
     			   
     			   // 发送数据到服务器并使用Reader读取返回的数据 
     			
     			   reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")); 
     			
     			   lines = reader.readLine();
     			  int code = connection.getResponseCode();
     			  System.out.println("----https协议请求的返回code："+code);
    			}
    			
    			   
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
				closecon(connection,connections, reader,out,null,null);
			}
    		   return lines;
    		
    		} 
       
       public static String readContentFrompost_notencode(String url,String param,String requestMethod) throws IOException { 

    	   HttpURLConnection connection=null;
   	    HttpsURLConnection connections=null;
   		BufferedReader reader=null;
   		String lines=null;
   		DataOutputStream out = null;
   		InputStream inputStream=null;
   		InputStreamReader inputStreamReader=null;
   		try {
   			   // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码 
   			   
   			String getURL="";
   			getURL = url ; 
   			 
   			System.out.println(DateUtil.getDateAndMinute()+"--------------- getURL:"+getURL);
   			if(getURL.startsWith("https://")){
   				URL getUrl = new URL(getURL); 
   				connections =    (HttpsURLConnection) getUrl.openConnection();
                   TrustManager[] trustAllCerts = new TrustManager[]{
                           new X509TrustManager() {
								
								@Override
								public X509Certificate[] getAcceptedIssuers() {
									return null;
								}
								
								@Override
								public void checkServerTrusted(X509Certificate[] arg0, String arg1)
										throws CertificateException {
									
								}
								
								@Override
								public void checkClientTrusted(X509Certificate[] arg0, String arg1)
										throws CertificateException {
									
								}
								
							}
                       };
                   SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");  
                   sc.init(null, trustAllCerts, new java.security.SecureRandom());
                   connections.setSSLSocketFactory(sc.getSocketFactory());
                   HostnameVerifier hv = new HostnameVerifier() {
						
						@Override
						public boolean verify(String arg0, SSLSession arg1) {
							return true;
						}
					}; 
                   connections.setHostnameVerifier(hv);
                   
//                   connections.setHostnameVerifier(new HttpRequest().new TrustAnyHostnameVerifier());
                       
                   connections.setRequestProperty("Accept-Charset", "utf-8");
                   connections.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
                   connections.setRequestProperty("Connection","keep-alive");
                   
                   connections.setDoInput(true);
                   connections.setDoOutput(true);
                  
                   connections.setRequestMethod(requestMethod);
                   connections.setUseCaches(false);
                   connections.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                   
                   
                   connections.setConnectTimeout(8000);
                   connections.setReadTimeout(8000);
                   connections.setInstanceFollowRedirects(true); 
                   
                   
                   if ("GET".equalsIgnoreCase(requestMethod)){  
                	   connections.connect();  
                   }  
                   
                   
                  if(param !=null){
                	   byte[] b=param.getBytes("utf-8");
                	   
                       // 获取URLConnection对象对应的输出流
                       out = new DataOutputStream(connections.getOutputStream());
                       // 发送请求参数
                       out.write(b);
                       // flush输出流的缓冲
                       out.flush();
                       out.close();
                   }
                   
                   int code = connections.getResponseCode();
                   System.out.println("----https协议请求的返回code："+code);
                   // 取得该连接的输入流，以读取响应内容
                   inputStream=connections.getInputStream();
    			   inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
                   reader = new BufferedReader(inputStreamReader);
                   
//                   lines = reader.readLine();
                   String str = null;  
                   while ((str = reader.readLine()) != null) {  
                	   lines+=str;  
                   }
                   
   			}else{
   				if(StringUtils.isNotEmpty(param)){
   					getURL=getURL+"?"+ param;
   				}
   				URL getUrl = new URL(getURL);
    			   // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection 
    			
    			   connection = (HttpURLConnection) getUrl.openConnection(); 
    			
    			   // 建立与服务器的连接，并未发送数据 
    			   connection.setReadTimeout(8000);
    			   connection.setConnectTimeout(8000);
    			   
    			   // 设置通用属性  
    			   connection.setRequestProperty("Accept", "*/*");  
    			   connection.setRequestProperty("Connection", "Keep-Alive");  
    			   connection.setRequestProperty("User-Agent",  
    	                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)"); 
    			   
    			   connection.connect(); 
    			   
    			   // 发送数据到服务器并使用Reader读取返回的数据 
    			   inputStream=connection.getInputStream();
    			   inputStreamReader=new InputStreamReader(inputStream,"utf-8");
                   reader = new BufferedReader(inputStreamReader);
    			   
    			  
    			
//    			   lines = reader.readLine();
    			   lines="";
    			   String line="";
    			   while ((line = reader.readLine()) != null) {
    			        lines += line;
    			    }
    			   
    			  int code = connection.getResponseCode();
    			  System.out.println("----http协议请求的返回code："+code);
   			}
   			
   			   
   		} catch (Exception e) {
   			e.printStackTrace();
   		} finally{
   			closecon(connection,connections, reader,out,inputStream,inputStreamReader);
   		}
   		   
   		   
   		   return lines;
   		
   		} 
       

       
      
       public static String post(String actionUrl, Map<String, String> headParams,  
               Map<String, String> params,  
               Map<String, File> files) throws IOException {  
     
           String BOUNDARY = java.util.UUID.randomUUID().toString();  
           String PREFIX = "--", LINEND = "\r\n";  
           String MULTIPART_FROM_DATA = "multipart/form-data";  
           String CHARSET = "UTF-8";  
     
           URL uri = new URL(actionUrl);  
           HttpURLConnection conn = (HttpURLConnection) uri.openConnection();  
           conn.setReadTimeout(30 * 1000); // 缓存的最长时间  
           conn.setDoInput(true);// 允许输入  
           conn.setDoOutput(true);// 允许输出  
           conn.setUseCaches(false); // 不允许使用缓存  
           conn.setRequestMethod("POST");  
           conn.setRequestProperty("connection", "keep-alive");  
           conn.setRequestProperty("Charsert", "UTF-8");  
           conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA  
                   + ";boundary=" + BOUNDARY);  
           if(headParams!=null){  
               for(String key : headParams.keySet()){  
                   conn.setRequestProperty(key, headParams.get(key));  
               }  
           }  
           StringBuilder sb = new StringBuilder();  
     
           if (params!=null) {  
               // 首先组拼文本类型的参数  
               for (Map.Entry<String, String> entry : params.entrySet()) {  
                   sb.append(PREFIX);  
                   sb.append(BOUNDARY);  
                   sb.append(LINEND);  
                   sb.append("Content-Disposition: form-data; name=\""  
                           + entry.getKey() + "\"" + LINEND);  
                   sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);  
                   sb.append("Content-Transfer-Encoding: 8bit" + LINEND);  
                   sb.append(LINEND);  
                   sb.append(entry.getValue());  
                   sb.append(LINEND);  
               }  
                 
           }  
             
           DataOutputStream outStream = new DataOutputStream(  
                   conn.getOutputStream());  
           if (!StringUtils.isEmpty(sb.toString())) {  
               outStream.write(sb.toString().getBytes());  
           }  
             
     
           // 发送文件数据  
           if (files != null)  
               for (Map.Entry<String, File> file : files.entrySet()) {  
                   StringBuilder sb1 = new StringBuilder();  
                   sb1.append(PREFIX);  
                   sb1.append(BOUNDARY);  
                   sb1.append(LINEND);  
                   sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""  
                           + file.getKey() + "\"" + LINEND);  
                   sb1.append("Content-Type: application/octet-stream; charset="  
                           + CHARSET + LINEND);  
                   sb1.append(LINEND);  
                   outStream.write(sb1.toString().getBytes());  
     
                   InputStream is = new FileInputStream(file.getValue());  
                   byte[] buffer = new byte[1024];  
                   int len = 0;  
                   while ((len = is.read(buffer)) != -1) {  
                       outStream.write(buffer, 0, len);  
//                       System.out.println("HttpUtil", "写入中...");  
                   }  
     
                   is.close();  
                   outStream.write(LINEND.getBytes());  
               }  
     
           // 请求结束标志  
           byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();  
           outStream.write(end_data);  
           outStream.flush();  
//           System.out.println("HttpUtil", "conn.getContentLength():"+conn.getContentLength());  
     
           // 得到响应码  
           int res = conn.getResponseCode();  
           InputStream in = conn.getInputStream();  
           if (res == 200) {  
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));    
                      StringBuffer buffer = new StringBuffer();    
                    String line = "";    
                while ((line = bufferedReader.readLine()) != null){    
                      buffer.append(line);    
                }    
     
   //          int ch;  
   //          StringBuilder sb2 = new StringBuilder();  
   //          while ((ch = in.read()) != -1) {  
   //              sb2.append((char) ch);  
   //          }  
               return buffer.toString();  
           }  
           outStream.close();  
           conn.disconnect();  
           return in.toString();  
     
       }  
       
       public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {  
           StringBuffer buffer = new StringBuffer();  
           try {  
           	
               // 创建SSLContext对象，并使用我们指定的信任管理器初始化   
               TrustManager[] trustAllCerts = new TrustManager[]{
                       new X509TrustManager() {
							
							@Override
							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}
							
							@Override
							public void checkServerTrusted(X509Certificate[] arg0, String arg1)
									throws CertificateException {
								
							}
							
							@Override
							public void checkClientTrusted(X509Certificate[] arg0, String arg1)
									throws CertificateException {
								
							}
							
						}
                   };
//               SSLContext sc = SSLContext.getInstance("TLS");  
               SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
               sslContext.init(null, trustAllCerts, new java.security.SecureRandom());  
               
     
               //打开连接
               URL url = new URL(requestUrl);  
               HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
               httpUrlConn.setSSLSocketFactory(sslContext.getSocketFactory());  
               httpUrlConn.setDoOutput(true);  
               httpUrlConn.setDoInput(true);  
               httpUrlConn.setUseCaches(false);  
               httpUrlConn.setHostnameVerifier(new HttpRequest().new TrustAnyHostnameVerifier());
               
               
//               httpUrlConn.setRequestProperty("Host", "www.chinacourt.org");
//               connections.setRequestProperty("Accept-Charset", "utf-8");
//               connections.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
//               connections.setRequestProperty("Connection","keep-alive");
               
               // 设置请求方式（GET/POST）   
               httpUrlConn.setRequestMethod(requestMethod);  
     
               if ("GET".equalsIgnoreCase(requestMethod)){  
                   httpUrlConn.connect();  
               }
               // 当有数据需要提交时   
               if (null != outputStr) {  
                   OutputStream outputStream = httpUrlConn.getOutputStream();  
                   // 注意编码格式，防止中文乱码   
                   outputStream.write(outputStr.getBytes("UTF-8"));  
                   outputStream.close();  
               }  
     
               // 将返回的输入流转换成字符串   
               InputStream inputStream = httpUrlConn.getInputStream();  
               InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
               BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
               String str = null;  
               while ((str = bufferedReader.readLine()) != null) {  
                   buffer.append(str);  
               }  
               bufferedReader.close();  
               inputStreamReader.close();  
               // 释放资源   
               inputStream.close();  
               inputStream = null;  
               httpUrlConn.disconnect();  
           } catch (Exception ce) {  
           	 ce.printStackTrace();
           }  
           return buffer.toString();  
       } 
       
       public class TrustAnyHostnameVerifier implements HostnameVerifier {
    	   public boolean verify(String hostname, SSLSession session) {
    	    // 直接返回true
    	    return true;
    	   }
    	  }
       
       
	public static void closecon(HttpURLConnection connection,HttpsURLConnection connections,BufferedReader reader,DataOutputStream out,InputStream inputStream,InputStreamReader inputStreamReader){
    	try {
			if(null!=reader){
				
				reader.close(); 
			}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	try {
			if(null!=inputStreamReader){
				
				inputStreamReader.close(); 
			}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	try {
			if(null!=out){
				
				out.close(); 
			}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	try {
			if(null!=inputStream){
				
				inputStream.close();; 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		try {
			if(null!=connection){
				
				connection.disconnect(); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(null!=connections){
				
				connections.disconnect(); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws InterruptedException { 

		
		try {
			String str=readContentFrompost_notencode("https://www.chinacourt.org/law/more/law_type_id/MzAwNEAFAA%3D%3D/page/1.shtml", null,"GET");
			
//			String str=httpRequest("https://www.chinacourt.org/law/more/law_type_id/MzAwNEAFAA%3D%3D/page/1.shtml", "GET", null);
			
			
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}	
}
