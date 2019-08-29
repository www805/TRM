package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.RecordStatusCacheParam;
import java.util.*;

/**
 * 笔录状态缓存
 */
public class RecordStatusCache {

    private static List<RecordStatusCacheParam> recordStatusCache = new ArrayList<>();

    /**
     * 获取所有笔录状态缓存
     * @return
     */
    public static List<RecordStatusCacheParam> getRecordStatusCacheParam() {
        if(null == recordStatusCache){
            recordStatusCache = new ArrayList<>();
        }
        return recordStatusCache;
    }

    /**
     * 设置笔录状态缓存
     * @param recordStatusCacheParam
     */
    public static void setRecordStatusCache(RecordStatusCacheParam recordStatusCacheParam) {

        boolean status = false;

        List<RecordStatusCacheParam> list = RecordStatusCache.getRecordStatusCacheParam();
        for (RecordStatusCacheParam recordStatusCache : list) {
            if (recordStatusCacheParam.getRecordssid().equals(recordStatusCache.getRecordssid())) {
                recordStatusCache.setLasttime(recordStatusCacheParam.getLasttime());
                status = true;
                break;
            }
        }

        if (status == false) {
            RecordStatusCache.getRecordStatusCacheParam().add(recordStatusCacheParam);
        }

    }

    /**
     * 移除某个
     * @param ssid
     */
    public static void removeRecordInfoCache(String ssid){
        List<RecordStatusCacheParam> mapList = RecordStatusCache.recordStatusCache;

        Iterator<RecordStatusCacheParam> iterator = mapList.iterator();
        while (iterator.hasNext()) {
            RecordStatusCacheParam param = iterator.next();
            if (param.getRecordssid().equals(ssid)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 清空所有
     */
    public static void delRecordInfoCache(){
        recordStatusCache = null;
    }

}
