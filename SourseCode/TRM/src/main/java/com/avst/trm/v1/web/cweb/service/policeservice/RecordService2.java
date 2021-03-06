package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.conf.CreateVodThread;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.gzip.GZIPCache;
import com.avst.trm.v1.common.util.gzip.GZIPCacheParam;
import com.avst.trm.v1.common.util.gzip.GZIPThread;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.poiwork.WordToHtmlUtil;
import com.avst.trm.v1.common.util.poiwork.WordToPDF;
import com.avst.trm.v1.common.util.poiwork.XwpfTUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.common.util.uploadfile.UploadFileThread;
import com.avst.trm.v1.common.util.uploadfile.UploadType;
import com.avst.trm.v1.common.util.uploadfile.param.FileParam;
import com.avst.trm.v1.common.util.uploadfile.param.UploadParam_FD;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetDefaultSaveInfoParam;
import com.avst.trm.v1.feignclient.ec.req.GetSaveFilesPathByiidParam;
import com.avst.trm.v1.feignclient.ec.req.GetSavePathParam;
import com.avst.trm.v1.feignclient.ec.req.GetToOutFlushbonadingListParam;
import com.avst.trm.v1.feignclient.ec.vo.GetDefaultSaveInfoVO;
import com.avst.trm.v1.feignclient.ec.vo.GetSavepathVO;
import com.avst.trm.v1.feignclient.ec.vo.fd.Flushbonadinginfo;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordSavepathParam;
import com.avst.trm.v1.feignclient.ec.vo.ph.GetPolygraphAnalysisVO;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMCaLLUserAsrTxtListParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetMc_modelParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetPHDataParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetPhssidByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.feignclient.mc.vo.Avstmt_modelAll;
import com.avst.trm.v1.feignclient.mc.vo.PhDataParam_toout;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetRecordrealingVO;
import com.avst.trm.v1.web.cweb.cache.RecordProtectCache;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.cache.Recordrealing_LastCache;
import com.avst.trm.v1.web.cweb.cache.param.RecordProtectParam;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.avst.trm.v1.common.cache.CommonCache.getSQEntity;

/**
 * recordService行数太长加个分支
 */
