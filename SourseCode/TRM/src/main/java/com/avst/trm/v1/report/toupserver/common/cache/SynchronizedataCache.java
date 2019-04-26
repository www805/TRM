package com.avst.trm.v1.report.toupserver.common.cache;

import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_1_Param;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 其实系统同步数据一般只会有一个，这里可以不用map，
 * 但是为了以后拓展还是用map的形式
 * toupserverTBToken同步的token当key
 */
public class SynchronizedataCache {


    /**
     * 数据同步的缓存
     */
    private static Map<String, StartSynchronizedata_1_Param> synchronizeDataMap;

    public synchronized  static StartSynchronizedata_1_Param getSynchronizeData(String toupserverTBToken){

        if(null!=synchronizeDataMap&&synchronizeDataMap.containsKey(toupserverTBToken)){
            return synchronizeDataMap.get(toupserverTBToken);
        }
        return null;
    }

    /**
     * 初始化本次同步的缓存
     * @param toupserverTBToken
     * @param data
     * @return
     */
    public synchronized static boolean initSynchronizedataBySortnum(String toupserverTBToken,StartSynchronizedata_1_Param data){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        synchronized(synchronizeDataMap){
            synchronizeDataMap.put(toupserverTBToken,data);
        }
        return true;
    }



    /**
     * 往同步的缓存中添加数据
     * @param toupserverTBToken
     * @param param
     * @return
     */
    public synchronized static boolean addSynchronizedataBySortnum(String toupserverTBToken,StartSynchronizedata_2_Param param){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        if(null!=param){
            setSynchronizedataBySortnum(toupserverTBToken,param);
        }
        return true;
    }

    /**
     * 往同步的缓存中更新list数据集合
     * @param toupserverTBToken
     * @param paramlist
     * @return
     */
    public synchronized static boolean addSynchronizedataListBySortnum(String toupserverTBToken,List<StartSynchronizedata_2_Param> paramlist){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        if(null!=paramlist&&paramlist.size() > 0){

            for(StartSynchronizedata_2_Param p:paramlist){
                addSynchronizedataBySortnum(toupserverTBToken,p);
            }
        }
        return true;
    }

    /**
     * 往同步的缓存中更新数据
     * @param toupserverTBToken
     * @param param
     * @return
     */
    public synchronized static boolean setSynchronizedataBySortnum(String toupserverTBToken,StartSynchronizedata_2_Param param){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        if(null!=param){
            StartSynchronizedata_1_Param data=new StartSynchronizedata_1_Param();
            List<StartSynchronizedata_2_Param> datalist=new ArrayList<StartSynchronizedata_2_Param>();
            if(synchronizeDataMap.containsKey(toupserverTBToken)){

                data = synchronizeDataMap.get(toupserverTBToken);
                if(null!=data){
                    if(null!=data.getDatalist()&&data.getDatalist().size() > 0){
                        datalist=data.getDatalist();
                    }
                }else{
                    data=new StartSynchronizedata_1_Param();
                }
            }else{
                datalist=new ArrayList<StartSynchronizedata_2_Param>();
            }

            if(null!=datalist&&datalist.size() > 0){
                for(int i=0;i<datalist.size();i++){
                    StartSynchronizedata_2_Param p=datalist.get(i);
                    if(param.getDataname().equals(p.getDataname())
                            &&param.getDatassid().equals(p.getDatassid())){
                        datalist.remove(i);
                        i--;
                        break;
                    }
                }
            }
            datalist.add(param);
            setSynchronizedataBySortnum(toupserverTBToken,datalist);
        }
        return true;
    }


    /**
     * 往同步的缓存中更新数据
     * 覆盖StartSynchronizedata_1_Param中list的数据
     * @param toupserverTBToken
     * @param datalist
     * @return
     */
    public synchronized static boolean setSynchronizedataBySortnum(String toupserverTBToken,List<StartSynchronizedata_2_Param> datalist){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        StartSynchronizedata_1_Param data=new StartSynchronizedata_1_Param();

        if(synchronizeDataMap.containsKey(toupserverTBToken)){
            data=synchronizeDataMap.get(toupserverTBToken);
        }

        //修改总数和完成数
        if(null==datalist){
            datalist=new ArrayList<StartSynchronizedata_2_Param>();
            data.setFinishednum(0);
            data.setTotalnum(0);
        }else{
            data.setTotalnum(datalist.size());
            int finishednum=0;
            for(StartSynchronizedata_2_Param p:datalist){
                if(p.isOverwork()){
                    finishednum++;
                }
            }
            data.setFinishednum(finishednum);
        }
        data.setDatalist(datalist);
        setSynchronizedataBySortnum(toupserverTBToken,data);

        return true;
    }

    /**
     * 往同步的缓存中更新数据
     * 覆盖StartSynchronizedata_1_Param的数据
     * @param toupserverTBToken
     * @param data
     * @return
     */
    public synchronized static boolean setSynchronizedataBySortnum(String toupserverTBToken,StartSynchronizedata_1_Param data){

        if(null==synchronizeDataMap){
            synchronizeDataMap=new HashMap<String , StartSynchronizedata_1_Param>();
        }
        synchronized(synchronizeDataMap){
            synchronizeDataMap.put(toupserverTBToken,data);
        }
        return true;
    }



    /**
     * 关闭本次同步
     * @return
     */
    public synchronized static boolean delSynchronizedataBySortnum(String toupserverTBToken){

        if(null!=synchronizeDataMap&&synchronizeDataMap.containsKey(toupserverTBToken)){
            synchronized(synchronizeDataMap){

                //先关闭线程
                SynchronizedataThreadCache.delSynchronizedataThread();
                //再关闭本次同步
                synchronizeDataMap.remove(toupserverTBToken);
            }
        }
        return true;
    }


}
