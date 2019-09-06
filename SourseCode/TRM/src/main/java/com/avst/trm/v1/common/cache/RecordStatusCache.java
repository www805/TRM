package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.RecordStatusCacheParam;
import java.util.*;

/**
 * 笔录状态缓存
 */
public class RecordStatusCache {

    private static List<RecordStatusCacheParam> recordStatusCache = null;

    /**
     * 获取所有笔录状态缓存
     * @return
     */
    public static List<RecordStatusCacheParam> getRecordStatusCacheParam() {
        synchronized(RecordStatusCache.class){
            return recordStatusCache;
        }
    }

    /**
     * 设置笔录状态缓存
     * @param recordStatusCacheParam
     */
    public static void setRecordStatusCache(RecordStatusCacheParam recordStatusCacheParam) {
        synchronized(RecordStatusCache.class){
            if(null==recordStatusCache||recordStatusCache.size() == 0){
                recordStatusCache=new ArrayList<RecordStatusCacheParam>();
                recordStatusCache.add(recordStatusCacheParam);
            }else{
                for (RecordStatusCacheParam recordStatusCache : recordStatusCache) {
                    if (recordStatusCacheParam.getRecordssid().equals(recordStatusCache.getRecordssid())) {
                        recordStatusCache.setLasttime(recordStatusCacheParam.getLasttime());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 移除某个
     * @param ssid
     */
    public static void removeRecordInfoCache(String ssid){
        synchronized(RecordStatusCache.class){

            if(null==recordStatusCache||recordStatusCache.size() == 0){
                return ;
            }
            Iterator<RecordStatusCacheParam> iterator = recordStatusCache.iterator();
            while (iterator.hasNext()) {
                RecordStatusCacheParam param = iterator.next();
                if (param.getRecordssid().equals(ssid)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 清空所有
     */
    public static synchronized void delRecordInfoCache(){
        synchronized(RecordStatusCache.class){
            recordStatusCache = null;
        }
    }

}
