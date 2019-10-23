package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.util.*;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetSaveFilePath_localParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordByIdVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 生成点播线程
 * 点播回放其实是要在设备控制存储组件里面写的，这里的result不好获取
 *
 */
public class CreateVodThread extends Thread{

    private RResult result;

    private Police_recordMapper police_recordMapper;
    private Police_record police_record;

    public CreateVodThread(RResult result_, Police_recordMapper police_recordMapper_, Police_record police_record_){
        result=result_;
        police_recordMapper=police_recordMapper_;
        police_record=police_record_;
    }

    private boolean uploadPdfAndWord=false;//修改PDF和Word才会用到的参数
    private String wordrealurl;
    private String pdfrealurl;
    private String iid;

    public CreateVodThread(String wordrealurl_,String pdfrealurl_,String iid_){
        iid=iid_;
        wordrealurl=wordrealurl_;
        pdfrealurl=pdfrealurl_;
        uploadPdfAndWord=true;
    }

   boolean  bool=true;

    @Override
    public void run() {


        if(uploadPdfAndWord){//只是修改PDF和Word
            uploadVod();
        }else{
            createVod();
        }

        LogUtil.intoLog(1,this.getClass(),"CreateVodThread 出来了---");

    }



    private void createVod(){

        try {

//            if(bool){
//                System.out.println("------------------------生成点播文件暂时不使用------------------------");
//                return;
//            }

            GetRecordByIdVO getRecordByIdVO=(GetRecordByIdVO)result.getData();
            if(null==getRecordByIdVO||null==getRecordByIdVO.getGetPlayUrlVO()){
                LogUtil.intoLog(4,this.getClass(),"getRecordByIdVO or getRecordByIdVO.getGetPlayUrlVO is null,还没有生成可以使用的播放文件，不允许生成点播文件");
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
                    LogUtil.intoLog(4,this.getClass(),"state is not 2,播放文件的状态异常，不允许生成点播文件");
                    return ;
                }

            }else{
                LogUtil.intoLog(4,this.getClass(),"statelist is null,播放文件没有找到状态，不允许生成点播文件");
                return ;
            }

            //1、生成所有的脱机回放可能需要的文件
            //读取文件保存地址，iid所在地
            String iidsavepath="";
            String iid=getPlayUrlVO.getIid();
            iidsavepath=getIidPath(iid);
            if(StringUtils.isEmpty(iidsavepath)){
                LogUtil.intoLog(4,this.getClass(),"iid对应存储地址没有找到，不生成回放文件(包含PDF和Word)，直接跳出，iid："+iid);
                return ;
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
                LogUtil.intoLog(4,this.getClass(),"recordPlayParams is null,播放文件没有一个视频，不允许生成iidplay.txt");
                return ;
            }

            //Word和PDF copy到iid存储位置
            Record record=getRecordByIdVO.getRecord();
            String wordrealurl= null;
            boolean wordandpdfbool=false;

            wordandpdfbool=uploadPDFAndWord(record.getWordrealurl(),record.getPdfrealurl(),iidsavepath);

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

            if(wordandpdfbool&&resultbool&&filebool){//这四个文件都成功才填入iid
                //更新数据库的iid
                police_record.setGz_iid(iid);
                int updateById=police_recordMapper.updateById(police_record);
                if(updateById > -1){

                }else{
                    LogUtil.intoLog(4,this.getClass(),"-----CreateVodThread-police_recordMapper.updateById is error");
                    return ;
                }
            }else{
                LogUtil.intoLog(4,this.getClass(),"-----可能有文件没有新增或者copy成功，wordandpdfbool："+wordandpdfbool+",resultbool:"+resultbool+",filebool:"+filebool);
                return ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private void uploadVod(){

        String iidsavepath=getIidPath(iid);
        if(StringUtils.isEmpty(iidsavepath)){
            LogUtil.intoLog(4,this.getClass(),"iid对应存储地址没有找到，不生成回放文件(包含PDF和Word)，直接跳出，iid："+iid);
            return ;
        }
        if(StringUtils.isEmpty(wordrealurl)||StringUtils.isEmpty(pdfrealurl)){
            LogUtil.intoLog(4,this.getClass(),"修改PDF和Word文件的时候文件路径为空，直接跳出，iid："+iid);
            LogUtil.intoLog(4,this.getClass(),wordrealurl+"：wordrealurl----修改PDF和Word文件失败，文件路径--pdfrealurl："+pdfrealurl);
            return ;
        }
        uploadPDFAndWord(wordrealurl,pdfrealurl,iidsavepath);

    }

    private boolean uploadPDFAndWord(String wordrealurl,String pdfrealurl,String iidsavepath){
        boolean wordbool=false;
        try {
            if(StringUtils.isNotEmpty(wordrealurl)||OpenUtil.fileisexist(wordrealurl)){
                String iid_wordrealurl=iidsavepath+OpenUtil.getfilename(wordrealurl);
                FileUtils.copyFile(new File(wordrealurl),new File(iid_wordrealurl));
                wordbool=true;
            }else{
                //需要生成Word文件
                //后期写入自动生成Word文件
                LogUtil.intoLog(3,this.getClass(),"-----CreateVodThread-需要生成Word文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean pdfbool=false;
        try {
            if(StringUtils.isNotEmpty(pdfrealurl)||OpenUtil.fileisexist(pdfrealurl)){
                String iid_pdfrealurl=iidsavepath+OpenUtil.getfilename(pdfrealurl);
                FileUtils.copyFile(new File(pdfrealurl),new File(iid_pdfrealurl));
                pdfbool=true;
            }else{
                //需要生成pdf文件
                //后期写入自动生成pdf文件
                LogUtil.intoLog(3,this.getClass(),"-----CreateVodThread-需要生成pdf文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (pdfbool&&wordbool);
    }

    private String getIidPath(String iid){
        EquipmentControl ec=SpringUtil.getBean(EquipmentControl.class);
        GetSaveFilePath_localParam getSaveFilePath_localParam=new GetSaveFilePath_localParam();
        getSaveFilePath_localParam.setIid(iid);
        getSaveFilePath_localParam.setSsType(SSType.AVST);
        RResult result_getsavepath=ec.getSaveFilePath_local(getSaveFilePath_localParam);
        if(null!=result_getsavepath&&result_getsavepath.getActioncode().equals(Code.SUCCESS.toString())&&null!=result_getsavepath.getData()){
            return result_getsavepath.getData().toString();
        }else{
            return null;
        }

    }
}
