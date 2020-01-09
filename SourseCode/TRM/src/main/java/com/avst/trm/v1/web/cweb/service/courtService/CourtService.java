package com.avst.trm.v1.web.cweb.service.courtService;

import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.poiwork.HtmlToWord;
import com.avst.trm.v1.common.util.poiwork.WordToHtmlUtil;
import com.avst.trm.v1.common.util.poiwork.WordToPDF;
import com.avst.trm.v1.common.util.poiwork.XwpfTUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetPhssidByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.req.SetMCTagTxtParam_out;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.conf.UserinfogradeType;
import com.avst.trm.v1.web.cweb.req.courtreq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.req.policereq.ExportWordParam;
import com.avst.trm.v1.web.cweb.req.policereq.UploadWordTemplateParam;
import com.avst.trm.v1.web.cweb.req.policereq.param.ArrUserExpandParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService2;
import com.avst.trm.v1.web.cweb.vo.courtvo.Export_asrVO;
import com.avst.trm.v1.web.cweb.vo.courtvo.Exporttemplate_ueVO;
import com.avst.trm.v1.web.cweb.vo.courtvo.GetUserinfogradeByssidVO;
import com.avst.trm.v1.web.cweb.vo.courtvo.GetUserinfogradePageVO;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckKeywordVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("courtService")
public class CourtService extends BaseService {
    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;

    private Gson gson=new Gson();

    @Autowired
    private RecordService2 recordService2;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Police_userinfototypeMapper police_userinfototypeMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;


    @Autowired
    private OutService outService;

    @Autowired
    private MeetingControl meetingControl;


    public void getUserinfogradePage(RResult result, GetUserinfogradePageParam param){
        GetUserinfogradePageVO vo=new GetUserinfogradePageVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__getUserinfogradePage__param is null__"+param);
            return;
         }

        EntityWrapper getew=new EntityWrapper();
         String gradename=param.getGradename();
         if (StringUtils.isNotBlank(gradename)){
             getew.eq("gradename",gradename);
         }

        getew.orderBy("gradetype",true);
        getew.orderBy("createtime",false);
        int count=police_userinfogradeMapper.countgetUserinfogradePage(getew);
        param.setRecordCount(count);

        Page<Userinfograde> page=new Page<Userinfograde>(param.getCurrPage(),param.getPageSize());
        List<Userinfograde> list=police_userinfogradeMapper.getUserinfogradePage(page,getew);
        vo.setPageparam(param);

        if (null!=list&&list.size()>0){
            vo.setPagelist(list);
        }

