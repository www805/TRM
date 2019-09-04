package com.avst.trm.v1.common.util.gzip;

import java.util.HashMap;
import java.util.Map;

public class GZIPCache {

    private static Map<String ,GZIPCacheParam> gzipCacheMap;

    public synchronized  static GZIPCacheParam getGzipCacheParam(String iid){
        if(null!=gzipCacheMap&&gzipCacheMap.containsKey(iid)){
            return gzipCacheMap.get(iid);
        }
        return null;
    }

    public synchronized  static void setGzipCacheParam(GZIPCacheParam gzipCacheParam,String iid){

        if(null!=gzipCacheMap&&gzipCacheMap.containsKey(iid)){
        }else if(null==gzipCacheMap){
            gzipCacheMap=new HashMap<String ,GZIPCacheParam>();
            gzipCacheMap.put(iid,gzipCacheParam);
        }else{
            gzipCacheMap.put(iid,gzipCacheParam);
        }
    }

    /**
     * 更新已打包的文件数
     * @param filepath
     * @param iid
     */
    public synchronized  static void updateGzipState(String filepath,String iid){

        if(null!=gzipCacheMap&&gzipCacheMap.containsKey(iid)){
            GZIPCacheParam gzipCacheParam=gzipCacheMap.get(iid);
            Map<String ,Boolean> filepathMap=gzipCacheParam.getFilepathMap();
            if(null!=filepathMap&&filepathMap.containsKey(filepath)){
                filepathMap.put(filepath,true);
                gzipCacheParam.setFilepathMap(filepathMap);
                gzipCacheParam.setOverzipnum(gzipCacheParam.getOverzipnum()+1);
                gzipCacheMap.put(iid,gzipCacheParam);
            }
        }
    }

    public synchronized  static boolean delGzipCacheParam(String iid){

        if(null!=gzipCacheMap&&gzipCacheMap.containsKey(iid)){
            gzipCacheMap.remove(iid);
            return true;
        }
        return false;
    }


    private static Map<String , GZIPThread> gzipThreadMap=new HashMap<String , GZIPThread>();


    public synchronized  static GZIPThread getGZIPThread(String iid){
        if(null!=gzipThreadMap&&gzipThreadMap.containsKey(iid)){
            return gzipThreadMap.get(iid);
        }
        return null;
    }

    public synchronized  static void setGZIPThread(GZIPThread paran,String iid){

        if(null==gzipThreadMap){
            gzipThreadMap=new HashMap<String ,GZIPThread>();
        }
        gzipThreadMap.put(iid,paran);

    }


    public synchronized  static boolean delGzipThread(String iid){

        if(null!=gzipThreadMap&&gzipThreadMap.containsKey(iid)){
            gzipThreadMap.remove(iid);
            return true;
        }
        return false;
    }
}
