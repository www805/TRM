package com.avst.trm.v1.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
	
       
       public static String readContentFromGet(String url,String param)  {

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
     			  System.out.println("----http协议请求的返回code："+code);
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

	/**
	 * 文件上传的方法
	 * 可以携带参数
	 * @param actionUrl：上传的路径
	 * @param uploadFilePath：需要上传的文件路径
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String uploadFile(String actionUrl, String uploadFilePath,Map<String,String> map) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		URLConnection urlConnection=null;
		HttpURLConnection httpURLConnection=null;
		HttpsURLConnection httpsURLConnection=null;
		OutputStream out=null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		File file = new File(uploadFilePath);

		//文件上传
		String uploadFile = uploadFilePath;
		String filename = file.getName();

		try {

			// 统一资源
			URL url = new URL(actionUrl);
			// 连接类的父类，抽象类
			urlConnection = url.openConnection();
			if(actionUrl.startsWith("https://")){
				// https的连接类
				httpsURLConnection=(HttpsURLConnection)urlConnection;

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
				httpsURLConnection.setSSLSocketFactory(sc.getSocketFactory());
				HostnameVerifier hv = new HostnameVerifier() {

					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				};
				httpsURLConnection.setHostnameVerifier(hv);

				// httpsURLConnection，默认情况下是true;
				httpsURLConnection.setDoInput(true);
				// 设置是否向httpUrlConnection输出
				httpsURLConnection.setDoOutput(true);
				// Post 请求不能使用缓存
				httpsURLConnection.setUseCaches(false);
				// 设定请求的方法，默认是GET
				httpsURLConnection.setRequestMethod("POST");
				// 设置字符编码连接参数
				httpsURLConnection.setRequestProperty("Connection", "Keep-Alive");
				// 设置字符编码
				httpsURLConnection.setRequestProperty("Charset", "UTF-8");
				httpsURLConnection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
				// 设置请求内容类型
				httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);




				// 请求正文信息
				// 第一部分：
				StringBuilder sb = new StringBuilder();
				if(null!=map){
					for (Map.Entry<String, String> entry : map.entrySet()) {
						sb.append(twoHyphens);
						sb.append(boundary);
						sb.append(end);
						sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + end);
						sb.append("Content-Type: text/plain; charset=UTF-8"  + end);
						sb.append("Content-Transfer-Encoding: 8bit" + end);
						sb.append(end);
						sb.append(entry.getValue());
						sb.append(end);
					}
				}


				sb.append("--"); // 必须多两道线
				sb.append(boundary);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + filename + "\"\r\n");
				//未知文件类型，以流的方式上传
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				byte[] head = sb.toString().getBytes("utf-8");
				// 获得输出流
				out = new DataOutputStream(httpURLConnection.getOutputStream());
				// 输出表头
				out.write(head);
				// 文件正文部分
				// 把文件已流文件的方式 推入到url中
				inputStream = new FileInputStream(file);
				DataInputStream dataIn = new DataInputStream(inputStream);
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = dataIn.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				inputStream.close();
				// 结尾部分
				byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
				out.write(foot);
				out.flush();
				out.close();

				if (httpURLConnection.getResponseCode() >= 300) {
					throw new Exception(
							"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
				}

				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					inputStream = httpURLConnection.getInputStream();
					inputStreamReader = new InputStreamReader(inputStream);
					reader = new BufferedReader(inputStreamReader);
					tempLine = null;
					resultBuffer = new StringBuffer();
					while ((tempLine = reader.readLine()) != null) {
						resultBuffer.append(tempLine);
						resultBuffer.append("\n");
					}
				}
			}else{
				// http的连接类
				httpURLConnection = (HttpURLConnection) urlConnection;
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				httpURLConnection.setDoInput(true);
				// 设置是否向httpUrlConnection输出
				httpURLConnection.setDoOutput(true);
				// Post 请求不能使用缓存
				httpURLConnection.setUseCaches(false);
				// 设定请求的方法，默认是GET
				httpURLConnection.setRequestMethod("POST");
				// 设置字符编码连接参数
				httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
				// 设置字符编码
				httpURLConnection.setRequestProperty("Charset", "UTF-8");
				httpURLConnection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
				// 设置请求内容类型
				httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


				// 请求正文信息
				// 第一部分：
				StringBuilder sb = new StringBuilder();

				if(null!=map){
					for (Map.Entry<String, String> entry : map.entrySet()) {
						sb.append(twoHyphens);
						sb.append(boundary);
						sb.append(end);
						sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + end);
						sb.append("Content-Type: text/plain; charset=UTF-8"  + end);
						sb.append("Content-Transfer-Encoding: 8bit" + end);
						sb.append(end);
						sb.append(entry.getValue());
						sb.append(end);
					}
				}


				sb.append(twoHyphens); // 必须多两道线
				sb.append(boundary);
				sb.append(end);
				sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + filename + "\""+end);
				//未知文件类型，以流的方式上传
				sb.append("Content-Type:application/octet-stream"+end+end);
				byte[] head = sb.toString().getBytes("utf-8");
				// 获得输出流
				out = new DataOutputStream(httpURLConnection.getOutputStream());
				// 输出表头
				out.write(head);
				// 文件正文部分
				// 把文件已流文件的方式 推入到url中
				inputStream = new FileInputStream(file);
				DataInputStream dataIn = new DataInputStream(inputStream);
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = dataIn.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				inputStream.close();
				// 结尾部分
				byte[] foot = (end+twoHyphens + boundary + twoHyphens+end).getBytes("utf-8");// 定义最后数据分隔线
				out.write(foot);
				out.flush();
				out.close();

				//返回值
				if (httpURLConnection.getResponseCode() >= 300) {
					throw new Exception(
							"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
				}

				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					inputStream = httpURLConnection.getInputStream();
					inputStreamReader = new InputStreamReader(inputStream);
					reader = new BufferedReader(inputStreamReader);
					tempLine = null;
					resultBuffer = new StringBuffer();
					while ((tempLine = reader.readLine()) != null) {
						resultBuffer.append(tempLine);
						resultBuffer.append("\n");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			closecon(httpURLConnection,httpsURLConnection,reader, null,inputStream,inputStreamReader);


			if(null!=out){
				try{
					out.close();
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			if(null!=urlConnection){
				urlConnection=null;
			}

		}
		return resultBuffer.toString();
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





	public static String uploadToUrl(String filepath,String url,Map<String,String> map)  {

		StringBuffer strBuf= null;
		try {
			File file=new File(filepath);
			String fileName = file.getName();
			strBuf = null;

			// 获取文件输入流
			InputStream in = new FileInputStream(file);
			if (null != in) {
				URL urlObj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
				con.setRequestMethod("POST"); // 设置关键值,以Post方式提交表单，默认get方式
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false); // post方式不能使用缓存
				// 设置请求头信息
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				// 设置边界
				String BOUNDARY = "----------" + System.currentTimeMillis();
				con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
				// 请求正文信息
				// 第一部分：
				StringBuilder sb = new StringBuilder();

				for (Map.Entry<String, String> entry : map.entrySet()) {
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + "\r\n");
					sb.append("Content-Type: text/plain; charset=UTF-8"  + "\r\n");
					sb.append("Content-Transfer-Encoding: 8bit" + "\r\n");
					sb.append("\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");

				}


				sb.append("--"); // 必须多两道线
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
				//未知文件类型，以流的方式上传
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				byte[] head = sb.toString().getBytes("utf-8");
				// 获得输出流
				OutputStream out = new DataOutputStream(con.getOutputStream());
				// 输出表头
				out.write(head);
				// 文件正文部分
				// 把文件已流文件的方式 推入到url中
				DataInputStream dataIn = new DataInputStream(in);
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = dataIn.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				in.close();
				// 结尾部分
				byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
				out.write(foot);
				out.flush();
				out.close();

				// 读取返回数据
				strBuf = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					strBuf.append(line).append("\n");
				}
				reader.close();
				con.disconnect();
				con = null;
			} else {
				throw new Throwable("获取文件流失败,文件下载地址url=" + filepath);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
		}
		return  strBuf.toString();
	}



}
