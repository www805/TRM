package com.avst.trm.v1.common.util.gzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
/**
 * @Title: GZIPUtil.java
 * @Description: gzip文件压缩和解压缩工具类
 */
public class GZIPUtil {


        public static void main(String[] args) {
            System.out.println(new Date().getTime());
            CompressedFiles_Gzip("D:\\ftpdata\\sb3\\2019-09-02\\a08a1f4d944b489fa10dfc3eb5212b48_sxsba2\\","D:\\ftpdata\\sb3\\2019-09-02\\a08a1f4d944b489fa10dfc3eb5212b48_sxsba2\\","a08a1f4d944b489fa10dfc3eb5212b48_sxsba2","测试打包",".zip","ts",false);
            System.out.println(new Date().getTime());
        }


    /**
     * 压缩文件成Gzip格式
     * 压缩文件夹生成后缀名为".gz"的文件并下载
     * @param folderPath,要压缩的文件夹的路径
     * @param targzipFilePath,压缩后文件的路径
     * @param targzipFileName,压缩后文件的名称
     * @param gztype 压缩格式
     * @param ssid 缓存中的唯一标识
     * @param notGZType 不打包的格式
     * */
    public static boolean CompressedFiles_Gzip(String folderPath, String targzipFilePath,String ssid, String targzipFileName,String gztype,String notGZType,boolean mustgzip)
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

        boolean bool= FileUtil.checkfilepath(targzipFilePath);
        if(!bool){
            LogUtil.intoLog(4,GZIPUtil.class,"FileUtil.checkfilepath IS FALSE,文件检测失败不允许压缩");
            return false;
        }

        List<String> filelist=FileUtil.getAllFilePath(folderPath,1);


        if(null==filelist||filelist.size()==0){
            LogUtil.intoLog(4,GZIPUtil.class,"打包的文件夹下一个文件都没有");
            return false;
        }

