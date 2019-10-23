package com.avst.trm.v1.common.util.gzip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GZIPThread extends Thread{

    private String folderPath;//单笔录，单文件夹打包
    private List<String> folderPathList;  //一个案件，多笔录，多个文件夹打包
    private String targzipFilePath;//打包之后压缩包所在位置的文件夹的路径
    private String targzipFileName;//打包之后压缩包的名字
    private String ssid;//从笔录里面下载就是iid，从案件里面下载就是案件ssid
    private String gztype=".zip";//打包的格式

    private String notGZType="ts";//不需要打包的文件类型

    private boolean mustgzip=false;//强制必须打包，有打包就删掉，重新打包

    /**
     * 单文件夹 带不需要打包的文件类型 的打包
     * @param folderPath
     * @param targzipFilePath
     * @param ssid
     * @param targzipFileName
     * @param gztype
     * @param notGZType
     */
    public  GZIPThread(String folderPath,String targzipFilePath,String ssid,String targzipFileName,String gztype,String notGZType,boolean mustgzip){
        this.folderPath=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.ssid=ssid;
        this.notGZType=notGZType;
        this.mustgzip=mustgzip;
    }

    /**
     * 多文件夹 带不需要打包的文件类型 的打包
     * @param folderPath
     * @param targzipFilePath
     * @param ssid
     * @param targzipFileName
     * @param gztype
     * @param notGZType
     */
    public  GZIPThread(List<String> folderPath,String targzipFilePath,String ssid,String targzipFileName,String gztype,String notGZType,boolean mustgzip){
        this.folderPathList=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.ssid=ssid;
        this.notGZType=notGZType;
        this.mustgzip=mustgzip;
    }

    /**
     * 单文件夹 默认不需要打包的文件类型 的打包
     * @param folderPath
     * @param targzipFilePath
     * @param ssid
     * @param targzipFileName
     * @param gztype
     */
    public  GZIPThread(String folderPath,String targzipFilePath,String ssid,String targzipFileName,String gztype,boolean mustgzip){
        this.folderPath=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.ssid=ssid;
        this.mustgzip=mustgzip;
    }

    /**
     * 多文件夹 默认不需要打包的文件类型 的打包
     * @param folderPath
     * @param targzipFilePath
     * @param ssid
     * @param targzipFileName
     * @param gztype
     */
    public  GZIPThread(List<String> folderPath,String targzipFilePath,String ssid,String targzipFileName,String gztype,boolean mustgzip){
        this.folderPathList=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.ssid=ssid;
        this.mustgzip=mustgzip;
    }


    @Override
    public void run() {

        GZIPCache.setGZIPThread(this,ssid);

        System.out.println(targzipFileName+" GZIPThread is start,"+new Date().getTime());
        try {
            if(null!=folderPathList&&folderPathList.size() > 0){

                GZIPUtil.CompressedFiles_Gzip(folderPathList,targzipFilePath,ssid,targzipFileName,gztype,notGZType,mustgzip);
            }else{

                GZIPUtil.CompressedFiles_Gzip(folderPath,targzipFilePath,ssid,targzipFileName,gztype,notGZType,mustgzip);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(targzipFileName+" GZIPThread is end,"+new Date().getTime());

        GZIPCache.delGzipThread(ssid);

    }

    public static void main(String[] args) {

        List<String> pathlist=new ArrayList<>();
        pathlist.add("D:\\ftpdata\\sb3\\2019-09-16\\88d0ef57a91e4450bdae8432329b7f82_sxsba2");
        pathlist.add("D:\\ftpdata\\sb3\\2019-09-16\\saKV8j12GikR");

        GZIPThread thread=new GZIPThread("D:\\ftpdata\\sb3\\2019-09-16\\88d0ef57a91e4450bdae8432329b7f82_sxsba2","D:\\ftpdata\\sb3\\2019-09-16\\88d0ef57a91e4450bdae8432329b7f82_sxsba2","谈话案件","谈话案件",".zip","cs",false);
        thread.start();

    }
}
