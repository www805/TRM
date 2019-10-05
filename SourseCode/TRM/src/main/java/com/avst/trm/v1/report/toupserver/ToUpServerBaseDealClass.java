package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.GetSynchronizedDataSheet_DownServer;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_downserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RRParam;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.report.cache.ReportCahce;
import com.avst.trm.v1.report.conf.ReportConf;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataCache;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataThreadCache;
import com.avst.trm.v1.report.toupserver.common.conf.AddDataToSynchronizeDataConf;
import com.avst.trm.v1.report.toupserver.common.conf.SynchronizedataThread;
import com.avst.trm.v1.report.toupserver.common.reqparam.*;
import com.avst.trm.v1.report.toupserver.common.vo.StartSynchronizedataVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请求上级服务器的处理
 */
@Service
public class ToUpServerBaseDealClass {


    @Autowired
    private Base_datainfoMapper base_datainfoMapper;
    @Autowired
    private Base_filesaveMapper base_filesaveMapper;
    @Autowired
    private Base_datasynchroni_downserverMapper base_datasynchroni_downserverMapper;


    public boolean initsynchronizeddata(String url) {

        String param= ReportConf.getParam(null,true);
        String rr= HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            LogUtil.intoLog(this.getClass(),result.getActioncode()+":result.getActioncode() initsynchronizeddata");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){

                //请求成功的处理
                String token=(String)result.getData();
                //存入缓存
                boolean bool= ReportCahce.setToupserverTBToken(token);
                //bool==false说明上级服务器有同步的token，不用新增
                return bool;
            }
        }
        return false;
    }

    public RRParam startSynchronizedata(String url, SynchronizeDataTypeParam synchronizeDataTypeParam) {

        RRParam rrParam=new RRParam();

        //先在此判断token的属实性
        String token = ReportCahce.getToupserverTBToken();
        if(StringUtils.isEmpty(token)){//虽说多次一举但是还是加着吧
            rrParam.setMessage("初始化的token为空，请重新初始化");
            return rrParam;
        }
        String sdip=synchronizeDataTypeParam.getSdIP();
        if(StringUtils.isEmpty(sdip)){
            rrParam.setMessage("同步的上级服务器IP为空");
            return rrParam;
        }
        int datatype=synchronizeDataTypeParam.getType();
        List<StartSynchronizedata_2_Param> sdlist=new ArrayList<StartSynchronizedata_2_Param>();
        if(0==datatype){//全部数据同步
            EntityWrapper<Base_datainfo> entityWrapper=new EntityWrapper<Base_datainfo>();
            List<Base_datainfo> tablelist=base_datainfoMapper.selectList(entityWrapper);//获取全部需要同步的表
            if(null==tablelist||tablelist.size() == 0){
                LogUtil.intoLog(this.getClass(),"-------------------------------------------------");
                LogUtil.intoLog(this.getClass(),"需要同步的数据库表查询为空，请查看-------重大问题");
                LogUtil.intoLog(this.getClass(),"-------------------------------------------------");
                rrParam.setMessage("需要同步的数据库表查询为空，请查看-------重大问题");
                return rrParam;
            }
            sdlist= AddDataToSynchronizeDataConf.addDataToSynchronizeData(sdlist,tablelist);
        }else if(1==datatype){//单表同步，现阶段单表同步不处理关联字段的数据,暂时不走单表逻辑
            String datatablename=synchronizeDataTypeParam.getDatatablename();
            sdlist=AddDataToSynchronizeDataConf.addDataToSynchronizeData(sdlist,datatablename);
        }else{//单个数据传递
            StartSynchronizedata_2_Param newsd = new StartSynchronizedata_2_Param(
                    synchronizeDataTypeParam.getDatatablename(),1,synchronizeDataTypeParam.getDatassid());
            sdlist.add(newsd);
            AddDataToSynchronizeDataConf.addFileToSynchronizeData(sdlist,newsd,base_filesaveMapper);
        }//注意文件的传输

        //获取了数据库数据，开始比对已经更新的数据库，得到真正需要更新的数据list
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("dd.upserverip",sdip);
        List<GetSynchronizedDataSheet_DownServer> list=base_datasynchroni_downserverMapper.getSynchronizedDataSheet_DownServer(entityWrapper);
        sdlist=AddDataToSynchronizeDataConf.checkDataToSynchronizeData(sdlist,list);
        int sdnum=0;
        if(null==sdlist||sdlist.size()==0){
            rrParam.setMessage("需要同步的数据对比之后为空，已经不需要同步了");
            rrParam.setCode(Code.SUCCESS_NOTHINGTODO.hashCode());
            return rrParam;
        }
        sdnum=list.size();
        String data=JacksonUtil.objebtToString(sdlist);//Jackson转对象为String
        String param= ReportConf.getParam(data,false);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            LogUtil.intoLog(this.getClass(),result.getActioncode()+":result.getActioncode() startSynchronizedata");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){

                //请求成功的处理
                StartSynchronizedataVO vo=(StartSynchronizedataVO)result.getData();

                int total=vo.getTotal();
                if(total==0){//这里说明不需要同步
                    rrParam.setCode(Code.SUCCESS_NOTHINGTODO.hashCode());
                    return rrParam;
                }else{
                    List<StartSynchronizedata_2_Param> datalist=vo.getDatalist();//偷了个懒，没有把这个返回对象转得更简单一点

                    if(null==datalist||datalist.size()==0){
                        rrParam.setCode(Code.FAIL.hashCode());
                        rrParam.setMessage("上级服务器返回数据异常");
                        return rrParam;
                    }else{

                        //新增同步数据表
                        String newssid = OpenUtil.getUUID_32();
                        Base_datasynchroni_downserver base_datasynchroni_downserver=new Base_datasynchroni_downserver();
                        base_datasynchroni_downserver.setUploadcount((sdnum+1));
                        base_datasynchroni_downserver.setSsid(newssid);
                        base_datasynchroni_downserver.setUpserverip(sdip);
                        base_datasynchroni_downserver.setLastuploadtime(new Date());
                        int insert=base_datasynchroni_downserverMapper.insert(base_datasynchroni_downserver);
                        if(insert>=0){
                            StartSynchronizedata_1_Param startSynchronizedata_1_param=new StartSynchronizedata_1_Param();
                            startSynchronizedata_1_param.setTotalnum(datalist.size());
                            startSynchronizedata_1_param.setDatalist(datalist);
                            startSynchronizedata_1_param.setSdTableSsid(newssid);
                            startSynchronizedata_1_param.setSdip(sdip);
                            SynchronizedataCache.initSynchronizedataBySortnum(token,startSynchronizedata_1_param);
                            rrParam.setCode(Code.SUCCESS.hashCode());
                        }else{
                            rrParam.setMessage("新增同步数据失败，本次同步取消，请联系管理员");
                            rrParam.setCode(Code.FAIL.hashCode());
                            return rrParam;
                        }
                    }

                }
                rrParam.setMessage("请求成功");
                return rrParam;
            }else{
                //同步请求返回失败

            }
        }

        return rrParam;
    }

    /**
     * 准备去开启同步
     * @param url_data
     * @param url_file
     * @param synchronizedataParam
     * @return
     */
    public RRParam gotosynchronizedata(String url_data, String url_file , GotoSynchronizedataParam synchronizedataParam) {
        RRParam rrParam=new RRParam();

        //先在此判断token的属实性
        String token = ReportCahce.getToupserverTBToken();
        if(StringUtils.isEmpty(token)){//虽说多次一举但是还是加着吧
            rrParam.setMessage("初始化的token为空，请重新初始化");
            return rrParam;
        }

        String sdip = synchronizedataParam.getSdIP();
        if(StringUtils.isEmpty(sdip)){
            rrParam.setMessage("上级服务器的IP为空");
            return rrParam;
        }
        //需要同步的数据
        StartSynchronizedata_1_Param startSynchronizedata_1_param=SynchronizedataCache.getSynchronizeData(token);
        int totalnum=startSynchronizedata_1_param.getTotalnum();
        int finfshnum=startSynchronizedata_1_param.getFinishednum();
        if(totalnum <= finfshnum){
            rrParam.setMessage("已经同步完成，等待关闭");
            return rrParam;
        }

        //检测同步数据
        SynchronizedataThread thread=SynchronizedataThreadCache.getSynchronizedataThread();
        if(null!=thread){
            rrParam.setMessage("已有数据同步中，请稍等");
            rrParam.setCode(Code.SUCCESS.hashCode());
            return rrParam;
        }
        thread=new SynchronizedataThread(url_data,url_file);
        thread.start();//开始同步中

        rrParam.setCode(Code.SUCCESS.hashCode());
        rrParam.setMessage("开始同步中，请稍等");
        return rrParam;
    }

    public RRParam overSynchronizedata(String url) {
        RRParam rrParam=new RRParam();
        String data="";//Jackson转对象为String
        String param= ReportConf.getParam(null,false);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult<List<StartSynchronizedata_2_Param>> result=(RResult<List<StartSynchronizedata_2_Param>>) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            LogUtil.intoLog(this.getClass(),result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){

                //请求成功的处理
                //删除本次同步缓存
                String token = ReportCahce.getToupserverTBToken();
                SynchronizedataCache.delSynchronizedataBySortnum(token);

                rrParam.setCode(Code.SUCCESS.hashCode());
                rrParam.setMessage("请求成功");
                return rrParam;
            }else{
                //还有没有同步完的就需要继续同步
                List<StartSynchronizedata_2_Param> list=result.getData();
                String token = ReportCahce.getToupserverTBToken();
                if(null==list||list.size()==0){//没有数据还是按着完成来做
                    //删除本次同步缓存
                    SynchronizedataCache.delSynchronizedataBySortnum(token);
                    rrParam.setCode(Code.SUCCESS.hashCode());
                    rrParam.setMessage(result.getMessage());
                    return rrParam;
                }else{
                    SynchronizedataCache.addSynchronizedataListBySortnum(token,list);
                    GotoSynchronizedataParam synchronizedataParam=new GotoSynchronizedataParam();
                    synchronizedataParam.setSdIP(SynchronizedataCache.getSynchronizeData(token).getSdip());
//                    ToUpServerBaseReqClass.synchronizedata(synchronizedataParam);//让用户在下级服务器中手动启动

                    //先关闭线程，再次开启就行了
                    SynchronizedataThreadCache.delSynchronizedataThread();
                }
            }
        }
        return rrParam;
    }

    public RRParam overSynchronizedata_must(String url,boolean mustOver) {
        RRParam rrParam=new RRParam();

        if(mustOver){
            String param= ReportConf.getParam(mustOver+"",false);
            String rr=HttpRequest.readContentFromGet(url,param);
            if(StringUtils.isNotEmpty(rr)) {

                RResult<List<StartSynchronizedata_2_Param>> result = (RResult<List<StartSynchronizedata_2_Param>>) JacksonUtil.stringToObjebt_1(rr, RResult.class);
                LogUtil.intoLog(this.getClass(),result.getActioncode() + ":result.getActioncode()");

                rrParam.setCode(result.getActioncode().hashCode());
                rrParam.setMessage(result.getMessage());
            }else{
                rrParam.setMessage("请求强制关闭同步失败");
            }

            //删除本次同步缓存
            String token = ReportCahce.getToupserverTBToken();
            SynchronizedataCache.delSynchronizedataBySortnum(token);
        }else{
            overSynchronizedata(url);
        }

        return rrParam;
    }



}
