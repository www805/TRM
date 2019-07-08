package com.avst.trm.v1.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ndktools.javamd5.core.MD5;
import org.apache.commons.codec.digest.DigestUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;


public class OpenUtil {

	
	
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "说明.txt", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z" };  


	public static String generateShortUuid_8() {  
	StringBuffer shortBuffer = new StringBuffer();  
	String uuid = UUID.randomUUID().toString().replace("-", "");  
	LogUtil.intoLog(OpenUtil.class,uuid);
	for (int i = 0; i < 8; i++) {  
	    String str = uuid.substring(i * 4, i * 4 + 4);  
	    int x = Integer.parseInt(str, 16);  
	    shortBuffer.append(chars[x % 0x3E]);  
	}  
	return shortBuffer.toString();  
	}
	
	public static final String EncoderMd5(String s){
	   MD5 md5=new MD5();
	   return md5.getMD5ofStr(s);
	}
	
	public static final String EncoderMd5_file(String url){

		String md5=null;
		
		try {
			FileInputStream fis= new FileInputStream(url);    
			md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
			IOUtils.closeQuietly(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
        LogUtil.intoLog(OpenUtil.class,"EncoderMd5_file MD5:"+md5);
		return md5;
		}
	 /** 
	     * 验证输入的邮箱格式是否符合 
	     * @cmparam email
	     * @return 是否合法 
	     */ 
	public static boolean checkEmail(String email) {
	    boolean flag = false;
	    try {
	        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	        Pattern regex = Pattern.compile(check);
	        Matcher matcher = regex.matcher(email);
	        flag = matcher.matches();
	    } catch (Exception e) {
	        flag = false;
	    }
	    return flag;
	}
	/**
	 * 判断字符串是否为手机号
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	/**
	 * 新增文件夹路径使用自带的参数（）
	 * @param files
	 * @param basepath
	 * @return
	 */
	public static String createpath_notpro(String files,String basepath ){
	
	Date dd=new Date();
	LogUtil.intoLog(OpenUtil.class,"basepath_:"+basepath);
	String path=basepath+DateUtil.getYear(dd)+"/"+DateUtil.getMonth(dd)+"/"+DateUtil.getDay(dd)+"/"+files;
	File file =new File(path);    
	if  (!file.exists()  && !file.isDirectory())      
	{       
		LogUtil.intoLog(OpenUtil.class,path+"//不存在");
		boolean bool=file.mkdirs();  
		if(!bool){
			boolean bool2=file.mkdirs();
			LogUtil.intoLog(OpenUtil.class,"------------------------");
			LogUtil.intoLog(OpenUtil.class,"                      ");
			LogUtil.intoLog(OpenUtil.class,"第一次创建存储ts的文件夹失败，第二次失败："+bool2);
			LogUtil.intoLog(OpenUtil.class,"                      ");
			LogUtil.intoLog(OpenUtil.class,"------------------------");
		}
	} else   
	{  
		LogUtil.intoLog(OpenUtil.class,path+"//已存在");
	}
	
	 return path;
}
	/**
	 * 创建文件夹
	 * @param path
	 * @return
	 */
	public static String createpath_files(String path ){
		
		try {
			File file =new File(path);    
			if  (!file.exists()  && !file.isDirectory())      
			{       
				LogUtil.intoLog(OpenUtil.class,path+"//不存在");
				boolean bool=file.mkdirs();  
				if(!bool){
					boolean bool2=file.mkdirs();
					LogUtil.intoLog(OpenUtil.class,"------------------------");
					LogUtil.intoLog(OpenUtil.class,"                      ");
					LogUtil.intoLog(OpenUtil.class,"第一次创建createpath_files失败，第二次："+bool2);
					LogUtil.intoLog(OpenUtil.class,"                      ");
					LogUtil.intoLog(OpenUtil.class,"------------------------");
				}
			} else   
			{  
				LogUtil.intoLog(OpenUtil.class,path+"//已存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 return path;
	}
	
	/**
	 * 创建文件夹(附带年月日)
	 * @param path
	 * @return
	 */
	public static String createpath_files_addymd(String path ){
		Date dd=new Date();	
		path=path+DateUtil.getYear(dd)+"/"+DateUtil.getMonth(dd)+"/"+DateUtil.getDay(dd);
		File file =new File(path);    
		if  (!file.exists()  && !file.isDirectory())      
		{       
			LogUtil.intoLog(OpenUtil.class,path+"//不存在");
			boolean bool=file.mkdirs();  
			if(!bool){
				boolean bool2=file.mkdirs();
				LogUtil.intoLog(OpenUtil.class,"------------------------");
				LogUtil.intoLog(OpenUtil.class,"                      ");
				LogUtil.intoLog(OpenUtil.class,"第一次创建createpath_files失败，第二次："+bool2);
				LogUtil.intoLog(OpenUtil.class,"                      ");
				LogUtil.intoLog(OpenUtil.class,"------------------------");
			}
		} else   
		{  
			LogUtil.intoLog(OpenUtil.class,path+"//已存在");
		}
		
		 return path;
	}
	
	
	
	
	/**
	 * 创建文件
	 * @param path
	 * @return
	 */
	public static String createpath_file(String path ){
		
		File file =new File(path); 
		String filepath2=file.getParentFile().getPath();
		File file2=new File(filepath2);
		//如果文件夹不存在则创建    
		if  (! file2.exists()  && !file2.isDirectory())      
		{       
		    LogUtil.intoLog(OpenUtil.class,filepath2+"//文件夹不存在");
		    file2.mkdirs();    
		} else   
		{  
		    LogUtil.intoLog(OpenUtil.class,filepath2+"//文件夹存在");
		}
		
		//创建目标文件  
    	if (!file.exists() && !file.isDirectory()) {
    		try {
    			boolean bool=file.createNewFile();
    			LogUtil.intoLog(OpenUtil.class,path+"//创建"+bool);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}else{
    		LogUtil.intoLog(OpenUtil.class,path+"//文件存在");
    	}
		
		 return path;
	}
	
	
	/**
	 * 创建文件，以日期格式为目录结构
	 * @param basepath
	 * @param filename
	 * @return
	 */
	public static String createpath_fileByBasepath(String basepath,String filename ){
		
		Date dd=new Date();	
		String path=basepath+DateUtil.getYear(dd)+"/"+DateUtil.getMonth(dd)+"/"+DateUtil.getDay(dd)+"/"+filename;
		File file =new File(path); 
		String filepath2=file.getParentFile().getPath();
		File file2=new File(filepath2);
		//如果文件夹不存在则创建    
		if  (! file2.exists()  && !file2.isDirectory())      
		{       
		    LogUtil.intoLog(OpenUtil.class,filepath2+"//文件夹不存在");
		    file2.mkdirs();    
		} else   
		{  
		    LogUtil.intoLog(OpenUtil.class,filepath2+"//文件夹存在");
		}
		
		//创建目标文件  
    	if (!file.exists() && !file.isDirectory()) {
    		try {
    			boolean bool=file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}else{
    		LogUtil.intoLog(OpenUtil.class,path+"//文件存在");
    	}
		
		 return path;
	}
	
	/**
	 * 创建文件夹，以日期格式为目录结构
	 * @param basepath
	 * @return
	 */
	public static String createpath_fileByBasepath(String basepath){
		
		Date dd=new Date();	
		String path=basepath+DateUtil.getYear(dd)+"/"+DateUtil.getMonth(dd)+"/"+DateUtil.getDay(dd)+"/";
		File file =new File(path); 
		//如果文件夹不存在则创建    
		if  (! file.exists()  && !file.isDirectory())      
		{       
		    LogUtil.intoLog(OpenUtil.class,file+"//文件夹不存在");
		    file.mkdirs();    
		} else   
		{  
		    LogUtil.intoLog(OpenUtil.class,file+"//文件夹存在");
		}
		 return path;
	}
	

	/**
	 * 获取32位的uuid
	 * @return
	 */
	public static String getUUID_32(){
		return UUID.randomUUID().toString().replace("-", ""); 
	}
	
	 /*** 
     * MD5加密 生成32位md5码
     * @param inStr 待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) {
        try {
			MessageDigest md5 = null;
			try {
			    md5 = MessageDigest.getInstance("MD5");
			} catch (Exception e) {
			    LogUtil.intoLog(OpenUtil.class,e.toString());
			    e.printStackTrace();
			    return "";
			}

			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
			    int val = ((int) md5Bytes[i]) & 0xff;
			    if (val < 16) {
			        hexValue.append("0");
			    }
			    hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
public static String numtoStr(int digit,Integer num){
		
		String numstr=num+"";
			if(numstr.length() <digit){
				int s=digit-(numstr.length());
				for(int i=0;i<s;i++){
					numstr="0"+numstr;
				}
			}
		return numstr;
	} 
	
	public static Integer strtimetoSS(String strtime){
		String[] arr=strtime.split(":");
		if(arr.length==3){
			String time=strtime.substring(0, strtime.indexOf("."));
			Integer time_s=Integer.parseInt((time.substring(0, time.indexOf(":"))))*3600+Integer.parseInt((time.substring(time.indexOf(":")+1, time.lastIndexOf(":"))))*60+Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
			LogUtil.intoLog(OpenUtil.class,time_s);
			return time_s;
		}else if(arr.length==2){
			String time3=strtime.substring(0, strtime.indexOf("."));
			String t1=time3.substring(0, time3.lastIndexOf(":"));
			String t2=time3.substring(time3.indexOf(":")+1);
			Integer time_s3=Integer.parseInt(t1)*60+Integer.parseInt(t2);
			LogUtil.intoLog(OpenUtil.class,time_s3);
			return time_s3;
		}else{
			LogUtil.intoLog(OpenUtil.class,"strtimetoSS出错");
			return 0;
		}		
	}
	
	
	/**
	 * 检测文件是否存在
	 */
   public static boolean fileisexist(String fulepath){
	   
	   
	   try {
		if(null!=fulepath&&!fulepath.trim().equals("")){
			   
			   File file =new File(fulepath);   
			   boolean bool1=file.exists();
			   boolean bool2=file.isDirectory();
				if( bool1 ){//存在这个文件，并且不是一个目录
					if(!bool2){
						return true;
					}
				}
		   }
	} catch (Exception e) {
		e.printStackTrace();
	}
	   return false;
   }

   
   /**
	 * 检测字段的首尾的特殊字符检测
	 */
  public static String strtrim(String str,String trimchar){
	   
	   
	   try {
		if(null!=str&&!str.trim().equals("")){
			   String str_=str.trim();
			   String firstchar=str_.substring(0, 1);
			   String thelastchar=str_.substring(str_.length()-1, str_.length());
			   if(firstchar.equals(trimchar)){
				   str_=str_.substring(1);
			   }
			   if(thelastchar.equals(trimchar)){
				   str_=str_.substring(0, str_.length()-1);
			   }
			   return str_;
			   
		   }
	} catch (Exception e) {
		e.printStackTrace();
	}
	   
	   return str;
	   
  }
	
  
	public static boolean isNotEmpty(String str){
		
		if(null!=str&&!str.trim().equals("")){
			return true;
		}
		return false;
	}
	/**
	 * 添加base路径，得到绝对路径
	 * @param basepath 添加的首路径
	 * @param cutstr 切割的的标志
	 * @param waitAddPath 等待被添加的路径
	 * @return
	 */
	public static String strAddBasePath(String basepath,String cutstr,String waitAddPath){
		
		if(StringUtils.isNotEmpty(cutstr)&&StringUtils.isNotEmpty(waitAddPath)&&StringUtils.isNotEmpty(basepath)){
			return basepath.substring(0, basepath.indexOf(cutstr)-1)+waitAddPath;
		}
		return waitAddPath;
	}
	
	/**
	 * 给出相对路径
	 * @param cutstr 切割的的标志
	 * @param waitMinusPath 等待被裁剪的路径
	 * @return
	 */
	public static String strMinusBasePath(String cutstr,String waitMinusPath){
		
		if(StringUtils.isNotEmpty(cutstr)&&StringUtils.isNotEmpty(waitMinusPath)){
			
			String path=null;
			try {
				path = waitMinusPath.substring(waitMinusPath.indexOf("/"+cutstr));
			} catch (Exception e) {
				try {
					path = waitMinusPath.substring(waitMinusPath.indexOf("\\"+cutstr));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			return path;
		}
		return waitMinusPath;
	}
	
	/**
	 * 获取文件的hash值，字母大写
	 * @param filepath
	 * @return
	 */
	public static String getMD5Checksum(String filepath){
		
		String result_=null;
		try {
			InputStream fis =  new FileInputStream(filepath);

			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;

			do {
			    numRead = fis.read(buffer);
			    if (numRead > 0) {
			        complete.update(buffer, 0, numRead);
			    }
			} while (numRead != -1);

			fis.close();
			
			byte[] b = complete.digest();
			String result = "";
			for (int i=0; i < b.length; i++) {
				String rr=Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
				
					result +=rr ;
			}
			result_="";
			char[] arr=result.toCharArray();
			for(char c:arr){
				if(c>='a' && c<='z'){
					c-=32;
				}
				result_+=String.valueOf(c);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
        return result_;
        
	}
	
	/**
	 * MD5值，字母大写
	 * @param str
	 * @return
	 */
	public static String MD5ADD32(String str){
		
		String result_="";
		try {
			char[] arr=str.toCharArray();
			for(char c:arr){
				if(c>='a' && c<='z'){
					c-=32;
				}
				result_+=String.valueOf(c);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
        return result_;
        
	}
	
	/**
	 * 判断文件类型
	 * @param filename
	 * @return
	 */
	public static String getfiletype(String filename){
		
		try {
			if(StringUtils.isNotEmpty(filename)){
				String [] arr=filename.split("\\.");//
				if(null!=arr&&arr.length > 0){
					return arr[arr.length-1];
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
        return "unknown";
        
	}
	
	/**
	 * 获取文件的文件夹路径
	 * @param filename
	 * @return
	 */
	public static String getfile_folder(String filename){
		
		try {
			if(StringUtils.isNotEmpty(filename)){
				
				if(filename.contains("/")){
					return filename.substring(0, filename.lastIndexOf("/"));
				}else if (filename.contains("\\")){
					return filename.substring(0, filename.lastIndexOf("\\"));
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
        return null;
        
	}
	
	/**
	 * 按要求字符切割字符串
	 * @param cutstr 要求的切割字符
	 * @param waitMinusPath 等待切割的字符串
	 * @return 返回list 2个字符串或者一个字符串
	 */
	public static String[] strMinus(String cutstr,String waitMinusPath){
		
		String[] slist={waitMinusPath};
		if(StringUtils.isNotEmpty(cutstr)&&StringUtils.isNotEmpty(waitMinusPath)){
			
			if(waitMinusPath.contains(cutstr)){
				String[] arr= waitMinusPath.split(cutstr, 2);
				return arr;
			}
			
		}
		return slist;
	}
	
	/**
	 * 常规字符切割字符串
	 * @param cutstr 要求的切割字符
	 * @param waitMinusPath 等待切割的字符串
	 * @return 返回list 
	 */
	public static List<String> strMinusToList(String cutstr,String waitMinusPath){
		
		if(StringUtils.isNotEmpty(cutstr)&&StringUtils.isNotEmpty(waitMinusPath)){
			List<String> list=new ArrayList<String>();
			String[] arr=waitMinusPath.split(cutstr);
			if(null!=arr&&arr.length > 0){
				
				for(String str:arr){
					if(StringUtils.isNotEmpty(str)){
						list.add(str);
					}
				}
				
				if(list.size()==0){
					list.add(waitMinusPath);
				}
			}else{
				list.add(waitMinusPath);
			}
			return list;
			
		}else{
			LogUtil.intoLog(OpenUtil.class,cutstr+":cutstr waitMinusPath:"+waitMinusPath+" ,have one is null");
		}
		return null;
	}
	

	
	
	/**
	 * 判断一个字符串中是否包含数字
	 * @param str
	 * @return
	 */
	 public static boolean HasDigit(String str) {
	        boolean flag = false;
	        Pattern p = Pattern.compile(".*\\d+.*");
	        Matcher m = p.matcher(str);
	        if (m.matches()) {
	            flag = true;
	        }
	        return flag;
	    }
	 
	 /**
	  * 是否包含英文，字母
	  * @param str
	  * @return
	  */
	 public static boolean isNotEnglish(String str) {
	        boolean flag = false;
	        Pattern p = Pattern.compile("[a-zA-z]");
	        Matcher m = p.matcher(str);
	        if (m.find()) {
	            flag = true;
	        }
	        return flag;
	    }
	
	
	public static String stringtoutf8(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		if(!java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(str)){
			try {
				str = new String(str.getBytes("iso-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	/**
	 * 判断字符串中是否包含中文
	 * @param str
	 * 待校验字符串
	 * @return 是否有中文
	 * @warn 不能校验是否为中文标点符号 
	 */
	public static boolean isContainChinese(String str) {
	 Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	 Matcher m = p.matcher(str);
	 if (m.find()) {
	  return true;
	 }
	 return false;
	}
	//qudiao 
	public static String qu(String str) {
		if (str.length()>0) {
			String indextxt="";
			for (int i = 0; i < str.length(); i++) {
				indextxt=String.valueOf(str.charAt(i));
				if (!OpenUtil.isContainChinese(indextxt)) {
				str=str.replace(indextxt,"");
				i--;
				}
			}
		}
		return str;
		}
	
	/**
	 * 判断字符串中是否包含标点符号
	 * @param str 待校验字符串
	 * @return 是否有标点符号
	 * 暂时用起来感觉不行,
	 */
	public static boolean isbiaodianfuhao(String str) {
	 Pattern p = Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]$"); 
	 Matcher m = p.matcher(str);
	 if (m.find()) {
	  return true;
	 }
	 return false;
	}
	
	/**
	 * 判断字符串中是否包含标点符号
	 * @param str 待校验字符串
	 * @return 是否有标点符号
	 */
	 public static boolean isSpecialChar(String str) {
	        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
	        Pattern p = Pattern.compile(regEx);
	        Matcher m = p.matcher(str);
	        return m.find();
	    }
	
	
	/**
	 * 提取字符串中是否包含某些特定字符
	 * @param str 待校验字符串
	 * @return 是否有标点符号
	 */
	public static List<String> isnotbiaodianfuhaoandContainChinese(String str,String basestr,String endstr) {
	 String regex=basestr+"([^\u4e00-\u9fa5 | ^` | ^~ | ^! | ^@ | ^# | ^$ | ^^ | ^& | ^* | ^( | ^) | ^= | ^| | ^{ | ^} | ^' | ^: | ^; | ^, | ^\\ | ^\\[ | ^\\] | ^. | ^< | ^> | ^/ | ^? | ^￥ | ^… | ^& | ^* | ^（ | ^） | ^— | ^\\| | ^【 | ^】 | ^‘ | ^； | ^： | ^” | ^“ | ^。 | ^， | ^、 | ^？]){2,6}"+endstr;
	 Pattern pattern =Pattern.compile(regex);
	 List<String> slist=new ArrayList<String>();
	 Matcher m = pattern.matcher(str);
	 while(m.find()){
		 String s=m.group();
		 s=s.replaceAll("<br>", "");
		 LogUtil.intoLog(OpenUtil.class,s);
		 slist.add(s );
	 }
	 return slist;
	}
	
	/**
	 * 切割成字符串数组
	 * @param wiatstr
	 * @param splitstr
	 * @return
	 */
	public static List<String> split(String wiatstr,String splitstr){
		
		if(StringUtils.isNotEmpty(splitstr)&&StringUtils.isNotEmpty(wiatstr)){
			List<String> list=new ArrayList<String>();
			
			String [] arr=wiatstr.split(splitstr);
			if(null==arr||arr.length==0){
				return null;
			}
			for(String str:arr){
				if(StringUtils.isNotEmpty(str)){
					list.add(str);
				}
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 切割字典字多拼音解释
	 * @param str 字典字解释
	 * @param splitstr 待切割的符号，一般是@@
	 * @param zidianzi 待切割的字典字
	 * @return
	 */
	public static List<String> strsplitzhujieList(String str,String splitstr,String zidianzi){
		
		
		
		List<String> slist=new ArrayList<String>();
		
		if(StringUtils.isNotEmpty(str)&&StringUtils.isNotEmpty(splitstr)){
			
			str=strreplacejianjie(str);
			if(StringUtils.isNotEmpty(zidianzi)){
				
				zidianzi="";
			}
			String splitstr2=zidianzi+splitstr;
			
			
			if(str.contains(splitstr2)){
				String[] arr2=str.split(splitstr2);
				if(null!=arr2&&arr2.length>0){
					for(String str2:arr2){
						if(StringUtils.isNotEmpty(str2)){
							String[] arr3=str2.split(splitstr);
							if(null!=arr3&&arr3.length>0){
								for(String str3:arr3){
									if(StringUtils.isNotEmpty(str3)&&str3.length()>1){
										slist.add(str3);
									}
								}
							}
						}
					}
				}
			}else{
				if(str.contains(splitstr)){
					
					String[] arr3=str.split(splitstr);
					if(null!=arr3&&arr3.length>0){
						for(String str3:arr3){
							if(StringUtils.isNotEmpty(str3)&&str3.length()>1){
								slist.add(str3);
							}
						}
					}
				}else
				{
					
				}
			}
			return slist;
		}
		
		return null;
	}
	
	
	
	/**
	 * 切割字符串list
	 * @param strlist 待切割的字符串list
	 * @param custr 切割的字符串 注意切割时的特殊字符的处理
	 * @return
	 */
	public static List<String> qiege(List<String> strlist,String custr){
		
		if(StringUtils.isEmpty(custr)||null==strlist){
			return null; 
		}
			
		List<String> list=new ArrayList<String>();
		for(String ss:strlist){
			if(StringUtils.isEmpty(ss)){
				continue;
			}
			String [] arr=ss.split(custr);//切割字符串
			if(null!=arr&&arr.length > 0){
				for(String str:arr){
					if(StringUtils.isEmpty(str)){
						continue;
					}
					list.add(str);
				}
			}
			
		}
		return list;
	}
	
	/**
	 * 
	 * @param strlist 待切割的字符串list
	 * @param culist 切割的字符串list 注意切割时的特殊字符的处理
	 * @return
	 */
	public static List<String> qiegelist(List<String> strlist,List<String> culist){
		
		if(null==culist||null==strlist){
			return null; 
		}
			
		List<String> list=strlist;
		int i=0;
		while (i<culist.size()) {
			
			String cu=culist.get(i);
			
			List<String> slist=qiege(list,cu);
			if(null!=slist&&slist.size() > 0){
				list=slist;
			}
			
			i++;
		}
		
		
		return list;
	}


	public static String strreplacejianjie(String str){
		
		if(StringUtils.isNotEmpty(str)){
			
			if(str.contains("||")){
				str=str.substring(str.indexOf("||")+2);
			}
		}
		return str;
	}
	/**
	 * 求两个数的百分比,保留两位小数
	 * @param num1  除数
	 * @param num2 被除数
	 * @return
	 */
	public static String Percentage(Integer num1,Integer num2){
		String result="";
		try {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			result = (numberFormat.format((float) num1 / (float) num2 * 100))+"%";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}
	
	/**
	 * 修改文件名称
	 * @param filePath
	 * @param newFileName
	 * @return
	 */
	public static String FileRenameTo(String filePath, String newFileName) {
        File f = new File(filePath);
        if (!f.exists()) { // 判断原文件是否存在
        	LogUtil.intoLog(OpenUtil.class,"原文件不存在，请注意--");
            return null;
        }
        
        newFileName = newFileName.trim();
        if ("".equals(newFileName) || newFileName == null) {
        	LogUtil.intoLog(OpenUtil.class,"文件名不能为空，请注意--");
            return null;
        }
        String newFilePath = null;
        if (f.isDirectory()) { // 判断是否为文件夹
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/"))+ "/"  + newFileName + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        if (!f.exists()) { // 判断需要修改为的文件是否存在（防止文件名冲突）
           return null;
        }
        
        try {
            f.renameTo(nf); // 修改文件名
        } catch(Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
	}
	/**
	 * 截取字符串末尾的标点符号
	 * @param str
	 * @return
	 */
	public static String subStringToEnd(String str) {
		if (str==null||str.length()<0) {
			return null;
		}
		boolean specialChar=true;
		while (true) {
			if (str.length()==1) {
				specialChar = OpenUtil.isSpecialChar(str);
				if (specialChar) {
					return null;
				}else{
					return str;
				}
			}
			String lastIndexStr=str.substring(str.length()-1,str.length());
				 specialChar = OpenUtil.isSpecialChar(lastIndexStr);
				 if (specialChar) {
					 str=str.substring(0, str.length()-1);
				}else{
					break;
				}
		}
		return str;
	}

	/**
	 *获取项目所在文件夹的路径
	 * @return
	 */
	public static String getXMSoursePath(){

		//当前项目下路径
		try {
			String filePath = System.getProperty("user.dir");

			if(filePath.endsWith("trm")||filePath.endsWith("TRM")){
				if(filePath.indexOf("/") > -1){//Linux截取
					filePath=filePath.substring(0,filePath.lastIndexOf("/"));//win的截取方式
				}else{
					filePath=filePath.substring(0,filePath.lastIndexOf("\\"));//win的截取方式
				}
			}

			LogUtil.intoLog(OpenUtil.class,filePath);
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *获取jdk/jre的路径
	 * @return
	 */
	public static String getJDKorJREPath(){

		String str=System.getProperty("java.home");
		if(null!=str&&(str.indexOf("jdk")>-1 || str.indexOf("jre")>-1 )){
			LogUtil.intoLog(OpenUtil.class,"----getJDKorJREPath str:"+str);

			try {
				File file = new File(str);
				boolean bool=file.canWrite();
				LogUtil.intoLog(OpenUtil.class,"----原始JDKorJREPath file.canWrite():"+bool);
				if(!bool){
					bool=file.setWritable(true);
					LogUtil.intoLog(OpenUtil.class,"----修改后JDKorJREPath file.setWritable():"+bool);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return str.endsWith("\\") ? (str+"bin\\"):(str+"\\bin\\");
		}
		return null;
	}


	/**
	 * 隐藏文件
	 * @param filepath
	 * @return
	 */
	public static boolean setFileHide(String filepath){

		if(StringUtils.isEmpty(filepath)){
			LogUtil.intoLog(OpenUtil.class,"setFileHide filepath is null");
			return false;
		}

		File file = new File(filepath);
		if(!file.exists()){
			LogUtil.intoLog(OpenUtil.class,"setFileHide file is null 文件不存在");
			return false;
		}

		try {
			Runtime runtime=Runtime.getRuntime();
			runtime.exec("attrib " + "\"" + file.getAbsolutePath() + "\""+ " +H");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}



	
	
	public static void main(String[] args) {


		LogUtil.intoLog(getfile_folder("d:\\ws\\12\\1/txt"));





	}
}