        vo.setPagelist(list);
        result.setData(vo);
        changeResultToSuccess(result);
        return;

    }

    public void getUserinfogradeByssid(RResult result, GetUserinfogradeByssidParam param){
        GetUserinfogradeByssidVO vo=new GetUserinfogradeByssidVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__getUserinfogradeByssid__param is null__"+param);
            return;
        }
        String ssid=param.getSsid();
        if (StringUtils.isNotBlank(ssid)){
            Police_userinfograde police_userinfograde=new Police_userinfograde();
            police_userinfograde.setSsid(ssid);
            police_userinfograde = police_userinfogradeMapper.selectOne(police_userinfograde);
            if (null!=police_userinfograde){
                Userinfograde userinfograde=gson.fromJson(gson.toJson(police_userinfograde),Userinfograde.class);
                if (null!=userinfograde){
                    vo.setUserinfograde(userinfograde);
                    result.setData(vo);
                    changeResultToSuccess(result);
                    return;
                }
            }
        }
        return;
    }

    public void addUserinfograde(RResult result, AddUserinfogradeParam param){
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__addUserinfograde__param is null__"+param);
            return;
        }

        Date createtime = param.getCreatetime();
        param.setCreatetime(null);
        Police_userinfograde police_userinfograde = gson.fromJson(gson.toJson(param), Police_userinfograde.class);
        if (null!=police_userinfograde){


            EntityWrapper ordernum_param=new EntityWrapper();
            ordernum_param.eq("gradename",police_userinfograde.getGradename());
            List<Police_userinfograde> police_userinfogrades=police_userinfogradeMapper.selectList(ordernum_param);
            if (null!=police_userinfogrades&&police_userinfogrades.size()>0){
                result.setMessage("级别名称已存在");
                return;
            }

            police_userinfograde.setCreatetime(new Date());
            police_userinfograde.setSsid(OpenUtil.getUUID_32());
            police_userinfograde.setTypessid("13");//法院类型13
            int police_userinfogradeMapper_insert_bool = police_userinfogradeMapper.insert(police_userinfograde);
            if (police_userinfogradeMapper_insert_bool >0) {
                result.setData(police_userinfogradeMapper_insert_bool);
                changeResultToSuccess(result);
                LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_insert_bool_"+police_userinfogradeMapper_insert_bool);
                return;
            }
        }
        return;
    }

    public void updateUserinfograde(RResult result, UpdateUserinfogradeParam param){
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__updateUserinfograde__param is null__"+param);
            return;
        }

        Date createtime = param.getCreatetime();
        param.setCreatetime(null);
        Police_userinfograde police_userinfograde=gson.fromJson(gson.toJson(param),Police_userinfograde.class);
        police_userinfograde.setCreatetime(createtime);
        if (null!=police_userinfograde){
            String ssid=police_userinfograde.getSsid();
            if (null!=ssid){

                EntityWrapper ordernum_param=new EntityWrapper();
                ordernum_param.eq("gradename",police_userinfograde.getGradename());
                ordernum_param.ne("ssid",ssid);
                List<Police_userinfograde> police_userinfogrades=police_userinfogradeMapper.selectList(ordernum_param);
                if (null!=police_userinfogrades&&police_userinfogrades.size()>0){
                    result.setMessage("级别名称已存在");
                    return;
                }


                EntityWrapper ew=new EntityWrapper();
                ew.eq("ssid",ssid);
                int police_userinfogradeMapper_update_bool = police_userinfogradeMapper.update(police_userinfograde,ew);
                if (police_userinfogradeMapper_update_bool >0) {
                    result.setData(police_userinfogradeMapper_update_bool);
                    changeResultToSuccess(result);
                    LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_update_bool_"+police_userinfogradeMapper_update_bool);
                    return;
                }
            }else {
                result.setMessage("参数为空");
                LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_update_bool_ssid is null");
                return;
            }
        }
        return;
    }

    public  void importtemplate_ue(RResult result, ReqParam<Importtemplate_ueParam> param, MultipartFile file){
        //请求参数转换
        Importtemplate_ueParam importtemplate_ueParam=gson.fromJson(gson.toJson(param.getParam()), Importtemplate_ueParam.class);
        if (null==importtemplate_ueParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=importtemplate_ueParam.getRecordssid();
        LogUtil.intoLog(1,this.getClass(),"导入word笔录模板____参数_____recordssid："+recordssid);
        if (StringUtils.isEmpty(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        if (file.isEmpty()) {
            result.setMessage("请选择doc或者docx文件进行导入");
            return;
        }
        String filename=file.getOriginalFilename();
        if (filename.endsWith(".doc")||filename.endsWith(".docx")){
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                String html= WordToHtmlUtil.wordToHtml_in2str(inputStream,filename);
                //替换对应笔录参数===start
                Map<String,String> dataMap=recordService2.exportData(recordssid,false);
                if (dataMap != null) {
                    for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                        html = html.replace(entry.getKey(), entry.getValue());
                    }
                }
                //替换对应笔录参数===end
                result.setData(html);
                changeResultToSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            result.setMessage("请选择doc或者docx文件进行导入");
            return;
        }
        return;
    }

    public void exporttemplate_ue(RResult result, Exporttemplate_ueParam param){
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        Exporttemplate_ueVO vo=new Exporttemplate_ueVO();
        String recordssid=param.getRecordssid();
        Integer exporttype=param.getExporttype();
        LogUtil.intoLog(1,this.getClass(),"导出的笔录word或者pdf__参数：__recordssid__"+recordssid+"__exporttype__"+exporttype);
        if (StringUtils.isEmpty(recordssid)||null==exporttype){
            result.setMessage("参数为空");
            return;
        }
        vo.setExporttype(exporttype);

        EntityWrapper recordParam = new EntityWrapper();
        recordParam.eq("r.ssid", recordssid);
        Record record = police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record){
            List<RecordToProblem> recordToProblems= RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合
            if (null!=recordToProblems&&recordToProblems.size()>0){
                //1、收集数据
                String content="";
                for (RecordToProblem recordToProblem : recordToProblems) {
                    content+=recordToProblem.getProblem();
                }

                ///2、获取导出的地址
                String uploadpath= PropertiesListenerConfig.getProperty("upload.basepath");
                String savePath=PropertiesListenerConfig.getProperty("file.recordwordOrpdf");
                String qg=PropertiesListenerConfig.getProperty("file.qg");

                //获取生成的真实地址
                String filename=record.getRecordname().replace(" ", "").replace("\"", "");
                String wordfilename=filename+".doc";
                String wordrealurl = OpenUtil.createpath_fileByBasepath(savePath, wordfilename);
                LogUtil.intoLog(this.getClass(),"导出的笔录word或者pdf真实地址__"+wordrealurl);
                //获取生成的下载地址
                String worddownurl =uploadpath+OpenUtil.strMinusBasePath(qg, wordrealurl) ;
                LogUtil.intoLog(this.getClass(),"导出的笔录word或者pdf下载地址____"+worddownurl);

                //html转换成word
                boolean  htmltowordbool=HtmlToWord.HtmlToWord(wordrealurl,content);
                if (htmltowordbool){
                    //word
                    String oldwordrealurl=record.getWordrealurl();//查看之前是否存在word地址
                    String word_filesavessid=record.getWord_filesavessid();
                    if (StringUtils.isNotBlank(wordrealurl)&&StringUtils.isNotBlank(worddownurl)){
                        Base_filesave base_filesave=new Base_filesave();
                        base_filesave.setDatassid(recordssid);
                        base_filesave.setUploadfilename(wordfilename);
                        base_filesave.setRealfilename(wordfilename);
                        base_filesave.setRecordrealurl(wordrealurl);
                        base_filesave.setRecorddownurl(worddownurl);
                        if (StringUtils.isNotBlank(oldwordrealurl)&&StringUtils.isNotEmpty(word_filesavessid)){
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
                        vo.setWord_downurl(worddownurl);
                    }

                    //判断是否还需要word转pdf：暂时转换不了
                    String pdf_filesavessid=null;
                    if (exporttype==2){
                        //pdf
                       String pdffilename=filename+".pdf";
                        String pdfrealurl=OpenUtil.createpath_fileByBasepath(savePath, pdffilename);
                        LogUtil.intoLog(this.getClass(),"导出的笔录word或者pdf真实地址__"+pdfrealurl);
                        String pdfdownurl=uploadpath+OpenUtil.strMinusBasePath(qg, pdfrealurl) ;
                        LogUtil.intoLog(this.getClass(),"导出的笔录word或者pdf下载地址__"+pdfdownurl);
                        boolean wordtopdf_bool= WordToPDF.word2pdf(pdfrealurl,wordrealurl);
                        if (wordtopdf_bool){
                            String oldpdfrealurl=record.getPdfrealurl();
                            pdf_filesavessid=record.getPdf_filesavessid();
                            if (StringUtils.isNotBlank(pdfdownurl)&&StringUtils.isNotBlank(pdfrealurl)){
                                Base_filesave base_filesave=new Base_filesave();
                                base_filesave.setDatassid(recordssid);
                                base_filesave.setUploadfilename(pdffilename);
                                base_filesave.setRealfilename(pdffilename);
                                base_filesave.setRecordrealurl(pdfrealurl);
                                base_filesave.setRecorddownurl(pdfdownurl);
                                if (StringUtils.isNotBlank(oldpdfrealurl)&&StringUtils.isNotEmpty(pdf_filesavessid)){
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
                                vo.setPdf_downurl(pdfdownurl);
                            }
                        }
                    }

                    //开始进行保存操作
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

                        result.setData(vo);
                        changeResultToSuccess(result);
                    }
                }
            }
        }
        return;
    }

    public void export_asr(RResult result,Export_asrParam param){
        Export_asrVO vo=new Export_asrVO();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=param.getRecordssid();
        LogUtil.intoLog(1,this.getClass(),"export_asr__recordssid："+recordssid);
        if (StringUtils.isEmpty(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper recordParam=new EntityWrapper();
        recordParam.eq("r.ssid",recordssid);
        Record record=police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record){
            Integer recordbool=record.getRecordbool();
            if (!(recordbool.intValue()==2||recordbool.intValue()==3)){
                result.setMessage("该笔录未结束");
                return;
            }
            String mtssid = null;
            Map<String,String> usergradesMap=new HashMap<>();
            Police_arraignment police_arraignment=new Police_arraignment();
            police_arraignment.setRecordssid(recordssid);
            police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
            if (null!=police_arraignment) {
                 mtssid = police_arraignment.getMtssid();

                 //找到人员对应多角色
                EntityWrapper arre=new EntityWrapper();
                arre.eq("arraignmentssid",police_arraignment.getSsid());
                List<Police_arraignmentexpand> arraignmentexpands = police_arraignmentexpandMapper.selectList(arre);
                if (null!=arraignmentexpands&&arraignmentexpands.size()>0){
                    for (Police_arraignmentexpand arraignmentexpand : arraignmentexpands) {
                        String gradessid=arraignmentexpand.getExpandname();//拓展名为登记表ssid
                        String userssid=arraignmentexpand.getExpandvalue();//拓展值为用户的ssid
                        if (StringUtils.isNotBlank(gradessid)&&StringUtils.isNotBlank(userssid)){
                            //查找等级
                            Police_userinfograde police_userinfograde=new Police_userinfograde();
                            police_userinfograde.setSsid(gradessid);
                            police_userinfograde=police_userinfogradeMapper.selectOne(police_userinfograde);
                            if (null!=police_userinfograde){
                                usergradesMap.put(userssid,police_userinfograde.getGradename());
                            }
                        }
                    }
                }
            }

            LogUtil.intoLog(1,this.getClass(),"export_asr__mtssid："+mtssid);
            if (StringUtils.isEmpty(mtssid)){
                result.setMessage("不支持导出");
                return;
            }else {
                //开始收集数据
                String content="";//导出的内容收集
                //getRecord：获取会议asr识别数据
                GetMCVO getMCVO=new GetMCVO();
                ReqParam getrecord_param=new ReqParam<>();
                GetPhssidByMTssidParam_out getPhssidByMTssidParam_out=new GetPhssidByMTssidParam_out();
                getPhssidByMTssidParam_out.setMcType(MCType.AVST);
                getPhssidByMTssidParam_out.setMtssid(mtssid);
                getrecord_param.setParam(getPhssidByMTssidParam_out);
                RResult getrecord_rr=new RResult();
                getrecord_rr= outService.getRecord(getrecord_rr,getrecord_param);
                if (null!=getrecord_rr&&getrecord_rr.getActioncode().equals(Code.SUCCESS.toString())){
                    getMCVO=gson.fromJson(gson.toJson(getrecord_rr.getData()),GetMCVO.class);
                    if (null!=getMCVO){
                        List<AsrTxtParam_toout> asrTxtParam_toouts=getMCVO.getList();
                        if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                            for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                                String txt=asrTxtParam_toout.getTxt();
                                String userssid= asrTxtParam_toout.getUserssid();
                                String starttime= asrTxtParam_toout.getAsrstartime();
                                String usergrade="未知";
                                if (!usergradesMap.isEmpty()){
                                    usergrade=usergradesMap.get(userssid)==null?"未知":usergradesMap.get(userssid);
                                }
                                content+= "<div style='text-align: left;margin: 5px 0;'>" +
                                        "<span style='font-size:16pt;color:#000000;'>"+usergrade+"："+txt+"</span></div>";
                            }
                        }else {
                            result.setMessage("未找到可导出的语音识别内容");
                            return;
                        }
                    }
                    LogUtil.intoLog(1,this.getClass(),"export_asr__outService.getRecord__请求成功");
                }else {
                    Object msg=getrecord_rr==null?getrecord_rr:getrecord_rr.getMessage();
                    LogUtil.intoLog(1,this.getClass(),"export_asr__outService.getRecord__请求失败__"+msg);
                    result.setMessage(String.valueOf(msg));
                    return;
                }

                //收集完 开始导出
                String filename=record.getRecordname().replace(" ", "").replace("\"", "");
                filename=filename+"_asr.doc";
                String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");
                String savePath=PropertiesListenerConfig.getProperty("file.phreport");//使用情绪报告的保存地址
                String qg=PropertiesListenerConfig.getProperty("file.qg");

                String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                LogUtil.intoLog(1,this.getClass(),"export_asr__真实地址__："+realurl);
                boolean  htmltowordbool = HtmlToWord.HtmlToWord(realurl,content);
                if (htmltowordbool) {
                    String downurl = OpenUtil.strMinusBasePath(qg, realurl);
                    LogUtil.intoLog(1, this.getClass(), "情绪报表的下载地址__：" + downurl);

                    if (StringUtils.isNotBlank(realurl) && StringUtils.isNotBlank(downurl)) {
                        //添加数据库
                        Base_filesave base_filesave = new Base_filesave();
                        base_filesave.setDatassid(recordssid);
                        base_filesave.setUploadfilename(filename);
                        base_filesave.setRealfilename(filename);
                        base_filesave.setRecordrealurl(realurl);
                        base_filesave.setRecorddownurl(downurl);
                        base_filesave.setSsid(OpenUtil.getUUID_32());
                        base_filesave.setFilebool(1);
                        base_filesave.setFiletype("word");//固定类型
                        base_filesave.setCreatetime(DateUtil.getDateAndMinute());
                        int filesaveinsert_bool = base_filesaveMapper.insert(base_filesave);
                        LogUtil.intoLog(1, this.getClass(), "export_asr__filesaveinsert_bool__" + filesaveinsert_bool);
                        if (filesaveinsert_bool > 0) {
                            vo.setDownurl(uploadbasepath + downurl);
                            result.setData(vo);
                            changeResultToSuccess(result);
                        }
                    } else {
                        LogUtil.intoLog(1, this.getClass(), "情绪报表地址有误__realurl__" + realurl + "__downurl__" + downurl);
                    }
                }
            }
        }else {
            result.setMessage("未找到该笔录");
            return;
        }
        return;
    }


    public void  setMCTagTxtreal(RResult result,SetMCTagTxtrealParam param){
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        String mtssid=param.getMtssid();
        String starttime=param.getStarttime();
        String tagtxt=param.getTagtxt();
        String userssid=param.getUserssid();
        LogUtil.intoLog(1,this.getClass(),"setMCTagTxtreal参数：__mtssid："+mtssid+"__starttime："+starttime+"__tagtxt："+tagtxt+"__userssid："+userssid);
        if (StringUtils.isNotEmpty(mtssid)&&StringUtils.isNotEmpty(starttime)&&StringUtils.isNotEmpty(tagtxt)&&StringUtils.isNotEmpty(userssid)){
            ReqParam<SetMCTagTxtParam_out> setMCTagTxtParam_outReqParam=new ReqParam<>();
            SetMCTagTxtParam_out setMCTagTxtParam_out=new SetMCTagTxtParam_out();
            setMCTagTxtParam_out.setMcType(MCType.AVST);
            setMCTagTxtParam_out.setMtssid(mtssid);
            setMCTagTxtParam_out.setStarttime(starttime);
            setMCTagTxtParam_out.setTagtxt(tagtxt);
            setMCTagTxtParam_out.setUserssid(userssid);
            setMCTagTxtParam_outReqParam.setParam(setMCTagTxtParam_out);
            RResult rr = meetingControl.setMCTagTxt(setMCTagTxtParam_outReqParam);
            if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
               LogUtil.intoLog(1,this.getClass(),"setMCTagTxtreal__meetingControl.setMCTagTxt请求失败");
                changeResultToSuccess(result);
            }else {
               LogUtil.intoLog(1,this.getClass(),"setMCTagTxtreal__meetingControl.setMCTagTxt请求成功");
            }
        }else {
            result.setMessage("参数为空");
            return;
        }
        return;
    }

    public void updateCaseToUser(RResult result,UpdateCaseToUserParam param){
         String recordssid=param.getRecordssid();//笔录ssid

        Police_case case_=param.getCase_();//案件信息
        Police_arraignment arraignment=param.getArraignment();//提讯信息

         List<UserInfo> arraignmentexpand=param.getArraignmentexpand();//拓展表数据：针对未存在用户，主要用于外来人员
         List<ArrUserExpandParam> arrUserExpandParams=param.getArrUserExpandParams();//拓展表数据;针对已存在用户：主要用于内部管理员

        LogUtil.intoLog(1,this.getClass(),"法院人员案件编辑___updateCaseToUser_recordssid__"+recordssid);
        if (StringUtils.isBlank(recordssid)){
            result.setMessage("系统异常");
            return;
        }

        String mtssid=null;//是否存在会议

        //根据笔录ssid获取提讯信息
        Police_arraignment police_arraignment=new Police_arraignment();
        police_arraignment.setRecordssid(recordssid);
        police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
        if (null!=police_arraignment){
            String arraignmentssid=police_arraignment.getSsid();//提讯ssid
            String userssid=police_arraignment.getUserssid();//被询问人ssid

            mtssid=police_arraignment.getMtssid();


            //修改提讯信息
            EntityWrapper arraignmentupdate_ew=new EntityWrapper();
            arraignmentupdate_ew.eq("ssid",arraignmentssid);
            police_arraignment.setAsknum(arraignment.getAsknum());
            police_arraignment.setOtheradminssid(arraignment.getOtheradminssid());
            police_arraignment.setRecordadminssid(arraignment.getRecordadminssid());
            int police_arraignmentMapper_update_bool=police_arraignmentMapper.update(police_arraignment,arraignmentupdate_ew);
            LogUtil.intoLog(1,this.getClass(),"法院人员案件编辑___police_arraignmentMapper_update_bool___"+police_arraignmentMapper_update_bool);


          List<Police_arraignmentexpand> addarraignmentexpands=new ArrayList<>();//需要变动的

            //修改外部人员信息
            if (null!=arraignmentexpand&&arraignmentexpand.size()>0){
                for (UserInfo userInfo : arraignmentexpand) {
                    if (null!=userInfo){
                        String userinfogradessid=userInfo.getUserinfogradessid();
                        if (StringUtils.isNotEmpty(userinfogradessid)){
                            if (StringUtils.isBlank(userInfo.getCardtypessid())){
                                userInfo.setCardtypessid(PropertiesListenerConfig.getProperty("cardtype_default"));
                            }
                            String userInfocardnum = userInfo.getCardnum();//人员证件号码
                            String userInfossid_=userInfo.getSsid();


                            List<UserInfo> checkuserInfoinfos=new ArrayList<>();
                            EntityWrapper checkuserInfoparam=new EntityWrapper();
                            checkuserInfoparam.eq("ut.cardtypessid",userInfo.getCardtypessid());

                            if (StringUtils.isNotEmpty(userInfossid_)){
                                checkuserInfoparam.eq("u.ssid",userInfossid_);
                                checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                            }else if (StringUtils.isNotEmpty(userInfocardnum)&&(null==checkuserInfoinfos||checkuserInfoinfos.size()<1)){
                                checkuserInfoparam.eq("ut.cardnum",userInfocardnum);
                                checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                            }


                            if (null!=checkuserInfoinfos&&checkuserInfoinfos.size()==1){
                                UserInfo userinfo_=checkuserInfoinfos.get(0);
                                userInfossid_=userinfo_.getSsid();


                                EntityWrapper ew1=new EntityWrapper();
                                ew1.eq("cardnum",userInfo.getCardnum());
                                ew1.eq("cardtypessid",userInfo.getCardtypessid());
                                ew1.ne("userssid",userInfossid_);
                                List<Police_userinfototype> police_userinfototypes=police_userinfototypeMapper.selectList(ew1);//人员证件类型对应表

                                //判断会议状态再去先判断证件号是否存在；会议存在 人员不可更换；用户证件号不可使用已存在的人的
                                //会议不存在 可以使用更换或者修改证件号以及人员
                                if (StringUtils.isNotBlank(mtssid)){
                                    //会议存在：证件号使用已存在的人
                                    if (null!=police_userinfototypes&&police_userinfototypes.size()>0){
                                        LogUtil.intoLog(1,this.getClass(),"已存在对应证件号人员不允许修改********************************mtssid："+mtssid);
                                        result.setMessage("对应证件号已存在，请更换其他证件号");
                                        return;
                                    }
                                }else {
                                    //会议不存在：更换用户 使用证件号对应的用户
                                  /*  if (null!=police_userinfototypes&&police_userinfototypes.size()>0){
                                        userInfossid_=police_userinfototypes.get(0).getUserssid();//使用证件号对应的人员
                                        //需要更换其他人：可能需要修改提讯表等等信息：==暂停操作==组件化之后处理pp

                                    }*/
                                }
                                //修改用户信息
                                EntityWrapper updateuserinfoParam=new EntityWrapper();
                                updateuserinfoParam.eq("ssid",userInfossid_);
                                Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                                int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
                                LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                                if (updateuserinfo_bool>0){
                                    if (StringUtils.isNotEmpty(userInfo.getCardnum())&&StringUtils.isNotEmpty(userInfo.getCardtypessid())){
                                        if (null==police_userinfototypes||police_userinfototypes.size()<1){
                                            Police_userinfototype police_userinfototype=new Police_userinfototype();
                                            police_userinfototype.setCardnum(userInfo.getCardnum());
                                            EntityWrapper ew2=new EntityWrapper();
                                            ew2.eq("cardtypessid",userInfo.getCardtypessid());
                                            ew2.eq("userssid",userInfossid_);
                                            int police_userinfototypeMapper_update_bool=police_userinfototypeMapper.update(police_userinfototype,ew2);
                                            LogUtil.intoLog(1,this.getClass(),"police_userinfototypeMapper_update_bool__"+police_userinfototypeMapper_update_bool);
                                         }else {
                                            result.setMessage("对应证件号已存在，请更换其他证件号");
                                            return;
                                        }
                                    }
                                }
                            }else  if (null==checkuserInfoinfos||checkuserInfoinfos.size()<1){
                                //第二人员：
                                //案件类型
                                //案件号
                                String time2=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                                if (StringUtils.isBlank(userInfo.getCardnum())){
                                    userInfo.setCardnum("未知_"+time2);//默认身份证号码
                                }

                              //新增
                                userInfo.setSsid(OpenUtil.getUUID_32());
                                userInfo.setCreatetime(new Date());
                                Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                                int insertuserinfo_bool = police_userinfoMapper.insert(police_userinfo);
                                LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
                                if (insertuserinfo_bool>0){
                                    Police_userinfototype police_userinfototype=new Police_userinfototype();
                                    police_userinfototype.setCardnum(userInfo.getCardnum());//
                                    police_userinfototype.setSsid(OpenUtil.getUUID_32());
                                    police_userinfototype.setCreatetime(new Date());
                                    police_userinfototype.setCardtypessid(userInfo.getCardtypessid());//
                                    police_userinfototype.setUserssid(userInfo.getSsid());
                                    int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
                                    LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
                                    if (insertuserinfototype_bool>0){
                                        userInfossid_=police_userinfo.getSsid();
                                    }
                                }
                            }else {
                                LogUtil.intoLog(this.getClass(),"拓展数据人员数据可能有异常啊啊啊啊啊啊啊啊啊啊啊啊啊啊____"+checkuserInfoinfos);
                            }
                            LogUtil.intoLog(1,this.getClass(),"法院人员案件编辑___添加拓展表数据外部人员__userInfossid："+userInfossid_+"__userinfogradessid："+userinfogradessid);
                            if (StringUtils.isNotEmpty(userInfossid_)&&StringUtils.isNotEmpty(userinfogradessid)){
                                    Police_arraignmentexpand police_arraignmentexpand=new Police_arraignmentexpand();
                                    police_arraignmentexpand.setArraignmentssid(arraignmentssid);
                                    police_arraignmentexpand.setSsid(OpenUtil.getUUID_32());
                                    police_arraignmentexpand.setCreatetime(new Date());
                                    police_arraignmentexpand.setExpandname(userinfogradessid);
                                    police_arraignmentexpand.setExpandvalue(userInfossid_);
                                   addarraignmentexpands.add(police_arraignmentexpand);
                            }
                        }
                    }
                }
            }
            //修改内部人员
            if (null!=arrUserExpandParams&&arrUserExpandParams.size()>0){
                for (ArrUserExpandParam arrUserExpandParam : arrUserExpandParams) {
                    if (null!=arrUserExpandParam){
                        String userssid_=arrUserExpandParam.getUserssid();
                        String userinfogradessid=arrUserExpandParam.getUserinfogradessid();
                        if (StringUtils.isNotBlank(userssid_)&StringUtils.isNotBlank(userinfogradessid)){
                                Police_arraignmentexpand police_arraignmentexpand=new Police_arraignmentexpand();
                                police_arraignmentexpand.setArraignmentssid(arraignmentssid);
                                police_arraignmentexpand.setSsid(OpenUtil.getUUID_32());
                                police_arraignmentexpand.setCreatetime(new Date());
                                police_arraignmentexpand.setExpandname(userinfogradessid);
                                police_arraignmentexpand.setExpandvalue(userssid_);
                                addarraignmentexpands.add(police_arraignmentexpand);
                        }else {
                            LogUtil.intoLog(1,this.getClass(),"police_arraignmentexpandMappe_insertbool__userssid_"+userssid_+"__userinfogradessid__"+userinfogradessid);
                        }
                    }
                }
            }

            //关系
            EntityWrapper arraignmentexpanddeleteew=new EntityWrapper();
            arraignmentexpanddeleteew.eq("arraignmentssid",arraignmentssid);
            int police_arraignmentexpandMappe_deletebool=police_arraignmentexpandMapper.delete(arraignmentexpanddeleteew);
            if (null!=addarraignmentexpands&&addarraignmentexpands.size()>0){
                for (Police_arraignmentexpand addarraignmentexpand : addarraignmentexpands) {
                    EntityWrapper arraignmentexpandew=new EntityWrapper();
                    arraignmentexpandew.eq("arraignmentssid",addarraignmentexpand.getArraignmentssid());
                    arraignmentexpandew.eq("expandname",addarraignmentexpand.getExpandname());
                    arraignmentexpandew.eq("expandvalue",addarraignmentexpand.getExpandvalue());
                    int police_arraignmentexpandMappercount=police_arraignmentexpandMapper.selectCount(arraignmentexpandew);
                        //不存在就新增
                       int police_arraignmentexpandMappe_insertbool = police_arraignmentexpandMapper.insert(addarraignmentexpand);
                }
            }


            //修改案件信息
            Police_casetoarraignment police_casetoarraignment=new Police_casetoarraignment();
            police_casetoarraignment.setArraignmentssid(arraignmentssid);
            police_casetoarraignment=police_casetoarraignmentMapper.selectOne(police_casetoarraignment);
            if (null!=police_casetoarraignment){
                String casessid=police_casetoarraignment.getCasessid();
                Police_case police_case=new Police_case();
                police_case.setSsid(casessid);
                police_case=police_caseMapper.selectOne(police_case);
                if (null!=police_case){
                    String casename=case_.getCasename();//案件名称
                    String casenum=case_.getCasenum();//案件编号
                    //------------
                    if (StringUtils.isNotBlank(casename)){
                        //判断案件是否重复
                        EntityWrapper police_cases_param=new EntityWrapper();
                        police_cases_param.eq("casename",casename.trim());
                        police_cases_param.ne("ssid",casessid);
                        police_cases_param.ne("casebool",-1);
                        List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
                        if (null!=police_cases_&&police_cases_.size()>0){
                            result.setMessage("案件名称不能重复");
                            return;
                        }
                    }

                    if (StringUtils.isNotBlank(casenum)){
                        //判断案件是否重复
                        EntityWrapper police_cases_param=new EntityWrapper();
                        police_cases_param.eq("casenum",casenum);
                        police_cases_param.ne("ssid",casessid);
                        police_cases_param.ne("casebool",-1);
                        List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
                        if (null!=police_cases_&&police_cases_.size()>0){
                            result.setMessage("案件编号不能重复");
                            return;
                        }
                    }

                    EntityWrapper caseupdate_ew=new EntityWrapper();
                    caseupdate_ew.eq("ssid",casessid);

                    int police_caseMapper_update_bool=police_caseMapper.update(case_,caseupdate_ew);
                    LogUtil.intoLog(1,this.getClass(),"police_caseMapper_update_bool___"+police_caseMapper_update_bool);
                }
            }
        }


        result.setData(1);
        changeResultToSuccess(result);
        return;
    }




}
