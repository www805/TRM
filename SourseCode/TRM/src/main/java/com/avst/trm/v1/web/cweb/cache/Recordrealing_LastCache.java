package com.avst.trm.v1.web.cweb.cache;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 笔录问答进行中【重置】保存当前的实时笔录以便于撤销操作
 */
public class Recordrealing_LastCache {
    private static Map<String, List<RecordToProblem>> recordreal_LastMap=null;//String：recordssid 笔录ssid  List<RecordToProblem>：笔录问答集合


    /**
     * 根据笔录ssid获取全部问答
     * @param recordssid
     * @return
     */
    public static synchronized List<RecordToProblem> getRecordreal_LastByRecordssid(String recordssid){
        if(null!=recordreal_LastMap&&recordreal_LastMap.containsKey(recordssid)){
            List<RecordToProblem> list=recordreal_LastMap.get(recordssid);
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
    public static  synchronized boolean setRecordreal_Last(String recordssid,List<RecordToProblem>  recordToProblems){
        if(null==recordreal_LastMap){
            recordreal_LastMap=new HashMap<String,List<RecordToProblem>>();
        }
        if(recordreal_LastMap.containsKey(recordssid)){//应该不会有的才是
            recordreal_LastMap.remove(recordssid);
        }
        recordreal_LastMap.put(recordssid,recordToProblems);
        return true;
    }

    /**
     * 删除缓存数据
     * @param recordssid
     * @return
     */
    public static synchronized  boolean delRecordreal(String recordssid){
        if(null!=recordreal_LastMap&&recordreal_LastMap.containsKey(recordssid)){
            recordreal_LastMap.remove(recordssid);
            return true;
        }
        return false;
    }
}
