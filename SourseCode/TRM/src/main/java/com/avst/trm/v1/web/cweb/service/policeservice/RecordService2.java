package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.conf.type.SSType;
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
import com.avst.trm.v1.feignclient.ec.req.GetSaveFilesPathByiidParam;
import com.avst.trm.v1.feignclient.ec.req.GetSavePathParam;
import com.avst.trm.v1.feignclient.ec.req.GetToOutFlushbonadingListParam;
import com.avst.trm.v1.feignclient.ec.vo.GetSavepathVO;
import com.avst.trm.v1.feignclient.ec.vo.fd.Flushbonadinginfo;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordSavepathParam;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.cache.Recordrealing_LastCache;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
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


                        GZIPThread gzipThread=new GZIPThread(folderPath,realurl,ssid,exportfilename,gztype);
                        gzipThread.start();


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

    /**
     * ==上传第三方先拼接默认模板里面的设备  后期需要更改
     * @param result
     * @param param
     * @param session
     */
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
                                                    if (!path_.endsWith("mp4")&&!path_.endsWith("st")){
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


    /**
     * 上传笔录相关信息并刻盘
     * @return
     */
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


    /**
     * 打包回放（其实打包回放应该存储组件完成）
     * @return
     */
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
                GZIPThread gzipThread=new GZIPThread(zippath,zippath,iid,zipfilename,gztype);
                gzipThread.start();

                String zipfilepath=zippath;
                if(zipfilepath.endsWith("\\")||zipfilepath.endsWith("/")){
                    zipfilepath=zipfilepath.substring(0,zipfilepath.length()-1);
                }
                if(zipfilepath.indexOf("\\") > -1){
                    zipfilepath+="\\\\"+zipfilename+gztype;
                }else {
                    zipfilepath+="/"+zipfilename+gztype;
                }
                String staticpath=PropertiesListenerConfig.getProperty("staticpath");
                String httpbasestaticpath=PropertiesListenerConfig.getProperty("httpbasestaticpath");
                String httpzipfilepath=httpbasestaticpath+OpenUtil.strMinusBasePath(staticpath,zipfilepath);
                LogUtil.intoLog(1,this.getClass(),"打包下载的地址,httpzipfilepath:"+httpzipfilepath);

                result.setData(httpzipfilepath);
                this.changeResultToSuccess(result);
            }
        }else{
            LogUtil.intoLog(4,this.getClass(),"根据iid获取文件路径异常，iid："+iid);
        }
        return result;
    }


    /**
     * 获取线程打包的进度
     * @param result
     * @param
     * @return
     */
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


    public void getCaseStatistics(RResult result, ReqParam<GetCaseStatisticsParam> paramReqParam, HttpSession session){
        GetCaseStatisticsVO vo=new GetCaseStatisticsVO();

        GetCaseStatisticsParam param=paramReqParam.getParam();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
        Calendar c = Calendar.getInstance();

        String years=param.getYearstype();
        System.out.println(years);
        if (!StringUtils.isNotBlank(years)){
            years=df.format(new Date());
        }

        //仅仅看自己的案件
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);


        EntityWrapper record_num_ew=new EntityWrapper();
        record_num_ew.eq("c.creator",user.getSsid());
        record_num_ew.ne("c.casebool",-1);
        record_num_ew.ne("r.recordbool",-1);

        EntityWrapper case_num_ew=new EntityWrapper();
        case_num_ew.ne("casebool",-1);
        case_num_ew.eq("creator",user.getSsid());

        Integer record_num=police_arraignmentMapper.getArraignmentCount(record_num_ew);//审讯数量
        Integer case_num= police_caseMapper.selectCount(case_num_ew);//案件数量


        //================================================1
        Integer case_startnum=police_caseMapper.getCase_startnum(case_num_ew);//案件开始提讯数量
        Integer case_endnum=police_caseMapper.Getcase_endnum(case_num_ew);//案件未开始提讯数量

        EntityWrapper record_num_ew2=new EntityWrapper();
        record_num_ew2.eq("c.creator",user.getSsid());
        record_num_ew2.ne("c.casebool",-1);
        List<Integer> recordbools=new ArrayList<>();
        recordbools.add(2);
        recordbools.add(3);
        record_num_ew2.in("r.recordbool",recordbools);
        Integer record_finishnum=police_arraignmentMapper.getArraignmentCount(record_num_ew2);//已完成笔录数量
        EntityWrapper record_num_ew3=new EntityWrapper();
        record_num_ew3.eq("c.creator",user.getSsid());
        record_num_ew3.ne("c.casebool",-1);
        record_num_ew3.eq("r.recordbool",1);
        Integer record_unfinishnum=police_arraignmentMapper.getArraignmentCount(record_num_ew3);//进行中的笔录数量
        EntityWrapper record_num_ew4=new EntityWrapper();
        record_num_ew4.eq("c.creator",user.getSsid());
        record_num_ew4.ne("c.casebool",-1);
        record_num_ew4.eq("r.recordbool",0);
        Integer record_waitnum=police_arraignmentMapper.getArraignmentCount(record_num_ew4);///未开始笔录数量

        vo.setCase_num(case_num==null?0:case_num);
        vo.setRecord_num(record_num==null?0:record_num);
        vo.setCase_startnum(case_startnum==null?0:case_startnum);
        vo.setCase_endnum(case_endnum==null?0:case_endnum);
        vo.setRecord_finishnum(record_finishnum==null?0:record_finishnum);
        vo.setRecord_unfinishnum(record_unfinishnum==null?0:record_unfinishnum);
        vo.setRecord_waitnum(record_waitnum==null?0:record_waitnum);
        //================================================1

        //-----------------------------------------------------------------------------------------------------2

        List<Integer> case_monthnum_y=new ArrayList<>();//12月案件
        List<Integer> record_monthnum_y=new ArrayList<>();//12月审讯
        for (int i = 1; i < 13; i++) {
            EntityWrapper case_monthnum_y_ew=new EntityWrapper();
            case_monthnum_y_ew.ne("casebool",-1);
            case_monthnum_y_ew.eq("creator",user.getSsid());
            case_monthnum_y_ew.where("date_format(createtime,'%m')={0} and  date_format(createtime,'%Y')={1}",String.format("%02d",i),years);
            Integer now_case=police_caseMapper.selectCount(case_monthnum_y_ew);
            case_monthnum_y.add(now_case==null?0:now_case);

            EntityWrapper record_monthnum_y_ew=new EntityWrapper();
            record_monthnum_y_ew.eq("c.creator",user.getSsid());
            record_monthnum_y_ew.ne("c.casebool",-1);
            record_monthnum_y_ew.ne("r.recordbool",-1);
            record_monthnum_y_ew.where("date_format(r.createtime,'%m')={0} and  date_format(r.createtime,'%Y')={1}",String.format("%02d",i),years);
            Integer now_record=police_arraignmentMapper.getArraignmentCount(record_monthnum_y_ew);
            record_monthnum_y.add(now_record==null?0:now_record);
        }
        vo.setRecord_monthnum_y(record_monthnum_y);
        vo.setCase_monthnum_y(case_monthnum_y);
        //-----------------------------------------------------------------------------------------------------2



        //-----------------------------------------------------------------------------------------------------3
        case_num_ew.where(" date_format(createtime,'%Y')={0}",years);
        Integer case_num_y=police_caseMapper.selectCount(case_num_ew);//案件总数
        Integer case_startnum_y=police_caseMapper.getCase_startnum(case_num_ew);//案件开始提讯数量
        Integer case_endnum_y=police_caseMapper.Getcase_endnum(case_num_ew);//案件未开始提讯数量



        //总
        EntityWrapper recordparam3=new EntityWrapper();
        recordparam3.eq("c.creator",user.getSsid());
        recordparam3.ne("c.casebool",-1);
        recordparam3.ne("r.recordbool",-1);
        recordparam3.where(" date_format(r.createtime,'%Y')={0}",years);
        Integer record_num_y=police_arraignmentMapper.getArraignmentCount(recordparam3);//笔录总数
        //进行中
        EntityWrapper recordparam1=new EntityWrapper();
        recordparam1.eq("c.creator",user.getSsid());
        recordparam1.ne("c.casebool",-1);
        recordparam1.eq("r.recordbool",1);
        recordparam1.where(" date_format(r.createtime,'%Y')={0}",years);
        Integer record_unfinishnum_y= police_arraignmentMapper.getArraignmentCount(recordparam1);
        //已完成
        EntityWrapper recordparam2=new EntityWrapper();
        recordparam2.eq("c.creator",user.getSsid());
        recordparam2.ne("c.casebool",-1);
        List<Integer> recordbools2=new ArrayList<>();
        recordbools2.add(2);
        recordbools2.add(3);
        recordparam2.in("r.recordbool",recordbools2);
        recordparam2.where(" date_format(r.createtime,'%Y')={0}",years);
        Integer record_finishnum_y =police_arraignmentMapper.getArraignmentCount(recordparam2);
        //未开始
        EntityWrapper recordparam4=new EntityWrapper();
        recordparam4.eq("c.creator",user.getSsid());
        recordparam4.ne("c.casebool",-1);
        recordparam4.eq("r.recordbool",0);
        recordparam4.where(" date_format(r.createtime,'%Y')={0}",years);
        Integer record_waitnum_y= police_arraignmentMapper.getArraignmentCount(recordparam4);
        //-----------------------------------------------------------------------------------------------------3


        vo.setCase_num_y(case_num_y==null?0:case_num_y);
        vo.setRecord_num_y(record_num_y==null?0:record_num_y);
        vo.setRecord_finishnum_y(record_finishnum_y==null?0:record_finishnum_y);
        vo.setRecord_unfinishnum_y(record_unfinishnum_y==null?0:record_unfinishnum_y);
        vo.setRecord_waitnum_y(record_waitnum_y==null?0:record_waitnum_y);
        vo.setCase_startnum_y(case_startnum_y==null?0:case_startnum_y);
        vo.setCase_endnum_y(case_endnum_y==null?0:case_endnum_y);


        AppCacheParam appCacheParam = AppCache.getAppCacheParam();
        vo.setClientname(appCacheParam.getTitle());




        vo.setDq_y(years);
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }

    /***************************笔录问答实时缓存****start***************************/
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


    /***************************笔录问答实时缓存****end***************************/

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


                System.out.println((new Date()).getTime());
                XwpfTUtil.replaceAndGenerateWord(wordtemplate_realurl,wordrealurl,dataMap,null);
                System.out.println((new Date()).getTime());

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
                    int police_recordMapper_updatebool=police_recordMapper.update(record,e);
                    LogUtil.intoLog(this.getClass(),"police_recordMapper_updatebool__"+police_recordMapper_updatebool);
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

                System.out.println((new Date()).getTime());
                XwpfTUtil.replaceAndGenerateWord(wordtemplate_realurl,wordrealurl,dataMap,null);
                System.out.println((new Date()).getTime());

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
                }
                int police_recordMapper_updatebool=police_recordMapper.update(record,e);
                LogUtil.intoLog(this.getClass(),"police_recordMapper_updatebool__"+police_recordMapper_updatebool);

                exportWordVO.setWord_path(worddownurl);
                result.setData(exportWordVO);
                changeResultToSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }


    /**
     * 收集导出数据
     * @param recordssid
     * @return talkbool 是否为头文件
     */
    public  Map<String,String> exportData(String recordssid,boolean talkbool) {
        //根据笔录ssid获取录音数据
        EntityWrapper recordParam = new EntityWrapper();
        recordParam.eq("r.ssid", recordssid);
        Record record = police_recordMapper.getRecordBySsid(recordParam);
        Map<String, String> dataMap = new HashMap<String, String>();
        if (null != record) {
            String talk="";
            if (!talkbool){
                EntityWrapper ew = new EntityWrapper();
                ew.eq("r.ssid", record.getSsid());
                ew.orderBy("p.ordernum", true);
                ew.orderBy("p.createtime", true);
                /*  List<RecordToProblem> questionandanswer = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(ew);*/
                List<RecordToProblem> questionandanswer=RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合
                if (null != questionandanswer && questionandanswer.size() > 0) {
                    for (RecordToProblem problem : questionandanswer) {
                        String gnlist=getSQEntity.getGnlist();
                        if (gnlist.indexOf(SQVersion.FY_T)!= -1){
                            //法院的
                            talk+= problem.getProblem()+"\r";
                        }else {
                            //其他
                            talk+="问："+problem.getProblem()+"\r";
                            String problemssid = problem.getSsid();
                            if (StringUtils.isNotBlank(problemssid)) {
                                EntityWrapper answerParam = new EntityWrapper();
                                answerParam.eq("recordtoproblemssid", problemssid);
                                answerParam.orderBy("ordernum", true);
                                answerParam.orderBy("createtime", true);
                                List<Police_answer> answers = police_answerMapper.selectList(answerParam);
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
            }
            System.out.println(talk);


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
            recorduserinfosParam.eq("a.recordssid", record.getSsid());
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
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
            String both ="未知";
            if (null!=police_userinfo.getBoth()){
                both = sdf2.format(police_userinfo.getBoth());
            }


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

        }
        return dataMap;
    }


    /******************************笔录word模板******start*********************/
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

        String wordtemplate_filesavessid=null;
        if(StringUtils.isNotBlank(ssid)){
            EntityWrapper wordtemplate_param=new EntityWrapper();
            wordtemplate_param.eq("wordtemplatename",wordtemplatename);
            wordtemplate_param.ne("ssid",ssid);
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
        Integer defaultbool=changeboolWordTemplateParam.getDefaultbool();


        Police_wordtemplate police_wordtemplate=new Police_wordtemplate();
        police_wordtemplate.setSsid(ssid);
        police_wordtemplate=police_wordtemplateMapper.selectOne(police_wordtemplate);
        if (null!=police_wordtemplate){
            if (defaultbool==1){
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
            EntityWrapper update=new EntityWrapper();
            update.eq("ssid",ssid);
            police_wordtemplate.setDefaultbool(defaultbool);
            int updatebool2 =  police_wordtemplateMapper.update(police_wordtemplate,update);

            result.setData(1);
            changeResultToSuccess(result);
        }
        return;
    }

    public void getWordTemplates(RResult result,ReqParam<GetWordTemplatesParam> paramReqParam){
        GetWordTemplatesVO vo=new GetWordTemplatesVO();
        EntityWrapper word_ew=new EntityWrapper();
        word_ew.ne("wordtype",2);
        List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word_ew);
        if (null!=wordTemplates&&wordTemplates.size()>0){
            vo.setWordTemplates(wordTemplates);
        }
        vo.setDefault_word(PropertiesListenerConfig.getProperty("wordtemplate_default"));//默认
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }
/******************************笔录word模板*****end**********************/
}
