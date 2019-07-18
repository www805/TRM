package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.GZVodThread;
import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_nationalityMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.poiwork.WordToHtmlUtil;
import com.avst.trm.v1.common.util.poiwork.WordToPDF;
import com.avst.trm.v1.common.util.poiwork.XwpfTUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.ec.req.GetURLToPlayParam;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMCStateParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetPhssidByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.GetPHDataBackParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service("recordService")
public class RecordService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_recordtypeMapper police_recordtypeMapper;

    @Autowired
    private Police_cardtypeMapper police_cardtypeMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_answerMapper police_answerMapper;

    @Autowired
    private Police_usertoMapper police_usertoMapper;

    @Autowired
    private Police_recordtoproblemMapper police_recordtoproblemMapper;

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

    @Autowired
    private Police_userinfototypeMapper police_userinfototypeMapper;

    @Autowired
    private Police_wordtemplateMapper police_wordtemplateMapper;

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    @Autowired
    private Base_nationalityMapper base_nationalityMapper;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private OutService outService;


    @Value("${file.basepath}")
    private String filePath;

    @Value("${upload.basepath}")
    private String uploadbasepath;

    @Value("${file.wordtemplate}")
    private String filewordtemplate; //word模板存储位置

    @Value("${file.recordwordOrpdf}")
    private String filerecordwordOrpdf; //笔录word或者pdf路径

    private boolean addRecordbool=false;

    public void getRecords(RResult result, ReqParam<GetRecordsParam> param){
        GetRecordsVO getRecordsVO=new GetRecordsVO();

        //请求参数转换
        GetRecordsParam getRecordsParam = param.getParam();
        if (null==getRecordsParam){
            result.setMessage("参数为空");
            return;
        }
         Integer recordbool=getRecordsParam.getRecordbool();//状态
         String recordname=getRecordsParam.getRecordname();//笔录名
         String recordtypessid=getRecordsParam.getRecordtypessid();//笔录类型

        EntityWrapper recordparam=new EntityWrapper();
        if (StringUtils.isNotBlank(recordtypessid)){
            recordparam.eq("r.recordtypessid",recordtypessid);
        }
        if (StringUtils.isNotBlank(recordname)){
            recordparam.like("r.recordname",recordname);
        }
        if (null!=recordbool){
            recordparam.eq("r.recordbool",recordbool);
        }

        int count = police_recordMapper.countgetRecords(recordparam);
        getRecordsParam.setRecordCount(count);

        recordparam.orderBy("r.createtime",false);
        Page<AdminAndWorkunit> page=new Page<AdminAndWorkunit>(getRecordsParam.getCurrPage(),getRecordsParam.getPageSize());
        List<Record> records=police_recordMapper.getRecords(page,recordparam);
        getRecordsVO.setPageparam(getRecordsParam);
        if (null!=records&&records.size()>0){
            for (Record record : records) {
                String  recordssid=record.getSsid();
                Integer recordbool_=record.getRecordbool();//笔录状态1进行中2已完成
                if (null!=recordbool_&&recordbool_!=2){
                    List<RecordToProblem> recordToProblems = RecordrealingCache.getRecordrealByRecordssid(recordssid);
                    record.setProblems(recordToProblems);
                }else {
                    //查找笔录的全部题目
                    EntityWrapper probleparam=new EntityWrapper();
                    probleparam.eq("r.ssid",record.getSsid());
                    probleparam.orderBy("p.ordernum",true);
                    probleparam.orderBy("p.createtime",true);
                    List<RecordToProblem> problems = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(probleparam);
                    if (null!=problems&&problems.size()>0){
                        //根据题目和笔录查找对应答案
                        for (RecordToProblem problem : problems) {
                            String problemssid=problem.getSsid();
                            if (StringUtils.isNotBlank(problem.getSsid())){
                                EntityWrapper answerParam=new EntityWrapper();
                                answerParam.eq("recordtoproblemssid",problemssid);
                                answerParam.orderBy("ordernum",true);
                                answerParam.orderBy("createtime",true);
                                List<Police_answer> answers=police_answerMapper.selectList(answerParam);
                                if (null!=answers&&answers.size()>0){
                                    problem.setAnswers(answers);
                                }
                            }
                        }
                        record.setProblems(problems);
                    }
                }
            }
        }
        getRecordsVO.setPagelist(records);
        result.setData(getRecordsVO);
        changeResultToSuccess(result);
        return;
    }

    //提供给笔录问答实时记录初始化使用
    public  List<Record> initRecordrealingCache(){
        EntityWrapper recordparam=new EntityWrapper();
        recordparam.eq("recordbool",1).or().eq("recordbool",0);//获取进行中的笔录
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


    public void addRecord(RResult result, ReqParam<AddRecordParam> param){

        if (addRecordbool){
            LogUtil.intoLog(this.getClass(),"---addRecordbool---Start---");
            result.setMessage("保存处理中,请稍等重试...");
            return;
        }
        addRecordbool=true;


        //请求参数转换
        AddRecordParam addRecordParam = param.getParam();
        if (null==addRecordParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=addRecordParam.getRecordssid();//笔录ssid
      /*  List<RecordToProblem> recordToProblems1=addRecordParam.getRecordToProblems();//笔录携带的题目答案集合*/

        List<RecordToProblem> recordToProblems1=RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合

        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return;
        }


        //获取该笔录下的全部题目答案
        EntityWrapper recordToProblemsParam=new EntityWrapper();
        recordToProblemsParam.eq("recordssid",recordssid);
        List<Police_recordtoproblem> recordToProblems=police_recordtoproblemMapper.selectList(recordToProblemsParam);
        if (null!=recordToProblems&&recordToProblems.size()>0){
            for (Police_recordtoproblem recordToProblem : recordToProblems) {
                //删除所有题目
                EntityWrapper answersParam=new EntityWrapper();
                answersParam.eq("recordtoproblemssid",recordToProblem.getSsid());
                int answerdelete_bool=police_answerMapper.delete(answersParam);
                LogUtil.intoLog(this.getClass(),"answerdelete_bool__"+answerdelete_bool);
            }
             int recordtoproblemdelete_bool=police_recordtoproblemMapper.delete(recordToProblemsParam);
             LogUtil.intoLog(this.getClass(),"recordtoproblemdelete_bool__"+recordtoproblemdelete_bool);
        }else{
            LogUtil.intoLog(this.getClass(),"该笔录没有任何题目答案__1");
        }

        //根据参数笔录题目包括答案，新增笔录题目答案
        if (null!=recordToProblems1&&recordToProblems1.size()>0){
            for (int i = 0; i < recordToProblems1.size(); i++) {
                RecordToProblem problem=recordToProblems1.get(i);
                problem.setCreatetime(new Date());
                problem.setSsid(OpenUtil.getUUID_32());
                problem.setOrdernum(Integer.valueOf(i+1));
                problem.setRecordssid(recordssid);
                int recordtoprobleminsert_bool=police_recordtoproblemMapper.insert(problem);
                LogUtil.intoLog(this.getClass(),"recordtoprobleminsert_bool__"+recordtoprobleminsert_bool);
                if (recordtoprobleminsert_bool>0){
                    List<Police_answer> answers=problem.getAnswers();
                    if (null!=answers&&answers.size()>0){
                        for (int j = 0; j < answers.size(); j++) {
                            Police_answer answer  = answers.get(j);
                            answer.setSsid(OpenUtil.getUUID_32());
                            answer.setCreatetime(new Date());
                            answer.setOrdernum(Integer.valueOf(j+1));
                            answer.setRecordtoproblemssid(problem.getSsid());
                          int answerinsert_bool =  police_answerMapper.insert(answer);
                            LogUtil.intoLog(this.getClass(),"answerinsert_bool__"+answerinsert_bool);
                        }
                    }
                }
            }
        }else{
            LogUtil.intoLog(this.getClass(),"该笔录没有任何题目答案__2");
        }


        //修改笔录状态
        Integer recordbool=addRecordParam.getRecordbool();
        LogUtil.intoLog(this.getClass(),"recordbool__"+recordbool);
        if (null!=recordbool&&recordbool==2){
            //生成word 和pdf
            RResult exportPdf_rr=new RResult();
            ExportPdfParam exportPdfParam=new ExportPdfParam();
            exportPdfParam.setRecordssid(recordssid);
            ReqParam reqParam=new ReqParam();
            reqParam.setParam(exportPdfParam);
            exportPdf_rr=exportPdf(exportPdf_rr, reqParam);
            if (null != exportPdf_rr && exportPdf_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                LogUtil.intoLog(this.getClass(),"笔录结束时exportPdf__成功__保存问答");
            }else{
                LogUtil.intoLog(this.getClass(),"笔录结束时exportPdf__出错__"+exportPdf_rr.getMessage());
            }


            EntityWrapper updaterecordParam=new EntityWrapper();
            updaterecordParam.eq("ssid",recordssid);
            Police_record record=new Police_record();
            record.setSsid(recordssid);
            record.setRecordbool(recordbool);
            int updaterecord_bool=police_recordMapper.update(record,updaterecordParam);
            LogUtil.intoLog(this.getClass(),"updaterecord_bool__"+updaterecord_bool);
        }

        addRecordbool=false;
        result.setData(recordssid);
        changeResultToSuccess(result);
        return;
    }

    public void getRecordById(RResult result, ReqParam<GetRecordByIdParam> param){
        GetRecordByIdVO getRecordByIdVO= new GetRecordByIdVO();
        //请求参数转换
        GetRecordByIdParam getRecordByIdParam = param.getParam();
        if (null==getRecordByIdParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=getRecordByIdParam.getRecordssid();
        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return;
        }


        //根据笔录ssid获取录音数据
            EntityWrapper recordParam=new EntityWrapper();
            recordParam.eq("r.ssid",recordssid);
            Record record=police_recordMapper.getRecordBySsid(recordParam);

            if (null!=record){
                try {
                    /**
                     *  获取题目答案
                     */
                    EntityWrapper ew=new EntityWrapper();
                    ew.eq("r.ssid",record.getSsid());
                    ew.orderBy("p.ordernum",true);
                    ew.orderBy("p.createtime",true);
                    List<RecordToProblem> problems = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(ew);
                    if (null!=problems&&problems.size()>0){
                        //根据题目和笔录查找对应答案
                        for (RecordToProblem problem : problems) {
                            String problemssid=problem.getSsid();
                            if (StringUtils.isNotBlank(problemssid)){
                                EntityWrapper answerParam=new EntityWrapper();
                                answerParam.eq("recordtoproblemssid",problemssid);
                                answerParam.orderBy("ordernum",true);
                                answerParam.orderBy("createtime",true);
                                List<Police_answer> answers=police_answerMapper.selectList(answerParam);
                                if (null!=answers&&answers.size()>0){
                                    problem.setAnswers(answers);
                                }
                            }
                        }
                        record.setProblems(problems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    /**
                     *   获取提讯人和被询问人
                     */
                    EntityWrapper recorduserinfosParam=new EntityWrapper();
                    recorduserinfosParam.eq("a.recordssid",record.getSsid());
                    RecordUserInfos recordUserInfos=police_recordMapper.getRecordUserInfosByRecordSsid(recorduserinfosParam);
                    if (null!=recordUserInfos){
                        record.setRecordUserInfos(recordUserInfos);
                    }

                    getRecordByIdVO.setRecord(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //根据笔录ssid获取案件信息
                try {
                    EntityWrapper caseParam=new EntityWrapper();
                    caseParam.eq("r.ssid",recordssid);
                    CaseAndUserInfo caseAndUserInfo = police_caseMapper.getCaseByRecordSsid(caseParam);
                    if (null!=caseAndUserInfo){
                        caseAndUserInfo.setOccurrencetime_format(caseAndUserInfo.getOccurrencetime());
                        record.setCaseAndUserInfo(caseAndUserInfo);
                        getRecordByIdVO.setCaseAndUserInfo(caseAndUserInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //根据笔录ssid获取提讯数据
                String mtssid=null;
                try {
                    Police_arraignment police_arraignment=new Police_arraignment();
                    police_arraignment.setRecordssid(recordssid);
                    police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                    Integer mtstate=null;
                    if (null!=police_arraignment){
                        if (StringUtils.isNotBlank(police_arraignment.getMtssid())){
                            mtssid=police_arraignment.getMtssid();
                            ReqParam<GetMCStateParam_out> getMCStateParam_outReqParam=new ReqParam<>();
                            GetMCStateParam_out getMCStateParam_out=new GetMCStateParam_out();
                            getMCStateParam_out.setMcType(MCType.AVST);
                            getMCStateParam_out.setMtssid(mtssid);
                            getMCStateParam_outReqParam.setParam(getMCStateParam_out);
                            RResult rr = meetingControl.getMCState(getMCStateParam_outReqParam);
                            if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                                mtstate= (Integer) rr.getData();
                                record.setMcbool(mtstate);
                            }
                        }
                        record.setPolice_arraignment(police_arraignment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String iid=null;
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
                        iid=getMCVO.getIid();
                        getRecordByIdVO.setGetMCVO(getMCVO);
                    }
                    LogUtil.intoLog(this.getClass()," outService.getRecord__请求成功");
                }else {
                    Object msg=getrecord_rr==null?getrecord_rr:getrecord_rr.getMessage();
                    LogUtil.intoLog(this.getClass()," outService.getRecord__请求失败__"+msg);
                }

                //getPlayUrl:获取直播地址
                if (StringUtils.isNotBlank(iid)){
                    GetPlayUrlVO getPlayUrlVO =new GetPlayUrlVO();
                    GetURLToPlayParam getURLToPlayParam=new GetURLToPlayParam();
                    getURLToPlayParam.setSsType(SSType.AVST);
                    getURLToPlayParam.setIid(iid);
                    RResult getplayurl_rr=new RResult();
                    outService.getPlayUrl(getplayurl_rr,getURLToPlayParam);
                    if (null!=getplayurl_rr&&getplayurl_rr.getActioncode().equals(Code.SUCCESS.toString())){
                        getPlayUrlVO=gson.fromJson(gson.toJson(getplayurl_rr.getData()),GetPlayUrlVO.class);
                        if (null!=getMCVO){
                            getRecordByIdVO.setGetPlayUrlVO(getPlayUrlVO);
                        }
                        LogUtil.intoLog(this.getClass()," outService.getPlayUrl__请求成功");
                    }else {
                        Object msg=getplayurl_rr==null?getplayurl_rr:getplayurl_rr.getMessage();
                        LogUtil.intoLog(this.getClass()," outService.getPlayUrl__请求失败__"+msg);
                    }
                }

                //getPHDataBack：获取身心回放
                List<PHDataBackVoParam> phDataBackVoParams=new ArrayList<>();
                ReqParam getphdataback_param=new ReqParam<>();
                GetPHDataBackParam getPHDataBackParam=new GetPHDataBackParam();
                getPHDataBackParam.setMtssid(mtssid);
                getphdataback_param.setParam(getPHDataBackParam);
                RResult getphdataback_rr=new RResult();
                outService.getPHDataBack(getphdataback_rr,getphdataback_param);
                if (null!=getphdataback_rr&&getphdataback_rr.getActioncode().equals(Code.SUCCESS.toString())){
                    phDataBackVoParams=gson.fromJson(gson.toJson(getphdataback_rr.getData()), new TypeToken<List<PHDataBackVoParam>>(){}.getType());
                    if (null!=phDataBackVoParams){
                        getRecordByIdVO.setPhDataBackVoParams(phDataBackVoParams);
                    }
                    LogUtil.intoLog(this.getClass()," outService.getPHDataBack__请求成功");
                }else {
                    Object msg=getphdataback_rr==null?getphdataback_rr:getphdataback_rr.getMessage();
                    LogUtil.intoLog(this.getClass()," outService.getPHDataBack__请求失败__"+msg);
                }
                //获取实时数据
                result.setData(getRecordByIdVO);
                changeResultToSuccess(result);

                String gz=record.getGz_iid();
                //检测有没有下载包，没有就打包
                if(StringUtils.isEmpty(gz)){
                    //调用打包线程
                    try {
                        Police_record police_record=record;
                        GZVodThread thread=new GZVodThread(result,police_recordMapper,police_record);
                        thread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else{
                result.setMessage("笔录查询为空");
                LogUtil.intoLog(this.getClass()," police_recordMapper.getRecordBySsid，recordssid："+recordssid);
            }


        return;
    }

    public void uploadRecord(RResult result, ReqParam param){
        return;
    }

    public void recordIndex(RResult result, ReqParam param){
        return;
    }

    public void getRecordtypes(RResult result, ReqParam<GetRecordtypesParam> param){
        GetRecordtypesVO getRecordtypesVO=new GetRecordtypesVO();

        //请求参数转换
        GetRecordtypesParam getRecordtypesParam = param.getParam();
        if (null==getRecordtypesParam){
            result.setMessage("参数为空");
            return;
        }

        Integer pid=getRecordtypesParam.getPid();
        EntityWrapper ew=new EntityWrapper();
        if (null!=pid){
            ew.eq("pid",pid);
        }
        ew.orderBy("ordernum",true);
        ew.orderBy("createtime",false);
        List<Police_recordtype> list=police_recordtypeMapper.selectList(ew);
        List<GetRecordtypesVOParam> getRecordtypesVOParamList =new ArrayList<GetRecordtypesVOParam>();
        if (null!=list&&list.size()>0){
            List<GetRecordtypesVOParam> recordtypes=gson.fromJson(gson.toJson(list), new TypeToken<List<GetRecordtypesVOParam>>(){}.getType());
            for (int i = 0; i < recordtypes.size(); i++) {
                for (int j = 0; j < recordtypes.size(); j++) {
                    if(recordtypes.get(i).getId()==recordtypes.get(j).getPid()){
                        GetRecordtypesVOParam m= recordtypes.get(i);
                        m.getPolice_recordtypes().add(recordtypes.get(j));
                    }
                }
            }
            for (int i = 0; i < recordtypes.size(); i++) {
                GetRecordtypesVOParam m= recordtypes.get(i);
                if (null!=m){
                    String recordtypessid=m.getSsid();
                    EntityWrapper word_ew=new EntityWrapper();
                    word_ew.eq("r.ssid",recordtypessid);
                    List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word_ew);
                    if (null!=wordTemplates&&wordTemplates.size()>0){
                        m.setWordTemplates(wordTemplates);
                    }
                }
                if(recordtypes.get(i).getPid()==0){
                    getRecordtypesVOParamList.add(m);
                }
                if (null!=pid){
                    getRecordtypesVOParamList.add(m);
                }
            }
            getRecordtypesVO.setGetRecordtypesVOParamList(getRecordtypesVOParamList);
        }
        result.setData(getRecordtypesVO);
        changeResultToSuccess(result);
        return;
    }

    public void getPidRecordtypes(RResult result, ReqParam param){
        EntityWrapper pidparam=new EntityWrapper();
        pidparam.eq("pid",0);
        List<Police_recordtype> police_recordtypesByPid=police_recordtypeMapper.selectList(pidparam);
        if (null!=police_recordtypesByPid&&police_recordtypesByPid.size()>0){
           result.setData(police_recordtypesByPid);
           changeResultToSuccess(result);
        }
        return;
    }

    public void getRecordtypeById(RResult result, ReqParam<GetRecordtypeByIdParam> param){
        //请求参数转换
        GetRecordtypeByIdParam getRecordtypeByIdParam = param.getParam();
        if (null==getRecordtypeByIdParam){
            result.setMessage("参数为空");
            return;
        }
        Recordtype recordtype=new Recordtype();
        Police_recordtype police_recordtype=new Police_recordtype();
        police_recordtype.setId(getRecordtypeByIdParam.getId());
        police_recordtype =  police_recordtypeMapper.selectOne(police_recordtype);
        if (null==police_recordtype){
            LogUtil.intoLog(this.getClass(),"未找到该笔录类型--");
            result.setMessage("系统错误");
        }
        recordtype=gson.fromJson(gson.toJson(police_recordtype),Recordtype.class);
            String recordtypessid=recordtype.getSsid();
            EntityWrapper word_ew=new EntityWrapper();
            word_ew.eq("r.ssid",recordtypessid);
            List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word_ew);
            if (null!=wordTemplates&&wordTemplates.size()>0){
                recordtype.setWordTemplates(wordTemplates);
            }



        //开始获取子集
        List<Police_recordtype> records_son=new ArrayList<>();

        EntityWrapper ew=new EntityWrapper();
        ew.eq("pid",police_recordtype.getId());
        records_son =  police_recordtypeMapper.selectList(ew);
        if (null!=records_son&&records_son.size()>0) {
          List<RecordtypeToWord> recordtypes=gson.fromJson(gson.toJson(records_son), new TypeToken<List<RecordtypeToWord>>(){}.getType());
            for (RecordtypeToWord recordtypeToWord : recordtypes) {
                String recordtypessid_=recordtypeToWord.getSsid();
                EntityWrapper word_ew_=new EntityWrapper();
                word_ew_.eq("r.ssid",recordtypessid);
                List<WordTemplate> wordTemplates_=police_wordtemplateMapper.getWordTemplate(word_ew_);
                if (null!=wordTemplates_&&wordTemplates_.size()>0){
                    recordtypeToWord.setWordTemplates(wordTemplates_);
                }
            }
          recordtype.setRecordtypes(recordtypes);
        }

        result.setData(recordtype);
        changeResultToSuccess(result);
        return;
    }

    public void addRecordtype(RResult result, ReqParam<Police_recordtype> param){
        Police_recordtype police_recordtype=param.getParam();
        if (null==police_recordtype){
            result.setMessage("参数为空");
            return;
        }
        police_recordtype.setSsid(OpenUtil.getUUID_32());
        police_recordtype.setCreatetime(new Date());
        int insert_bool=police_recordtypeMapper.insert(police_recordtype);
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool>0){
            result.setData(insert_bool);
            changeResultToSuccess(result);
        }
        return;
    }

    public void updateRecordtype(RResult result, ReqParam<Police_recordtype> param){
        Police_recordtype police_recordtype=param.getParam();
        if (null==police_recordtype){
            result.setMessage("参数为空");
            return;
        }
        String ssid=police_recordtype.getSsid();

        int update_bool=police_recordtypeMapper.updateById(police_recordtype);
        LogUtil.intoLog(this.getClass(),"update_bool__"+update_bool);
        if (update_bool>0){

            //开始获取子集并且修改
            EntityWrapper ew=new EntityWrapper();
            ew.eq("pid",police_recordtype.getId());
            List<Police_recordtype> records_son =  police_recordtypeMapper.selectList(ew);
            if (null!=records_son&&records_son.size()>0&&police_recordtype.getPid()!=0) {
                for (Police_recordtype rt : records_son) {
                    rt.setPid(police_recordtype.getPid());
                    int updatepoliceRecordtype_bool=police_recordtypeMapper.updateById(rt);
                    LogUtil.intoLog(this.getClass(),"updatepoliceRecordtype_bool__"+updatepoliceRecordtype_bool);
                }
            }

            result.setData(update_bool);
            changeResultToSuccess(result);
        }
        return;
    }

    public void addRecordTemplate(RResult result, ReqParam param){
        return;
    }

    public void addCaseToArraignment(RResult result, ReqParam<AddCaseToArraignmentParam> param, HttpSession session){
        AddCaseToArraignmentParam addCaseToArraignmentParam=param.getParam();
        if (null==addCaseToArraignmentParam){
            result.setMessage("参数为空");
            return;
        }

        List<Police_userto> usertos=addCaseToArraignmentParam.getUsertos();//其他在场人员信息
        String recordtypessid=addCaseToArraignmentParam.getRecordtypessid();//笔录类型
        String recordname=addCaseToArraignmentParam.getRecordname().replace(" ", "").replace("\"", "");//笔录类型
        String casessid=addCaseToArraignmentParam.getCasessid();//案件ssid
        String userssid=addCaseToArraignmentParam.getUserssid();//人员ssid
        String mtmodelssid=addCaseToArraignmentParam.getMtmodelssid();//会议模板ssid

        UserInfo addUserInfo=addCaseToArraignmentParam.getAddUserInfo();//新增人员的信息
        Police_case addPolice_case=addCaseToArraignmentParam.getAddPolice_case();//新增案件的信息



        //需要新增人员信息
        if (StringUtils.isBlank(userssid)){
            LogUtil.intoLog(this.getClass(),"需要新增人员____");
            addUserInfo.setSsid(OpenUtil.getUUID_32());
            addUserInfo.setCreatetime(new Date());
            int insertuserinfo_bool = police_userinfoMapper.insert(addUserInfo);
            LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
           if (insertuserinfo_bool>0){
               Police_userinfototype police_userinfototype=new Police_userinfototype();
               police_userinfototype.setCardnum(addUserInfo.getCardnum());
               police_userinfototype.setSsid(OpenUtil.getUUID_32());
               police_userinfototype.setCreatetime(new Date());
               police_userinfototype.setCardtypessid(addUserInfo.getCardtypessid());
               police_userinfototype.setUserssid(addUserInfo.getSsid());
              int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
               LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
               userssid=addUserInfo.getSsid();//得到用户的ssid
               LogUtil.intoLog(this.getClass(),"新增的人员ssid____"+userssid);
           }
        }else{
          //修改用户信息
            EntityWrapper updateuserinfoParam=new EntityWrapper();
            updateuserinfoParam.eq("ssid",userssid);
            Police_userinfo police_userinfo=gson.fromJson(gson.toJson(addUserInfo),UserInfo.class);
            int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
            LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
        }

            //需要新增案件信息
         if (StringUtils.isBlank(casessid)){
                LogUtil.intoLog(this.getClass(),"需要新增案件信息____");
                addPolice_case.setSsid(OpenUtil.getUUID_32());
                addPolice_case.setCreatetime(new Date());
                addPolice_case.setOrdernum(0);
                addPolice_case.setUserssid(userssid);
                addPolice_case.setCasebool(0);
                addPolice_case.setCreator(addCaseToArraignmentParam.getAdminssid());
                int insertcase_bool =  police_caseMapper.insert(addPolice_case);
                LogUtil.intoLog(this.getClass(),"insertcase_bool__"+insertcase_bool);
                if (insertcase_bool>0){
                    casessid=addPolice_case.getSsid();
                }
         }else{
                //修改案件信息
                EntityWrapper updatecaseParam=new EntityWrapper();
                updatecaseParam.eq("ssid",casessid);
                int updatecase_bool = police_caseMapper.update(addPolice_case,updatecaseParam);
                LogUtil.intoLog(this.getClass(),"updatecase_bool__"+updatecase_bool);
         }


        if (StringUtils.isBlank(userssid)||StringUtils.isBlank(casessid)){
            LogUtil.intoLog(this.getClass(),"userssid__"+userssid+"__casessid__"+casessid);
            result.setMessage("参数为空");
            return;
        }

        String otheruserinfoname=addCaseToArraignmentParam.getOtheruserinfoname();//新增询问人二的名称
        String otherworkname=addCaseToArraignmentParam.getOtherworkname();//新增询问人二的对应的工作单位
        String otheradminssid=addCaseToArraignmentParam.getOtheradminssid();//询问人二的ssid
        String otherworkssid=addCaseToArraignmentParam.getOtherworkssid();//询问人二对应的工作单位ssid

        if (StringUtils.isBlank(otherworkssid)&&StringUtils.isNotBlank(otherworkname)){
            LogUtil.intoLog(this.getClass(),"需要新增工作单位____"+otherworkname);
            Police_workunit workunit=new Police_workunit();
            workunit.setSsid(OpenUtil.getUUID_32());
            workunit.setCreatetime(new Date());
            workunit.setWorkname(otherworkname);
           int police_workunitMapper_insertbool= police_workunitMapper.insert(workunit);
           LogUtil.intoLog("police_workunitMapper_insertbool__"+police_workunitMapper_insertbool);
            if (police_workunitMapper_insertbool>0){
                otherworkssid=workunit.getSsid();
            }
        }

        if (StringUtils.isBlank(otheradminssid)&&StringUtils.isNotBlank(otheruserinfoname)){
            LogUtil.intoLog(this.getClass(),"需要新增询问人二____"+otheruserinfoname);
            Base_admininfo base_admininfo=new Base_admininfo();
            base_admininfo.setSsid(OpenUtil.getUUID_32());
            base_admininfo.setTemporaryaskbool(1);//是否为临时询问人
            AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);
            base_admininfo.setCreator(user.getSsid());
            base_admininfo.setWorkunitssid(otherworkssid==null?user.getWorkunitssid():otherworkssid);//没有选择默认使用创建者相同的工作单位
            base_admininfo.setLoginaccount(OpenUtil.getUUID_32());
            base_admininfo.setUsername(otheruserinfoname);
            base_admininfo.setRegistertime(new Date());
            int base_admininfoMapper_insertbool=base_admininfoMapper.insert(base_admininfo);
            LogUtil.intoLog(this.getClass(),"base_admininfoMapper_insertbool__"+base_admininfoMapper_insertbool);
            if (base_admininfoMapper_insertbool>0){
                otheradminssid=base_admininfo.getSsid();
            }
        }


        //添加笔录信息
        Police_record record=new Police_record();
        record.setSsid(OpenUtil.getUUID_32());
        record.setCreatetime(new Date());
        record.setRecordbool(0);//1进行中2未开始
        record.setRecordtypessid(recordtypessid);
        record.setRecordname(recordname);
        int insertrecord_bool=police_recordMapper.insert(record);
        LogUtil.intoLog(this.getClass(),"insertrecord_bool__"+insertrecord_bool);
        if (insertrecord_bool<0){
            result.setMessage("系统异常");
            return;
        }



       //添加提讯数据
        if (StringUtils.isBlank(mtmodelssid)){
            //会议模板为空，直接取默认的
            Base_type base_type=new Base_type();
            base_type.setType(CommonCache.getCurrentServerType());
            base_type=base_typeMapper.selectOne(base_type);
            if (null!=base_type){
                mtmodelssid=base_type.getMtmodelssid();
            }
        }
        Police_arraignment arraignment=new Police_arraignment();
        arraignment.setSsid(OpenUtil.getUUID_32());
        arraignment.setCreatetime(new Date());
        arraignment.setAdminssid(addCaseToArraignmentParam.getAdminssid());
        arraignment.setAsknum(addCaseToArraignmentParam.getAsknum()+1);
        arraignment.setAskobj(addCaseToArraignmentParam.getAskobj());
        arraignment.setRecordadminssid(addCaseToArraignmentParam.getRecordadminssid());
        arraignment.setRecordplace(addCaseToArraignmentParam.getRecordplace());
        arraignment.setOtheradminssid(otheradminssid);
        arraignment.setRecordssid(record.getSsid());
        arraignment.setMtmodelssid(mtmodelssid);//会议模板ssid
        int insertarraignment_bool=police_arraignmentMapper.insert(arraignment);
        LogUtil.intoLog(this.getClass(),"insertarraignment_bool__"+insertarraignment_bool);

        if (insertarraignment_bool<0){
            result.setMessage("系统异常");
            return;
        }

        //添加案件提讯信息
        if (StringUtils.isNotBlank(casessid)){
                Police_casetoarraignment casetoarraignment=new Police_casetoarraignment();
                casetoarraignment.setCreatetime(new Date());
                casetoarraignment.setSsid(OpenUtil.getUUID_32());
                casetoarraignment.setArraignmentssid(arraignment.getSsid());
                casetoarraignment.setCasessid(casessid);
                int insertcasetoarraignment_bool=police_casetoarraignmentMapper.insert(casetoarraignment);
                LogUtil.intoLog(this.getClass(),"insertcasetoarraignment_bool__"+insertcasetoarraignment_bool);


            //修改案件状态
            Police_case police_case=new Police_case();
            police_case.setCasebool(1);
            EntityWrapper ew=new EntityWrapper();
            ew.eq("ssid",casessid);
            int police_caseMapper_updatebool = police_caseMapper.update(police_case,ew);
            LogUtil.intoLog(this.getClass(),"police_caseMapper_updatebool__"+police_caseMapper_updatebool);
         }

        //添加其他
        if (null!=usertos&&usertos.size()>0){
            for (Police_userto userto : usertos) {
              Police_userto  userto1=new Police_userto();
                userto1.setSsid(OpenUtil.getUUID_32());
                userto1.setCreatetime(new Date());
                userto1.setArraignmentssid(arraignment.getSsid());
                userto1.setUserssid(userssid);
                userto1.setLanguage(userto.getLanguage());
                userto1.setOtheruserssid(userto.getOtheruserssid());
                userto1.setRelation(userto.getRelation());
                userto1.setUsertitle(userto.getUsertitle());
                userto1.setUsertype(userto.getUsertype());
                int insertuserto_bool= police_usertoMapper.insert(userto1);
                LogUtil.intoLog(this.getClass(),"insertuserto_bool__"+insertuserto_bool);
            }
        }


         result.setData(record.getSsid());//返回开始笔录的ssid
        changeResultToSuccess(result);
        return;
    }

    public void getUserByCard(RResult result, ReqParam<GetUserByCardParam> param, HttpSession httpSession){
        GetUserByCardVO getUserByCardVO=new GetUserByCardVO();

        GetUserByCardParam getUserByCardParam=param.getParam();
        if (null==getUserByCardParam){
            result.setMessage("参数为空");
            return;
        }
         String cardtypesssid=getUserByCardParam.getCardtypesssid();//证件类型ssid
         String cardnum=getUserByCardParam.getCardnum();//证件号
        LogUtil.intoLog(this.getClass(),"证件类型："+cardtypesssid);
        LogUtil.intoLog(this.getClass(),"证件号码："+cardnum);

        if (StringUtils.isBlank(cardtypesssid)||StringUtils.isBlank(cardnum)){
            result.setMessage("请输入证件号");
            return;
        }

        //根据证件类型和证件号查询用户信息
        EntityWrapper userparam=new EntityWrapper();
        userparam.eq("ut.cardtypessid",cardtypesssid);
        userparam.eq("ut.cardnum",cardnum);
        List<UserInfo> userinfos=police_userinfoMapper.getUserByCard(userparam);
        if (null==userinfos||userinfos.size()<1){
            result.setMessage("未找到该人员信息");
            return;
        }

        if (null!=userinfos&&userinfos.size()>0){
            if (userinfos.size()==1){
                UserInfo police_userinfo=userinfos.get(0);
                getUserByCardVO.setUserinfo(police_userinfo);


                //根据用户userssid查询案件列表
                EntityWrapper caseparam=new EntityWrapper();
                caseparam.eq("c.userssid",police_userinfo.getSsid());
                caseparam.orderBy("c.occurrencetime",false);
                List<CaseAndUserInfo> cases=police_caseMapper.getCaseByUserSsid(caseparam);//加入询问次数
                if (null!=cases&&cases.size()>0){
                    for (CaseAndUserInfo c: cases) {
                        //提讯数据
                        EntityWrapper ewarraignment=new EntityWrapper();
                        ewarraignment.eq("cr.casessid",c.getSsid());
                        ewarraignment.orderBy("a.createtime",false);
                        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                        if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                            c.setArraignments(arraignmentAndRecords);
                        }
                        c.setAsknum(arraignmentAndRecords.size());
                    }
                    getUserByCardVO.setCases(cases);
                }

                result.setData(getUserByCardVO);
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"人员用户找到多个--"+cardnum);
                result.setMessage("系统异常");
                return;
            }
        }
        return;
    }


    public void getCaseById(RResult result, ReqParam<GetCaseByIdParam> param,HttpSession session){
        GetCaseByIdVO getCaseByIdVO=new GetCaseByIdVO();

        GetCaseByIdParam getCaseByIdParam=param.getParam();
        if (null==getCaseByIdParam){
            result.setMessage("参数为空");
            return;
        }

        String userssid=getCaseByIdParam.getUserssid();
        if (StringUtils.isBlank(userssid)){
            result.setMessage("参数为空");
            return;
        }

        AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);

        //根据用户userssid查询案件列表
        EntityWrapper caseparam=new EntityWrapper();
        caseparam.eq("c.userssid",userssid);
        caseparam.eq("c.creator",user.getSsid());
        caseparam.orderBy("c.occurrencetime",false);
        List<CaseAndUserInfo> cases=police_caseMapper.getCaseByUserSsid(caseparam);//加入询问次数
        if (null!=cases&&cases.size()>0){
            for (CaseAndUserInfo c: cases) {
                //提讯数据
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",c.getSsid());
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    c.setArraignments(arraignmentAndRecords);
                }
                c.setAsknum(arraignmentAndRecords.size());
            }
            getCaseByIdVO.setCases(cases);
        }
        result.setData(getCaseByIdVO);
        //根据案件ssid查询该案件信息
        changeResultToSuccess(result);
        return;
    }

    public void getCards(RResult result, ReqParam param){
        List<Police_cardtype> list=police_cardtypeMapper.selectList(null);
        result.setData(list);
        changeResultToSuccess(result);
        return;
    }

    public void getTime(RResult result, ReqParam param){
        GetTimeVO getTimeVO=new GetTimeVO();
        String currenttime;//当前时间
        String yesterdaytime;//昨日时间

        currenttime = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(new Date());//设置日期格式

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time=cal.getTime();
        yesterdaytime= new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(time);
        getTimeVO.setCurrenttime(currenttime);
        getTimeVO.setYesterdaytime(yesterdaytime);
        result.setData(getTimeVO);
        changeResultToSuccess(result);
        return;
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

        Map<String,String> dataMap=exportData(recordssid);

        //根据笔录ssid获取录音数据
        EntityWrapper recordParam=new EntityWrapper();
        recordParam.eq("r.ssid",recordssid);
        Record record=police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record) {

            //1、获取模板的真实地址
            String wordtemplate_realurl=null;//模板路径
            EntityWrapper wordew=new EntityWrapper();
            wordew.eq("w.recordtypessid",record.getRecordtypessid());
            List<WordTemplate> wordTemplate=police_wordtemplateMapper.getWordTemplate(wordew);
            if (null!=wordTemplate&&wordTemplate.size()>0){
                for (WordTemplate template : wordTemplate) {
                    if (template.getDefaultbool()==1){
                        wordtemplate_realurl=template.getWordtemplate_realurl();
                        break;
                    }
                }
            }
            LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板真实地址__"+wordtemplate_realurl);
            if (StringUtils.isBlank(wordtemplate_realurl)){
                result.setMessage("请先上传笔录类型对应的word笔录模板");
                return result;
            }

            try {
                String uploadpath=uploadbasepath;
                String savePath=filerecordwordOrpdf;
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

    public void exportWord(RResult<ExportWordVO> result, ReqParam<ExportWordParam> param, HttpServletRequest request){
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

        Map<String,String> dataMap=exportData(recordssid);

        //根据笔录ssid获取录音数据
        EntityWrapper recordParam = new EntityWrapper();
        recordParam.eq("r.ssid", recordssid);
        Record record = police_recordMapper.getRecordBySsid(recordParam);
        if (null!=record){

         //1、获取模板的真实地址
          String wordtemplate_realurl=null;//模板路径
           EntityWrapper wordew=new EntityWrapper();
           wordew.eq("w.recordtypessid",record.getRecordtypessid());
           List<WordTemplate> wordTemplate=police_wordtemplateMapper.getWordTemplate(wordew);
           if (null!=wordTemplate&&wordTemplate.size()>0){
               for (WordTemplate template : wordTemplate) {
                    if (template.getDefaultbool()==1){
                        wordtemplate_realurl=template.getWordtemplate_realurl();
                        break;
                    }
               }
           }
            LogUtil.intoLog(this.getClass(),"笔录类型对应的word笔录模板真实地址__"+wordtemplate_realurl);
           if (StringUtils.isBlank(wordtemplate_realurl)){
               result.setMessage("请先上传笔录类型对应的word笔录模板");
               return;
           }

            try {
                String uploadpath=uploadbasepath;
                String savePath=filerecordwordOrpdf;
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

                //将地址保存在文件存储表以及修改笔录标的文件存储ssid
                String word_filesavessid=record.getWord_filesavessid();
                if (StringUtils.isNotBlank(wordrealurl)&&StringUtils.isNotBlank(worddownurl)){
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
                record.setWord_filesavessid(word_filesavessid);
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
     * @return
     */
    public  Map<String,String> exportData(String recordssid) {
        //根据笔录ssid获取录音数据
        EntityWrapper recordParam = new EntityWrapper();
        recordParam.eq("r.ssid", recordssid);
        Record record = police_recordMapper.getRecordBySsid(recordParam);
        Map<String, String> dataMap = new HashMap<String, String>();
        if (null != record) {

            EntityWrapper ew = new EntityWrapper();
            ew.eq("r.ssid", record.getSsid());
            ew.orderBy("p.ordernum", true);
            ew.orderBy("p.createtime", true);
            List<RecordToProblem> questionandanswer = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(ew);
            /* List<Talk> talks = new ArrayList<>();//问答*/
            String talk="";
            if (null != questionandanswer && questionandanswer.size() > 0) {
                for (RecordToProblem problem : questionandanswer) {
                  /*  Talk talk = new Talk();
                    List<Answer> as = new ArrayList<>();//答集合
                    talk.setQuestion("问：" + problem.getProblem());*/
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
                              /*  Answer a = new Answer();
                                a.setAnswer("答：" + answer.getAnswer());
                                as.add(a);*/
                                talk+="答："+answer.getAnswer()+"\r";
                            }
                            problem.setAnswers(answers);
                        } else {
                          /*  Answer a = new Answer();
                            a.setAnswer("答：");
                            as.add(a);*/
                            talk+="答：\r";
                        }
                    }
                   /* talk.setAnswers(as);
                    talks.add(talk);*/
                }
            }

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

            String recordtypename = police_recordtype.getTypename();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            String recordstarttime = sdf.format(record.getCreatetime());
            String recordendtime = sdf.format(new Date());
            String recordplace = police_arraignment.getRecordplace();

            //工作单位
            Police_workunit police_workunit1 = new Police_workunit();
            police_workunit1.setSsid(recordUserInfos.getWorkunitssid1());
            police_workunit1 = police_workunitMapper.selectOne(police_workunit1);
            Police_workunit police_workunit2 = new Police_workunit();
            police_workunit2.setSsid(recordUserInfos.getWorkunitssid2());
            police_workunit2 = police_workunitMapper.selectOne(police_workunit2);
            Police_workunit police_workunit3 = new Police_workunit();
            police_workunit3.setSsid(recordUserInfos.getWorkunitssid3());
            police_workunit3 = police_workunitMapper.selectOne(police_workunit3);

            String workname1 = police_workunit1.getWorkname();
            String workname2 = police_workunit2.getWorkname();
            String workname3 = police_workunit3.getWorkname();
            String username = police_userinfo.getUsername();
            String sex = police_userinfo.getSex() == 1 ? "男" : "女";
            String age = police_userinfo.getAge().toString();
            String politicsstatus = police_userinfo.getPoliticsstatus();
            String workunits = police_userinfo.getWorkunits();
            String residence = police_userinfo.getResidence();
            String phone = police_userinfo.getPhone();
            String domicile = police_userinfo.getDomicile();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
            String both = sdf2.format(police_userinfo.getBoth());

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

            dataMap.put("${recordtypename}", recordtypename == null ? "" : recordtypename);
            dataMap.put("${recordstarttime}", recordstarttime == null ? "" : recordstarttime);
            dataMap.put("${recordendtime}", recordendtime == null ? "" : recordendtime);
            dataMap.put("${recordplace}", recordplace == null ? "" : recordplace);
            dataMap.put("${workname1}", workname1 == null ? "" : workname1);
            dataMap.put("${workname2}", workname2 == null ? "" : workname2);
            dataMap.put("${workname3}", workname3 == null ? "" : workname3);
            dataMap.put("${username}", username == null ? "" : username);
            dataMap.put("${sex}", sex == null ? "" : sex);
            dataMap.put("${age}", age == null ? "" : age);
            dataMap.put("${cardnum}", cardnum == null ? "" : cardnum);
            dataMap.put("${politicsstatus}", politicsstatus == null ? "" : politicsstatus);
            dataMap.put("${workunits}", workunits == null ? "" : workunits);
            dataMap.put("${residence}", residence == null ? "" : residence);
            dataMap.put("${phone}", phone == null ? "" : phone);
            dataMap.put("${domicile}", domicile == null ? "" : domicile);
            dataMap.put("${both}", both == null ? "" : both);
            dataMap.put("${talk}", talk == null ? "" : talk);
            dataMap.put("${nationality}", nationality == null ? "" : nationality);

        }
        return dataMap;
    }

    public void updateArraignment(RResult result, ReqParam<UpdateArraignmentParam> param){
        UpdateArraignmentParam updateArraignmentParam=param.getParam();
        if (null==updateArraignmentParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=updateArraignmentParam.getRecordssid();
        String mtssid=updateArraignmentParam.getMtssid();
        if (StringUtils.isBlank(recordssid)||StringUtils.isBlank(mtssid)){
            result.setMessage("参数为空");
            return;
        }

        try {
            //根据recordssid获取提讯
            Police_arraignment police_arraignment=new Police_arraignment();
            police_arraignment.setRecordssid(recordssid);
            police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
            if (null!=police_arraignment){
                police_arraignment.setMtssid(mtssid);
               int arraignmentupdateById_bool = police_arraignmentMapper.updateById(police_arraignment);
                LogUtil.intoLog(this.getClass(),"arraignmentupdateById_bool__"+arraignmentupdateById_bool);
                if (arraignmentupdateById_bool>0){
                    result.setData(true);
                    changeResultToSuccess(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCases(RResult result,ReqParam<GetCasesParam> param,HttpSession session){
        GetCasesParam getCasesParam=param.getParam();
        if (null==getCasesParam){
            result.setMessage("参数为空");
            return;
        }

        GetCasesVO getCasesVO=new GetCasesVO();
        //请求参数组合
        EntityWrapper ew=new EntityWrapper();
        if (StringUtils.isNotBlank(getCasesParam.getCasename())){
            ew.like(true,"c.casename",getCasesParam.getCasename().trim());
        }
        if (StringUtils.isNotBlank(getCasesParam.getCasenum())){
            ew.like(true,"c.casenum",getCasesParam.getCasenum().trim());
        }
        if (StringUtils.isNotBlank(getCasesParam.getUsername())){
            ew.like(true,"u.username",getCasesParam.getUsername().trim());
        }

        AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);
        ew.eq("c.creator",user.getSsid());

        int count = police_caseMapper.countgetArraignmentList(ew);
        getCasesParam.setRecordCount(count);

        ew.orderBy("c.ordernum",true);
        ew.orderBy("c.createtime",false);
        Page<CaseAndUserInfo> page=new Page<CaseAndUserInfo>(getCasesParam.getCurrPage(),getCasesParam.getPageSize());
        List<CaseAndUserInfo> list=police_caseMapper.getArraignmentList(page,ew);
        getCasesVO.setPageparam(getCasesParam);

        if (null!=list&&list.size()>0){
            //绑定多次提讯数据
            for (CaseAndUserInfo recordAndCase : list) {
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",recordAndCase.getSsid());
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    recordAndCase.setArraignments(arraignmentAndRecords);
                }
                if(StringUtils.isNotEmpty(recordAndCase.getCreator())){
                    //查出创建人的名称ew
                    Base_admininfo base_admininfo = new Base_admininfo();
                    base_admininfo.setSsid(recordAndCase.getCreator());
                    Base_admininfo admininfo = base_admininfoMapper.selectOne(base_admininfo);
                    recordAndCase.setCreator(admininfo.getUsername());
                }
            }
            getCasesVO.setPagelist(list);
        }
        result.setData(getCasesVO);
        changeResultToSuccess(result);
    }

    public void addCase(RResult result,ReqParam<AddCaseParam> param,HttpSession session){
        AddCaseParam addCaseParam=param.getParam();
        if (null==addCaseParam){
            result.setMessage("参数为空");
            return;
        }

        AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);
        addCaseParam.setSsid(OpenUtil.getUUID_32());
        addCaseParam.setCreatetime(new Date());
        addCaseParam.setCreator(user.getSsid());
        addCaseParam.setCasebool(0);//初始化0
       int caseinsert_bool = police_caseMapper.insert(addCaseParam);
       LogUtil.intoLog(this.getClass(),"caseinsert_bool__"+caseinsert_bool);
        if (caseinsert_bool>0){
            result.setData(addCaseParam.getSsid());
            changeResultToSuccess(result);
        }
        return;

    }

    public void updateCase(RResult result,ReqParam<UpdateCaseParam> param){
        UpdateCaseParam updateCaseParam=param.getParam();
        if (null==updateCaseParam){
            result.setMessage("参数为空");
            return;
        }
        String casessid=updateCaseParam.getSsid();
        if (StringUtils.isBlank(casessid)){
            result.setMessage("参数为空");
            LogUtil.intoLog(this.getClass(),"getCaseBySsid__ssid:"+casessid);
            return;
        }

        EntityWrapper updateParam=new EntityWrapper();
        updateParam.eq("ssid",casessid);
        int caseupdate_bool = police_caseMapper.update(updateCaseParam,updateParam);
        LogUtil.intoLog(this.getClass(),"caseupdate_bool__"+caseupdate_bool);
        if (caseupdate_bool>0){
            result.setData(caseupdate_bool);
            changeResultToSuccess(result);
        }
        return;
    }

    public void getCaseBySsid(RResult result,ReqParam<GetCaseBySsidParam> param){
        GetCaseBySsidVO getCaseBySsidVO=new GetCaseBySsidVO();
        GetCaseBySsidParam getCaseBySsidParam=param.getParam();
        if (null==getCaseBySsidParam){
            result.setMessage("参数为空");
            return;
        }

        String casessid=getCaseBySsidParam.getCasessid();
        if (StringUtils.isBlank(casessid)){
            result.setMessage("参数为空");
            LogUtil.intoLog(this.getClass(),"getCaseBySsid__ssid:"+casessid);
            return;
        }
        EntityWrapper caseParam=new EntityWrapper();
        caseParam.eq("c.ssid",casessid);
        List<CaseAndUserInfo> caseAndUserInfos =  police_caseMapper.getCaseByUserSsid(caseParam);
        if (null!=caseAndUserInfos&&caseAndUserInfos.size()==1){
            CaseAndUserInfo caseAndUserInfo=caseAndUserInfos.get(0);
            getCaseBySsidVO.setCaseAndUserInfo(caseAndUserInfo);
            result.setData(getCaseBySsidVO);
            changeResultToSuccess(result);
        }else{
            LogUtil.intoLog(this.getClass(),"查找到案件数__"+caseAndUserInfos.size());
            result.setMessage("系统异常");
            return;
        }
        return;
    }

    public void getUserinfoList(RResult result,ReqParam<GetUserinfoListParam> param){
        GetUserinfoListVO getUserinfoListVO=new GetUserinfoListVO();
        GetUserinfoListParam getUserinfoListParam=param.getParam();
        if (null==getUserinfoListParam){
            result.setMessage("参数为空");
            return;
        }

        String cardtypesssid=getUserinfoListParam.getCardtypesssid();//证件类型ssid
        String cardnum=getUserinfoListParam.getCardnum();//证件号

        //根据证件类型和证件号查询用户信息
        EntityWrapper userparam=new EntityWrapper();
        if (StringUtils.isNotBlank(cardtypesssid)){
            userparam.eq("ut.cardtypessid",cardtypesssid);
        }
        if (StringUtils.isNotBlank(cardnum)){
            userparam.like("ut.cardnum",cardnum);
        }

        List<UserInfo> userInfos=police_userinfoMapper.getUserByCard(userparam);

        getUserinfoListVO.setUserinfos(userInfos);
        result.setData(getUserinfoListVO);
        changeResultToSuccess(result);
        return;
    }


    public void addUser(RResult result,ReqParam<AddUserParam> param, HttpSession session){
        AddUserParam addUserParam=param.getParam();
        if (null==addUserParam){
            result.setMessage("参数为空");
            return;
        }

        String username=addUserParam.getUsername();
        Base_admininfo base_admininfo=new Base_admininfo();
        base_admininfo.setSsid(OpenUtil.getUUID_32());
        base_admininfo.setTemporaryaskbool(1);
        AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);
        base_admininfo.setCreator(user.getSsid());
        base_admininfo.setWorkunitssid(user.getWorkunitssid());
        base_admininfo.setLoginaccount(OpenUtil.getUUID_32());
        base_admininfo.setUsername(username);
        base_admininfo.setRegistertime(new Date());
        int insert_bool=base_admininfoMapper.insert(base_admininfo);
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool>0){
            result.setData(base_admininfo.getSsid());
            changeResultToSuccess(result);
            return;
        }
        return;
    }

    public void getRecordByCasessid(RResult result,ReqParam<GetRecordByCasessidParam> param){
        GetRecordByCasessidParam getRecordByCasessidParam=param.getParam();
        if (null==getRecordByCasessidParam){
            result.setMessage("参数为空");
            return;
        }

        String casessid=getRecordByCasessidParam.getCasessid();
        if (StringUtils.isBlank(casessid)){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper caseparam=new EntityWrapper();
        caseparam.eq("cr.casessid",casessid);
        caseparam.orderBy("a.createtime",false);
        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(caseparam);
        if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
           //获取案件提讯的笔录问答
            result.setData(arraignmentAndRecords);
        }
        changeResultToSuccess(result);
        return;
    }

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

        int count=police_wordtemplateMapper.countgetWordTemplateList(ew);
        getWordTemplateListParam.setRecordCount(count);

        Page<WordTemplate> page=new Page<>(getWordTemplateListParam.getCurrPage(),getWordTemplateListParam.getPageSize());
        List<WordTemplate> pagelist=police_wordtemplateMapper.getWordTemplateList(page,ew);

        //检测html文件是否存在-------------------------------------start----------------------------
        for (WordTemplate wordTemplate : pagelist) {
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
            wordTemplate.setWordtemplate_downurl_html(wordtemplate_downurl_html);
        }
        //检测html文件是否存在-------------------------------------end----------------------------
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
        String recordtypessid=uploadWordTemplateParam.getRecordtypessid();
        Integer defaultbool=uploadWordTemplateParam.getDefaultbool();
        String wordtemplatename=uploadWordTemplateParam.getWordtemplatename();



        Police_wordtemplate police_wordtemplate=new Police_wordtemplate();
        police_wordtemplate.setDefaultbool(defaultbool);
        police_wordtemplate.setRecordtypessid(recordtypessid);
        police_wordtemplate.setWordtemplatename(wordtemplatename);

        String wordtemplate_filesavessid=null;
        if(StringUtils.isNotBlank(ssid)){
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
                    String uploadpath=uploadbasepath;
                    String savePath=filewordtemplate;
                    String qg=PropertiesListenerConfig.getProperty("file.qg");
                    String oldwordtemplate_realurl=oldwordTemplate.getWordtemplate_realurl();//旧真实地址




                    String oldfilename=multipartfile.getOriginalFilename();
                    String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                    String filename = wordtemplatename+"_"+DateUtil.getSeconds()+"."+suffix;//模板名称加后缀
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
                        String downurl =uploadpath+OpenUtil.strMinusBasePath(qg, realurl) ;
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
            //新增
            police_wordtemplate.setSsid(OpenUtil.getUUID_32());
            police_wordtemplate.setCreatetime(new Date());
            police_wordtemplate.setWordtemplate_filesavessid(wordtemplate_filesavessid);
            int police_wordtemplateMapper_insertbool =  police_wordtemplateMapper.insert(police_wordtemplate);
            LogUtil.intoLog(this.getClass(),"police_wordtemplateMapper_insertbool__"+police_wordtemplateMapper_insertbool);
            ssid=police_wordtemplate.getSsid();


            if (null!=multipartfile){
                //开始进行文件上传

                try {
                    String uploadpath=uploadbasepath;
                    String savePath=filewordtemplate;
                    String qg=PropertiesListenerConfig.getProperty("file.qg");

                    String oldfilename=multipartfile.getOriginalFilename();
                    String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                    String filename = wordtemplatename+"_"+DateUtil.getSeconds()+"."+suffix;//模板名称加后缀

                    if(oldfilename.endsWith(".doc")||oldfilename.endsWith(".DOC")||oldfilename.endsWith(".docx")||oldfilename.endsWith(".DOCX")){
                        String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                        LogUtil.intoLog(this.getClass(),"笔录word模板真实地址："+realurl);
                        multipartfile.transferTo(new File(realurl));
                        String downurl =uploadpath+OpenUtil.strMinusBasePath(qg, realurl) ;
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
                            if (filesaveinsert_bool>0){
                                //存在文件开始上传，回填上传文件的ssid
                                wordtemplate_filesavessid=base_filesave.getSsid();
                                police_wordtemplate.setWordtemplate_filesavessid(wordtemplate_filesavessid);
                                EntityWrapper updateew=new EntityWrapper();
                                updateew.eq("ssid",ssid);
                                int police_wordtemplateMapper_updatebool =  police_wordtemplateMapper.update(police_wordtemplate,updateew);
                            }
                        }
                    }else {
                        result.setMessage("请选择doc或者docx的word文档进行上传");
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (police_wordtemplateMapper_insertbool>0){
                result.setData(1);
                changeResultToSuccess(result);
            }
        }

        if (defaultbool==1){
            //获取该模板的类型，并且将该类型的处理ssi全部1改为-1
            EntityWrapper updateew=new EntityWrapper();
            updateew.eq("recordtypessid",recordtypessid);
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
                    String recordtypessid=police_wordtemplate.getRecordtypessid();
                    EntityWrapper updateew=new EntityWrapper();
                    updateew.eq("recordtypessid",recordtypessid);
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

    public void changeboolCase(RResult result,ReqParam<ChangeboolCaseParam> param){
        ChangeboolCaseParam changeboolCaseParam=param.getParam();
        if (null==changeboolCaseParam){
            result.setMessage("参数为空");
            return;
        }

        String ssid=changeboolCaseParam.getSsid();
        Integer bool=changeboolCaseParam.getBool();

        if (bool==2){
            int ingsize=0;//进行中的笔录
            //归档，判断下面是否有进行中的笔录
            EntityWrapper ewarraignment=new EntityWrapper();
            ewarraignment.eq("cr.casessid",ssid);
            ewarraignment.orderBy("a.createtime",false);
            List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
            if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {
                    if (Integer.valueOf(arraignmentAndRecord.getRecordbool())!=2){//未完成的
                        ingsize++;
                    }
                }
            }
            if (ingsize>0){
                LogUtil.intoLog(this.getClass(),"ingsize__"+ingsize);
                result.setMessage("请先结束未完成的笔录，再进行归档");
                return;
            }
        }

        Police_case police_case=new Police_case();
        police_case.setEndtime(new Date());
        police_case.setCasebool(bool);

        EntityWrapper ew=new EntityWrapper();
        ew.eq("ssid",ssid);
       int police_caseMapper_updatebool = police_caseMapper.update(police_case,ew);
       if (police_caseMapper_updatebool>0){
           result.setData(police_caseMapper_updatebool);
           changeResultToSuccess(result);
       }
        return;
    }



    public void gnlist(RResult result,ReqParam param){
        GnlistVO vo=new GnlistVO();
        List<String> list= CommonCache.gnlist();
        if (null!=list&&list.size()>0){
            for (String s : list) {
                LogUtil.intoLog(this.getClass(),"gnlist__"+s);
            }
            vo.setLists(list);
        }
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
        RecordrealingCache.setRecordreal(recordssid,recordToProblems);
        changeResultToSuccess(result);
        result.setData(1);
        return;
    }
    /***************************笔录问答实时缓存****end***************************/


}
