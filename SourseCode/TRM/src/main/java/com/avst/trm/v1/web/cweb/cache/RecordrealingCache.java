package com.avst.trm.v1.web.cweb.cache;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 笔录问答进行中ing实时记录
 */
public class RecordrealingCache {
    private static Map<String, List<RecordToProblem>> recordrealMap=null;//String：recordssid 笔录ssid  List<RecordToProblem>：笔录问答集合

    private static boolean initCache_bool=false;

    /**
     * 根据笔录ssid获取全部问答
     * @param recordssid
     * @return
     */
    public static synchronized List<RecordToProblem> getRecordrealByRecordssid(String recordssid){

        if(!initCache_bool){
            initCache();
        }
        if(null!=recordrealMap&&recordrealMap.containsKey(recordssid)){
            List<RecordToProblem> list=recordrealMap.get(recordssid);
            if(null!=list&&list.size() > 0){
                return list;
            }
        }
        return null;
    }


    /**
     * 往缓存里面加入新问答
     * @param recordssid
     * @param recordToProblems
     * @return
     */
    public static  synchronized boolean setRecordreal(String recordssid,List<RecordToProblem>  recordToProblems){
        if(null==recordrealMap){
            recordrealMap=new HashMap<String,List<RecordToProblem>>();
        }
        if(recordrealMap.containsKey(recordssid)){//应该不会有的才是
            recordrealMap.remove(recordssid);
        }
        recordrealMap.put(recordssid,recordToProblems);
        return true;
    }

    /**
     * 删除缓存数据
     * @param recordssid
     * @return
     */
    public static synchronized  boolean delRecordreal(String recordssid){
        if(null!=recordrealMap&&recordrealMap.containsKey(recordssid)){
            recordrealMap.remove(recordssid);
            return true;
        }
        return false;
    }

    public static synchronized boolean initCache(){
        RecordService recordService= SpringUtil.getBean(RecordService.class);
        List<Record> records = recordService.initRecordrealingCache();
        initCache_bool=true;
        if (null!=records&&records.size()>0){
            recordrealMap=new HashMap<String,List<RecordToProblem>>();
            LogUtil.intoLog(RecordrealingCache.class,"------RecordrealingCache-----initCache------笔录个数"+records.size());
            for (Record record : records) {
                recordrealMap.put(record.getSsid(),record.getProblems());
            }
            return true;
        }else {
            LogUtil.intoLog(RecordrealingCache.class,"------RecordrealingCache-----initCache------没有找到进行中的笔录"+records.size());
        }
        return false;
    }
}
