package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.util.*;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
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
 * 压缩点播线程
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


    @Override
    public void run() {

        try {

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

            //1、生成gz的压缩文件
            //读取播放器地址
            String vodplayerfilepath=PropertiesListenerConfig.getProperty("vodplayerfilepath");
            if(StringUtils.isEmpty(vodplayerfilepath)){
                LogUtil.intoLog(4,this.getClass(),"PropertiesListenerConfig.getProperty(\"vodplayerfilepath\") is null");
                return ;
            }
            String outfile=PropertiesListenerConfig.getProperty("outfile");
            if(StringUtils.isEmpty(outfile)){//这个不允许出错
                outfile="outfile";
            }

            //查看外部文件夹是否有文件，有的话就直接清空，在建
            outfile=vodplayerfilepath+outfile;
            File file=new File(outfile);
            try {
                if(file.exists()){//有的话直接干掉
                    file=null;
                    FileUtil.delAllFile(outfile);//有待考证
                }else{
                    file.mkdirs();
                    file=null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Record record=getRecordByIdVO.getRecord();
            String wordrealurl=record.getWordrealurl();
            String wordpath=outfile+"/"+FileUtil.getsavename(wordrealurl);
            String pdfrealurl=record.getPdfrealurl();
            String pdfpath=outfile+"/"+FileUtil.getsavename(pdfrealurl);

            try {
                FileUtils.copyFile(new File(wordrealurl),new File(wordpath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileUtils.copyFile(new File(pdfrealurl),new File(pdfpath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //生成RResult返回对象的TXT文件
            String str=JacksonUtil.objebtToString(result);
            String resultfilename=PropertiesListenerConfig.getProperty("resultfilename");
            if(StringUtils.isEmpty(resultfilename)){
                resultfilename="result.txt";
            }
            String resultpath=outfile+"/"+resultfilename;
            ReadWriteFile.writeTxtFile(str,resultpath);//写入TXT文件

            //调用压缩生成文件
            String gzfilename="client";
            String gzfilepath=null;
            String gzbasepath=PropertiesListenerConfig.getProperty("gzbasepath");
            if(StringUtils.isEmpty(gzbasepath)){
                gzbasepath="gz/";
            }
            String sourseRelativePath= OpenUtil.getpath_fileByBasepath2(gzbasepath)+gzfilename+".tar.gz";

            String iid=getPlayUrlVO.getIid();
            //准备调用本地储存系统
            String gz_iid=iid+"_gz";//相对于视频的iid加了一个gz
            EquipmentControl ec=SpringUtil.getBean(EquipmentControl.class);
            SaveFile_localParam param_save=new SaveFile_localParam();
            param_save.setIid(gz_iid);
            param_save.setSourseRelativePath(sourseRelativePath);
            param_save.setSsType(SSType.AVST);
            RResult result_SAVE=ec.saveFile_local(param_save);

            //请求设备控制，获取上传文件服务器的路径
            if(null!=result_SAVE&&result_SAVE.getActioncode().equals(Code.SUCCESS.toString())){
                gzfilepath=result_SAVE.getData().toString();

                //开始压缩
                long starttime=(new Date()).getTime();
                LogUtil.intoLog(1,this.getClass(),"-----GZVodThread-压缩开始,starttime:"+starttime);
                boolean bool=GZIPUtil.CompressedFiles_Gzip(vodplayerfilepath,FileUtil.getsavepath(gzfilepath)+"/"+gzfilename,gzfilename);
                long endtime=(new Date()).getTime();
                LogUtil.intoLog(1,this.getClass(),"-----GZVodThread-压缩结束,endtime:"+endtime);
                if(bool){
                    LogUtil.intoLog(1,this.getClass(),"-----GZVodThread-压缩成功,总计时间："+(endtime-starttime));

                    //更新数据库的压缩iid
                    police_record.setGz_iid(gz_iid);
                    int updateById=police_recordMapper.updateById(police_record);
                    if(updateById > -1){

                    }else{
                        LogUtil.intoLog(4,this.getClass(),"-----GZVodThread-police_recordMapper.updateById is error");
                        return ;
                    }

                }else{
                    LogUtil.intoLog(4,this.getClass(),"-----GZVodThread-压缩失败,总计时间："+(endtime-starttime));
                    return ;
                }

            }else{
                LogUtil.intoLog(4,this.getClass(),"-----GZVodThread-equipmentControl.saveFile_local 失败");
                return ;
            }

            //请求设备，请求设备允许上传到设备中的路径
            GetSavePathParam param=new GetSavePathParam();
            param.setIid(iid);
            param.setSsType(SSType.AVST);
            RResult<GetSavepathVO> result=ec.getSavePath(param);
            if(null!=result&&result.getActioncode().equals(Code.SUCCESS.toString())){

                GetSavepathVO vo=result.getData();
                if(null!=vo&&null!=vo.getRecordList()&&vo.getRecordList().size() > 0){

                    List<RecordSavepathParam> rlist=vo.getRecordList();//现阶段只需要其中一个就可以了，上传文件的路径
                    RecordSavepathParam recordPlayParam=rlist.get(0);
                    if(null==recordPlayParam||StringUtils.isEmpty(recordPlayParam.getSoursedatapath())){
                        LogUtil.intoLog(4,this.getClass(),"recordPlayParam or recordPlayParam.getSoursedatapath() is null,返回的视频原路径数据为空,最里面一层");
                        return;
                    }
                    String soursepath=recordPlayParam.getSoursedatapath();

                    //开始上传文件到设备


                }else{
                    LogUtil.intoLog(4,this.getClass(),"getURLToPlayVO or getURLToPlayVO.getRecordList() is null,返回的视频原路径数据为空");
                    return ;
                }


            }else{
                LogUtil.intoLog(4,this.getClass(),"ec.getSavePath is error,请求设备源地址失败");
                return ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        LogUtil.intoLog(4,this.getClass(),"GZVodThread 出来了---");

    }
}
