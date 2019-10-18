package com.avst.trm.v1.common.util;

import java.io.*;

/**
 * java执行 shell语句
 */
public class ExecuteCMD {

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

	/**
	 * 执行cmd命令(需要通道还执行)
	 * @param cmd
	 * @return
	 */
	public static String executeCMD_td(String cmd){


		String[] arr=new String[]{ "sh", "-c", cmd };
		return execute(null,arr);

	}


	public static String executeCMD(String cmd){

		return execute(cmd,null);
	}

	/**
	 * 执行cmd命令
	 * @param cmd
	 * @return
	 */
	private static String execute(String cmd,String[] cmdarr){
		OutputStream os = null;
		InputStream in=null;
		InputStream in2=null;
		Process process=null;
		try {
			long start = System.currentTimeMillis();
			if(null==cmd){
				process = Runtime.getRuntime().exec(cmdarr);
			}else{
				process = Runtime.getRuntime().exec(cmd);
			}
			os=process.getOutputStream();
			in=process.getInputStream();
			in2=process.getErrorStream();

			printMessage(in2);

			int exitvalue=process.waitFor();
			if(exitvalue!=0){
				throw new Exception("exitvalue is not 0, 说明代码有错");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {

			close(os,in,in2,process);
		}

		return null;

	}

	private static void close(OutputStream os,InputStream in,InputStream in2,Process process){
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
}  