        Map<String , String> map=new HashMap<String , String>();
        for(String newfile:filelist){
            map.put(newfile,targzipFileName+"\\1");//打包压缩的文件的相对路径
        }
        return gzip(filelist,map,targzipFilePath,ssid,targzipFileName,gztype,notGZType,mustgzip);
    }

    /**
     *
     * @param filelist
     * @param folderMap 每个文件对应的存放的文件夹
     * @param targzipFilePath
     * @param ssid 缓存中的唯一标识
     * @param targzipFileName
     * @param gztype 压缩格式
     * @param notGZType 不打包的格式
     * @return
     */
    public static boolean gzip(List<String> filelist,Map<String,String> folderMap,String targzipFilePath,String ssid, String targzipFileName,String gztype,String notGZType,boolean mustgzip){

        //打包逻辑
        final String zipfilename=targzipFilePath;
        byte[] buf = new byte[1024]; //设定读入缓冲区尺寸
        try
        {

            if(targzipFilePath.endsWith("/")){
                targzipFilePath=targzipFilePath.substring(0,targzipFilePath.length()-1)+"/"+targzipFileName;
            }else if(targzipFilePath.endsWith("\\")){
                targzipFilePath=targzipFilePath.substring(0,targzipFilePath.length()-1)+"\\\\"+targzipFileName;
            }else{
                if(targzipFilePath.indexOf("/") > -1){
                    targzipFilePath+="/"+targzipFileName;
                }else{
                    targzipFilePath+="\\\\"+targzipFileName;
                }
            }

            targzipFilePath+=gztype;
            File filezip=new File(targzipFilePath);
            if(filezip.exists()&&!filezip.isDirectory()){

                if(mustgzip){//需要强制删除，重新打包
                    try {
                        filezip.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    LogUtil.intoLog(1,GZIPUtil.class,"压缩文件已存在，targzipFileName："+targzipFileName);
                    return true;
                }

            }


            //写入缓存
            GZIPCacheParam gzipCacheParam=new GZIPCacheParam();
            Map<String, Boolean> filepathMap=new HashMap<String, Boolean>();
            List<String> pathlist=new ArrayList<String>();
            for(String path:filelist){
                if(null==notGZType||notGZType.trim().equals("")||!path.endsWith(notGZType)){
                    pathlist.add(path);
                    filepathMap.put(path,false);
                }
            }
            gzipCacheParam.setFilepathMap(filepathMap);
            gzipCacheParam.setStartTime(new Date().getTime());
            gzipCacheParam.setTotalzipnum(pathlist.size());
            gzipCacheParam.setZipfilename(zipfilename);
            GZIPCache.setGzipCacheParam(gzipCacheParam,ssid);

            //建立压缩文件输出流
            FileOutputStream fout=new FileOutputStream(targzipFilePath);
            //建立tar压缩输出流
            TarArchiveOutputStream tout=new TarArchiveOutputStream(fout);
            try {
                for(String path:pathlist)
                {

                    File file=new File(path);
                    if(file.isDirectory()){
                        System.out.println(path+":path,path is 文件夹");
                        continue;
                    }
                    //打开需压缩文件作为文件输入流
                    FileInputStream fin=new FileInputStream(path);   //filename是文件全路径
                    TarArchiveEntry tarEn=new TarArchiveEntry(file); //此处必须使用new TarEntry(File file);

                    //带当前文件夹路径的打包
                    tarEn.setName(folderMap.get(path)+"\\"+file.getName());  //此处需重置名称，默认是带全路径的，否则打包后会带全路径


                    tout.putArchiveEntry(tarEn);
                    int num;
                    while ((num=fin.read(buf, 0, 1024)) != -1)
                    {
                        tout.write(buf,0,num);
                    }
                    tout.closeArchiveEntry();
                    try {
                        fin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //更新打包缓存
                    GZIPCache.updateGzipState(path,ssid);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    tout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //删除打包线程记录缓存
                GZIPCache.delGzipCacheParam(ssid);
            }

            return true;

        }catch(FileNotFoundException e)
        {
            System.out.println(e);
        }catch(Exception e)
        {
            System.out.println(e);
        }finally {
            //删除打包线程记录缓存
            GZIPCache.delGzipCacheParam(zipfilename);
        }
        return false;

    }

    /**
     * 打包多个文件夹
     * 压缩文件成Gzip格式
     * 压缩文件夹生成后缀名为".gz"的文件并下载
     * @param folderPathList,全部要压缩的文件夹的路径的集合，多个文件夹压缩到一起
     * @param targzipFilePath,压缩后文件的路径
     * @param targzipFileName,压缩后文件的名称
     * @param gztype 压缩格式
     * @param notGZType 不打包的格式
     * @param ssid 缓存中的唯一标识
     * */
    public static boolean CompressedFiles_Gzip(List<String> folderPathList, String targzipFilePath,String ssid, String targzipFileName,String gztype,String notGZType,boolean mustgzip)
    {
        List<String> filelist=new ArrayList<String>();
        int foldernum=1;
        Map<String,String> map=new HashMap<String,String>();
        for(String folderPath:folderPathList){


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

            boolean bool= FileUtil.checkfilepath(targzipFilePath);
            if(!bool){
                LogUtil.intoLog(4,GZIPUtil.class,"FileUtil.checkfilepath IS FALSE,文件检测失败不允许压缩");
                return false;
            }

            List<String> filelistnew=FileUtil.getAllFilePath(folderPath,1);
            if(null!=filelistnew&&filelistnew.size() > 0){
                for(String newfile:filelistnew){
                    map.put(newfile,targzipFileName+"\\"+foldernum);//打包压缩的文件的相对路径
                }
                filelist.addAll(filelistnew);
            }
            foldernum++;

        }

        if(null==filelist||filelist.size()==0){
            LogUtil.intoLog(4,GZIPUtil.class,"打包的文件夹下一个文件都没有");
            return false;
        }
        return gzip(filelist,map,targzipFilePath,ssid,targzipFileName,gztype,notGZType,mustgzip);
    }

}
