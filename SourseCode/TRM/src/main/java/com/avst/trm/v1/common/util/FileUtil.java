package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {


	/**
	 * 
	 * @param dir 获取文件路径的文件夹的路径
	 * @param isfilespath 是否显示里面的文件夹的路径2/1 2是需要1是不需要
	 * @return
	 */
	public static List<String> getAllFiles(File dir, List<String> filelist,int isfilespath){  
        File[] fs = dir.listFiles();  
        for (int i = 0; i < fs.length; i++) {  
        	//只存文件路径
        	File file=fs[i];
        	if(file.exists()){
        		if(2==isfilespath){
        			
        			filelist.add(file.getAbsolutePath());  
        		}else if(1==isfilespath ){
        			if(!file.isDirectory()){
            			filelist.add(file.getAbsolutePath());
        			}
        		}

        	}
            //若为文件夹，就调用getAllFiles方法  
            if (file.isDirectory()) {  
                try {  
                    getAllFiles(file, filelist, isfilespath);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return filelist;  
    } 
	
	/**
	 * 
	 * @param basepath 获取文件路径的文件夹的路径
	 * @param isfilespath 是否显示里面的文件夹的路径2/1 2是需要1是不需要
	 * @return
	 */
	public static List<String> getAllFilePath(String basepath,int isfilespath){
		
		File file = new File(basepath);
		List<String> filelist = new ArrayList<String>();
		if (file.exists()){
			List<String> list = getAllFiles(file, filelist,isfilespath );
			return list;
		}
		return null;
	}

	
	
	/** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean deleteFile(String sPath) {  
        boolean flag = false;  
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }

//删除指定文件夹下的所有文件
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	//删除文件夹
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件所在文件夹路径
	 * @param filePath
	 * @return
	 */
	public static String getsavepath(String filePath) {

		if(StringUtils.isEmpty(filePath)){
			LogUtil.intoLog(4,FileUtil.class,"is null filePath:"+filePath);
			return null;
		}
		String savapath=null;
		if(filePath.indexOf("\\") > 0){
			savapath=filePath.substring(0,filePath.lastIndexOf("\\"));
		}else if(filePath.indexOf("/") > 0){
			savapath=filePath.substring(0,filePath.lastIndexOf("/"));
		}else{
			LogUtil.intoLog(4,FileUtil.class,"filePath.indexOf(\\) and filePath.indexOf(/) is < 0,filePath:"+filePath);
			return null;
		}

    	return savapath;
	}

	/**
	 * 获取文件路径下的文件名
	 * @param filePath
	 * @return
	 */
	public static String getsavename(String filePath) {

		if(StringUtils.isEmpty(filePath)){
			LogUtil.intoLog(4,FileUtil.class,"is null filePath:"+filePath);
			return null;
		}
		String savename=null;
		if(filePath.indexOf("\\") > 0){
			savename=filePath.substring(filePath.lastIndexOf("\\")+1);
		}else if(filePath.indexOf("/") > 0){
			savename=filePath.substring(filePath.lastIndexOf("/")+1);
		}else{
			LogUtil.intoLog(4,FileUtil.class,"filePath.indexOf(\\) and filePath.indexOf(/) is < 0,filePath:"+filePath);
			return null;
		}

		return savename;
	}

	/**
	 * 检测文件路径，没有就新增
	 * @param filePath
	 * @return
	 */
	public static boolean checkfilepath(String filePath) {

		if(StringUtils.isEmpty(filePath)){
			LogUtil.intoLog(4,FileUtil.class,"is null filePath:"+filePath);
			return false;
		}
		String savapath=null;
		if(filePath.indexOf("\\") > 0){
			savapath=filePath.substring(0,filePath.lastIndexOf("\\"));
		}else if(filePath.indexOf("/") > 0){
			savapath=filePath.substring(0,filePath.lastIndexOf("/"));
		}else{
			LogUtil.intoLog(4,FileUtil.class,"filePath.indexOf(\\) and filePath.indexOf(/) is < 0,filePath:"+filePath);
			return false;
		}

		try {
			File file=new File(savapath);
			if(!file.exists()||!file.isDirectory()){
				file.mkdirs();
			}
			file=null;
			file=new File(filePath);
			if(!file.exists()||file.isDirectory()){
				file.createNewFile();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 检测文件夹路径，没有就新增
	 * @param directoryPath
	 * @return
	 */
	public static boolean checkDirectoryPath(String directoryPath) {

		if(StringUtils.isEmpty(directoryPath)){
			LogUtil.intoLog(4,FileUtil.class,"is null directoryPath:"+directoryPath);
			return false;
		}
		try {
			File file=new File(directoryPath);
			if(!file.exists()||!file.isDirectory()){
				file.mkdirs();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}


	public static void main(String[] args) {

		System.out.println(getsavepath("f:\\32\\43\\57\\fr.ds"));
	}

}
