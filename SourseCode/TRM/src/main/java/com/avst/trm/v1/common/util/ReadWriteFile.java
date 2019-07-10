package com.avst.trm.v1.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;




/**
 */
public class ReadWriteFile {
    
    
    private static BufferedReader bufread=null;


	/**
     * 读取文件文本
     * 
     */
    public static String readTxtFileToStr(String path){
        String read="";
        String readStr=new String();
        FileReader fileread=null;
        try {
        	File filenamepath = new File(path);
        	if(!filenamepath.exists()  && !filenamepath.isDirectory()){
        		return null;
        	}
            fileread = new FileReader(filenamepath);
            BufferedReader bufread = new BufferedReader(fileread);
            try {
                while ((read = bufread.readLine()) != null) {
                    readStr = readStr + read+ "\r\n";
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
            	try {
					bufread.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
        	try {
				fileread.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		String rr=readStr.toString();
		return rr.endsWith("\r\n")?rr.substring(0,rr.lastIndexOf("\r\n")):rr;
    }
    
    /**
     * 读取文件文本
     * 
     */
    public static List<String> readTxtFileToList(String path,String code){
        String read="";
        InputStream input = null;
        InputStreamReader inp=null;
        try {
        	List<String> readList=new ArrayList<String>();	
        	LogUtil.intoLog(ReadWriteFile.class,"文件路径为："+path);
            
            
            input = new FileInputStream(path);
            inp=new InputStreamReader(input,code);//字节流字符流转化的桥梁

            bufread = new BufferedReader(inp);//以字符流方式读入
            
            try {
                while ((read = bufread.readLine()) != null) {
                	read=read.trim();
                	readList.add(read);
                }
                
                return readList;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
            	bufread.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		input.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        return null;
    }
    
    /**
     * 读取文件文本
     * 返回字符串带txt换行符号
     */
    public static String readTxtFileToStr(String path,String code){
        String read="";
        InputStream input = null;
        InputStreamReader inp=null;
        try {
        	LogUtil.intoLog(ReadWriteFile.class,"文件路径为："+path);
            
            
            input = new FileInputStream(path);
            inp=new InputStreamReader(input,code);//字节流字符流转化的桥梁
            bufread = new BufferedReader(inp);//以字符流方式读入
            try {
            	  StringBuffer str=new StringBuffer();
                while ((read = bufread.readLine()) != null) {
                	read=read.trim();
                	if (read.length()>1) {
                		str.append(read+ "\r\n");
					}
                }
                String rr=str.toString();
                return rr.endsWith("\r\n")?rr.substring(0,rr.lastIndexOf("\r\n")):rr;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
            	bufread.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		input.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        return null;
    }
    
    public static List<String> readTxtFileToList(InputStream inputStream,String code,Integer tm,Integer showFrontRow){
        String read="";
        InputStream input = inputStream;
        InputStreamReader inp=null;
        try {
        	List<String> readList=new ArrayList<String>();	
            inp=new InputStreamReader(input,code);//字节流字符流转化的桥梁
            bufread = new BufferedReader(inp);//以字符流方式读入
            try {
            	//1表示以不转码的方式获取十条数据
            	if (tm==1) {
            		Integer index=0;
            		while ((read = bufread.readLine()) != null&&index<showFrontRow) {
                    	read=read.trim();
                    	readList.add(read);
                    	index++;
                    }
				}
            	//3表示以不转码的方式获取所有数据
            	else if(tm==3){
					while ((read = bufread.readLine()) != null) {
                    	read=read.trim();
                    	readList.add(read);
                    }
				}
                
                return readList;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
            	bufread.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		input.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        return null;
    }
    
    /**
     * 追加数据
     * 
     */
    public static boolean writeApptoTxtFile(String newStr,String path) throws IOException{
        //LogUtil.intoLog(ReadWriteFile.class,"进入写文件"+newStr);
    	
    	String filepath="";
		try {
			filepath = path.substring(0, path.lastIndexOf("/"));
		} catch (Exception e1) {
			filepath=path.substring(0, path.lastIndexOf("\\"));
		}
    	File filenamepath = new File(filepath);
    	if(!filenamepath.exists()  && !filenamepath.isDirectory()){
    		LogUtil.intoLog(ReadWriteFile.class,path+"//注意，读取的是一个不存在的地址文件，启动创建");
    		
			boolean bool=filenamepath.mkdirs();  
			if(!bool){
				boolean bool2=filenamepath.mkdirs();
				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
				LogUtil.intoLog(ReadWriteFile.class,"                      ");
				LogUtil.intoLog(ReadWriteFile.class,"第一次创建文件夹失败，第二次失败："+bool2);
				LogUtil.intoLog(ReadWriteFile.class,"                      ");
				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
			}
    	}
    	
    	File f = new File(path);    
        if (f.exists()) {    
            System.out.print("文件存在");    
        } else {    
            System.out.print("文件不存在");    
            f.createNewFile();// 不存在则创建    
        } 
        try {
        	
        	BufferedWriter output = new BufferedWriter(new FileWriter(f,true));
			try {
				output.write(newStr);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				output.close();
			}
			return true;
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
        return false;
    }
    
    /**
     * 写数据
     * 
     */
    public static boolean writeTxtFile(String newStr,String path) {
        //LogUtil.intoLog(ReadWriteFile.class,"进入写文件"+newStr);
        try {
        	String filepath="";
			try {
				filepath = path.substring(0, path.lastIndexOf("/"));
			} catch (Exception e1) {
				filepath=path.substring(0, path.lastIndexOf("\\"));
			}
        	File filenamepath = new File(filepath);
        	if(!filenamepath.exists()  && !filenamepath.isDirectory()){
        		LogUtil.intoLog(ReadWriteFile.class,path+"//注意，读取的是一个不存在的地址文件，启动创建");

    			boolean bool=filenamepath.mkdirs();
    			if(!bool){
    				boolean bool2=filenamepath.mkdirs();
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"第一次创建文件夹失败，第二次失败："+bool2);
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    			}
        	}
			FileOutputStream out = new FileOutputStream(path,false);
			try {
				out.write(new String("utf-8").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}

			File f = new File(path);
        	FileWriter fw=null;
			try {
				fw =  new FileWriter(f);
				fw.write(newStr);

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(null!=fw){
					fw.flush();
					fw.close();
				}


			}

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }
    
    
    
    /**
     * 写数据,自身提供写入的编码格式
     * 
     */
    public static boolean writeTxtFile(String newStr,String path,String code){
        //LogUtil.intoLog(ReadWriteFile.class,"进入写文件"+newStr);
		FileOutputStream out=null;
		FileWriter fw=null;
        try {
        	String filepath="";
			try {
				filepath = path.substring(0, path.lastIndexOf("/"));
			} catch (Exception e1) {
				filepath=path.substring(0, path.lastIndexOf("\\"));
			}
        	File filenamepath = new File(filepath);

        	if(!filenamepath.exists()  && !filenamepath.isDirectory()){
        		LogUtil.intoLog(ReadWriteFile.class,path+"//注意，读取的是一个不存在的地址文件，启动创建");

				boolean bool=filenamepath.mkdirs();
    			if(!bool){
    				boolean bool2=filenamepath.mkdirs();
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"第一次创建文件夹失败，第二次失败："+bool2);
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    			}
        	}

        	File filename = new File(path);
        	if (!filename.exists()&& !filename.isDirectory()) {
        		try {
					filename.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        	 out = new FileOutputStream(filename,false);
        	if(StringUtils.isEmpty(code)){
        		code="utf-8";
        	}
			try {
				out.write(new String(code).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}


			File f = new File(path);
        	fw =  new FileWriter(f);

			try {
				fw.write(newStr);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fw.flush();
				fw.close();
			}
			return true;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * 写数据//转码
     * 
     */
    public static boolean writeTxtFile_gbk(String newStr,String path) {
        LogUtil.intoLog(ReadWriteFile.class,"进入写文件"+newStr);
        try {
        	String filepath="";
			try {
				filepath = path.substring(0, path.lastIndexOf("/"));
			} catch (Exception e1) {
				filepath=path.substring(0, path.lastIndexOf("\\"));
			}
        	File filenamepath = new File(filepath);
        	if(!filenamepath.exists()  && !filenamepath.isDirectory()){
        		LogUtil.intoLog(ReadWriteFile.class,path+"//注意，读取的是一个不存在的地址文件，启动创建");
        		
    			boolean bool=filenamepath.mkdirs();  
    			if(!bool){
    				boolean bool2=filenamepath.mkdirs();
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"第一次创建文件夹失败，第二次失败："+bool2);
    				LogUtil.intoLog(ReadWriteFile.class,"                      ");
    				LogUtil.intoLog(ReadWriteFile.class,"------------------------");
    			}
        	}
        	
        	File filename = new File(path);
        	
        	if (!filename.exists()&& !filename.isDirectory()) {
        		try {
        			
					boolean bool_add=filename.createNewFile();
					LogUtil.intoLog(ReadWriteFile.class,"新增文件 bool_add:"+bool_add);
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        	FileOutputStream fos = new FileOutputStream(path);   
        	
        	if(null!=newStr&&!newStr.trim().equals("")){
        		newStr=newStr.trim();
        		FileOutputStream out = new FileOutputStream(filename,false);
				try {
					out.write(new String("GBK").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					out.flush();
					out.close();
				}


				File f = new File(path);
        		FileWriter fw =  new FileWriter(f);

				try {
					fw.write(newStr);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					fw.flush();
					fw.close();
				}

				fos.close();
        	}
        	
        	return true;
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
        return false;
    }
    
	public static boolean updateTxtToPath(String path,String txt){
		try {
			File file=new File(path);
			if (OpenUtil.fileisexist(path)) {
				FileWriter fw = new FileWriter(file);
				// 清除该文件的所有内容
				try {
					fw.write(txt);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					fw.flush();
					fw.close();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}
		LogUtil.intoLog(ReadWriteFile.class,"文件出错");
		return false;
	}
    
    
    /**
     * main
     * @param s
     * @throws IOException
     */
    public static void main(String[] s) throws IOException {

//    	writeTxtFile("12346","H:\\gitspace\\TRM\\SourseCode\\ceshi.txt");
		writeTxtFile("12346","H:\\gitspace\\TRM\\SourseCode\\java.txt");
    }


}
