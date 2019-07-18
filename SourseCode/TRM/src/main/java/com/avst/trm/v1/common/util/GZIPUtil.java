package com.avst.trm.v1.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
/**
 * @Title: GZIPUtil.java
 * @Description: gzip文件压缩和解压缩工具类
 */
public class GZIPUtil {


        public static void main(String[] args) {
            CompressedFiles_Gzip("I:\\wubin\\笔录管理系统\\服务安装\\桌面式应用\\客户端\\","I:\\wubin\\笔录管理系统\\服务安装\\桌面式应用\\client","client");
        }


    /**
     * 压缩文件成Gzip格式
     * 压缩文件夹生成后缀名为".gz"的文件并下载
     * @param folderPath,要压缩的文件夹的路径
     * @param targzipFilePath,压缩后文件的路径
     * @param targzipFileName,压缩后文件的名称
     * */
    public static boolean CompressedFiles_Gzip(String folderPath, String targzipFilePath, String targzipFileName)
    {
        if(folderPath.endsWith("/")||folderPath.endsWith("\\")){
            folderPath=folderPath.substring(0,folderPath.length()-1);
        }
        File srcPath =new File(folderPath);
        String zippath="";
        if(folderPath.indexOf("/")> -1){
            zippath=folderPath.substring(folderPath.lastIndexOf("/"))+"/";
        }else{
            zippath=folderPath.substring(folderPath.lastIndexOf("\\")+1)+"\\\\";
        }

        boolean bool=FileUtil.checkfilepath(targzipFilePath);
        if(!bool){
            LogUtil.intoLog(4,GZIPUtil.class,"FileUtil.checkfilepath IS FALSE,文件检测失败不允许压缩");
            return false;
        }

        List<String> filelist=FileUtil.getAllFilePath(folderPath,1);
        byte[] buf = new byte[1024]; //设定读入缓冲区尺寸
        try
        {
            //建立压缩文件输出流
            FileOutputStream fout=new FileOutputStream(targzipFilePath);
            //建立tar压缩输出流
            TarArchiveOutputStream tout=new TarArchiveOutputStream(fout);
            for(String path:filelist)
            {
                File file=new File(path);
                if(file.isDirectory()){
                    System.out.println(path+":path,path is 文件夹");
                    continue;
                }
                //打开需压缩文件作为文件输入流
                FileInputStream fin=new FileInputStream(path);   //filename是文件全路径
                TarArchiveEntry tarEn=new TarArchiveEntry(file); //此处必须使用new TarEntry(File file);
                String[] arr=path.split(zippath);
                if(arr.length >1){
                    tarEn.setName(arr[1]);  //此处需重置名称，默认是带全路径的，否则打包后会带全路径
                }else{
                    tarEn.setName(file.getName());  //此处需重置名称，默认是带全路径的，否则打包后会带全路径
                }

                tout.putArchiveEntry(tarEn);
                int num;
                while ((num=fin.read(buf, 0, 1024)) != -1)
                {
                    tout.write(buf,0,num);
                }
                tout.closeArchiveEntry();
                fin.close();
            }
            tout.close();
            fout.close();

            //建立压缩文件输出流
            FileOutputStream gzFile=new FileOutputStream(targzipFilePath+".tar.gz");
            //建立gzip压缩输出流
            GZIPOutputStream gzout=new GZIPOutputStream(gzFile);
            //打开需压缩文件作为文件输入流
            FileInputStream tarin=new FileInputStream(targzipFilePath);   //targzipFilePath是文件全路径
            int len;
            while ((len=tarin.read(buf, 0, 1024)) != -1)
            {
                gzout.write(buf,0,len);
            }
            gzout.close();
            gzFile.close();
            tarin.close();

            try {
                File f = new File(targzipFilePath);
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;

        }catch(FileNotFoundException e)
        {
            System.out.println(e);
        }catch(IOException e)
        {
            System.out.println(e);
        }

        return false;
    }


}
