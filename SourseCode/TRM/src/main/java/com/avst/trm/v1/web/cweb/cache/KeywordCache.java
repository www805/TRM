package com.avst.trm.v1.web.cweb.cache;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键字缓存
 */
public class KeywordCache {
    private static  List<Base_keyword> keywordList=null;

    private static boolean initCache_bool=false;



    public static synchronized List<Base_keyword> getKeywordList(){
        if(!initCache_bool){
            initCache();
        }
        return keywordList;
    }



    public static  synchronized boolean setKeyword(Base_keyword  keyword_){
        if(null==keywordList){
            return false;
        }
        try {
            if(null==keywordList){
                keywordList=new ArrayList<Base_keyword>();
            }
            for(int i=0;i<keywordList.size();i++){
                Base_keyword cacheParam=keywordList.get(i);
                if(cacheParam.getSsid().equals(keyword_.getSsid())){
                    keywordList.remove(i);
                    i--;
                }
            }
            keywordList.add(keyword_);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static boolean setKeywordList(List<Base_keyword> keywordList_) {
        try {
            if(null==keywordList){
                keywordList=new ArrayList<Base_keyword>();
            }

            if(null!=keywordList_&&keywordList_.size() > 0){
                keywordList=keywordList_;
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public synchronized static boolean delKeyword(String ssid){
        try {
            if(null!=keywordList&&keywordList.size() > 0){
                for(int i=0;i<keywordList.size();i++){
                    Base_keyword cacheParam=keywordList.get(i);
                    if(cacheParam.getSsid().equals(ssid)){
                        keywordList.remove(i);
                        i--;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public static synchronized boolean initCache(){
        Base_keywordMapper base_keywordMapper= SpringUtil.getBean(Base_keywordMapper.class);
        EntityWrapper keywords_ew=new EntityWrapper();
        List<Base_keyword> keywords= base_keywordMapper.selectList(keywords_ew);
        initCache_bool=true;
        if (null!=keywords&&keywords.size()>0){
          KeywordCache.setKeywordList(keywords);
            return true;
        }
        return false;
    }


}
