package com.avst.trm.v1.report.toupserver.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.report.cache.ReportCahce;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseDealClass;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqClass;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqInterface;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataCache;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataThreadCache;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_1_Param;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;
import com.avst.trm.v1.report.toupserver.police.v1.ToUpServerDeal.ToUpServerBaseDeal_police;

import java.util.List;

/**
 * 数据同步的执行线程
 */
public class SynchronizedataThread implements Runnable{

    public boolean workbool=true;//用于中断线程

    private String synchronizedataurl_data=null;//数据同步的请求地址

    private String synchronizedataurl_file=null;//数据同步的请求地址

    public SynchronizedataThread(String url,String url_file){
        synchronizedataurl_data = url;
        synchronizedataurl_file = url_file;
    }

    @Override
    public void run() {

        SynchronizedataThreadCache.setSynchronizedataThread(this);

        String token = ReportCahce.getToupserverTBToken();
        StartSynchronizedata_1_Param startSynchronizedata_1_param= SynchronizedataCache.getSynchronizeData(token);
        synchronized (startSynchronizedata_1_param){

            int Finishednum = startSynchronizedata_1_param.getFinishednum();
            int Totalnum = startSynchronizedata_1_param.getTotalnum();
            String downserverssid=startSynchronizedata_1_param.getSdTableSsid();
            if(Finishednum>=Totalnum){//一般情况不会出现

            }else{
                //开始工作
                List<StartSynchronizedata_2_Param> datalist=startSynchronizedata_1_param.getDatalist();
                if(null==datalist||datalist.size() == 0){

                }else{
                    for(StartSynchronizedata_2_Param data:datalist){
                        if (workbool){
                            if(data.isOverwork()){//跳过已完成的完成
                                continue;
                            }

                            String url;
                            if(2==data.getType()){
                                url=synchronizedataurl_data;
                            }else{
                                url=synchronizedataurl_file;
                            }
                            getToUpServerBaseReqInterface().synchronizedata(url,data,downserverssid);
                        }else{
                            break;//中途中断跳出
                        }
                    }

                    //任务全部完成后，检测缓存中的任务是否全部完成
                    startSynchronizedata_1_param= SynchronizedataCache.getSynchronizeData(token);
                    int totalnum=startSynchronizedata_1_param.getTotalnum();
                    int finishednum=startSynchronizedata_1_param.getFinishednum();
                    if(finishednum >= totalnum){ //全部完成，请求检测上级服务器端的缓存是否全部完成
                        ToUpServerBaseReqClass.overSynchronizedata();
                    }else{
                        //不会再这里处理，要在页面上再次点击同步，不能变成死循环
                    }

                }
            }
        }

        SynchronizedataThreadCache.delSynchronizedataThread();//当执行完了就关闭线程缓存
    }



    /**
     * @return
     */
    public ToUpServerBaseReqInterface getToUpServerBaseReqInterface(){

        String serverType= CommonCache.getCurrentServerType();

        if(serverType.startsWith("court")){

        }else if(serverType.startsWith("police")){
            return new ToUpServerBaseDeal_police();
        }else if(serverType.startsWith("meeting")){

        }else if(serverType.startsWith("dis")){

        }

        return null;
    }
}