@Service("recordService2")
public class RecordService2 extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_recordtypeMapper police_recordtypeMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_answerMapper police_answerMapper;

    @Autowired
    private Police_recordtoproblemMapper police_recordtoproblemMapper;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

    @Autowired
    private Police_wordtemplateMapper police_wordtemplateMapper;

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    @Autowired
    private Base_nationalityMapper base_nationalityMapper;

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private OutService outService;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;


    //=================================================关于导出==================================================================start
    public void exportUdisk(RResult result, ExportUdiskParam param){
        ExportUdiskVO vo=new ExportUdiskVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(3,this.getClass(),"recordService.exportUdisk__请求参数param is null__"+param);
            return;
        }

        String ssid=param.getSsid();
        if (StringUtils.isBlank(ssid)){
            result.setMessage("参数为空");
            LogUtil.intoLog(3,this.getClass(),"recordService.exportUdisk__请求参数ssid is null__"+ssid);
            return;
        }

        Police_case police_case=new Police_case();
        police_case.setSsid(ssid);
        police_case=police_caseMapper.selectOne(police_case);
        if (null!=police_case){
            List<String> folderPath=new ArrayList<>();//多文件
            try {
                //根据案件查找所有提讯下的文件iid
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",ssid);
                ewarraignment.eq("recordbool",2).or().eq("recordbool",3);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {
                        String iid=arraignmentAndRecord.getIid();
                        if (StringUtils.isNotBlank(iid)){
                            //根据iid找到需要上传的所有文件
                            GetSaveFilesPathByiidParam getSaveFilesPathByiidParam=new GetSaveFilesPathByiidParam();
                            getSaveFilesPathByiidParam.setIid(iid);
                            getSaveFilesPathByiidParam.setVideobool(0);//不需要上传视频文件
                            getSaveFilesPathByiidParam.setSsType(SSType.AVST);
                            RResult rResult=equipmentControl.getSaveFilesPathByiid(getSaveFilesPathByiidParam);
                            //请求设备允许上传到设备中的路径，一个一个传过去
                            if(null!=rResult&&null!=rResult.getData()){
                                String pathlist=rResult.getData().toString();
                                String[] patharr=pathlist.split(",");
                                if(patharr!=null&&patharr.length > 0) {
                                    String path=patharr[0];
                                    String zippath= OpenUtil.getfile_folder(path);
                                    LogUtil.intoLog(1,this.getClass(),"iid："+iid+"----打包的文件夹zippath:"+zippath);
                                    folderPath.add(zippath);
                                }
                            }else{
                                LogUtil.intoLog(4,this.getClass(),"根据iid获取文件路径异常，iid："+iid);
                            }
                        }else {
                            LogUtil.intoLog(1,this.getClass(),"recordService.exportUdisk__该笔录未有打包文件recordssid___iid is null__"+arraignmentAndRecord.getRecordssid());
                        }
                    }
                    if (null!=folderPath&&folderPath.size()>0){
                        String gztype= PropertiesListenerConfig.getProperty("gztype");
                        if(StringUtils.isEmpty(gztype)){
                            gztype=".zip";
                        }

                        String exportfilename=police_case.getCasename();
                        if(StringUtils.isEmpty(exportfilename)){
                            exportfilename=ssid;
                        }

                        //地址
                        String savePath=PropertiesListenerConfig.getProperty("file.casezip");
                        String realurl = OpenUtil.createpath_fileByBasepath(savePath);
                        LogUtil.intoLog(this.getClass(),"案件打包地址真实文件夹地址__"+realurl);

                        boolean repackbool_=false;
                        Integer repackbool=police_case.getRepackbool();
                        if (repackbool==1&&null!=repackbool){
                            repackbool_=true;
                        }
                        GZIPThread gzipThread=new GZIPThread(folderPath,realurl,ssid,exportfilename,gztype,repackbool_);//需要根据数据库进行判断是否需要强制重新打包压缩
                        gzipThread.start();
                        LogUtil.intoLog(1,this.getClass(),"本次导出是否需要重复【案件】______________________________________________"+repackbool_);


                        //案件重复打包：这里只需要修改案件重复打包状态
                        EntityWrapper c=new EntityWrapper();
                        police_case.setRepackbool(-1);
                        EntityWrapper updateew=new EntityWrapper();
                        updateew.eq("ssid",ssid);
                        int police_caseMapper_updatebool=police_caseMapper.update(police_case,updateew);
                        LogUtil.intoLog(this.getClass(),"police_caseMapper_updatebool__"+police_caseMapper_updatebool);



                        //获取下载路径
                        String zipfilepath=realurl;
                        if(zipfilepath.endsWith("\\")||zipfilepath.endsWith("/")){
                            zipfilepath=zipfilepath.substring(0,zipfilepath.length()-1);
                        }
                        if(zipfilepath.indexOf("\\") > -1){
                            zipfilepath+="\\\\"+exportfilename+gztype;
                        }else {
                            zipfilepath+="/"+exportfilename+gztype;
                        }
                        String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
                        String qg=PropertiesListenerConfig.getProperty("file.qg");

                        String httpzipfilepath=uploadpath+OpenUtil.strMinusBasePath(qg,zipfilepath);
                        LogUtil.intoLog(1,this.getClass(),"打包下载的地址,httpzipfilepath:"+httpzipfilepath);
                        if (StringUtils.isNotBlank(httpzipfilepath)){
                            vo.setDownurl(httpzipfilepath);
                            result.setData(vo);
                            changeResultToSuccess(result);
                            return;
                        }else {
                            LogUtil.intoLog(1,this.getClass(),"打包下载的地址,httpzipfilepath is null"+httpzipfilepath);
                        }
                    }else {
                        result.setMessage("未找到可导出的文件可能正在生成中请稍等");
                        LogUtil.intoLog(1,this.getClass(),"案件导出到U盘————未找到可导出的文件可能正在生成中请稍等"+ssid);
                        return;
                    }
                }else {
                    result.setMessage("该案件未找到可导出的文件");
                    LogUtil.intoLog(1,this.getClass(),"案件导出到U盘————该案件未找到可导出的文件"+ssid);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            result.setMessage("案件信息未找到");
            LogUtil.intoLog(1,this.getClass(),"案件导出到U盘————案件信息未找到 case isn null"+ssid);
            return;
        }
        return;
    }

    public void exportUdiskProgress(RResult result,ExportUdiskProgressParam param){
        ExportUdiskProgressVO vo=new ExportUdiskProgressVO();
        String ssid=param.getSsid();
        if(StringUtils.isEmpty(ssid)){
            result.setMessage("未找到该案件");
            return;
        }

        vo.setSsid(ssid);

        GZIPCacheParam gzipCacheParam= GZIPCache.getGzipCacheParam(ssid);
        vo.setGzipCacheParam(gzipCacheParam);
        result.setData(vo);
        if(null==gzipCacheParam){
            result.setActioncode(Code.SUCCESS_NOTHINGTODO.toString());
            result.setMessage("未找到该打包进度，可能已经打包完成");
        }else{
            //测试:LogUtil.intoLog(1,this.getClass(),"导出U盘导出文件进度______________________________totalzipnum__"+gzipCacheParam.getTotalzipnum()+"__overzipnum__"+gzipCacheParam.getOverzipnum());
            this.changeResultToSuccess(result);
        }
        return;
    }

    public void exportLightdisk(RResult result, ExportLightdiskParam param, HttpSession session){
        ExportLightdiskVO vo=new ExportLightdiskVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(3,this.getClass(),"recordService.exportLightdisk__请求参数param is null__"+param);
            return;
        }
        String ssid=param.getSsid();
        if (StringUtils.isBlank(ssid)){
            result.setMessage("参数为空");
            LogUtil.intoLog(3,this.getClass(),"recordService.exportLightdisk__请求参数ssid is null__"+ssid);
            return;
        }

        Police_case police_case=new Police_case();
        police_case.setSsid(ssid);
        police_case=police_caseMapper.selectOne(police_case);
        if (null!=police_case){
            List<FileParam<UploadParam_FD>> fileList=new ArrayList<>();//上传的所有文件集合
            try {
                //根据案件查找所有提讯下的文件iid
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",ssid);
                ewarraignment.eq("recordbool",2).or().eq("recordbool",3);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){

                    //此处暂时采用默认设备的地址==后期变更
                    String actionUrl_ip=null;
                    Integer actionUrl_port=null;
                    ReqParam<GetToOutFlushbonadingListParam> param_ = new ReqParam<>();
                    GetToOutFlushbonadingListParam listParam = new GetToOutFlushbonadingListParam();
                    listParam.setFdType(FDType.FD_AVST);
                    param_.setParam(listParam);
                    RResult result_ = equipmentControl.getToOutDefault(param_);
                    if (null != result_ && result_.getActioncode().equals(Code.SUCCESS.toString())&&null!=result_.getData()) {
                        Flushbonadinginfo flushbonadinginfo=gson.fromJson(gson.toJson(result_.getData()), Flushbonadinginfo.class);
                        if (null!=flushbonadinginfo){
                            actionUrl_ip=flushbonadinginfo.getEtip();
                            actionUrl_port=flushbonadinginfo.getPort();
                        }
                    }else{
                        LogUtil.intoLog(this.getClass(),"请求equipmentControl.getToOutDefault__出错");
                    }
                    if (null==actionUrl_port||StringUtils.isBlank(actionUrl_ip)){
                        result.setMessage("上传服务器地址未找到");
                        LogUtil.intoLog(4,this.getClass(),"案件导出到光盘————上传服务器地址未找到actionUrl_port is null and actionUrl_ip is null");
                        return;
                    }

                    String actionUrl="http://"+actionUrl_ip+":"+actionUrl_port+"/uploadService/httpFileUpload";
                    LogUtil.intoLog(1,this.getClass(),"案件导出到光盘————上传服务器地址____actionUrl____"+actionUrl);


                    for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {
                        String iid=arraignmentAndRecord.getIid();
                        if (StringUtils.isNotBlank(iid)){
                            //根据iid找到需要上传的所有文件
                            GetSavePathParam getSavePathParam=new GetSavePathParam();
                            getSavePathParam.setIid(iid);
                            getSavePathParam.setSsType(SSType.AVST);
                            RResult rResult=equipmentControl.getSavePath(getSavePathParam);
                            //请求设备允许上传到设备中的路径，一个一个传过去
                            if(null!=rResult&&null!=rResult.getData()){
                                GetSavepathVO getSavepathVO=gson.fromJson(gson.toJson(rResult.getData()), GetSavepathVO.class);
                                if (null!=getSavepathVO){
                                    List<RecordSavepathParam> recordList=getSavepathVO.getRecordList();
                                    String pathlist=null;//文件所在地址
                                    String dstpath=null;//需要上传到的文件夹地址
                                    if (null!=recordList&&recordList.size()>0){
                                        for (RecordSavepathParam recordSavepathParam : recordList) {
                                            String datasavepath=recordSavepathParam.getSavepath();
                                            dstpath=recordSavepathParam.getSoursedatapath();
                                            if (StringUtils.isNotBlank(dstpath)){
                                                dstpath=OpenUtil.getfile_folder(dstpath);
                                                if(dstpath.indexOf("/") > 0){
                                                    dstpath+="\\";
                                                }else{
                                                    dstpath+="/";
                                                }
                                            }
                                            if(StringUtils.isNotEmpty(datasavepath)){
                                                pathlist=OpenUtil.getfile_folder(datasavepath);
                                                if(pathlist.indexOf("/") > 0){
                                                    pathlist+="/";
                                                }else{
                                                    pathlist+="\\";
                                                }
                                                break;
                                            }
                                        }
                                    }

                                    String[] patharr=pathlist.split(",");
                                    if(patharr!=null&&patharr.length > 0) {
                                        String path=patharr[0];
                                        String zippath=OpenUtil.getfile_folder(path);
                                        LogUtil.intoLog(1,this.getClass(),"iid："+iid+"----打包的文件夹zippath:"+zippath);

                                        if (StringUtils.isNotBlank(dstpath)&&StringUtils.isNotBlank(zippath)){
                                            //收集数据
                                            List<String> filelist= FileUtil.getAllFilePath(zippath,2);
                                            if(null!=filelist&&filelist.size() > 0){
                                                for(String path_:filelist){
                                                    //去除视频类的文件
                                                    if (!path_.endsWith("mp4")&&!path_.endsWith("st")&&!path_.endsWith("ts")){
                                                        File file=new File(path_);
                                                        UploadParam_FD fd=new UploadParam_FD();
                                                        fd.setDiscFileName(file.getName());
                                                        fd.setDstPath(dstpath);
                                                        fd.setFileName(file.getName());
                                                        fd.setUpload_task_id(iid);

                                                        FileParam<UploadParam_FD> fdFileParam=new FileParam<>();
                                                        fdFileParam.setUploadparam(fd);
                                                        fdFileParam.setActionURL(actionUrl);
                                                        fdFileParam.setFilePath(path_);
                                                        fileList.add(fdFileParam);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                LogUtil.intoLog(4,this.getClass(),"根据iid获取文件路径异常，iid："+iid);
                            }
                        }else {
                            LogUtil.intoLog(1,this.getClass(),"recordService.exportUdisk__该笔录未有打包文件recordssid___iid is null__"+arraignmentAndRecord.getRecordssid());
                        }
                    }
                    if (null!=fileList&&fileList.size()>0){
                        String gztype=PropertiesListenerConfig.getProperty("gztype");
                        if(StringUtils.isEmpty(gztype)){
                            gztype=".zip";
                        }

                        String exportfilename=police_case.getCasename();
                        if(StringUtils.isEmpty(exportfilename)){
                            exportfilename=ssid;
                        }
                        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);

                        UploadParam_FD uploadParam_fd=new UploadParam_FD();
                        UploadFileThread<UploadParam_FD> uploadFileThread=new UploadFileThread(fileList, UploadType.AVST_FD,ssid,user.getUsername(),exportfilename);
                        uploadFileThread.start();

                        changeResultToSuccess(result);
                        return;

                    }else {
                        result.setMessage("未找到可导出的文件可能正在生成中请稍等");
                        LogUtil.intoLog(1,this.getClass(),"案件导出到光盘————未找到可导出的文件可能正在生成中请稍等"+ssid);
                        return;
                    }
                }else {
                    result.setMessage("该案件未找到可导出的文件");
                    LogUtil.intoLog(1,this.getClass(),"案件导出到光盘————该案件未找到可导出的文件"+ssid);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            result.setMessage("案件信息未找到");
            LogUtil.intoLog(1,this.getClass(),"案件导出到光盘————案件信息未找到 case isn null"+ssid);
            return;
        }

        return;
    }

    public RResult uploadRecordMessageToETAndTimeRecord(RResult result, UploadRecordMessageToETAndTimeRecordParam param){

        String iid="";

        //根据iid找到需要上传的所有文件
        GetSaveFilesPathByiidParam getSaveFilesPathByiidParam=new GetSaveFilesPathByiidParam();
        getSaveFilesPathByiidParam.setIid(iid);
        getSaveFilesPathByiidParam.setVideobool(0);//不需要上传视频文件
        getSaveFilesPathByiidParam.setSsType(SSType.AVST);
        RResult rResult=equipmentControl.getSaveFilesPathByiid(getSaveFilesPathByiidParam);
        //请求设备允许上传到设备中的路径，一个一个传过去
        if(null!=rResult&&null!=rResult.getData()){

            String pathlist=rResult.getData().toString();
            String[] patharr=pathlist.split(",");
            if(patharr!=null&&patharr.length > 0){
                for(String path:patharr){
                    if(StringUtils.isNotEmpty(path)){
                        //开始上传
                    }
                }
            }else{
                result.setMessage("没有一个可以打包上传的文件路径");
            }
        }


        return result;
    }

    public RResult gZIPVod(RResult result,ReqParam<GZIPVodParam> paramReqParam){
        GZIPVodParam param=paramReqParam.getParam();
        String iid=param.getIid();
        String zipfilename=param.getZipfilename();
        if(StringUtils.isEmpty(iid)){
            result.setMessage("iid唯一标识未找到");
            return result;
        }

        if(StringUtils.isEmpty(zipfilename)){
            zipfilename=iid;
        }

        //判断是否需要重新打包
        String recordssid=param.getRecordssid();
        if(StringUtils.isEmpty(recordssid)){
            result.setMessage("系统异常");
            LogUtil.intoLog(1,this.getClass(),"打包回放__gZIPVod_recordssid is null");
            return result;
        }
        Police_record record=new Police_record();
        EntityWrapper policerecordew=new EntityWrapper();
        policerecordew.eq("ssid",recordssid);
        List<Police_record> police_records=police_recordMapper.selectList(policerecordew);//使用mybatisplus的selectone查询结果为null
        if (null!=police_records&&police_records.size()==1) {
            record = police_records.get(0);
        }else {
            result.setMessage("笔录异常");
            return result;
        }

        if (null==record){
            result.setMessage("未找到该笔录");
            return result;
        }


        boolean repackbool_=false;
        Integer repackbool=record.getRepackbool();
        if (repackbool==1&&null!=repackbool){
            repackbool_=true;
        }


        //根据iid找到需要上传的所有文件
        GetSaveFilesPathByiidParam getSaveFilesPathByiidParam=new GetSaveFilesPathByiidParam();
        getSaveFilesPathByiidParam.setIid(iid);
        getSaveFilesPathByiidParam.setVideobool(0);//不需要上传视频文件
        getSaveFilesPathByiidParam.setSsType(SSType.AVST);
        RResult rResult=equipmentControl.getSaveFilesPathByiid(getSaveFilesPathByiidParam);
        //请求设备允许上传到设备中的路径，一个一个传过去
        if(null!=rResult&&null!=rResult.getData()){

            String pathlist=rResult.getData().toString();
            String[] patharr=pathlist.split(",");
            if(patharr!=null&&patharr.length > 0) {
                String path=patharr[0];
                String zippath=OpenUtil.getfile_folder(path);
                String gztype=PropertiesListenerConfig.getProperty("gztype");
                if(StringUtils.isEmpty(gztype)){
                    gztype=".zip";
                }
                LogUtil.intoLog(1,this.getClass(),zipfilename+":zipfilename,开始打包VOD，iid："+iid+"----打包的文件夹zippath:"+zippath);


                GZIPThread gzipThread=new GZIPThread(zippath,zippath,iid,zipfilename,gztype,repackbool_);//需要根据数据库进行判断是否需要强制重新打包压缩
                gzipThread.start();
                LogUtil.intoLog(1,this.getClass(),"本次导出是否需要重复【笔录】______________________________________________"+repackbool_);

                //本次打包后改为不需要重新打包
                EntityWrapper e=new EntityWrapper();
                e.eq("ssid",recordssid);
                record.setRepackbool(-1);//笔录重复打包
                int police_recordMapper_updatebool=police_recordMapper.update(record,e);
                LogUtil.intoLog(this.getClass(),"police_recordMapper_updatebool__"+police_recordMapper_updatebool);
                /*if (police_recordMapper_updatebool>0){
                    //案件重复打包
                    EntityWrapper getcasebyrecordssidew=new EntityWrapper();
                    getcasebyrecordssidew.eq("r.ssid",recordssid);
                    Case case_ =  police_caseMapper.getCaseByRecordSsid(getcasebyrecordssidew);
                    if (null!=case_){
                        EntityWrapper c=new EntityWrapper();
                        case_.setRepackbool(-1);
                        EntityWrapper updateew=new EntityWrapper();
                        updateew.eq("ssid",case_.getSsid());
                        int police_caseMapper_updatebool=police_caseMapper.update(case_,updateew);
                        LogUtil.intoLog(this.getClass(),"police_caseMapper_updatebool__"+police_caseMapper_updatebool);
                    }
                }*/



                String zipfilepath=zippath;
                if(zipfilepath.endsWith("\\")||zipfilepath.endsWith("/")){
                    zipfilepath=zipfilepath.substring(0,zipfilepath.length()-1);
                }
                if(zipfilepath.indexOf("\\") > -1){
                    zipfilepath+="\\\\"+zipfilename+gztype;
                }else {
                    zipfilepath+="/"+zipfilename+gztype;
                }


                //请求默认存储服务
                GetDefaultSaveInfoParam getDefaultSaveInfoParam=new GetDefaultSaveInfoParam();
                getDefaultSaveInfoParam.setSsType(SSType.AVST);
                RResult result1=equipmentControl.getDefaultSaveInfo(getDefaultSaveInfoParam);
                if(null!=result1&&result1.getActioncode().equals(Code.SUCCESS.toString())){

                    String staticpath="ftpdata";
                    Gson gson=new Gson();
                    GetDefaultSaveInfoVO getDefaultSaveInfoVO=gson.fromJson(gson.toJson(result1.getData()),GetDefaultSaveInfoVO.class);
                    if(null!=getDefaultSaveInfoVO&&StringUtils.isNotEmpty(getDefaultSaveInfoVO.getSsstatic())){
                        staticpath=getDefaultSaveInfoVO.getSsstatic();
                    }
                    String httpbasestaticpath="http://localhost";
                    if(null!=getDefaultSaveInfoVO&&StringUtils.isNotEmpty(getDefaultSaveInfoVO.getHttpbasestaticpath())){
                        httpbasestaticpath=getDefaultSaveInfoVO.getHttpbasestaticpath();
                    }

                    String httpzipfilepath=httpbasestaticpath+OpenUtil.strMinusBasePath(staticpath,zipfilepath);
                    LogUtil.intoLog(1,this.getClass(),"打包下载的地址,httpzipfilepath:"+httpzipfilepath);

                    result.setData(httpzipfilepath);
                    this.changeResultToSuccess(result);

                }else{
                    LogUtil.intoLog(4,this.getClass(),null==result1?"请求获取默认存储服务失败,sstype:"+SSType.AVST:result1.getMessage());
                }


            }
        }else{
            LogUtil.intoLog(4,this.getClass(),"根据iid获取文件路径异常，iid："+iid);
        }
        return result;
    }

    public RResult zIPVodProgress(RResult result, ReqParam<GZIPVodParam> paramReqParam){
        GZIPVodParam param=paramReqParam.getParam();
        String iid=param.getIid();
        if(StringUtils.isEmpty(iid)){
            result.setMessage("iid唯一标识未找到");
            return result;
        }

        GZIPCacheParam gzipCacheParam= GZIPCache.getGzipCacheParam(iid);
        if(null==gzipCacheParam){
            result.setActioncode(Code.SUCCESS_NOTHINGTODO.toString());
            result.setMessage("未找到该打包进度，可能已经打包完成");
        }else{
            result.setData(gzipCacheParam);
            this.changeResultToSuccess(result);
        }
        return result;
    }

    public RResult exportPdf(RResult result, ReqParam<ExportPdfParam> param){
        ExportPdfParam exportPdfParam=param.getParam();
        if (null==exportPdfParam){
            result.setMessage("参数为空");
            return result;
        }
        String recordssid=exportPdfParam.getRecordssid();
        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return result;
        }

        Map<String,String> dataMap=exportData(recordssid,false);

        //根据笔录ssid获取录音数据
        EntityWrapper recordParam=new EntityWrapper();
        recordParam.eq("r.ssid",recordssid);
        Record record=police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record) {

            //1、获取模板的真实地址
            String wordtemplate_realurl=null;//模板路径
            EntityWrapper wordew=new EntityWrapper();
            wordew.eq("w.ssid",record.getWordtemplatessid());
            List<WordTemplate> wordTemplate=police_wordtemplateMapper.getWordTemplate(wordew);
            if (null!=wordTemplate&&wordTemplate.size()==1){
                WordTemplate wordTemplate_=wordTemplate.get(0);
                wordtemplate_realurl=wordTemplate_.getWordtemplate_realurl();
            }
            LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板真实地址__"+wordtemplate_realurl);
            if (StringUtils.isBlank(wordtemplate_realurl)){
                result.setMessage("请先上传笔录类型对应的word笔录模板");
                return result;
            }

            try {
                String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
                String savePath=PropertiesListenerConfig.getProperty("file.recordwordOrpdf");
                String qg=PropertiesListenerConfig.getProperty("file.qg");

                //获取生成的真实地址
                String filename=record.getRecordname().replace(" ", "").replace("\"", "");
                String suffix =wordtemplate_realurl.substring(wordtemplate_realurl.lastIndexOf(".") + 1);

                String wordfilename=filename+"."+suffix;
                String wordrealurl = OpenUtil.createpath_fileByBasepath(savePath, wordfilename);
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word真实地址__"+wordrealurl);

                //获取生成的下载地址
                String worddownurl =uploadpath+OpenUtil.strMinusBasePath(qg, wordrealurl) ;
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word下载地址__"+worddownurl);


                LogUtil.intoLog(this.getClass(),"替换笔录模板中的标识数据，开始时间："+(new Date()).getTime());
                XwpfTUtil.replaceAndGenerateWord(wordtemplate_realurl,wordrealurl,dataMap,null);
                LogUtil.intoLog(this.getClass(),"替换笔录模板中的标识数据，结束时间："+(new Date()).getTime());

                String oldfilepath=record.getWordrealurl();
                String word_filesavessid=record.getWord_filesavessid();
                //将地址保存在文件存储表以及修改笔录标的文件存储ssid
                if (StringUtils.isNotBlank(wordrealurl)&&StringUtils.isNotBlank(worddownurl)){
                    //word转html-----------------start---------------
                    if(wordrealurl.endsWith(".doc")){
                        String replace = wordrealurl.replace(".doc", ".html");
                        WordToHtmlUtil.wordToHtml(wordrealurl, replace);
                    }else if(wordrealurl.endsWith(".docx")){
                        String replace = wordrealurl.replace(".docx", ".html");
                        WordToHtmlUtil.wordToHtml(wordrealurl, replace);
                    }
                    //word转html-----------------end-----------------
                    Base_filesave base_filesave=new Base_filesave();
                    base_filesave.setDatassid(recordssid);
                    base_filesave.setUploadfilename(wordfilename);
                    base_filesave.setRealfilename(wordfilename);
                    base_filesave.setRecordrealurl(wordrealurl);
                    base_filesave.setRecorddownurl(worddownurl);
                    if (StringUtils.isNotBlank(oldfilepath)){
                        //修改
                        EntityWrapper filesaveparam = new EntityWrapper();
                        filesaveparam.eq("ssid",word_filesavessid);
                        int filesaveupdate_bool=base_filesaveMapper.update(base_filesave,filesaveparam);
                        LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveupdate_bool);
                    }else{
                        //新增
                        base_filesave.setSsid(OpenUtil.getUUID_32());
                        int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                        LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveinsert_bool);
                        word_filesavessid=base_filesave.getSsid();
                    }
                }

                LogUtil.intoLog(this.getClass(),"__________________________开始word转pdf_____________________");
                //获取pdf下载和真实地址、
                String pdffilename=filename+".pdf";
                String pdfrealurl=OpenUtil.createpath_fileByBasepath(savePath, pdffilename);
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word转pdf真实地址__"+pdfrealurl);
                String pdfdownurl=uploadpath+OpenUtil.strMinusBasePath(qg, pdfrealurl) ;
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word转pdf下载地址__"+pdfdownurl);
                boolean wordtopdf_bool= WordToPDF.word2pdf(pdfrealurl,wordrealurl);
                if (wordtopdf_bool){
                    String oldPdfrealurl=record.getPdfrealurl();
                    String pdf_filesavessid=record.getPdf_filesavessid();
                    if (StringUtils.isNotBlank(pdfdownurl)&&StringUtils.isNotBlank(pdfrealurl)){
                        Base_filesave base_filesave=new Base_filesave();
                        base_filesave.setDatassid(recordssid);
                        base_filesave.setUploadfilename(pdffilename);
                        base_filesave.setRealfilename(pdffilename);
                        base_filesave.setRecordrealurl(pdfrealurl);
                        base_filesave.setRecorddownurl(pdfdownurl);
                        if (StringUtils.isNotBlank(oldPdfrealurl)){
                            //修改
                            EntityWrapper filesaveparam = new EntityWrapper();
                            filesaveparam.eq("ssid",pdf_filesavessid);
                            int filesaveupdate_bool=base_filesaveMapper.update(base_filesave,filesaveparam);
                            LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveupdate_bool);
                        }else{
                            //新增
                            base_filesave.setSsid(OpenUtil.getUUID_32());
                            int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                            LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveinsert_bool);
                            pdf_filesavessid=base_filesave.getSsid();
                        }
                    }
                    EntityWrapper e=new EntityWrapper();
                    e.eq("ssid",recordssid);
                    record.setWord_filesavessid(word_filesavessid);
                    record.setPdf_filesavessid(pdf_filesavessid);
                    record.setRepackbool(1);//笔录重复打包
                    int police_recordMapper_updatebool=police_recordMapper.update(record,e);
                    LogUtil.intoLog(this.getClass(),"police_recordMapper_updatebool__"+police_recordMapper_updatebool);
                    if (police_recordMapper_updatebool>0){
                        //案件重复打包
                        EntityWrapper getcasebyrecordssidew=new EntityWrapper();
                        getcasebyrecordssidew.eq("r.ssid",recordssid);
                        Case case_ =  police_caseMapper.getCaseByRecordSsid(getcasebyrecordssidew);
                        if (null!=case_){
                            EntityWrapper c=new EntityWrapper();
                            case_.setRepackbool(1);
                            EntityWrapper updateew=new EntityWrapper();
                            updateew.eq("ssid",case_.getSsid());
                            int police_caseMapper_updatebool=police_caseMapper.update(case_,updateew);
                            LogUtil.intoLog(this.getClass(),"police_caseMapper_updatebool__"+police_caseMapper_updatebool);
                        }
                    }

                    if (null!=record.getRecordbool()&&(record.getRecordbool()==2||record.getRecordbool()==3)){
                        //获取iid
                        String mtssid=null;
                        String iid=record.getGz_iid();
                        if (StringUtils.isBlank(iid)){
                            Police_arraignment police_arraignment=new Police_arraignment();
                            police_arraignment.setRecordssid(recordssid);
                            police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                            if (null!=police_arraignment) {
                                mtssid = police_arraignment.getMtssid();
                            }
                            GetMCVO getMCVO=new GetMCVO();
                            ReqParam getrecord_param=new ReqParam<>();
                            GetPhssidByMTssidParam_out getPhssidByMTssidParam_out=new GetPhssidByMTssidParam_out();
                            getPhssidByMTssidParam_out.setMcType(MCType.AVST);
                            getPhssidByMTssidParam_out.setMtssid(mtssid);
                            getrecord_param.setParam(getPhssidByMTssidParam_out);
                            RResult getrecord_rr=new RResult();
                            getrecord_rr= outService.getRecord(getrecord_rr,getrecord_param);
                            if (null!=getrecord_rr&&getrecord_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                                getMCVO = gson.fromJson(gson.toJson(getrecord_rr.getData()), GetMCVO.class);
                                if (null != getMCVO) {
                                    iid = getMCVO.getIid();
                                }
                            }
                        }

                        //
                        if (StringUtils.isNotBlank(iid)){
                            CreateVodThread createVodThread=new CreateVodThread(wordrealurl,pdfrealurl,iid);
                            createVodThread.start();
                        }
                    }




                    result.setData(pdfdownurl);
                    changeResultToSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void exportWord(RResult<ExportWordVO> result, ReqParam<ExportWordParam> param){
        ExportWordVO exportWordVO=new ExportWordVO();
        ExportWordParam exportWordParam=param.getParam();
        if (null==exportWordParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=exportWordParam.getRecordssid();
        if (null==recordssid){
            result.setMessage("参数为空");
            return;
        }

        boolean wordhead=exportWordParam.isWordheadbool();

        Map<String,String> dataMap=exportData(recordssid,wordhead);

        //根据笔录ssid获取录音数据
        EntityWrapper recordParam = new EntityWrapper();
        recordParam.eq("r.ssid", recordssid);
        Record record = police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record){

            //1、获取模板的真实地址
            String wordtemplate_realurl=null;//模板路径
            EntityWrapper wordew=new EntityWrapper();
            wordew.eq("w.ssid",record.getWordtemplatessid());
            List<WordTemplate> wordTemplate=police_wordtemplateMapper.getWordTemplate(wordew);
            if (null!=wordTemplate&&wordTemplate.size()==1){
                WordTemplate wordTemplate_=wordTemplate.get(0);
                wordtemplate_realurl=wordTemplate_.getWordtemplate_realurl();
            }
            LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板真实地址__"+wordtemplate_realurl);
            if (StringUtils.isBlank(wordtemplate_realurl)){
                result.setMessage("请先上传笔录类型对应的word笔录模板");
                return;
            }

            try {
                String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
                String savePath=PropertiesListenerConfig.getProperty("file.recordwordOrpdf");
                String qg=PropertiesListenerConfig.getProperty("file.qg");

                //获取生成的真实地址
                String filename=record.getRecordname().replace(" ", "").replace("\"", "");

                if (wordhead){
                    filename= filename+"_头文件";
                }

                String suffix =wordtemplate_realurl.substring(wordtemplate_realurl.lastIndexOf(".") + 1);

                String wordfilename=filename+"."+suffix;
                String wordrealurl = OpenUtil.createpath_fileByBasepath(savePath, wordfilename);
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word真实地址__"+wordrealurl);

                //获取生成的下载地址
                String worddownurl =uploadpath+OpenUtil.strMinusBasePath(qg, wordrealurl) ;
                LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板生成的word下载地址__"+worddownurl);

                XwpfTUtil.replaceAndGenerateWord(wordtemplate_realurl,wordrealurl,dataMap,null);

                String oldfilepath=record.getWordrealurl();

                //将地址保存在文件存储表以及修改笔录标的文件存储ssid
                String word_filesavessid=record.getWord_filesavessid();
                if (StringUtils.isNotBlank(wordrealurl)&&StringUtils.isNotBlank(worddownurl)){
                    //word转html-----------------start---------------
                    if(wordrealurl.endsWith(".doc")){
                        String replace = wordrealurl.replace(".doc", ".html");
                        WordToHtmlUtil.wordToHtml(wordrealurl, replace);
                    }else if(wordrealurl.endsWith(".docx")){
                        String replace = wordrealurl.replace(".docx", ".html");
                        WordToHtmlUtil.wordToHtml(wordrealurl, replace);
                    }
                    //word转html-----------------end-----------------

                    Base_filesave base_filesave=new Base_filesave();
                    base_filesave.setDatassid(recordssid);
                    base_filesave.setUploadfilename(wordfilename);
                    base_filesave.setRealfilename(wordfilename);
                    base_filesave.setRecordrealurl(wordrealurl);
                    base_filesave.setRecorddownurl(worddownurl);
                    if (StringUtils.isNotBlank(oldfilepath)){
                        //修改
                        EntityWrapper filesaveparam = new EntityWrapper();
                        filesaveparam.eq("ssid",word_filesavessid);
                        int filesaveupdate_bool=base_filesaveMapper.update(base_filesave,filesaveparam);
                        LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveupdate_bool);
                    }else{
                        //新增
                        base_filesave.setSsid(OpenUtil.getUUID_32());
                        int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                        LogUtil.intoLog(this.getClass(),"filesaveinsert_bool__"+filesaveinsert_bool);
                        word_filesavessid=base_filesave.getSsid();
                    }
                }

                EntityWrapper e=new EntityWrapper();
                e.eq("ssid",recordssid);
                if (wordhead){
                    record.setWordhead_filesavessid(word_filesavessid);
                }else {
                    record.setWord_filesavessid(word_filesavessid);
                    record.setRepackbool(1);//笔录重复打包
                }
                int police_recordMapper_updatebool=police_recordMapper.update(record,e);
                LogUtil.intoLog(this.getClass(),"police_recordMapper_updatebool__"+police_recordMapper_updatebool);
                if (police_recordMapper_updatebool>0&&record.getRecordbool()==1){
                    //案件重复打包
                    EntityWrapper getcasebyrecordssidew=new EntityWrapper();
                    getcasebyrecordssidew.eq("r.ssid",recordssid);
                    Case case_ =  police_caseMapper.getCaseByRecordSsid(getcasebyrecordssidew);
                    if (null!=case_){
                        EntityWrapper c=new EntityWrapper();
                        case_.setRepackbool(1);
                        EntityWrapper updateew=new EntityWrapper();
                        updateew.eq("ssid",case_.getSsid());
                        int police_caseMapper_updatebool=police_caseMapper.update(case_,updateew);
                        LogUtil.intoLog(this.getClass(),"police_caseMapper_updatebool__"+police_caseMapper_updatebool);
                    }
                }

                exportWordVO.setWord_path(worddownurl);
                result.setData(exportWordVO);
                changeResultToSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    //talkbool是否需要问答 true 不需要 false需要
    public  Map<String,String> exportData(String recordssid,boolean talkbool) {
        Map<String, String> dataMap = new HashMap<String, String>();
        List<String>  gnlist= CommonCache.gnlist();
        LogUtil.intoLog(1,this.getClass(),"导出word的笔录ssid__recordssid__"+recordssid);
        if (StringUtils.isNotEmpty(recordssid)){
            //根据笔录ssid获取录音数据
            EntityWrapper recordParam = new EntityWrapper();
            recordParam.eq("r.ssid", recordssid);
            Record record = police_recordMapper.getRecordBySsid(recordParam);
            if (null != record) {
                String talk="";
                if (!talkbool){
                    List<RecordToProblem> questionandanswer=RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合
                    if (null != questionandanswer && questionandanswer.size() > 0) {
                        for (RecordToProblem problem : questionandanswer) {
                            if (gnlist.indexOf(SQVersion.FY_T)!= -1){
                                //法院的
                                talk+= problem.getProblem()+"\r";
                            }else {
                                //其他
                                talk+="问："+problem.getProblem()+"\r";
                                List<Police_answer> answers = problem.getAnswers();
                                if (null != answers && answers.size() > 0) {
                                    for (Police_answer answer : answers) {
                                        talk+="答："+answer.getAnswer()+"\r";
                                    }
                                    problem.setAnswers(answers);
                                } else {
                                    talk+="答：\r";
                                }
                            }
                        }
                    }
                }
                LogUtil.intoLog(1,this.getClass(),"exportData 笔录问答集合："+talk);


                //根据笔录ssid获取案件信息
                Case case_ =new Case();
                try {
                    EntityWrapper caseParam=new EntityWrapper();
                    caseParam.eq("r.ssid",recordssid);
                    case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
                    if (null!=case_){
                        case_.setOccurrencetime_format(case_.getOccurrencetime());
                        record.setCase_(case_);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String casename=case_.getCasename();
                String cause=case_.getCause();
                String casenum=case_.getCasenum();
                String occurrencetime=case_.getOccurrencetime_format()==null?null:case_.getOccurrencetime_format().toString();
                String starttime=case_.getStarttime()==null?null:case_.getStarttime().toString();
                String endtime=case_.getEndtime()==null?null:case_.getEndtime().toString();
                String caseway=case_.getCaseway();


                /**
                 *   获取提讯人和被询问人
                 */
                EntityWrapper recorduserinfosParam = new EntityWrapper();
                recorduserinfosParam.eq("a.recordssid", recordssid);
                RecordUserInfos recordUserInfos = police_recordMapper.getRecordUserInfosByRecordSsid(recorduserinfosParam);

                String userssid = recordUserInfos.getUserssid();
                Police_userinfo police_userinfo = new Police_userinfo();
                police_userinfo.setSsid(userssid);
                police_userinfo = police_userinfoMapper.selectOne(police_userinfo);


                Police_arraignment police_arraignment = new Police_arraignment();
                police_arraignment.setRecordssid(recordssid);
                police_arraignment = police_arraignmentMapper.selectOne(police_arraignment);


                Police_recordtype police_recordtype = new Police_recordtype();
                police_recordtype.setSsid(record.getRecordtypessid());
                police_recordtype = police_recordtypeMapper.selectOne(police_recordtype);

                String recordtypename= recordtypename= police_recordtype.getTypename();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
                String recordstarttime = sdf.format(record.getCreatetime());
                String recordendtime = sdf.format(new Date());
                String recordplace = police_arraignment.getRecordplace();

                //工作单位
                Police_workunit police_workunit1 = new Police_workunit();
                police_workunit1.setSsid(recordUserInfos.getWorkunitssid1());
                if (null!=recordUserInfos.getWorkunitssid1()){
                    police_workunit1 = police_workunitMapper.selectOne(police_workunit1);
                }
                Police_workunit police_workunit2 = new Police_workunit();
                police_workunit2.setSsid(recordUserInfos.getWorkunitssid2());
                if (null!=recordUserInfos.getWorkunitssid2()){
                    police_workunit2 = police_workunitMapper.selectOne(police_workunit2);
                }

                Police_workunit police_workunit3 = new Police_workunit();
                police_workunit3.setSsid(recordUserInfos.getWorkunitssid3());
                if (null!=recordUserInfos.getWorkunitssid3()){
                    police_workunit3 = police_workunitMapper.selectOne(police_workunit3);
                }


                String workname1 = police_workunit1.getWorkname();
                String workname2 = police_workunit2.getWorkname();
                String workname3 = police_workunit3.getWorkname();
                String username = police_userinfo.getUsername();
                String sex = police_userinfo.getSex() ==null?"未知":(police_userinfo.getSex()==1 ? "男" : "女");
                String age = police_userinfo.getAge()==null?"未知": police_userinfo.getAge().toString();
                String politicsstatus = police_userinfo.getPoliticsstatus();
                String workunits = police_userinfo.getWorkunits();
                String residence = police_userinfo.getResidence();
                String phone = police_userinfo.getPhone();
                String domicile = police_userinfo.getDomicile();
                String both ="未知";
                if (null!=police_userinfo.getBoth()){
                    both =  new SimpleDateFormat("yyyy年MM月dd日").format(police_userinfo.getBoth());
                }
                String issuingauthority=police_userinfo.getIssuingauthority();
                String validity=police_userinfo.getValidity();

                EntityWrapper userinfoparam = new EntityWrapper();
                userinfoparam.eq("u.ssid", userssid);
                List<UserInfo> userInfos = police_userinfoMapper.getUserByCard(userinfoparam);
                String cardnum = null;
                String nationality=null;
                if (null != userInfos && userInfos.size() > 0) {
                    UserInfo userInfo=userInfos.get(0);
                    cardnum =userInfo.getCardtypename() + userInfo.getCardnum();
                    String nationalityssid=userInfo.getNationalityssid();
                    if (StringUtils.isNotBlank(nationalityssid)){
                        Base_nationality base_nationality=new Base_nationality();
                        base_nationality.setSsid(nationalityssid);
                        base_nationality=base_nationalityMapper.selectOne(base_nationality);
                        if (null!=base_nationality){
                            nationality=base_nationality.getZhname();
                        }
                    }
                }

                //talk 问答
                talk = talk.replaceAll("\\<.*?>", "").replaceAll("\\&[a-zA-Z]{1,10};", "");

                //获取法院相关的
                if (gnlist.indexOf(SQVersion.FY_T)!= -1){
                    dataMap.put("${庭审时间}", occurrencetime == null ? "" : occurrencetime);
                    String mtmodelssidname="";
                    if (StringUtils.isNotEmpty(police_arraignment.getMtmodelssid())){
                        List<Avstmt_modelAll> modelAlls=new ArrayList<>();
                        GetMc_modelParam_out getMc_modelParam_out=new GetMc_modelParam_out();
                        getMc_modelParam_out.setMcType(MCType.AVST);
                        getMc_modelParam_out.setModelssid(police_arraignment.getMtmodelssid());
                        ReqParam reqParam=new ReqParam();
                        reqParam.setParam(getMc_modelParam_out);
                        try {
                            RResult rr = meetingControl.getMc_model(reqParam);
                            if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
                                modelAlls=gson.fromJson(gson.toJson(rr.getData()), new TypeToken<List<Avstmt_modelAll>>(){}.getType());
                                if (null!=modelAlls&&modelAlls.size()==1){
                                    mtmodelssidname=modelAlls.get(0).getExplain();
                                }
                                LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__成功");
                            }else{
                                LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__失败"+rr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    dataMap.put("${庭审地点}", mtmodelssidname == null ? "" : mtmodelssidname);
                    if (StringUtils.isNotEmpty(police_arraignment.getSsid())){
                        List<Usergrade> usergrades=new ArrayList<>();
                        EntityWrapper arre=new EntityWrapper();
                        arre.eq("arraignmentssid",police_arraignment.getSsid());
                        List<Police_arraignmentexpand> arraignmentexpands = police_arraignmentexpandMapper.selectList(arre);
                        if (null!=arraignmentexpands&&arraignmentexpands.size()>0){
                            for (Police_arraignmentexpand arraignmentexpand : arraignmentexpands) {
                                String gradessid=arraignmentexpand.getExpandname();//拓展名为登记表ssid
                                String userssid_=arraignmentexpand.getExpandvalue();//拓展值为用户的ssid
                                if (StringUtils.isNotBlank(gradessid)&&StringUtils.isNotBlank(userssid_)){
                                    //查找等级
                                    Police_userinfograde police_userinfograde=new Police_userinfograde();
                                    police_userinfograde.setSsid(gradessid);
                                    police_userinfograde=police_userinfogradeMapper.selectOne(police_userinfograde);


                                    //查找用户:人员表
                                    Police_userinfo police_userinfo_=new Police_userinfo();
                                    police_userinfo_.setSsid(userssid_);
                                    police_userinfo_=police_userinfoMapper.selectOne(police_userinfo_);

                                    //查找用户：管理员表
                                    Base_admininfo admininfo=new Base_admininfo();
                                    admininfo.setSsid(userssid_);
                                    admininfo=base_admininfoMapper.selectOne(admininfo);

                                    if (null!=police_userinfograde){
                                        Usergrade usergrade=new Usergrade();
                                        usergrade=gson.fromJson(gson.toJson(police_userinfograde), Usergrade.class);
                                        usergrade.setUserssid(userssid_);
                                        if (null!=police_userinfo_){
                                            usergrade.setUsername(police_userinfo_.getUsername());
                                            usergrades.add(usergrade);
                                        }else if (null!=admininfo){
                                            usergrade.setUsername(admininfo.getUsername());
                                            usergrades.add(usergrade);
                                        }
                                    }
                                }
                            }
                        }

                        if (null!=usergrades&&usergrades.size()>0){
                            List<Usergrade> newarr=new ArrayList<>();
                            for (int i = 0; i < usergrades.size(); i++) {
                                boolean bool=true;
                                for (int j = 0; j < newarr.size(); j++) {
                                    if((null!=usergrades.get(i).getGrade()&&null!=newarr.get(j).getGrade()&& usergrades.get(i).getGrade()==newarr.get(j).getGrade())){
                                        newarr.get(j).setUsername(newarr.get(j).getUsername()+"、"+usergrades.get(i).getUsername());
                                        bool=false;
                                    }
                                }
                                if (bool){
                                    newarr.add(usergrades.get(i));
                                }
                            }
                            for (Usergrade usergrade : newarr) {
                                if (StringUtils.isNotEmpty(usergrade.getGradename())){
                                    dataMap.put("${"+usergrade.getGradename()+"}", usergrade.getUsername() == null ? "" : usergrade.getUsername());
                                }
                            }
                        }
                    }
                }

                dataMap.put("${笔录标题}", recordtypename == null ? "" : recordtypename);
                dataMap.put("${开始时间}", recordstarttime == null ? "" : recordstarttime);
                dataMap.put("${结束时间}", recordendtime == null ? "" : recordendtime);
                dataMap.put("${地点}", recordplace == null ? "" : recordplace);
                dataMap.put("${工作单位1}", workname1 == null ? "" : workname1);
                dataMap.put("${工作单位2}", workname2 == null ? "" : workname2);
                dataMap.put("${工作单位3}", workname3 == null ? "" : workname3);
                dataMap.put("${被询问人}", username == null ? "" : username);
                dataMap.put("${性别}", sex == null ? "" : sex);
                dataMap.put("${年龄}", age == null ? "" : age);
                dataMap.put("${身份证件及号码}", cardnum == null ? "" : cardnum);
                dataMap.put("${政治面貌}", politicsstatus == null ? "" : politicsstatus);
                dataMap.put("${工作单位}", workunits == null ? "" : workunits);
                dataMap.put("${现住址}", residence == null ? "" : residence);
                dataMap.put("${联系方式}", phone == null ? "" : phone);
                dataMap.put("${户籍所在地}", domicile == null ? "" : domicile);
                dataMap.put("${出生日期}", both == null ? "" : both);
                dataMap.put("${问答}", talk == null ? "" : talk);
                dataMap.put("${国籍}", nationality == null ? "" : nationality);
                dataMap.put("${案件名称}", casename == null ? "" : casename);
                dataMap.put("${案由}", cause == null ? "" : cause);
                dataMap.put("${案件编号}", casenum == null ? "" : casenum);
                dataMap.put("${案发时间}", occurrencetime == null ? "" : occurrencetime);
                dataMap.put("${案件开始时间}", starttime == null ? "" : starttime);
                dataMap.put("${案发结束时间}", endtime == null ? "" : endtime);
                dataMap.put("${到案方式}", caseway == null ? "" : caseway);
                dataMap.put("${签发机关}", issuingauthority == null ? "" : issuingauthority);
                dataMap.put("${身份证有效期}", validity == null ? "" : validity);
            }
        }
        return dataMap;
    }
    //=================================================关于导出=======================================================================end


    //================================================笔录问答实时缓存=============================================================start
    public void getRecordrealByRecordssid(RResult result,ReqParam<GetRecordrealByRecordssidParam> param){
        GetRecordrealByRecordssidParam getRecordrealByRecordssidParam=param.getParam();
        if (null==getRecordrealByRecordssidParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=getRecordrealByRecordssidParam.getRecordssid();
        if (null==recordssid){
            result.setMessage("参数为空");
            return;
        }
        List<RecordToProblem> recordToProblems = RecordrealingCache.getRecordrealByRecordssid(recordssid);
        changeResultToSuccess(result);
        result.setData(recordToProblems);
        return;
    }

    public void setRecordreal(RResult result,ReqParam<SetRecordrealParam> param){
        SetRecordrealParam setRecordrealParam=param.getParam();
        if (null==setRecordrealParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=setRecordrealParam.getRecordssid();
        List<RecordToProblem> recordToProblems=setRecordrealParam.getRecordToProblems();
        boolean bool=RecordrealingCache.setRecordreal(recordssid,recordToProblems);
        if (bool){
            changeResultToSuccess(result);
            result.setData(1);
        }
        return;
    }

    public void getRecordreal_LastByRecordssid(RResult result, ReqParam<GetRecordreal_LastByRecordssidParam>param){
        GetRecordreal_LastByRecordssidParam getRecordreal_lastByRecordssidParam=param.getParam();
        if (null==getRecordreal_lastByRecordssidParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=getRecordreal_lastByRecordssidParam.getRecordssid();
        if (null==recordssid){
            result.setMessage("参数为空");
            return;
        }
        List<RecordToProblem> recordToProblems = Recordrealing_LastCache.getRecordreal_LastByRecordssid(recordssid);
        changeResultToSuccess(result);
        result.setData(recordToProblems);
        return;
    }

    public void setRecordreal_Last(RResult result, ReqParam<SetRecordreal_LastParam>param){
        SetRecordreal_LastParam setRecordreal_lastParam=param.getParam();
        if (null==setRecordreal_lastParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=setRecordreal_lastParam.getRecordssid();
        List<RecordToProblem> recordToProblems=setRecordreal_lastParam.getRecordToProblems();
        boolean bool= Recordrealing_LastCache.setRecordreal_Last(recordssid,recordToProblems);
        if (bool){
            changeResultToSuccess(result);
            result.setData(1);
        }
        return;
    }
    //提供给笔录问答实时记录初始化使用
    public  List<Record> initRecordrealingCache(){
        EntityWrapper recordparam=new EntityWrapper();
        List<Integer> recordbools=new ArrayList<>();
        recordparam.ne("recordbool",-1);//笔录
        List<Police_record> list=police_recordMapper.selectList(recordparam);
        List<Record> records=new ArrayList<>();
        if (null!=list&&list.size()>0){
            for (Police_record police_record : list) {
                Record record=new Record();
                String  recordssid=police_record.getSsid();
                EntityWrapper probleparam=new EntityWrapper();
                probleparam.eq("r.ssid",recordssid);
                probleparam.orderBy("p.ordernum",true);
                probleparam.orderBy("p.createtime",true);
                List<RecordToProblem> problems = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(probleparam);
                if (null!=problems&&problems.size()>0){
                    for (RecordToProblem problem : problems) {
                        EntityWrapper answerParam=new EntityWrapper();
                        answerParam.eq("recordtoproblemssid",problem.getSsid());
                        answerParam.orderBy("ordernum",true);
                        answerParam.orderBy("createtime",true);
                        List<Police_answer> answers=police_answerMapper.selectList(answerParam);
                        if (null!=answers&&answers.size()>0){
                            problem.setAnswers(answers);
                        }
                    }
                    record.setSsid(recordssid);
                    record.setProblems(problems);
                    records.add(record);
                }
            }
        }
        return  records;
    }

    public void setRecordProtect(RResult result,ReqParam<SetRecordProtectParam> param){
        RecordProtectParam recordProtectParam=new RecordProtectParam();

        SetRecordProtectParam setRecordProtectParam=param.getParam();
        if (null==setRecordProtectParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=setRecordProtectParam.getRecordssid();
        String mtssid=setRecordProtectParam.getMtssid();
        List<RecordToProblem> recordToProblems=RecordrealingCache.getRecordrealByRecordssid(recordssid);
        List<AsrTxtParam_toout> asrTxtParamToouts=new ArrayList<>();//语音识别实时数据
        List<PHDataBackVoParam> phDataBackVoParams=new ArrayList<>();//身心监测数据


        if (StringUtils.isNotBlank(mtssid)){
            //会议不为空，开始获取语音实时数据以及身心监测实时数据
            //语音识别实时数据
            RResult asr_result = this.createNewResultOfFail();
            ReqParam reqParam=new ReqParam<>();
            GetMCaLLUserAsrTxtListParam_out getMCaLLUserAsrTxtListParam_out=new GetMCaLLUserAsrTxtListParam_out();
            getMCaLLUserAsrTxtListParam_out.setMcType(MCType.AVST);
            getMCaLLUserAsrTxtListParam_out.setMtssid(mtssid);
            reqParam.setParam(getMCaLLUserAsrTxtListParam_out);
            asr_result =meetingControl.getMCaLLUserAsrTxtList(reqParam);
            if (null != asr_result && asr_result.getActioncode().equals(Code.SUCCESS.toString())) {
                asrTxtParamToouts=gson.fromJson(gson.toJson(asr_result.getData()), new TypeToken<List<AsrTxtParam_toout>>(){}.getType());
            }

            //身心监测实时数据:暂时没有接口获取




        }




        if (StringUtils.isNotBlank(recordssid)){
            recordProtectParam.setRecordToProblems(recordToProblems);
            recordProtectParam.setRecordssid(recordssid);
            recordProtectParam.setMtssid(mtssid);
            recordProtectParam.setAsrTxtParamToouts(asrTxtParamToouts);
            recordProtectParam.setPhDataBackVoParams(phDataBackVoParams);
          boolean  setRecordecordProtectbool = RecordProtectCache.setRecordecordProtect(recordProtectParam);
            if (setRecordecordProtectbool){
                changeResultToSuccess(result);
                result.setData(1);
            }
            //开始保存(缓存到本地)
        }
        return;
    }
    //================================================笔录问答实时缓存============================================================end

    //================================================笔录word模板=================================================================start
    public void getWordTemplateList(RResult result,ReqParam<GetWordTemplateListParam> param){
        GetWordTemplateListParam getWordTemplateListParam=param.getParam();
        if (null==getWordTemplateListParam){
            result.setMessage("参数为空");
            return;
        }
        GetWordTemplateListVO vo=new GetWordTemplateListVO();

        String wordtemplatename=getWordTemplateListParam.getWordtemplatename();//word模板名称
        String recordtypessid=getWordTemplateListParam.getRecordtypessid();//笔录类型

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("w.createtime",false);
        if (StringUtils.isNotBlank(wordtemplatename)){
            ew.like("w.wordtemplatename",wordtemplatename);
        }
        if (StringUtils.isNotBlank(recordtypessid)){
            ew.eq("w.recordtypessid",recordtypessid);
        }

        ew.eq("w.wordtemplatebool",1);//模板状态为正常的
        ew.eq("w.wordtype",1);//查询word模板为1
        int count=police_wordtemplateMapper.countgetWordTemplateList(ew);
        getWordTemplateListParam.setRecordCount(count);

        Page<WordTemplate> page=new Page<>(getWordTemplateListParam.getCurrPage(),getWordTemplateListParam.getPageSize());
        List<WordTemplate> pagelist=police_wordtemplateMapper.getWordTemplateList(page,ew);

        String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
        //检测html文件是否存在-------------------------------------start----------------------------
        for (WordTemplate wordTemplate : pagelist) {
            String wordtemplate_downurl_html=null;
            String realurl=wordTemplate.getWordtemplate_realurl();
            String downurl=wordTemplate.getWordtemplate_downurl();
            if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                downurl=uploadpath+downurl;
                wordTemplate.setWordtemplate_downurl(downurl);
                if(realurl.endsWith(".doc")){
                    String replace = realurl.replace(".doc", ".html");
                    File f = new File(replace);
                    if (f.exists()) {
                        LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                        wordtemplate_downurl_html=downurl.replace(".doc", ".html");
                    }
                }else if(realurl.endsWith(".docx")){
                    String replace = realurl.replace(".docx", ".html");
                    File f = new File(replace);
                    if (f.exists()) {
                        LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                        wordtemplate_downurl_html=downurl.replace(".docx", ".html");
                    }
                }
            }
            wordTemplate.setWordtemplate_downurl_html(wordtemplate_downurl_html);
        }
        //检测html文件是否存在-------------------------------------end----------------------------

        //获取模板说明
        String wordtemplate_explaindownurl=null;//word模板说明制作下载地址
        String wordtemplate_explaindownurl_html=null;//word模板说明制作下载地址转html地址
        String wordtemplate_explaindownssid=null;
        EntityWrapper word2=new EntityWrapper();
        word2.eq("wordtype",2);
        List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word2);
        if (null!=wordTemplates&&wordTemplates.size()==1){
            WordTemplate wordTemplate2=wordTemplates.get(0);
            if (wordTemplate2.getWordtype()==2){
                String wordtemplate_downurl_html=null;
                String realurl=wordTemplate2.getWordtemplate_realurl();
                String downurl=wordTemplate2.getWordtemplate_downurl();
                if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                    downurl=uploadpath+downurl;
                    wordTemplate2.setWordtemplate_downurl(downurl);
                    if(realurl.endsWith(".doc")){
                        String replace = realurl.replace(".doc", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                            wordtemplate_downurl_html=downurl.replace(".doc", ".html");
                        }
                    }else if(realurl.endsWith(".docx")){
                        String replace = realurl.replace(".docx", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                            wordtemplate_downurl_html=downurl.replace(".docx", ".html");
                        }
                    }
                }
                wordtemplate_explaindownurl=downurl;
                wordtemplate_explaindownurl_html=wordtemplate_downurl_html;
                wordtemplate_explaindownssid=wordTemplate2.getSsid();
            }
        }
        //获取模板说明

        vo.setWordtemplate_explaindownurl(wordtemplate_explaindownurl);
        vo.setWordtemplate_explaindownurl_html(wordtemplate_explaindownurl_html);
        vo.setWordtemplate_explaindownssid(wordtemplate_explaindownssid);
        vo.setPagelist(pagelist);
        vo.setPageparam(getWordTemplateListParam);


        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }

    public void uploadWordTemplate(RResult result,ReqParam param, MultipartFile multipartfile){
        String Stringparam=(String)param.getParam();
        //请求参数转换
        UploadWordTemplateParam uploadWordTemplateParam=gson.fromJson(Stringparam, UploadWordTemplateParam.class);
        if (null==uploadWordTemplateParam){
            result.setMessage("参数为空");
            return;
        }
        String ssid=uploadWordTemplateParam.getSsid();
        Integer defaultbool=uploadWordTemplateParam.getDefaultbool();
        String wordtemplatename=uploadWordTemplateParam.getWordtemplatename();
        Integer wordtype=uploadWordTemplateParam.getWordtype();

        if (null!=wordtype&&wordtype==2){
            wordtemplatename="笔录模板制作说明";
        }


        Police_wordtemplate police_wordtemplate=new Police_wordtemplate();
        police_wordtemplate.setDefaultbool(defaultbool);
        police_wordtemplate.setWordtemplatename(wordtemplatename);
        police_wordtemplate.setWordtype(wordtype);
        police_wordtemplate.setWordtemplatebool(1);//状态为正常

        String wordtemplate_filesavessid=null;
        if(StringUtils.isNotBlank(ssid)){
            EntityWrapper wordtemplate_param=new EntityWrapper();
            wordtemplate_param.eq("wordtemplatename",wordtemplatename);
            wordtemplate_param.ne("ssid",ssid);
            wordtemplate_param.ne("wordtype",2);
            wordtemplate_param.ne("wordtemplatebool",-1);
            List<Police_case> police_cases_=police_wordtemplateMapper.selectList(wordtemplate_param);
            if (null!=police_cases_&&police_cases_.size()>0){
                result.setMessage("笔录模板名称不能重复");
                return;
            }


            EntityWrapper oldew=new EntityWrapper();
            oldew.eq("w.ssid",ssid);
            List<WordTemplate> oldwordTemplates=police_wordtemplateMapper.getWordTemplate(oldew);
            WordTemplate oldwordTemplate=new WordTemplate();
            if (null!=oldwordTemplates&&oldwordTemplates.size()==1){
                oldwordTemplate=oldwordTemplates.get(0);
            }

            //获取原来的地址，进行处理

            wordtemplate_filesavessid=oldwordTemplate.getWordtemplate_filesavessid();
            try {
                if (null!=multipartfile){
                    String savePath=PropertiesListenerConfig.getProperty("file.wordtemplate");
                    String qg=PropertiesListenerConfig.getProperty("file.qg");
                    String oldwordtemplate_realurl=oldwordTemplate.getWordtemplate_realurl();//旧真实地址




                    String oldfilename=multipartfile.getOriginalFilename();
                    String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                    String filename = wordtemplatename+"_"+ DateUtil.getSeconds()+"."+suffix;//模板名称加后缀
                    if(oldfilename.endsWith(".doc")||oldfilename.endsWith(".DOC")||oldfilename.endsWith(".docx")||oldfilename.endsWith(".DOCX")){
                        if (StringUtils.isNotBlank(oldwordtemplate_realurl)){
                            File oldfile=new File(oldwordtemplate_realurl);
                            if (oldfile.exists()) {
                                oldfile.delete();
                                LogUtil.intoLog(this.getClass(),"删除笔录word模板旧真实地址:"+oldwordtemplate_realurl);
                            }

                            if(oldwordtemplate_realurl.endsWith(".doc")){
                                String replace = oldwordtemplate_realurl.replace(".doc", ".html");
                                File f = new File(replace);
                                if (f.exists()) {
                                    f.delete();
                                    LogUtil.intoLog(this.getClass(),"删除笔录word模板html旧真实地址:"+replace);
                                }
                            }else if(oldwordtemplate_realurl.endsWith(".docx")){
                                String replace = oldwordtemplate_realurl.replace(".docx", ".html");
                                File f = new File(replace);
                                if (f.exists()) {
                                    f.delete();
                                    LogUtil.intoLog(this.getClass(),"删除笔录word模板html旧真实地址:"+replace);
                                }
                            }
                        }
                        String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                        LogUtil.intoLog(this.getClass(),"笔录word模板真实地址："+realurl);
                        multipartfile.transferTo(new File(realurl));
                        String downurl =OpenUtil.strMinusBasePath(qg, realurl) ;
                        LogUtil.intoLog(this.getClass(),"笔录word模板下载地址："+downurl);

                        if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){

                            //word转html-----------------start---------------
                            if(realurl.endsWith(".doc")){
                                String replace = realurl.replace(".doc", ".html");
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                            }else if(realurl.endsWith(".docx")){
                                String replace = realurl.replace(".docx", ".html");
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                            }
                            //word转html-----------------end-----------------

                            Base_filesave base_filesave=new Base_filesave();
                            base_filesave.setDatassid(ssid);
                            base_filesave.setUploadfilename(oldfilename);
                            base_filesave.setRealfilename(filename);
                            base_filesave.setRecordrealurl(realurl);
                            base_filesave.setRecorddownurl(downurl);
                            if (StringUtils.isNotBlank(oldwordtemplate_realurl)){
                                //修改
                                EntityWrapper filesaveparam = new EntityWrapper();
                                filesaveparam.eq("ssid",wordtemplate_filesavessid);
                                int filesaveupdate_bool=base_filesaveMapper.update(base_filesave,filesaveparam);
                                LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveupdate_bool);
                            }else{
                                //新增
                                base_filesave.setSsid(OpenUtil.getUUID_32());
                                int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                                LogUtil.intoLog(this.getClass(),"filesaveinsert_bool__"+filesaveinsert_bool);
                                wordtemplate_filesavessid=base_filesave.getSsid();
                            }
                        }
                    }else {
                        result.setMessage("请选择doc或者docx的word文档进行上传");
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //修改
            police_wordtemplate.setWordtemplate_filesavessid(wordtemplate_filesavessid);
            EntityWrapper updateew=new EntityWrapper();
            updateew.eq("ssid",ssid);
            int police_wordtemplateMapper_updatebool =  police_wordtemplateMapper.update(police_wordtemplate,updateew);
            LogUtil.intoLog(this.getClass(),"police_wordtemplateMapper_updatebool__"+police_wordtemplateMapper_updatebool);
            if (police_wordtemplateMapper_updatebool>0){
                result.setData(1);
                changeResultToSuccess(result);
            }
        }else {
            EntityWrapper wordtemplate_param=new EntityWrapper();
            wordtemplate_param.eq("wordtemplatename",wordtemplatename);
            wordtemplate_param.ne("wordtype",2);
            wordtemplate_param.ne("wordtemplatebool",-1);
            List<Police_case> police_cases_=police_wordtemplateMapper.selectList(wordtemplate_param);
            if (null!=police_cases_&&police_cases_.size()>0){
                result.setMessage("笔录模板名称不能重复");
                return;
            }


            if (null!=multipartfile){
                //开始进行文件上传
                try {
                    String savePath=PropertiesListenerConfig.getProperty("file.wordtemplate");
                    String qg=PropertiesListenerConfig.getProperty("file.qg");

                    String oldfilename=multipartfile.getOriginalFilename();
                    String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                    String filename = wordtemplatename+"_"+DateUtil.getSeconds()+"."+suffix;//模板名称加后缀

                    if(oldfilename.endsWith(".doc")||oldfilename.endsWith(".DOC")||oldfilename.endsWith(".docx")||oldfilename.endsWith(".DOCX")){
                        String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                        LogUtil.intoLog(this.getClass(),"笔录word模板真实地址："+realurl);
                        multipartfile.transferTo(new File(realurl));
                        String downurl =OpenUtil.strMinusBasePath(qg, realurl) ;
                        LogUtil.intoLog(this.getClass(),"笔录word模板下载地址："+downurl);

                        if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                            //word转html-----------------start---------------
                            if(realurl.endsWith(".doc")){
                                String replace = realurl.replace(".doc", ".html");
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                            }else if(realurl.endsWith(".docx")){
                                String replace = realurl.replace(".docx", ".html");
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                            }
                            //word转html-----------------end-----------------


                            Base_filesave base_filesave=new Base_filesave();
                            base_filesave.setDatassid(ssid);
                            base_filesave.setUploadfilename(oldfilename);
                            base_filesave.setRealfilename(filename);
                            base_filesave.setRecordrealurl(realurl);
                            base_filesave.setRecorddownurl(downurl);
                            base_filesave.setSsid(OpenUtil.getUUID_32());
                            int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                            LogUtil.intoLog(this.getClass(),"filesaveinsert_bool__"+filesaveinsert_bool);
                            wordtemplate_filesavessid=base_filesave.getSsid();
                        }
                    }else {
                        result.setMessage("请选择doc或者docx的word文档进行上传");
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //新增
            police_wordtemplate.setSsid(OpenUtil.getUUID_32());
            police_wordtemplate.setCreatetime(new Date());
            police_wordtemplate.setWordtemplate_filesavessid(wordtemplate_filesavessid);
            police_wordtemplate.setWordtype(wordtype);
            int police_wordtemplateMapper_insertbool =  police_wordtemplateMapper.insert(police_wordtemplate);
            LogUtil.intoLog(this.getClass(),"police_wordtemplateMapper_insertbool__"+police_wordtemplateMapper_insertbool);
            ssid=police_wordtemplate.getSsid();
            if (police_wordtemplateMapper_insertbool>0){
                //文件上传后回填datassid
                if (null!=wordtemplate_filesavessid){
                    Base_filesave base_filesave=new Base_filesave();
                    base_filesave.setDatassid(ssid);
                    EntityWrapper updateew=new EntityWrapper();
                    updateew.eq("ssid",wordtemplate_filesavessid);
                    int base_filesaveMapper_updatebool =  base_filesaveMapper.update(base_filesave,updateew);
                    LogUtil.intoLog(this.getClass(),"base_filesaveMapper_updatebool__"+base_filesaveMapper_updatebool);
                }
                result.setData(1);
                changeResultToSuccess(result);
            }
        }

        if (null!=defaultbool&&defaultbool==1){
            //获取该模板的类型，并且将该类型的处理ssi全部1改为-1
            EntityWrapper updateew=new EntityWrapper();
            updateew.ne("ssid",ssid);
            List<Police_wordtemplate> police_wordtemplates=police_wordtemplateMapper.selectList(updateew);
            for (Police_wordtemplate policeWordtemplate : police_wordtemplates) {
                if (policeWordtemplate.getDefaultbool()==1){
                    policeWordtemplate.setDefaultbool(-1);
                    int police_wordtemplatemapper__defaultboolbool =police_wordtemplateMapper.updateById(policeWordtemplate);
                    LogUtil.intoLog(this.getClass(),"police_wordtemplatemapper__defaultboolbool__"+police_wordtemplatemapper__defaultboolbool);
                }
            }
        }
        return;
    }

    public void  getWordTemplateByssid(RResult result,ReqParam<GetWordTemplateByssidParam> param){
        GetWordTemplateByssidParam getWordTemplateByssidParam=param.getParam();
        if (null==getWordTemplateByssidParam){
            result.setMessage("参数为空");
            return;
        }
        String ssid=getWordTemplateByssidParam.getSsid();
        if (StringUtils.isBlank(ssid)){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        ew.eq("w.ssid",ssid);

        WordTemplate wordTemplate=new WordTemplate();
        List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(ew);
        if (null!=wordTemplates&&wordTemplates.size()==1){
            wordTemplate=wordTemplates.get(0);
        }

        if (null!=wordTemplate){
            //检测html文件是否存在-------------------------------------start----------------------------
            String wordtemplate_downurl_html=null;
            String realurl=wordTemplate.getWordtemplate_realurl();
            String downurl=wordTemplate.getWordtemplate_downurl();
            if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                if(realurl.endsWith(".doc")){
                    String replace = realurl.replace(".doc", ".html");
                    File f = new File(replace);
                    if (f.exists()) {
                        LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                        wordtemplate_downurl_html=downurl.replace(".doc", ".html");
                    }
                }else if(realurl.endsWith(".docx")){
                    String replace = realurl.replace(".docx", ".html");
                    File f = new File(replace);
                    if (f.exists()) {
                        LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                        wordtemplate_downurl_html=downurl.replace(".docx", ".html");
                    }
                }
            }
            //检测html文件是否存在-------------------------------------end----------------------------
            wordTemplate.setWordtemplate_downurl_html(wordtemplate_downurl_html);
            result.setData(wordTemplate);
            changeResultToSuccess(result);
        }

        return;
    }

    public  void changeboolWordTemplate(RResult result,ReqParam<ChangeboolWordTemplateParam> param){
        ChangeboolWordTemplateParam changeboolWordTemplateParam=param.getParam();
        if (null==changeboolWordTemplateParam){
            result.setMessage("参数为空");
            return;
        }

        String ssid=changeboolWordTemplateParam.getSsid();
        Integer defaultbool = changeboolWordTemplateParam.getDefaultbool();
        Integer wordtemplatebool = changeboolWordTemplateParam.getWordtemplatebool();

        Police_wordtemplate police_wordtemplate=new Police_wordtemplate();
        police_wordtemplate.setSsid(ssid);
        police_wordtemplate=police_wordtemplateMapper.selectOne(police_wordtemplate);
        if (null!=police_wordtemplate){
            if (null!=defaultbool&&defaultbool==1){
                //获取该模板的类型，并将该类型的都改为默认
                EntityWrapper updateew=new EntityWrapper();
                List<Police_wordtemplate> police_wordtemplates=police_wordtemplateMapper.selectList(updateew);
                for (Police_wordtemplate policeWordtemplate : police_wordtemplates) {
                    if (policeWordtemplate.getDefaultbool()==1){
                        policeWordtemplate.setDefaultbool(-1);
                        int updatebool =police_wordtemplateMapper.updateById(policeWordtemplate);
                    }
                }
            }
            if (null!=wordtemplatebool&&wordtemplatebool==-1){
                //判断是否为默认的笔录模板，如果是不允许删除
                String default_wordssid = PropertiesListenerConfig.getProperty("wordtemplate_default");
                if (StringUtils.isNotEmpty(default_wordssid)&&default_wordssid.equals(ssid)){
                    result.setMessage("该默认模板不允许被删除");
                    return;
                }
                defaultbool=-1;//状态改为已删除 并且默认状态为-1
            }
            EntityWrapper update=new EntityWrapper();
            update.eq("ssid",ssid);
            police_wordtemplate.setDefaultbool(defaultbool);
            police_wordtemplate.setWordtemplatebool(wordtemplatebool);
            int police_wordtemplateMapper_update_bool =  police_wordtemplateMapper.update(police_wordtemplate,update);
            LogUtil.intoLog(1,this.getClass(),"修改笔录word模板状态__police_wordtemplateMapper_update_bool__"+police_wordtemplateMapper_update_bool);
            if (police_wordtemplateMapper_update_bool>0){
                result.setData(police_wordtemplateMapper_update_bool);
                changeResultToSuccess(result);
            }
        }else {
            result.setMessage("未找到该笔录模板");
            LogUtil.intoLog(1,this.getClass(),"修改笔录word模板状态__未找到该笔录模板 is null__ssid__"+ssid);
        }
        return;
    }

    public void getWordTemplates(RResult result,ReqParam<GetWordTemplatesParam> paramReqParam){
        GetWordTemplatesVO vo=new GetWordTemplatesVO();
        EntityWrapper word_ew=new EntityWrapper();
        word_ew.ne("wordtype",2);
        word_ew.eq("wordtemplatebool",1);//状态为正常的
        List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word_ew);
        if (null!=wordTemplates&&wordTemplates.size()>0){
            vo.setWordTemplates(wordTemplates);
        }
        vo.setDefault_word(PropertiesListenerConfig.getProperty("wordtemplate_default"));//默认
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }
    //================================================笔录word模板=================================================================end
}
