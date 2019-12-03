package com.avst.trm.v1.web.cweb.service.policeservice;

import com.alibaba.fastjson.JSON;
import com.avst.trm.v1.common.cache.*;
import com.avst.trm.v1.common.conf.CreateVodThread;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.base.entity.*;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.feignclient.ec.vo.CheckRecordFileStateVO;
import com.avst.trm.v1.feignclient.ec.vo.fd.Flushbonadinginfo;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMCStateParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetMc_modelParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetPhssidByMTssidParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetTdByModelSsidParam_out;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.feignclient.mc.vo.Avstmt_modelAll;
import com.avst.trm.v1.feignclient.mc.vo.GetTdByModelSsidVO;
import com.avst.trm.v1.feignclient.mc.vo.param.Avstmt_modeltd;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.GetPHDataBackParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;
import com.avst.trm.v1.web.cweb.cache.RecordProtectCache;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.conf.AddRecord_Thread;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.param.ArrUserExpandParam;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.avst.trm.v1.common.cache.CommonCache.getSQEntity;

@Service("recordService")
public class RecordService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_recordtypeMapper police_recordtypeMapper;

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
    private Base_nationalityMapper base_nationalityMapper;

    @Autowired
    private OutService outService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private MainService mainService;

    @Autowired
    private RecordService2 recordService2;

    @Autowired
    private Base_nationalMapper base_nationalMapper;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;

    @Autowired
    private EquipmentControl  equipmentControl;

    //=================================================关于笔录======================================================================start
    public void getRecords(RResult result, ReqParam<GetRecordsParam> param,HttpSession session){
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
         boolean creatorbool=getRecordsParam.isCreatorbool();

        EntityWrapper recordparam=new EntityWrapper();
        if (StringUtils.isNotBlank(recordtypessid)){
            recordparam.eq("r.recordtypessid",recordtypessid);
        }
        if (StringUtils.isNotBlank(recordname)){
            recordparam.like("r.recordname",recordname);
        }
        recordparam.ne("r.recordbool",-1);
        if (null!=recordbool){
            recordparam.eq("r.recordbool",recordbool);
        }
        recordparam.ne("c.casebool",-1);//案件不为删除状态

       /* //默认只看自己做的笔录
       if (creatorbool){
            AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
            recordparam.eq("c.creator",user.getSsid());
        }*/
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
        //如果不是超管就只能看自己的
        if (user.getSuperrolebool()==-1){ recordparam.eq("c.creator",user.getSsid());}

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
                List<RecordToProblem> recordToProblems = RecordrealingCache.getRecordrealByRecordssid(recordssid);
                record.setProblems(recordToProblems);

                //根据笔录ssid获取案件信息
                try {
                    EntityWrapper caseParam=new EntityWrapper();
                    caseParam.eq("r.ssid",recordssid);
                    Case case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
                    if (null!=case_){
                        if(StringUtils.isNotEmpty(case_.getCreator())){
                            //查出创建人的名称ew
                            Base_admininfo base_admininfo = new Base_admininfo();
                            base_admininfo.setSsid(case_.getCreator());
                            Base_admininfo admininfo = base_admininfoMapper.selectOne(base_admininfo);
                            case_.setCreatorname(admininfo.getUsername());
                        }
                        record.setCase_(case_);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //根据笔录ssid获取提讯信息
                Police_arraignment police_arraignment=new Police_arraignment();
                police_arraignment.setRecordssid(recordssid);
                police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                if(null!=police_arraignment){
                    record.setPolice_arraignment(police_arraignment);

                    //开始获取录像文件的状态
                    String mtssid=police_arraignment.getMtssid();
                    if (StringUtils.isNotEmpty(mtssid)&&null!=recordbool_&&(recordbool_.intValue()==2||recordbool_.intValue()==3)){
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
                                if (null!=getMCVO) {
                                    String iid=getMCVO.getIid();
                                    if (StringUtils.isNotBlank(iid)){
                                        RResult rr=new RResult();
                                        CheckRecordFileStateParam checkRecordFileStateParam=new CheckRecordFileStateParam();
                                        checkRecordFileStateParam.setSsType(SSType.AVST);
                                        checkRecordFileStateParam.setIid(iid);
                                        rr= equipmentControl.checkRecordFileState(checkRecordFileStateParam);
                                        CheckRecordFileStateVO checkRecordFileStateVO=new CheckRecordFileStateVO();
                                        Integer normalstate=0;//初始化
                                        if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                                            checkRecordFileStateVO = gson.fromJson(gson.toJson(rr.getData()), CheckRecordFileStateVO.class);
                                            List<RecordFileParam> recordFileParams = checkRecordFileStateVO.getRecordList();
                                            if (null != recordFileParams && recordFileParams.size() > 0) {
                                                for (RecordFileParam recordFileParam : recordFileParams) {
                                                    if(recordFileParam.getState()==2){ //2为正常状态
                                                        normalstate++;
                                                    }
                                                }
                                                if (recordFileParams.size()==normalstate.intValue()){
                                                    normalstate=1; //全部弄完
                                                }
                                            }else {
                                                normalstate=-1;
                                            }
                                            LogUtil.intoLog(this.getClass(), "文件数据存储状态__正常数" + normalstate);
                                        }else {
                                            normalstate=-1;
                                            LogUtil.intoLog(this.getClass(), "文件数据存储状态__失败" );
                                        }
                                        record.setRecordfilestate(normalstate);
                                    }

                                }
                            }else {
                                Object msg=getrecord_rr==null?getrecord_rr:getrecord_rr.getMessage();
                                LogUtil.intoLog(this.getClass()," outService.getRecord__请求失败__"+msg);
                            }
                    }

                }

            }
        }
        getRecordsVO.setPagelist(records);
        result.setData(getRecordsVO);
        changeResultToSuccess(result);
        return;
    }

    public void addRecord(RResult result, ReqParam<AddRecordParam> param){

        //请求参数转换
        AddRecordParam addRecordParam = param.getParam();
        if (null==addRecordParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=addRecordParam.getRecordssid();//笔录ssid
        boolean justqwbool=addRecordParam.isJustqwbool();//是否只需要普通的修改问答

      /*  List<RecordToProblem> recordToProblems1=addRecordParam.getRecordToProblems();//笔录携带的题目答案集合*/
        List<RecordToProblem> recordToProblems1=RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合

        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        RecordStatusCache.removeRecordInfoCache(recordssid);/**如果修改成功，删除这条缓存**/

        //判断笔录状态，如果等于2或者0（已完成或者已删除）不进行下一步
        Police_record police_record=new Police_record();
        police_record.setSsid(recordssid);
        police_record=police_recordMapper.selectOne(police_record);
        if (null==police_record){
            result.setMessage("系统异常");
            LogUtil.intoLog(this.getClass(),"addRecord__保存笔录异常__原因：未找到该笔录__");
            return;
        }

        if ((police_record.getRecordbool()==2||police_record.getRecordbool()==-1||police_record.getRecordbool()==3)&&!justqwbool){
            //2已完成或者-1已删除状态
            result.setMessage("该笔录已结束...");
            LogUtil.intoLog(this.getClass(),"addRecord__保存笔录异常__原因：该笔录已结束他人已结束或者已删除状态__police_record.getRecordbool()——-"+police_record.getRecordbool());
            return;
        }


        //修改笔录状态
        Integer recordbool=addRecordParam.getRecordbool();
        String mtssid=addRecordParam.getMtssid();



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

        if (justqwbool){
            //不需要进行下一步
            result.setData(recordssid);
            changeResultToSuccess(result);
            return;
        }


        LogUtil.intoLog(this.getClass(),"recordbool__"+recordbool);
        if (null!=recordbool&&recordbool==2){

            //如果为3就改为休庭
            EntityWrapper caseParam=new EntityWrapper();
            caseParam.eq("r.ssid",recordssid);
            Case case_= case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
            String casessid=null;
            if (null!=case_){
                casessid=case_.getSsid();
            }
            Integer casebool=addRecordParam.getCasebool();
            if (null!=casebool&&casebool.intValue()==3){
                //先获取案件ssid
                if (null!=case_){
                    casessid=case_.getSsid();
                    if (StringUtils.isNotBlank(casessid)){
                        EntityWrapper updateParam=new EntityWrapper();
                        updateParam.eq("ssid",casessid);
                        case_.setCasebool(casebool);
                        int caseupdate_bool = police_caseMapper.update(case_,updateParam);
                        LogUtil.intoLog(this.getClass(),"案件修改成休庭状态caseupdate_bool___"+caseupdate_bool+"__state__"+case_.getCasebool());
                        if (caseupdate_bool>0){
                            recordbool=3;//笔录也需要改为3休庭
                        }
                    }
                }
            }


            EntityWrapper updaterecordParam=new EntityWrapper();
            updaterecordParam.eq("ssid",recordssid);
            Police_record record=new Police_record();
            record.setSsid(recordssid);
            record.setRecordbool(recordbool);
            int updaterecord_bool=police_recordMapper.update(record,updaterecordParam);
            LogUtil.intoLog(this.getClass(),"updaterecord_bool__"+updaterecord_bool);
            if (updaterecord_bool>0){
                //导出笔录pdf和word并且关闭会议
                AddRecord_Thread addRecord_thread=new AddRecord_Thread(recordssid,recordService,recordService2,mtssid,outService);
                addRecord_thread.start();

                //笔录改为已结束，修改案件的谈话结束时间
                if (StringUtils.isNotEmpty(casessid)&&null!=case_){
                    EntityWrapper updateParam=new EntityWrapper();
                    updateParam.eq("ssid",casessid);
                    case_.setEndtime(new Date());
                    int caseupdate_bool = police_caseMapper.update(case_,updateParam);
                    LogUtil.intoLog(this.getClass(),"笔录改为已结束，修改案件的谈话结束时间caseupdate_bool___"+caseupdate_bool);
                }


                //结束后删除
                RecordProtectCache.delRecordecordProtect(recordssid);
            }
        }

        result.setData(recordssid);
        changeResultToSuccess(result);
        return;
    }

    public void updateRecord(RResult result,ReqParam<UpdateRecordParam> reqParam){
        UpdateRecordVO vo=new UpdateRecordVO();

        //请求参数转换
        UpdateRecordParam updateRecordParam = reqParam.getParam();
        if (null==updateRecordParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=updateRecordParam.getSsid();//笔录ssid
        LogUtil.intoLog(1,this.getClass(),"修改笔录参数__recordssid______"+recordssid);
        if (StringUtils.isEmpty(recordssid)){
            result.setMessage("未找到该笔录");
            return;
        }
        Police_record record=gson.fromJson(gson.toJson(updateRecordParam),Police_record.class);
        EntityWrapper recordew=new EntityWrapper();
        recordew.eq("ssid",recordssid);
        int police_recordMapper_update_bool=police_recordMapper.update(record,recordew);
        LogUtil.intoLog(this.getClass(),"police_recordMapper_update_bool__"+police_recordMapper_update_bool);
        if (police_recordMapper_update_bool>0){
            vo.setParam(updateRecordParam);
            result.setData(vo);
            changeResultToSuccess(result);
        }else {
            LogUtil.intoLog(this.getClass(),"修改笔录参数__失败"+police_recordMapper_update_bool);
        }
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

        getRecordByIdVO.setRecord_pausebool(PropertiesListenerConfig.getProperty("record.pausebool"));
        //根据笔录ssid获取录音数据
            EntityWrapper recordParam=new EntityWrapper();
            recordParam.eq("r.ssid",recordssid);
            Record record=police_recordMapper.getRecordBySsid(recordParam);

            if (null!=record){
                //获取头文件
                String realurl=record.getWordheadrealurl();
                String downurl=record.getWordheaddownurl();
                String wordheaddownurl_html=null;
                if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                    if(realurl.endsWith(".doc")){
                        String replace = realurl.replace(".doc", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                            wordheaddownurl_html=downurl.replace(".doc", ".html");
                        }
                    }else if(realurl.endsWith(".docx")){
                        String replace = realurl.replace(".docx", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                            wordheaddownurl_html=downurl.replace(".docx", ".html");
                        }
                    }
                }
                record.setWordheaddownurl_html(wordheaddownurl_html);


                Integer recordbool=record.getRecordbool();
                try {
                    /**
                     *  获取题目答案：未用到全部从缓存获取
                     */
                   /* EntityWrapper ew=new EntityWrapper();
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
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }




                //根据笔录ssid获取案件信息
                String casessid=null;
                try {
                    EntityWrapper caseParam=new EntityWrapper();
                    caseParam.eq("r.ssid",recordssid);

                    Case case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
                    if (null!=case_){
                        EntityWrapper ewuserinfo=new EntityWrapper<>();
                        ewuserinfo.eq("ctu.casessid",case_.getSsid());
                        List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                        if (null!=userInfos&&userInfos.size()>0){
                            case_.setUserInfos(userInfos);
                        }
                        casessid=case_.getSsid();
                        case_.setOccurrencetime_format(case_.getOccurrencetime());
                        record.setCase_(case_);



                        //开始判断是否已经存在了笔录系统
                        EntityWrapper ewarraignment=new EntityWrapper();
                        ewarraignment.eq("cr.casessid",case_.getSsid());
                        ewarraignment.eq("r.recordbool",3);
                        ewarraignment.orderBy("a.createtime",false);
                        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);//不出意外一般只存有一条数据
                        if (null==arraignmentAndRecords||arraignmentAndRecords.size()<1){
                            getRecordByIdVO.setRecord_adjournbool("1");
                        }
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
                        EntityWrapper ewuserinfo=new EntityWrapper<>();
                        ewuserinfo.eq("ctu.casessid",casessid);
                        ewuserinfo.eq("u.ssid",recordUserInfos.getUserssid());
                        List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                        if (null!=userInfos&&userInfos.size()>0){
                            UserInfo userInfo=userInfos.get(0);
                            //获取该案件人当前案件所使用的的证件
                            String usertotypessid=userInfo.getUsertotypessid();
                            EntityWrapper userparam=new EntityWrapper<>();
                            if (StringUtils.isNotBlank(usertotypessid)){
                                userparam.eq("ut.ssid",usertotypessid);
                            }
                            userparam.eq("u.ssid",userInfo.getSsid());
                            List<UserInfo> userinfos=police_userinfoMapper.getUserByCard(userparam);
                            if (null!=userinfos&&userinfos.size()==1){
                                UserInfo userInfo_=userinfos.get(0);
                                userInfo.setCardtypessid(userInfo_.getCardtypessid());
                                userInfo.setCardnum(userInfo_.getCardnum());
                                userInfo.setCardtypename(userInfo_.getCardtypename());
                            }
                            recordUserInfos.setUserInfo(userInfo);
                        }

                        //获取其他角色信息
                        //
                        List<Usergrade> usergrades=new ArrayList<>();
                        EntityWrapper arre=new EntityWrapper();
                        arre.eq("arraignmentssid",recordUserInfos.getArraignmentssid());
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


                                    //查找用户:人员表
                                    Police_userinfo police_userinfo=new Police_userinfo();
                                    police_userinfo.setSsid(userssid);
                                    police_userinfo=police_userinfoMapper.selectOne(police_userinfo);

                                    //查找用户：管理员表
                                    Base_admininfo admininfo=new Base_admininfo();
                                    admininfo.setSsid(userssid);
                                    admininfo=base_admininfoMapper.selectOne(admininfo);

                                    if (null!=police_userinfograde){
                                        Usergrade usergrade=new Usergrade();
                                        usergrade=gson.fromJson(gson.toJson(police_userinfograde), Usergrade.class);
                                        usergrade.setUserssid(userssid);
                                        if (null!=police_userinfo){
                                            usergrade.setUsername(police_userinfo.getUsername());
                                            usergrades.add(usergrade);
                                        }else if (null!=admininfo){
                                            usergrade.setUsername(admininfo.getUsername());
                                            usergrades.add(usergrade);
                                        }
                                    }
                                }
                            }
                        }
                        recordUserInfos.setUsergrades(usergrades);
                        record.setRecordUserInfos(recordUserInfos);
                    }

                    getRecordByIdVO.setRecord(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //根据笔录ssid获取提讯数据
                String mtssid=null;
                Integer mtstate=null;
                String modelssid=null;
                try {
                    Police_arraignment police_arraignment=new Police_arraignment();
                    police_arraignment.setRecordssid(recordssid);
                    police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                    if (null!=police_arraignment){
                        mtssid=police_arraignment.getMtssid();
                        modelssid=police_arraignment.getMtmodelssid();
                        //笔录为进行中的时候
                        if (StringUtils.isNotBlank(mtssid)&&null!=recordbool&&recordbool.intValue()==1){//笔录状态为进行中
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
                        //获取模板通道：笔录制作过程中获取：
                        if (StringUtils.isNotBlank(modelssid)){//&&null!=recordbool&&recordbool.intValue()!=2&&recordbool.intValue()!=3
                            RResult getTdByModelSsid__rr=new RResult();
                            GetTdByModelSsidParam_out getTdByModelSsidParam_out=new GetTdByModelSsidParam_out();
                            getTdByModelSsidParam_out.setModelssid(modelssid);
                            getTdByModelSsidParam_out.setMcType(MCType.AVST);
                            ReqParam reqParam=new ReqParam();
                            reqParam.setParam(getTdByModelSsidParam_out);
                            outService.getTdByModelSsid(getTdByModelSsid__rr,reqParam);
                            if (null!=getTdByModelSsid__rr&&getTdByModelSsid__rr.getActioncode().equals(Code.SUCCESS.toString())){
                                Object data=getTdByModelSsid__rr.getData();
                                if (null!=data){
                                    GetTdByModelSsidVO vo=gson.fromJson(gson.toJson(getTdByModelSsid__rr.getData()),GetTdByModelSsidVO.class);
                                    List<Avstmt_modeltd> modeltds=vo.getModeltds();
                                    if (null!=modeltds&&modeltds.size()>0){
                                        getRecordByIdVO.setModeltds(modeltds);
                                    }
                                }
                            }
                            LogUtil.intoLog(1,this.getClass()," outService.getTdByModelSsid__modeltds__"+getRecordByIdVO.getModeltds().size());
                        }

                        //获取嫌疑人详情

                      record.setPolice_arraignment(police_arraignment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            //未开始的笔录开始获取设备的默认地址作为预览视
            if (null!=recordbool&&recordbool.intValue()==0){
                  ReqParam<GetToOutFlushbonadingListParam> param_ = new ReqParam<>();
                  GetToOutFlushbonadingListParam listParam = new GetToOutFlushbonadingListParam();
                  listParam.setFdType(FDType.FD_AVST);
                  param_.setParam(listParam);
                  RResult result_ = equipmentControl.getToOutDefault(param_);
                  if (null != result_ && result_.getActioncode().equals(Code.SUCCESS.toString())&&null!=result_.getData()) {
                      Flushbonadinginfo flushbonadinginfo=gson.fromJson(gson.toJson(result_.getData()), Flushbonadinginfo.class);
                      if (null!=flushbonadinginfo&&null!=flushbonadinginfo.getLivingurl()){
                          getRecordByIdVO.setDefault_fhurl(flushbonadinginfo.getLivingurl());
                      }
                  }else {
                      LogUtil.intoLog(this.getClass(), "请求equipmentControl.getToOutDefault__出错");
                  }
            }







                if (null!=mtssid&&null!=recordbool&&(recordbool.intValue()==2||recordbool.intValue()==3)){
                    //回放数据，笔录制作中的时候不需要检测

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
                            getRecordByIdVO.setIid(iid);
                            List<AsrTxtParam_toout> asrTxtParam_toouts=getMCVO.getList();
                            if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                                for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                                    String txt=asrTxtParam_toout.getTxt();
                                    String keyword_txt=txt;

                                    //开始检测关键字
                                    RResult checkkeyword_rr=new RResult();
                                    CheckKeywordParam checkKeywordParam=new CheckKeywordParam();
                                    checkKeywordParam.setTxt(txt);
                                    ReqParam checkkeyword_param=new ReqParam();
                                    checkkeyword_param.setParam(checkKeywordParam);
                                    mainService.checkKeyword(checkkeyword_rr,checkkeyword_param);
                                    if (null!=checkkeyword_rr&&checkkeyword_rr.getActioncode().equals(Code.SUCCESS.toString())&&null!=checkkeyword_rr.getData()){
                                        CheckKeywordVO vo=gson.fromJson(gson.toJson(checkkeyword_rr.getData()),CheckKeywordVO.class);
                                        if (null!=vo&&null!=vo.getTxt()){
                                            keyword_txt=vo.getTxt();
                                            asrTxtParam_toout.setKeyword_txt(keyword_txt);
                                        }
                                    }else {
                                        asrTxtParam_toout.setKeyword_txt(keyword_txt);
                                    }
                                }
                            }

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
                            if (null!=getPlayUrlVO){
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
                }


                //用于编辑人员案件信息
                List<Base_nationality> nationalityList=base_nationalityMapper.selectList(null);
                List<Base_national> nationalList=base_nationalMapper.selectList(null);
                EntityWrapper adminparam=new EntityWrapper();
                adminparam.eq("a.adminbool",1);//正常人
                adminparam.orderBy("a.registerTime",false);
                List<AdminAndWorkunit> adminList=base_admininfoMapper.getAdminListAndWorkunit(adminparam);
                getRecordByIdVO.setNationalityList(nationalityList);
                getRecordByIdVO.setNationalList(nationalList);
                getRecordByIdVO.setAdminList(adminList);


                //获取实时数据
                result.setData(getRecordByIdVO);
                changeResultToSuccess(result);

                String gz=record.getGz_iid();
                //检测有没有生成点播文件，没有就生成点播文件
                if(StringUtils.isEmpty(gz)&&null!=recordbool&&(recordbool.intValue()==2||recordbool.intValue()==3)){//只有在完成状态下才会需要生成点播文件
                    //调用生成点播文件线程
                    try {
                        Police_record police_record=record;
                        CreateVodThread thread=new CreateVodThread(result,police_recordMapper,police_record);
                        thread.start();




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else{
                result.setMessage("未找到该笔录");
                LogUtil.intoLog(this.getClass()," police_recordMapper.getRecordBySsid，recordssid："+recordssid);
            }


        return;
    }

    public void recordIndex(RResult result, ReqParam param){
        return;
    }

    public void changeboolRecord(RResult result,ReqParam<ChangeboolRecordParam> param){
        ChangeboolRecordParam changeboolRecordParam=param.getParam();
        if (null==changeboolRecordParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=changeboolRecordParam.getRecordssid();
        Integer recordbool=changeboolRecordParam.getRecordbool();
        LogUtil.intoLog(this.getClass(),"changeboolRecord__参数：recordssid__"+recordssid+"__recordbool__"+recordbool);
        if (StringUtils.isBlank(recordssid)||null==recordbool){
            result.setMessage("参数为空");
            return;
        }

        Police_record record=new Police_record();
        record.setSsid(recordssid);
        record=police_recordMapper.selectOne(record);
        if (null!=record){
            Integer oldrecordbool=record.getRecordbool();
            if (oldrecordbool==1){
                result.setMessage("该笔录正在进行中，请先结束");
                return;
            }

            EntityWrapper entityWrapper=new EntityWrapper();
            entityWrapper.eq("ssid",recordssid);
            record.setSsid(recordssid);
            record.setRecordbool(recordbool);
            int  police_recordMapper_updatebool=police_recordMapper.update(record,entityWrapper);
            if (police_recordMapper_updatebool>0){
                result.setData(police_recordMapper_updatebool);
                changeResultToSuccess(result);
            }
        }else {
            LogUtil.intoLog(this.getClass(),"changeboolRecord__police_recordMapper.selectOne__未找到笔录信息__recordssid__"+recordssid);
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
        caseparam.ne("r.recordbool",-1);//笔录状态不为删除状态
        caseparam.orderBy("a.createtime",false);
        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(caseparam);
        if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
            //获取案件提讯的笔录问答
            result.setData(arraignmentAndRecords);
        }
        changeResultToSuccess(result);
        return;
    }
    //=================================================关于笔录===========================================================================end


    //=================================================关于笔录类型======================================================================start
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



        //开始获取子集
        List<Police_recordtype> records_son=new ArrayList<>();

        EntityWrapper ew=new EntityWrapper();
        ew.eq("pid",police_recordtype.getId());
        records_son =  police_recordtypeMapper.selectList(ew);
        if (null!=records_son&&records_son.size()>0) {
          List<RecordtypeToWord> recordtypes=gson.fromJson(gson.toJson(records_son), new TypeToken<List<RecordtypeToWord>>(){}.getType());
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

       String typename=police_recordtype.getTypename();
       if (StringUtils.isBlank(typename)){
           result.setMessage("请输入类型名称");
           return;
       }

       EntityWrapper police_recordtypes_param=new EntityWrapper();
       police_recordtypes_param.eq("typename",typename);
       List<Police_recordtype> police_recordtypes_=police_recordtypeMapper.selectList(police_recordtypes_param);
       if (null!=police_recordtypes_&&police_recordtypes_.size()>0){
           result.setMessage("笔录类型的名称不能重复");
           return;
       }

       //判断序号
        Integer pid=police_recordtype.getPid();
        Integer ordernum=police_recordtype.getOrdernum();
        if (ordernum!=null){
            EntityWrapper ordernum_param=new EntityWrapper();
            ordernum_param.eq("pid",pid);
            ordernum_param.eq("ordernum",ordernum);
            List<Police_recordtype> police_recordtypes_ordernum=police_recordtypeMapper.selectList(ordernum_param);
            if (null!=police_recordtypes_ordernum&&police_recordtypes_ordernum.size()>0){
                result.setMessage("同类目下排序不能重复");
                return;
            }
        }



        police_recordtype.setSsid(OpenUtil.getUUID_32());
        police_recordtype.setCreatetime(new Date());
        if (police_recordtype.getOrdernum()==null){
            police_recordtype.setOrdernum(1);
        }
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
        Integer id=police_recordtype.getId();
        if (id==null){
            result.setMessage("参数为空");
            LogUtil.intoLog(this.getClass(),"修改笔录类型id为空__id"+id);
            return;
        }

        String typename=police_recordtype.getTypename();
        if (StringUtils.isBlank(typename)){
            result.setMessage("请输入类型名称");
            return;
        }

        EntityWrapper police_recordtypes_param=new EntityWrapper();
        police_recordtypes_param.eq("typename",typename);
        police_recordtypes_param.ne("id",id);
        List<Police_recordtype> police_recordtypes_=police_recordtypeMapper.selectList(police_recordtypes_param);
        if (null!=police_recordtypes_&&police_recordtypes_.size()>0){
            result.setMessage("笔录类型的名称不能重复");
            return;
        }

        //判断序号
        Integer pid=police_recordtype.getPid();
        Integer ordernum=police_recordtype.getOrdernum();
        if (ordernum!=null){
            EntityWrapper ordernum_param=new EntityWrapper();
            ordernum_param.eq("pid",pid);
            ordernum_param.eq("ordernum",ordernum);
            ordernum_param.ne("id",id);
            List<Police_recordtype> police_recordtypes_ordernum=police_recordtypeMapper.selectList(ordernum_param);
            if (null!=police_recordtypes_ordernum&&police_recordtypes_ordernum.size()>0){
                result.setMessage("同类目下排序不能重复");
                return;
            }
        }

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
    //=================================================关于笔录类型=====================================================================end

}
