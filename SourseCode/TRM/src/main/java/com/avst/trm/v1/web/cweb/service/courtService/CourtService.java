package com.avst.trm.v1.web.cweb.service.courtService;

import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignmentexpand;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfograde;
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
import com.avst.trm.v1.feignclient.mc.req.GetPhssidByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.req.courtreq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.req.policereq.UploadWordTemplateParam;
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
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;



    @Autowired
    private OutService outService;


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
        Police_userinfograde police_userinfograde=gson.fromJson(gson.toJson(param),Police_userinfograde.class);
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

        Police_userinfograde police_userinfograde=gson.fromJson(gson.toJson(param),Police_userinfograde.class);
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
                                String usergrade="【未知】";
                                if (!usergradesMap.isEmpty()){
                                    usergrade=usergradesMap.get(userssid)==null?"【未知】":usergradesMap.get(userssid);
                                }
                                content+= "<div style='text-align: left'>" +
                                        "<p style='color: #999;'>【"+usergrade+"】："+starttime+"  </p><span>"+txt+"</span></div>";
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




}
