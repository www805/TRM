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
    			 
    			LogUtil.intoLog(HttpRequest.class,DateUtil.getDateAndMinute()+" getURL:"+getURL);
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
                    LogUtil.intoLog(HttpRequest.class,"----https协议请求的返回code："+code);
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
     			  LogUtil.intoLog(HttpRequest.class,"----http协议请求的返回code："+code);
    			}
    			
    			   
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
				closecon(connection,connections, reader,out,null,null);
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


   private class TrustAnyHostnameVerifier implements HostnameVerifier {
	   public boolean verify(String hostname, SSLSession session) {
		// 直接返回true
		return true;
	   }
	  }
       
       
	private static void closecon(HttpURLConnection connection,HttpsURLConnection connections,BufferedReader reader,DataOutputStream out,InputStream inputStream,InputStreamReader inputStreamReader){
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



}
