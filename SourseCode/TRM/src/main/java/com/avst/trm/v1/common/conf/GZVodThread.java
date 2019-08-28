package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.util.*;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetSaveFilePath_localParam;
import com.avst.trm.v1.feignclient.ec.req.GetSavePathParam;
import com.avst.trm.v1.feignclient.ec.vo.GetSavepathVO;
import com.avst.trm.v1.feignclient.ec.vo.GetURLToPlayVO;
import com.avst.trm.v1.feignclient.ec.vo.SaveFile_localParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordSavepathParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordByIdVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 生成点播线程
 * 暂时没有做多个人一起使用，稍后会做缓存多人用，一个一个来
 */
public class GZVodThread extends Thread{

    private RResult result;

    private Police_recordMapper police_recordMapper;
    private Police_record police_record;

    public GZVodThread(RResult result_,Police_recordMapper police_recordMapper_,Police_record police_record_){
        result=result_;
        police_recordMapper=police_recordMapper_;
        police_record=police_record_;
    }

boolean  bool=true;

    @Override
    public void run() {

        if(CommonCache.gzvodthreadnum > 0){
            LogUtil.intoLog(3,this.getClass(),"暂时只允许一个人打包，不允许多个打包线程一块跑");
            return;
        }
        CommonCache.gzvodthreadnum++;
        try {

//            if(bool){
//                System.out.println("------------------------打包暂时不使用，等对接设备光盘刻录成功再开启------------------------");
//                return;
//            }

            GetRecordByIdVO getRecordByIdVO=(GetRecordByIdVO)result.getData();
            if(null==getRecordByIdVO||null==getRecordByIdVO.getGetPlayUrlVO()){
                LogUtil.intoLog(4,this.getClass(),"getRecordByIdVO or getRecordByIdVO.getGetPlayUrlVO is null,还没有生成可以使用的播放文件，不允许打包");
                return ;
            }
            GetPlayUrlVO getPlayUrlVO=getRecordByIdVO.getGetPlayUrlVO();
            List<RecordFileParam> statelist = getPlayUrlVO.getRecordFileParams();
            if(null!=statelist&&statelist.size() > 0){
                boolean bool=true;
                for(RecordFileParam param:statelist){
                    int state=param.getState();//文件状态
                    if(state!=2){//2代表视频文件可播放
                        bool=false;
                    }
                }
                if(!bool){
                    LogUtil.intoLog(4,this.getClass(),"state is not 2,播放文件的状态异常，不允许打包");
                    return ;
                }

            }else{
                LogUtil.intoLog(4,this.getClass(),"statelist is null,播放文件没有找到状态，不允许打包");
                return ;
            }

            EquipmentControl ec=SpringUtil.getBean(EquipmentControl.class);
            //1、生成所有的脱机回放可能需要的文件
            //读取文件保存地址，iid所在地
            String iidsavepath="";
            String iid=getPlayUrlVO.getIid();
            GetSaveFilePath_localParam getSaveFilePath_localParam=new GetSaveFilePath_localParam();
            getSaveFilePath_localParam.setIid(iid);
            getSaveFilePath_localParam.setSsType(SSType.AVST);
            RResult result_getsavepath=ec.getSaveFilePath_local(getSaveFilePath_localParam);
            if(null!=result_getsavepath&&result_getsavepath.getActioncode().equals(Code.SUCCESS.toString())&&null!=result_getsavepath.getData()){
                iidsavepath=result_getsavepath.getData().toString();
            }else{
                LogUtil.intoLog(4,this.getClass(),"iid对应存储地址没有找到，不打包，不生成回放文件，直接跳出，iid："+iid);
                return;
            }

            //新增一个视频文件的说明文件
            String iidplayfilesname=PropertiesListenerConfig.getProperty("iidplayfilesname");
            if(StringUtils.isEmpty(iidplayfilesname)){
                iidplayfilesname="iidplay.txt";
            }
            boolean filebool=false;
            String iidplayfilespath=iidsavepath+iidplayfilesname;
            List<RecordPlayParam>  recordPlayParams=getPlayUrlVO.getRecordPlayParams();
            if(null!=recordPlayParams&&recordPlayParams.size() > 0){
                String toout_filename=PropertiesListenerConfig.getProperty("toout_filename");
                if(StringUtils.isEmpty(toout_filename)){
                    toout_filename="file_@i@:";
                }
                String filerr="iid:"+iid;
                int i=0;
                for(RecordPlayParam recordPlayParam:recordPlayParams){
                    String playurl=recordPlayParam.getPlayUrl();
                    String filename=OpenUtil.getfilename(playurl);
                    recordPlayParam.setPlayUrl(filename);
                    filename=toout_filename.replace("@i@",i+"")+filename;
                    filerr+="\r\n"+filename;
                    i++;
                }
                filebool= ReadWriteFile.writeTxtFile(filerr,iidplayfilespath,"utf8");
            }else{
                LogUtil.intoLog(4,this.getClass(),"recordPlayParams is null,播放文件没有一个视频，不允许打包");
                return ;
            }


            //Word和PDF copy到iid存储位置
            Record record=getRecordByIdVO.getRecord();
            String wordrealurl= null;
            boolean wordbool=false;
            try {
                wordrealurl = record.getWordrealurl();
                if(StringUtils.isNotEmpty(wordrealurl)||OpenUtil.fileisexist(wordrealurl)){
                    String iid_wordrealurl=iidsavepath+OpenUtil.getfilename(wordrealurl);
                    FileUtils.copyFile(new File(wordrealurl),new File(iid_wordrealurl));
                    wordbool=true;
                }else{
                    //需要生成Word文件
                    //后期写入自动生成Word文件
                    LogUtil.intoLog(3,this.getClass(),"-----GZVodThread-需要生成Word文件");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean pdfbool=false;
            try {
                String pdfrealurl=record.getPdfrealurl();
                if(StringUtils.isNotEmpty(pdfrealurl)||OpenUtil.fileisexist(pdfrealurl)){
                    String iid_pdfrealurl=iidsavepath+OpenUtil.getfilename(pdfrealurl);
                    FileUtils.copyFile(new File(pdfrealurl),new File(iid_pdfrealurl));
                    pdfbool=true;
                }else{
                    //需要生成pdf文件
                    //后期写入自动生成pdf文件
                    LogUtil.intoLog(3,this.getClass(),"-----GZVodThread-需要生成pdf文件");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //生成RResult返回对象的TXT文件,写入iid所在的文件中
            String str=JacksonUtil.objebtToString(result);

            //json数据处理
            //去掉\n,去掉\\,前面加上var result='  后面加上';
            str=str.replaceAll("\\\\n","");
            str=str.replaceAll("\\\\","\\\\\\\\");
            str="var result='"+str+"';";

            String resultfilename=PropertiesListenerConfig.getProperty("resultfilename");
            if(StringUtils.isEmpty(resultfilename)){
                resultfilename="result.js";
            }
            String iid_resultfilepath=iidsavepath+resultfilename;
            boolean resultbool= ReadWriteFile.writeTxtFile(str,iid_resultfilepath,"utf8");

            if(pdfbool&&resultbool&&wordbool&&filebool){//这四个文件都成功才填入iid
                //更新数据库的iid
                police_record.setGz_iid(iid);
                int updateById=police_recordMapper.updateById(police_record);
                if(updateById > -1){

                }else{
                    LogUtil.intoLog(4,this.getClass(),"-----GZVodThread-police_recordMapper.updateById is error");
                    return ;
                }
            }else{
                LogUtil.intoLog(4,this.getClass(),"-----可能有文件没有新增或者copy成功，pdfbool："+pdfbool+",resultbool:"+resultbool+",wordbool:"+wordbool+",filebool:"+filebool);
                return ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CommonCache.gzvodthreadnum--;
        }

        LogUtil.intoLog(1,this.getClass(),"GZVodThread 出来了---");

    }
}
