package com.avst.trm.v1.common.util.gzip;

import java.util.Date;

public class GZIPThread extends Thread{

    private String folderPath;
    private String targzipFilePath;
    private String targzipFileName;
    private String iid;
    private String gztype=".zip";

    private String notGZType="ts";//不需要打包的文件类型

    public  GZIPThread(String folderPath,String targzipFilePath,String iid,String targzipFileName,String gztype,String notGZType){
        this.folderPath=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.iid=iid;
        this.notGZType=notGZType;
    }

    public  GZIPThread(String folderPath,String targzipFilePath,String iid,String targzipFileName,String gztype){
        this.folderPath=folderPath;
        this.targzipFilePath=targzipFilePath;
        this.targzipFileName=targzipFileName;
        this.gztype=gztype;
        this.iid=iid;
    }


    @Override
    public void run() {

        GZIPCache.setGZIPThread(this,iid);

        System.out.println(targzipFileName+" GZIPThread is start,"+new Date().getTime());
        try {
            GZIPUtil.CompressedFiles_Gzip(folderPath,targzipFilePath,iid,targzipFileName,gztype,notGZType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(targzipFileName+" GZIPThread is end,"+new Date().getTime());

        GZIPCache.delGzipThread(iid);

    }
}
