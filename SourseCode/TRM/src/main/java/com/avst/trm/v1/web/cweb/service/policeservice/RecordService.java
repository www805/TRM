package com.avst.trm.v1.web.cweb.service.policeservice;

import com.alibaba.fastjson.JSON;
import com.avst.trm.v1.common.cache.*;
import com.avst.trm.v1.common.conf.CreateVodThread;
import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.datasourse.base.entity.*;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.req.*;
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
    private Police_casetouserinfoMapper police_casetouserinfoMapper;

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
        recordparam.eq("c.creator",user.getSsid());



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
                /*if (null!=recordbool_&&(recordbool_==1||recordbool_==0)){
                    List<RecordToProblem> recordToProblems = RecordrealingCache.getRecordrealByRecordssid(recordssid);
                    record.setProblems(recordToProblems);
                }else  if (null!=recordbool_&&(recordbool_==2||recordbool_==3)){
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
                }*/
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
            Integer casebool=addRecordParam.getCasebool();
            if (null!=casebool&&casebool.intValue()==3){
                //先获取案件ssid
                EntityWrapper caseParam=new EntityWrapper();
                caseParam.eq("r.ssid",recordssid);

                Case case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
                if (null!=case_){
                    String casessid=case_.getSsid();
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
                     *  获取题目答案：未用到
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
                        if (null!=userInfos&&userInfos.size()==1){
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
                                        usergrade.setGradeintroduce(police_userinfograde.getGradeintroduce());
                                        usergrade.setGrade(police_userinfograde.getGrade());
                                        usergrade.setGradename(police_userinfograde.getGradename());
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

    public void addRecordTemplate(RResult result, ReqParam param){
        return;
    }

    public void addCaseToArraignment(RResult result, ReqParam<AddCaseToArraignmentParam> param, HttpSession session){
        AddCaseToArraignmentVO addCaseToArraignmentVO=new AddCaseToArraignmentVO();

        AddCaseToArraignmentParam addCaseToArraignmentParam=param.getParam();
        if (null==addCaseToArraignmentParam){
            result.setMessage("参数为空");
            return;
        }



        //参数**************************************************************************************************************************************start
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);//session用户
        List<Userto> usertos=addCaseToArraignmentParam.getUsertos();//其他在场人员信息
        Integer skipCheckbool=addCaseToArraignmentParam.getSkipCheckbool();//是否跳过检测
        Integer skipCheckCasebool=addCaseToArraignmentParam.getSkipCheckCasebool();//是否跳过案件状态检测主要针对休庭状态
        Integer multifunctionbool=addCaseToArraignmentParam.getMultifunctionbool();//功能类型
        String mtmodelssid=addCaseToArraignmentParam.getMtmodelssid();//会议模板ssid
        String mtmodelssidname=addCaseToArraignmentParam.getMtmodelssidname();//会议模板名称
        String wordtemplatessid=addCaseToArraignmentParam.getWordtemplatessid();//笔录模板ssid
        //笔录信息
        String recordtypessid=addCaseToArraignmentParam.getRecordtypessid();//笔录类型
        String recordname=addCaseToArraignmentParam.getRecordname()==null?"":addCaseToArraignmentParam.getRecordname().replace(" ", "").replace("\"", "");//笔录名称
       //讯问信息
        String adminssid=addCaseToArraignmentParam.getAdminssid();//询问人一
        Integer asknum=addCaseToArraignmentParam.getAsknum()==null?0:addCaseToArraignmentParam.getAsknum();
        String askobj=addCaseToArraignmentParam.getAskobj();//询问对象
        String recordadminssid=addCaseToArraignmentParam.getRecordadminssid();//记录人sssid
        String recordplace=addCaseToArraignmentParam.getRecordplace();//问话地点


        //需要判断是否新增或者修改的参数******************************start
        String casessid=addCaseToArraignmentParam.getCasessid();//案件ssid
        String userssid=addCaseToArraignmentParam.getUserssid();//人员ssid
        UserInfo addUserInfo=addCaseToArraignmentParam.getAddUserInfo();//新增人员的信息
        Police_case addPolice_case=addCaseToArraignmentParam.getAddPolice_case();//新增案件的信息

        String otheruserinfoname=addCaseToArraignmentParam.getOtheruserinfoname();//新增询问人二的名称
        String otherworkname=addCaseToArraignmentParam.getOtherworkname();//新增询问人二的对应的工作单位
        String otheradminssid=addCaseToArraignmentParam.getOtheradminssid();//询问人二的ssid
        String otherworkssid=addCaseToArraignmentParam.getOtherworkssid();//询问人二对应的工作单位ssid
        //需要判断是否新增或者修改的参数******************************end

        //用于法院拓展表数据
        List<UserInfo> arraignmentexpand=addCaseToArraignmentParam.getArraignmentexpand();
        List<ArrUserExpandParam> arrUserExpandParams=addCaseToArraignmentParam.getArrUserExpandParams();
        //参数**************************************************************************************************************************************end

        //快速笔录，添加默认数据
      if (multifunctionbool==1){
            //一键谈话：默认使用会议的谈话模板ssid
            String cardtypessid=PropertiesListenerConfig.getProperty("cardtype_default");//默认使用身份证
            String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String conversationmsg="快速谈话_"+time;


            //用户信息使用默认
            addUserInfo=new UserInfo();
            addUserInfo.setUsername("未知");
            addUserInfo.setCardnum("未知");
            addUserInfo.setCardtypessid(cardtypessid);



            //案件信息默认
            addPolice_case=new Police_case();
            addPolice_case.setCasename("案件名_"+conversationmsg);
            addPolice_case.setOccurrencetime(new Date());
            addPolice_case.setStarttime(new Date());

            //笔录名称
            recordname="审讯笔录【快速谈话】_"+time;
            askobj="询问对象_"+conversationmsg;

            if (StringUtils.isBlank(adminssid)){
                adminssid=user.getSsid();
            }
        }
        addCaseToArraignmentVO.setMultifunctionbool(multifunctionbool);

        //整理模板
        if (multifunctionbool==1||multifunctionbool==2 ){//||单组件时候
            mtmodelssid=PropertiesListenerConfig.getProperty("mcmodel_conversation");//使用指定谈话模板
        }
        //模板为空使用默认指定模板
        if (StringUtils.isBlank(mtmodelssid)) {
            //会议模板为空，直接取默认的
            Base_type base_type=new Base_type();
            base_type.setType(CommonCache.getCurrentServerType());
            base_type=base_typeMapper.selectOne(base_type);
            if (null!=base_type){
                mtmodelssid=base_type.getMtmodelssid();
            }
        }
        LogUtil.intoLog(this.getClass(),"【开始笔录】添加笔录使用的会议模板ssid_"+mtmodelssid);
        if (StringUtils.isBlank(mtmodelssid)){
            result.setMessage("未找到会议模板");
            return;
        }


        //---------------------------------------------------------------------------------------------------------------检测开始
        //需要检测休庭中的案件 给出提示
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】休庭案件检测__skipCheckCasebool:"+skipCheckCasebool+"__casessid:"+casessid+"__userssid:"+userssid);
        if (skipCheckCasebool==-1&&StringUtils.isNotBlank(casessid)&&StringUtils.isNotBlank(userssid)){
            EntityWrapper caseparam=new EntityWrapper();
            caseparam.eq("c.ssid",casessid);
            List<Case> cases=police_caseMapper.getCase(caseparam);
            if (null!=cases&&cases.size()==1&&cases.get(0).getCasebool()==3){
                Case case_=cases.get(0);
                EntityWrapper ewuserinfo=new EntityWrapper<>();
                ewuserinfo.eq("ctu.casessid",case_.getSsid());
                List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                if (null!=userInfos&&userInfos.size()>0){
                    for (UserInfo userInfo : userInfos) {
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

                        ////获取该用户全部的证件
                        EntityWrapper ewcard=new EntityWrapper<>();
                        ewcard.eq("u.ssid",userInfo.getSsid());
                        List<UserInfoAndCard> cards=police_userinfoMapper.getCardByUser(ewcard);
                        userInfo.setCards(cards);
                    }
                    case_.setUserInfos(userInfos);
                }
                addCaseToArraignmentVO.setCase_(case_);
                addCaseToArraignmentVO.setCaseingbool(true);
                result.setData(addCaseToArraignmentVO);
                return;
            }
        }

        //人员检测：未针对法院全部人员
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】人员检测__skipCheckbool:"+skipCheckbool);
        if (skipCheckbool==-1){
            List<String> adminssids=new ArrayList<>();
            if (StringUtils.isNotBlank(otheradminssid)){
                adminssids.add(otheradminssid);//记录人
            }
            if (StringUtils.isNotBlank(adminssid)){
                adminssids.add(adminssid);//主询问人
            }
            CheckStartRecordParam checkStartRecordParam=new CheckStartRecordParam();
            checkStartRecordParam.setMtmodel_ssid(mtmodelssid);
            checkStartRecordParam.setUserinfo_ssid(userssid);//被询问人
            checkStartRecordParam.setAdmininfos_ssid(adminssids);
            RResult checkrecordforuser_rr=new RResult();
            Integer[] recordbools=new Integer[]{1};//检测状态可填两个
            boolean bool = checkRecordForUser(checkrecordforuser_rr,checkStartRecordParam,user.getSsid(),recordbools);
            if (!bool){
                CheckStartRecordVO vo=gson.fromJson(gson.toJson(checkrecordforuser_rr.getData()),CheckStartRecordVO.class);
                addCaseToArraignmentVO.setCheckStartRecordVO(vo);
                addCaseToArraignmentVO.setRecordingbool(true);
                result.setData(addCaseToArraignmentVO);
                 return;
            }
        }
        //---------------------------------------------------------------------------------------------------------------检测结束


        if (StringUtils.isBlank(mtmodelssidname)){
            List<Avstmt_modelAll> modelAlls=new ArrayList<>();
            GetMc_modelParam_out getMc_modelParam_out=new GetMc_modelParam_out();
            getMc_modelParam_out.setMcType(MCType.AVST);
            getMc_modelParam_out.setModelssid(mtmodelssid);
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




        //正式开始laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        if (StringUtils.isNotBlank(recordname)){
            EntityWrapper recordname_ew=new EntityWrapper();
            recordname_ew.eq("recordname",recordname);
            recordname_ew.ne("recordbool",-1);
            List<Police_record> police_records=police_recordMapper.selectList(recordname_ew);
            if (null!=police_records&&police_records.size()>0){
                result.setMessage("笔录名称不能重复");
                LogUtil.intoLog(1,this.getClass(),"【开始笔录】笔录名称不能重复__"+recordname);
                return;
            }
        }else {
            Police_recordtype police_recordtype = new Police_recordtype();
            police_recordtype.setSsid(recordtypessid);
            police_recordtype = police_recordtypeMapper.selectOne(police_recordtype);
            recordname=""+addUserInfo.getUsername()+"《"+addPolice_case.getCasename().trim()+"》"+mtmodelssidname+"_"+police_recordtype.getTypename().replace(" ", "")+"_第"+(Integer.valueOf(asknum)+1)+"次";
        }
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】recordname__"+recordname);



        //判断是否需要新增或修改工作单位
        if (StringUtils.isBlank(otherworkssid)&&StringUtils.isNotBlank(otherworkname)){
            LogUtil.intoLog(this.getClass(),"【开始笔录】需要新增工作单位____"+otherworkname);
            Police_workunit workunit=new Police_workunit();
            workunit.setSsid(OpenUtil.getUUID_32());
            workunit.setCreatetime(new Date());
            workunit.setWorkname(otherworkname);
            int police_workunitMapper_insertbool= police_workunitMapper.insert(workunit);
            LogUtil.intoLog("【开始笔录】police_workunitMapper_insertbool__"+police_workunitMapper_insertbool);
            if (police_workunitMapper_insertbool>0){
                otherworkssid=workunit.getSsid();
            }
        }
        //判断是否需要新增或修改询问人二
        if (StringUtils.isBlank(otheradminssid)&&StringUtils.isNotBlank(otheruserinfoname)){
            LogUtil.intoLog(this.getClass(),"【开始笔录】需要新增询问人二____"+otheruserinfoname);
            Base_admininfo base_admininfo=new Base_admininfo();
            base_admininfo.setSsid(OpenUtil.getUUID_32());
            base_admininfo.setTemporaryaskbool(1);//是否为临时询问人1是
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



        //需要新增人员信息
        String usertotypessid=null;//人员证件ssid
        if (StringUtils.isBlank(addUserInfo.getCardtypessid())){
            addUserInfo.setCardtypessid(PropertiesListenerConfig.getProperty("cardtype_default"));//使用默认身份证类型
        }

        String cardnum = addUserInfo.getCardnum();//人员证件号码
        String username = addUserInfo.getUsername();//人员名称
        List<UserInfo> checkuserinfos=new ArrayList<>();
        EntityWrapper checkuserparam=new EntityWrapper();
        checkuserparam.eq("ut.cardtypessid",addUserInfo.getCardtypessid());
        /*if (StringUtils.isBlank(cardnum)){
            checkuserparam.eq("u.username",username);
            checkuserinfos=police_userinfoMapper.getUserByCard(checkuserparam);
        }else */if (StringUtils.isNotEmpty(cardnum)){
            checkuserparam.eq("ut.cardnum",cardnum);
            checkuserinfos=police_userinfoMapper.getUserByCard(checkuserparam);
        }

        if ((null==checkuserinfos||checkuserinfos.size()<1)&&StringUtils.isBlank(userssid)){
            LogUtil.intoLog(this.getClass(),"【开始笔录】需要新增人员____");
            addUserInfo.setSsid(OpenUtil.getUUID_32());
            addUserInfo.setCreatetime(new Date());
            int insertuserinfo_bool = police_userinfoMapper.insert(addUserInfo);
            LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
           if (insertuserinfo_bool>0){
               Police_userinfototype police_userinfototype=new Police_userinfototype();
               police_userinfototype.setCardnum(cardnum==null?OpenUtil.getUUID_32():cardnum);
               police_userinfototype.setSsid(OpenUtil.getUUID_32());
               police_userinfototype.setCreatetime(new Date());
               police_userinfototype.setCardtypessid(addUserInfo.getCardtypessid());
               police_userinfototype.setUserssid(addUserInfo.getSsid());
              int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
               LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
               userssid=addUserInfo.getSsid();//得到用户的ssid
               usertotypessid=police_userinfototype.getSsid();
               LogUtil.intoLog(this.getClass(),"【开始笔录】新增的人员ssid____"+userssid+"___usertotypessid__"+usertotypessid);
           }
        }else if(checkuserinfos.size()==1){
            LogUtil.intoLog(this.getClass(),"【开始笔录】需要修改人员____");
            UserInfo userInfo=checkuserinfos.get(0);
            userssid=userInfo.getSsid();
          //修改用户信息
            EntityWrapper updateuserinfoParam=new EntityWrapper();
            updateuserinfoParam.eq("ssid",userssid);
            Police_userinfo police_userinfo=gson.fromJson(gson.toJson(addUserInfo),Police_userinfo.class);
            int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
            LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
            EntityWrapper userparam=new EntityWrapper();
            userparam.eq("ut.cardtypessid",addUserInfo.getCardtypessid());
            userparam.like("ut.cardnum",addUserInfo.getCardnum());
            List<UserInfo> userInfos_=police_userinfoMapper.getUserByCard(userparam);
            if (null!=userInfos_&&userInfos_.size()==1){
                UserInfo userInfo_=userInfos_.get(0);
                usertotypessid=userInfo_.getUsertotypessid();
            }
            LogUtil.intoLog(this.getClass(),"【开始笔录】修改的人员ssid____"+userssid+"___usertotypessid__"+usertotypessid);
        }
        LogUtil.intoLog(this.getClass(),"【开始笔录】"+userssid+"___usertotypessid__"+usertotypessid);
        if (StringUtils.isBlank(userssid)||StringUtils.isBlank(usertotypessid)){
            result.setMessage("人员数据有误请注意");
            LogUtil.intoLog(1,this.getClass(),"【开始笔录】人员数据有误请注意...");
            return;
        }


         //需要新增案件信息
         String casenum=addPolice_case.getCasenum();//案件号码
         Police_case police_case_1=null;
         if (StringUtils.isNotBlank(casessid)){
             police_case_1=new Police_case();
             police_case_1.setSsid(casessid);
             police_case_1=police_caseMapper.selectOne(police_case_1);
         }
         if (StringUtils.isBlank(casessid)&&police_case_1==null){
             String casename=addPolice_case.getCasename();//案件号码
             if (StringUtils.isNotBlank(casename)){
                 //判断案件是否重复
                 EntityWrapper police_cases_param=new EntityWrapper();
                 police_cases_param.eq("casename",casename.trim());
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
                 police_cases_param.ne("casebool",-1);
                 List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
                 if (null!=police_cases_&&police_cases_.size()>0){
                     result.setMessage("案件编号不能重复");
                     return;
                 }
             }


                LogUtil.intoLog(this.getClass(),"【开始笔录】需要新增案件信息____casename:"+casename);
                addPolice_case.setSsid(OpenUtil.getUUID_32());
                addPolice_case.setCreatetime(new Date());
                addPolice_case.setOrdernum(0);
                addPolice_case.setCasebool(0);
                addPolice_case.setCreator(user.getSsid());
                int insertcase_bool =  police_caseMapper.insert(addPolice_case);
                LogUtil.intoLog(this.getClass(),"insertcase_bool__"+insertcase_bool);
                if (insertcase_bool>0){
                    casessid=addPolice_case.getSsid();

                    //添加案件人员表
                    Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                    police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                    police_casetouserinfo_.setCreatetime(new Date());
                    police_casetouserinfo_.setCasessid(casessid);
                    police_casetouserinfo_.setUserssid(userssid);
                    police_casetouserinfo_.setUsertotypessid(usertotypessid);
                    int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                    LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
                }
         }else if (null!=police_case_1){
             String casename=addPolice_case.getCasename();//案件号码
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


             LogUtil.intoLog(this.getClass(),"【开始笔录】需要修改案件信息____casename:"+casename);

             //修改案件信息参数
             EntityWrapper updatecaseParam=new EntityWrapper();
             if (null!=police_case_1){
                 if (police_case_1.getCasebool()==3&&skipCheckCasebool==1){
                     updatecaseParam.eq("casebool",1);//改为进行中
                 }
                 updatecaseParam.eq("ssid",casessid);
                 int updatecase_bool = police_caseMapper.update(addPolice_case,updatecaseParam);
                 LogUtil.intoLog(this.getClass(),"updatecase_bool__"+updatecase_bool);
                 if (updatecase_bool>0){
                     //案件人员表修改，1根据用户ssid以及案件ssid判断该用户是否在该案件中，在不用管；不在新增案件人员表
                     EntityWrapper casetouserinfoParam=new EntityWrapper();
                     casetouserinfoParam.eq("casessid",casessid);
                     casetouserinfoParam.eq("userssid",userssid);
                     List<Police_casetouserinfo> police_casetouserinfos_=police_casetouserinfoMapper.selectList(casetouserinfoParam);
                     if (null==police_casetouserinfos_||police_casetouserinfos_.size()<1){
                         //新增关联案件人员表
                         Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                         police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                         police_casetouserinfo_.setCreatetime(new Date());
                         police_casetouserinfo_.setCasessid(casessid);
                         police_casetouserinfo_.setUserssid(userssid);
                         police_casetouserinfo_.setUsertotypessid(usertotypessid);
                         int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                         LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
                     }
                 }
             }
         }


        //修改案件编号
//        if (StringUtils.isBlank(addPolice_case.getCasenum())){
//            Police_case police_case_=new Police_case();
//            police_case_.setSsid(casessid);
//            police_case_=police_caseMapper.selectOne(police_case_);
//            if (null!=police_case_){
//                //截取类型的前一个字母
//                String type=CommonCache.getCurrentServerType();
//                int index=type.indexOf("_");
//                String q="";
//                if (index>-1&&index<type.length()-1){
//                    String test3before=type.substring(0,index);
//                    String test3after=type.substring(index+1);
//                    q=test3before.substring(0,1)+test3after.substring(0,1);
//                }
//
//                //拼接案件编号
//                String numbertType =q+ new SimpleDateFormat("yyMMdd").format(new Date());
//                String numberNo = getNumberNo(numbertType, String.valueOf(police_case_.getId()-1<1?0:police_case_.getId()-1));
//                addPolice_case.setCasenum(numberNo);
//
//                //修改编号
//                EntityWrapper updateparam=new EntityWrapper();
//                updateparam.eq("ssid",addPolice_case.getSsid());
//                int caseupdate_bool = police_caseMapper.update(addPolice_case,updateparam);
//                if (caseupdate_bool>0){
//                    LogUtil.intoLog(this.getClass(),"案件编号修改成功__"+numberNo);
//                }
//            }
//        }


        LogUtil.intoLog(this.getClass(),"userssid__"+userssid+"__casessid__"+casessid);
        if (StringUtils.isBlank(userssid)||StringUtils.isBlank(casessid)){
            result.setMessage("人员案件参数为空");
            return;
        }


        //添加笔录信息
        Police_record record=new Police_record();
        record.setSsid(OpenUtil.getUUID_32());
        record.setCreatetime(new Date());
        record.setRecordbool(0);//1进行中0未开始
        record.setRecordtypessid(recordtypessid==null?PropertiesListenerConfig.getProperty("recordtype_default"):recordtypessid);//默认谈话办案
        record.setRecordname(recordname);
        if (StringUtils.isBlank(wordtemplatessid)){
            //选用默认的
            EntityWrapper word_ew=new EntityWrapper();
            word_ew.ne("wordtype",2);
            List<WordTemplate> wordTemplates=police_wordtemplateMapper.getWordTemplate(word_ew);
            if (null!=wordTemplates&&wordTemplates.size()>0){
                for (WordTemplate wordTemplate : wordTemplates) {
                    if (wordTemplate.getDefaultbool()==1){
                        wordtemplatessid=wordTemplate.getSsid();
                    }
                }
            }
            //选用原始默认的
            if (StringUtils.isBlank(wordtemplatessid)){
                record.setWordtemplatessid(PropertiesListenerConfig.getProperty("wordtemplate_default"));
            }
        }
        record.setWordtemplatessid(wordtemplatessid);
        int insertrecord_bool=police_recordMapper.insert(record);
        LogUtil.intoLog(this.getClass(),"insertrecord_bool__"+insertrecord_bool);
        if (insertrecord_bool<0){
            result.setMessage("系统异常");
            return;
        }
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】新增的笔录ssid__recordssid："+record.getSsid());



       //添加提讯数据
        Police_arraignment arraignment=new Police_arraignment();
        arraignment.setSsid(OpenUtil.getUUID_32());
        arraignment.setCreatetime(new Date());
        arraignment.setAdminssid(adminssid);
        arraignment.setAsknum(asknum+1);
        arraignment.setAskobj(askobj);
        arraignment.setRecordadminssid(recordadminssid);
        arraignment.setRecordplace(recordplace);
        arraignment.setOtheradminssid(otheradminssid);
        arraignment.setRecordssid(record.getSsid());
        arraignment.setMtmodelssid(mtmodelssid);//会议模板ssid
        arraignment.setUserssid(userssid);
        arraignment.setMultifunctionbool(multifunctionbool);
        int insertarraignment_bool=police_arraignmentMapper.insert(arraignment);
        LogUtil.intoLog(this.getClass(),"insertarraignment_bool__"+insertarraignment_bool);
        if (insertarraignment_bool<0){
            result.setMessage("系统异常");
            return;
        }
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】新增的提讯ssid__arraignmentssid："+arraignment.getSsid());




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
            if (police_caseMapper_updatebool>0){
                //查找该案件下所有提讯的笔录，找到暂停中的笔录改为已完成
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",casessid);
                ewarraignment.eq("r.recordbool",3);
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    //修改笔录状态为已完成
                    for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {
                        Police_record record_=new Police_record();
                        String recordssid=arraignmentAndRecord.getRecordssid();
                        if (StringUtils.isNotBlank(recordssid)){
                            EntityWrapper entityWrapper=new EntityWrapper();
                            entityWrapper.eq("ssid",recordssid);
                            record_.setSsid(recordssid);
                            record_.setRecordbool(2);
                            int  police_recordMapper_updatebool=police_recordMapper.update(record_,entityWrapper);
                            if (police_recordMapper_updatebool>0){
                                LogUtil.intoLog(this.getClass(),"changeboolCase__police_recordMapper.update__暂停笔录状态改为已完成__police_recordMapper_updatebool__"+police_recordMapper_updatebool);
                            }
                        }
                    }
                }
            }
         }

        //添加其他
        if (null!=usertos&&usertos.size()>0){
            for (Userto userto : usertos) {
                Police_userinfo userinfo_=new Police_userinfo();

                String usertocardnum = userto.getCardnum();//人员证件号码
                String usertousername = userto.getUsername();//人员名称
                List<UserInfo> checkusertoinfos=new ArrayList<>();
                EntityWrapper checkusertoparam=new EntityWrapper();
                checkusertoparam.eq("ut.cardtypessid",userto.getCardtypessid());
               /* if (StringUtils.isBlank(usertocardnum)){
                    checkusertoparam.eq("u.username",usertousername);
                    checkusertoinfos=police_userinfoMapper.getUserByCard(checkusertoparam);
                }else */if (StringUtils.isNotEmpty(usertocardnum)){
                    checkusertoparam.eq("ut.cardnum",usertocardnum);
                    checkusertoinfos=police_userinfoMapper.getUserByCard(checkusertoparam);
                }
                //先检查是否存在了该人员信息，不存在新增，存在修改
                if ((null==checkusertoinfos||checkusertoinfos.size()<1)){  //找不到用户或者用户证件号为空直接新增
                    LogUtil.intoLog(this.getClass(),"【开始笔录】其他在场__需要新增人员____");
                    userinfo_.setSsid(OpenUtil.getUUID_32());
                    userinfo_.setCreatetime(new Date());
                    userinfo_.setSex(userto.getSex());
                    userinfo_.setPhone(userto.getPhone());
                    userinfo_.setUsername(userto.getUsername());
                    int insertuserinfo_bool = police_userinfoMapper.insert(userinfo_);
                    LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
                    if (insertuserinfo_bool>0){
                        Police_userinfototype police_userinfototype=new Police_userinfototype();
                        police_userinfototype.setCardnum(userto.getCardnum()==null?OpenUtil.getUUID_32():userto.getCardnum());
                        police_userinfototype.setSsid(OpenUtil.getUUID_32());
                        police_userinfototype.setCreatetime(new Date());
                        police_userinfototype.setCardtypessid(userto.getCardtypessid());
                        police_userinfototype.setUserssid(userinfo_.getSsid());
                        int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
                        LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
                    }
                }else if(checkusertoinfos.size()==1) {
                    LogUtil.intoLog(this.getClass(),"其他在场__需要修改人员____");
                    //修改
                    userinfo_=checkusertoinfos.get(0);
                    userinfo_.setSex(userto.getSex());
                    userinfo_.setPhone(userto.getPhone());
                    userinfo_.setUsername(userto.getUsername());
                    EntityWrapper updateuserinfoParam=new EntityWrapper();
                    updateuserinfoParam.eq("ssid",userinfo_.getSsid());
                    int updateuserinfo_bool = police_userinfoMapper.update(userinfo_,updateuserinfoParam);
                    LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                }

                //其他人员处理完毕处理人员关系
                if (null!=userinfo_.getSsid()){
                    Police_userto  userto1=new Police_userto();
                    userto1.setSsid(OpenUtil.getUUID_32());
                    userto1.setCreatetime(new Date());
                    userto1.setArraignmentssid(arraignment.getSsid());
                    userto1.setUserssid(userssid);
                    userto1.setLanguage(userto.getLanguage());
                    userto1.setOtheruserssid(userinfo_.getSsid());
                    userto1.setRelation(userto.getRelation());
                    userto1.setUsertitle(userto.getUsertitle());
                    userto1.setUsertype(userto.getUsertype());
                    int insertuserto_bool= police_usertoMapper.insert(userto1);
                    LogUtil.intoLog(this.getClass(),"insertuserto_bool__"+insertuserto_bool);
                }
            }
        }


        LogUtil.intoLog(1,this.getClass(),"【开始笔录】笔录头文件wordtemplatessid__"+wordtemplatessid);
        if (StringUtils.isNotEmpty(wordtemplatessid)){
            //生成初始化word头文件
            try {
                RResult exportwordhead_rr=new RResult();
                ExportWordParam exportwordheadParam=new ExportWordParam();
                exportwordheadParam.setRecordssid(record.getSsid());
                exportwordheadParam.setWordheadbool(true);
                ReqParam reqParam=new ReqParam();
                reqParam.setParam(exportwordheadParam);
                recordService2.exportWord(exportwordhead_rr, reqParam);
                if (null != exportwordhead_rr && exportwordhead_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                    LogUtil.intoLog(this.getClass(),"recordService.exportWord笔录结束时exportWord__成功__保存问答");
                }else{
                    LogUtil.intoLog(this.getClass(),"recordService.exportWord笔录结束时exportWord__出错__"+exportwordhead_rr.getMessage());
                }
            } catch (Exception e) {
                LogUtil.intoLog(1,this.getClass(),"【开始笔录】生成头文件有误__");
                e.printStackTrace();
            }
        }



        //添加提讯表拓展数据
        String arraignmentssid=arraignment.getSsid();
        if (null!=arraignmentexpand&&arraignmentexpand.size()>0){
            for (UserInfo userInfo : arraignmentexpand) {
                if (null!=userInfo){
                    String userinfogradessid=userInfo.getUserinfogradessid();
                    String userInfossid=null;
                    if (userinfogradessid.equals("userinfograde2")){//默认参数//对应被告==被询问人
                        userInfossid=userssid;//被询问人的，以免重复增加
                    }else {
                        if (StringUtils.isBlank(userInfo.getCardtypessid())){
                            userInfo.setCardtypessid(PropertiesListenerConfig.getProperty("cardtype_default"));
                        }

                        String userInfocardnum = userInfo.getCardnum();//人员证件号码
                        String userInfousername = userInfo.getUsername();//人员名称
                        List<UserInfo> checkuserInfoinfos=new ArrayList<>();
                        EntityWrapper checkuserInfoparam=new EntityWrapper();
                        checkuserInfoparam.eq("ut.cardtypessid",userInfo.getCardtypessid());
                            /*if (StringUtils.isBlank(userInfocardnum)){
                                checkuserInfoparam.eq("u.username",userInfousername);
                                checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                            }else */if (StringUtils.isNotEmpty(userInfocardnum)){
                            checkuserInfoparam.eq("ut.cardnum",userInfocardnum);
                            checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                        }

                        if (null==checkuserInfoinfos||checkuserInfoinfos.size()<1){
                            LogUtil.intoLog(this.getClass(),"【开始笔录】拓展数据需要新增人员____");
                            userInfo.setSsid(OpenUtil.getUUID_32());
                            userInfo.setCreatetime(new Date());
                            Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                            int insertuserinfo_bool = police_userinfoMapper.insert(police_userinfo);
                            LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
                            if (insertuserinfo_bool>0){
                                userInfossid=police_userinfo.getSsid();
                                Police_userinfototype police_userinfototype=new Police_userinfototype();
                                police_userinfototype.setCardnum(userInfo.getCardnum());
                                police_userinfototype.setSsid(OpenUtil.getUUID_32());
                                police_userinfototype.setCreatetime(new Date());
                                police_userinfototype.setCardtypessid(userInfo.getCardtypessid());
                                police_userinfototype.setUserssid(userInfo.getSsid());
                                int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
                                LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
                            }
                        }else if (checkuserInfoinfos.size()==1){
                            UserInfo userinfo_=checkuserInfoinfos.get(0);
                            //修改用户信息
                            EntityWrapper updateuserinfoParam=new EntityWrapper();
                            updateuserinfoParam.eq("ssid",userinfo_.getSsid());
                            Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                            int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
                            LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                            userInfossid=userinfo_.getSsid();
                        }
                    }

                    LogUtil.intoLog(1,this.getClass(),"【开始笔录】添加拓展表数据外部人员__userInfossid："+userInfossid+"__userinfogradessid："+userinfogradessid);
                    if (StringUtils.isNotEmpty(userInfossid)&&StringUtils.isNotEmpty(userinfogradessid)){
                        Police_arraignmentexpand police_arraignmentexpand=new Police_arraignmentexpand();
                        police_arraignmentexpand.setArraignmentssid(arraignmentssid);
                        police_arraignmentexpand.setSsid(OpenUtil.getUUID_32());
                        police_arraignmentexpand.setCreatetime(new Date());
                        police_arraignmentexpand.setExpandname(userinfogradessid);
                        police_arraignmentexpand.setExpandvalue(userInfossid);
                        int police_arraignmentexpandMappe_insertbool=police_arraignmentexpandMapper.insert(police_arraignmentexpand);
                        LogUtil.intoLog(1,this.getClass(),"police_arraignmentexpandMappe_insertbool__"+police_arraignmentexpandMappe_insertbool);
                    }
                }
            }
            LogUtil.intoLog(1,this.getClass(),"【开始笔录】添加拓展表数据外部人员数__"+arraignmentexpand.size());
        }

        //添加拓展表数据：一般内部人员
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
                        int police_arraignmentexpandMappe_insertbool=police_arraignmentexpandMapper.insert(police_arraignmentexpand);
                        LogUtil.intoLog(1,this.getClass(),"police_arraignmentexpandMappe_insertbool__"+police_arraignmentexpandMappe_insertbool);
                    }else {
                        LogUtil.intoLog(1,this.getClass(),"police_arraignmentexpandMappe_insertbool__userssid_"+userssid_+"__userinfogradessid__"+userinfogradessid);
                    }
                }
            }
            LogUtil.intoLog(1,this.getClass(),"【开始笔录】__内部人员数"+arrUserExpandParams.size());
        }
        LogUtil.intoLog(1,this.getClass(),"****************************【开始笔录】开始笔录成功****************************");
        addCaseToArraignmentVO.setRecordssid(record.getSsid());
        result.setData(addCaseToArraignmentVO);//返回开始笔录的ssid
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

        if (StringUtils.isBlank(cardnum)){
            result.setMessage("请输入证件号");
            return;
        }


        //根据证件类型和证件号查询用户信息
        EntityWrapper userparam=new EntityWrapper();
        if (StringUtils.isNotBlank(cardtypesssid)){
            userparam.eq("ut.cardtypessid",cardtypesssid);
        }else {
            //默认使用身份证
            cardtypesssid=PropertiesListenerConfig.getProperty("cardtype_default");
            userparam.eq("ut.cardtypessid",cardtypesssid);
        }

        userparam.eq("ut.cardnum",cardnum);
        List<UserInfo> userinfos=police_userinfoMapper.getUserByCard(userparam);
        if (null==userinfos||userinfos.size()<1){
            result.setMessage("该人员信息未有过记录");
            return;
        }

        if (null!=userinfos&&userinfos.size()>0){
            if (userinfos.size()==1){
                UserInfo police_userinfo=userinfos.get(0);
                getUserByCardVO.setUserinfo(police_userinfo);


                //根据用户userssid查询案件列表
                EntityWrapper caseparam=new EntityWrapper();
                caseparam.eq("u.ssid",police_userinfo.getSsid());
                caseparam.orderBy("c.occurrencetime",false);
                List<Case> cases=police_caseMapper.getCase(caseparam);//加入询问次数
                if (null!=cases&&cases.size()>0){
                    for (Case c: cases) {
                        //提讯数据
                        EntityWrapper ewarraignment=new EntityWrapper();
                        ewarraignment.eq("cr.casessid",c.getSsid());
                        ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                        ewarraignment.orderBy("a.createtime",false);
                        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                        if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                            c.setArraignments(arraignmentAndRecords);
                        }
                       /* c.setAsknum(arraignmentAndRecords.size());*/
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
        caseparam.eq("u.ssid",userssid);
        caseparam.eq("c.creator",user.getSsid());
        caseparam.ne("c.casebool",-1);//案件不为删除的
        caseparam.orderBy("c.occurrencetime",false);
        List<Case> cases=police_caseMapper.getCase(caseparam);//加入询问次数
        List<String> casessids=new ArrayList<>();
        if (null!=cases&&cases.size()>0){
            for (Case c: cases) {
                //提讯数据
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",c.getSsid());
                ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    c.setArraignments(arraignmentAndRecords);
                }
                casessids.add(c.getSsid());

            }
            getCaseByIdVO.setCases(cases);
        }




        //出来关联的其他案件
        EntityWrapper otherCasesparam=new EntityWrapper();
        if (null!=casessids&&casessids.size()>0){
            otherCasesparam.notIn("c.ssid",casessids);
        }
        otherCasesparam.ne("u.ssid",userssid);
        otherCasesparam.eq("c.creator",user.getSsid());
        otherCasesparam.orderBy("c.occurrencetime",false);
        List<Case> otherCases=police_caseMapper.getCase(otherCasesparam);//加入询问次数
        if (null!=otherCases&&otherCases.size()>0){
            for (Case c: otherCases) {
                //提讯数据
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",c.getSsid());
                ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    c.setArraignments(arraignmentAndRecords);
                }
            }
            getCaseByIdVO.setOthercases(otherCases);
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
        if (StringUtils.isNotBlank(getCasesParam.getUsername())){
            ew.like(true,"u.username",getCasesParam.getUsername().trim());
        }

        if(StringUtils.isNotEmpty(getCasesParam.getOccurrencetime_start()) && StringUtils.isNotEmpty(getCasesParam.getOccurrencetime_end())){
            ew.between("c.occurrencetime", getCasesParam.getOccurrencetime_start(), getCasesParam.getOccurrencetime_end());
        }
        if(StringUtils.isNotEmpty(getCasesParam.getStarttime_start()) && StringUtils.isNotEmpty(getCasesParam.getStarttime_end())){
            ew.between("c.starttime", getCasesParam.getStarttime_start(), getCasesParam.getStarttime_end());
        }

        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
        ew.eq("c.creator",user.getSsid());

        ew.ne("c.casebool",-1);//案件状态不能为删除状态

        int count = police_caseMapper.countgetCaseList(ew);
        getCasesParam.setRecordCount(count);

        ew.orderBy("c.createtime",false);
        Page<Case> page=new Page<Case>(getCasesParam.getCurrPage(),getCasesParam.getPageSize());
        List<Case> list=police_caseMapper.getCaseList(page,ew);
        getCasesVO.setPageparam(getCasesParam);

        if (null!=list&&list.size()>0){
            for (Case case_: list) {
                 Integer finish_file=0;//已完成并且有iid的笔录数
                 Integer total_file=0;//已完成总共笔录数

                //1、绑定多次提讯数据
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",case_.getSsid());
                ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {
                        if (null!=arraignmentAndRecord.getRecordbool()&&(arraignmentAndRecord.getRecordbool()==2||arraignmentAndRecord.getRecordbool()==3)){
                            total_file++;
                            if (StringUtils.isNotBlank(arraignmentAndRecord.getIid())){
                                finish_file++;
                            }
                        }
                    }
                    case_.setArraignments(arraignmentAndRecords);
                }
                case_.setFinish_filenum(finish_file);
                case_.setTotal_filenum(total_file);


                //2、多个案件人
                EntityWrapper ewuserinfo=new EntityWrapper<>();
                ewuserinfo.eq("ctu.casessid",case_.getSsid());
                List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                if (null!=userInfos&&userInfos.size()>0){
                    for (UserInfo userInfo : userInfos) {
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

                        ////获取该用户全部的证件
                        EntityWrapper ewcard=new EntityWrapper<>();
                        ewcard.eq("u.ssid",userInfo.getSsid());
                        List<UserInfoAndCard> cards=police_userinfoMapper.getCardByUser(ewcard);
                        userInfo.setCards(cards);
                    }
                    case_.setUserInfos(userInfos);
                }

                if(StringUtils.isNotEmpty(case_.getCreator())){
                    //查出创建人的名称ew
                    Base_admininfo base_admininfo = new Base_admininfo();
                    base_admininfo.setSsid(case_.getCreator());
                    Base_admininfo admininfo = base_admininfoMapper.selectOne(base_admininfo);
                    case_.setCreatorname(admininfo.getUsername());
                }
                case_.setRecord_pausebool(PropertiesListenerConfig.getProperty("record.pausebool"));
            }
            getCasesVO.setPagelist(list);
        }
        getCasesVO.setPagelist(list);

        result.setData(getCasesVO);
        changeResultToSuccess(result);
    }

    public void addCase(RResult result,ReqParam<AddCaseParam> param,HttpSession session){
        AddCaseParam addCaseParam=param.getParam();
        if (null==addCaseParam){
            result.setMessage("参数为空");
            return;
        }

        String casename=addCaseParam.getCasename();//案件号码
        if (StringUtils.isNotBlank(casename)){
            //判断案件是否重复
            EntityWrapper police_cases_param=new EntityWrapper();
            police_cases_param.eq("casename",casename.trim());
            police_cases_param.ne("casebool",-1);
            List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
            if (null!=police_cases_&&police_cases_.size()>0){
                result.setMessage("案件名称不能重复");
                return;
            }
        }

        String casenum=addCaseParam.getCasenum();//案件号码
        if (StringUtils.isNotBlank(casenum)){
            //判断案件是否重复
            EntityWrapper police_cases_param=new EntityWrapper();
            police_cases_param.eq("casenum",casenum.trim());
            police_cases_param.ne("casebool",-1);
            List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
            if (null!=police_cases_&&police_cases_.size()>0){
                result.setMessage("案件编号不能重复");
                return;
            }
        }


        AdminAndWorkunit user= (AdminAndWorkunit) session.getAttribute(Constant.MANAGE_CLIENT);
        addCaseParam.setSsid(OpenUtil.getUUID_32());
        addCaseParam.setCreatetime(new Date());
        addCaseParam.setCreator(user.getSsid());
        addCaseParam.setCasebool(0);//初始化0
        if (addCaseParam.getStarttime()==null){
            addCaseParam.setStarttime(new Date());//默认现在时间
        }


       int caseinsert_bool = police_caseMapper.insert(addCaseParam);
       LogUtil.intoLog(this.getClass(),"caseinsert_bool__"+caseinsert_bool);
        if (caseinsert_bool>0){
            result.setData(addCaseParam.getSsid());


            //自动生成编号回填
//            if (StringUtils.isBlank(casenum)){
//                //截取类型的前一个字母
//                String type=CommonCache.getCurrentServerType();
//                int index=type.indexOf("_");
//                String q="";
//                if (index>-1&&index<type.length()-1){
//                    String test3before=type.substring(0,index);
//                    String test3after=type.substring(index+1);
//                    q=test3before.substring(0,1)+test3after.substring(0,1);
//                }
//
//                //拼接案件编号
//                String numbertType =q+ new SimpleDateFormat("yyMMdd").format(new Date());
//                String numberNo = getNumberNo(numbertType, String.valueOf(addCaseParam.getId()-1<1?0:addCaseParam.getId()-1));
//                addCaseParam.setCasenum(numberNo);
//
//                //修改编号
//                EntityWrapper updateparam=new EntityWrapper();
//                updateparam.eq("ssid",addCaseParam.getSsid());
//                int caseupdate_bool = police_caseMapper.update(addCaseParam,updateparam);
//                if (caseupdate_bool>0){
//                        LogUtil.intoLog(this.getClass(),"案件编号修改成功__"+numberNo);
//                }
//            }


            //案件多用户
            List<UserInfo> userInfos=addCaseParam.getUserInfos();
            if (null!=userInfos&&userInfos.size()>0){
                for (UserInfo userInfo : userInfos) {
                    String cardtypessid=userInfo.getCardtypessid();
                    String cardnum=userInfo.getCardnum();
                    String userssid=null;
                    String usertotypessid=null;

                    //根据证件类型和证件号码，检测证件是否存在存在修改；不存在新增
                    EntityWrapper userparam=new EntityWrapper();
                    userparam.eq("ut.cardtypessid",cardtypessid);
                    userparam.like("ut.cardnum",cardnum);
                    List<UserInfo> userInfos_=police_userinfoMapper.getUserByCard(userparam);
                    if (null!=userInfos_&&userInfos_.size()==1){
                        UserInfo userInfo_=userInfos_.get(0);
                        //修改
                        userssid=userInfo_.getSsid();
                        usertotypessid=userInfo_.getUsertotypessid();




                        EntityWrapper updateuserinfoParam=new EntityWrapper();
                        updateuserinfoParam.eq("ssid",userssid);
                        Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                        int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
                        LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                    }else  if(null==userInfos_||userInfos_.size()<1){
                        //新增用户
                        userInfo.setSsid(OpenUtil.getUUID_32());
                        userInfo.setCreatetime(new Date());
                        int insertuserinfo_bool = police_userinfoMapper.insert(userInfo);
                        LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
                        if (insertuserinfo_bool>0){
                            userssid=userInfo.getSsid();

                            Police_userinfototype police_userinfototype=new Police_userinfototype();
                            police_userinfototype.setCardnum(userInfo.getCardnum());
                            police_userinfototype.setSsid(OpenUtil.getUUID_32());
                            police_userinfototype.setCreatetime(new Date());
                            police_userinfototype.setCardtypessid(userInfo.getCardtypessid());
                            police_userinfototype.setUserssid(userInfo.getSsid());
                            int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
                            LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
                            usertotypessid=police_userinfototype.getSsid();
                        }
                    }else {
                        LogUtil.intoLog(this.getClass(),"案件传过来的用户信息异常__cardtypessid_"+cardtypessid+"__cardnum_"+cardnum);
                        result.setMessage("系统异常");
                        return;
                    }


                    //添加用户与案件的关联
                    Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                    police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                    police_casetouserinfo_.setCreatetime(new Date());
                    police_casetouserinfo_.setCasessid(addCaseParam.getSsid());
                    police_casetouserinfo_.setUserssid(userssid);
                    police_casetouserinfo_.setUsertotypessid(usertotypessid);
                    int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                    LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
                }
            }


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

        String casename=updateCaseParam.getCasename();//案件号码
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


        String casenum=updateCaseParam.getCasenum();//案件号码
        if (StringUtils.isNotBlank(casenum)){
            //判断案件是否重复
            EntityWrapper police_cases_param=new EntityWrapper();
            police_cases_param.eq("casenum",casenum.trim());
            police_cases_param.ne("ssid",casessid);
            police_cases_param.ne("casebool",-1);
            List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
            if (null!=police_cases_&&police_cases_.size()>0){
                result.setMessage("案件编号不能重复");
                return;
            }
        }
//        else {
//            Police_case police_case_=new Police_case();
//            police_case_.setSsid(casessid);
//            police_case_=police_caseMapper.selectOne(police_case_);
//            //截取类型的前一个字母
//            String type=CommonCache.getCurrentServerType();
//            int index=type.indexOf("_");
//            String q="";
//            if (index>-1&&index<type.length()-1){
//                String test3before=type.substring(0,index);
//                String test3after=type.substring(index+1);
//                q=test3before.substring(0,1)+test3after.substring(0,1);
//            }
//
//            //拼接案件编号
//            String numbertType =q+ new SimpleDateFormat("yyMMdd").format(new Date());
//            String numberNo = getNumberNo(numbertType, String.valueOf(police_case_.getId()-1<1?0:police_case_.getId()-1));
//            updateCaseParam.setCasenum(numberNo);
//        }




        EntityWrapper updateParam=new EntityWrapper();
        updateParam.eq("ssid",casessid);
        int caseupdate_bool = police_caseMapper.update(updateCaseParam,updateParam);
        LogUtil.intoLog(this.getClass(),"caseupdate_bool__"+caseupdate_bool);
        if (caseupdate_bool>0){
            result.setData(caseupdate_bool);

            //删除案件的全部的关联
            EntityWrapper delew=new EntityWrapper();
            delew.eq("casessid",casessid);
            int police_casetouserinfoMapper_delete_bool=police_casetouserinfoMapper.delete(delew);
            LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_delete_bool__"+police_casetouserinfoMapper_delete_bool);

            //案件多用户
            List<UserInfo> userInfos=updateCaseParam.getUserInfos();


            if (null!=userInfos&&userInfos.size()>0){
                for (UserInfo userInfo : userInfos) {
                    String cardtypessid=userInfo.getCardtypessid();
                    String cardnum=userInfo.getCardnum();
                    String userssid=null;
                    String usertotypessid=null;

                    //根据证件类型和证件号码，检测证件是否存在存在修改；不存在新增
                    EntityWrapper userparam=new EntityWrapper();
                    userparam.eq("ut.cardtypessid",cardtypessid);
                    userparam.like("ut.cardnum",cardnum);
                    List<UserInfo> userInfos_=police_userinfoMapper.getUserByCard(userparam);
                    if (null!=userInfos_&&userInfos_.size()==1){
                        UserInfo userInfo_=userInfos_.get(0);
                        //修改
                        userssid=userInfo_.getSsid();
                        usertotypessid=userInfo_.getUsertotypessid();




                        EntityWrapper updateuserinfoParam=new EntityWrapper();
                        updateuserinfoParam.eq("ssid",userssid);
                        Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                        int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
                        LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                    }else  if(null==userInfos_||userInfos_.size()<1){
                        //新增用户
                        userInfo.setSsid(OpenUtil.getUUID_32());
                        userInfo.setCreatetime(new Date());
                        int insertuserinfo_bool = police_userinfoMapper.insert(userInfo);
                        LogUtil.intoLog(this.getClass(),"insertuserinfo_bool__"+insertuserinfo_bool);
                        if (insertuserinfo_bool>0){
                            userssid=userInfo.getSsid();

                            Police_userinfototype police_userinfototype=new Police_userinfototype();
                            police_userinfototype.setCardnum(userInfo.getCardnum());
                            police_userinfototype.setSsid(OpenUtil.getUUID_32());
                            police_userinfototype.setCreatetime(new Date());
                            police_userinfototype.setCardtypessid(userInfo.getCardtypessid());
                            police_userinfototype.setUserssid(userInfo.getSsid());
                            int insertuserinfototype_bool = police_userinfototypeMapper.insert(police_userinfototype);
                            LogUtil.intoLog(this.getClass(),"insertuserinfototype_bool__"+insertuserinfototype_bool);
                            usertotypessid=police_userinfototype.getSsid();
                        }
                    }else {
                        LogUtil.intoLog(this.getClass(),"案件传过来的用户信息异常__cardtypessid_"+cardtypessid+"__cardnum_"+cardnum);
                        result.setMessage("系统异常");
                        return;
                    }



                    //添加用户与案件的关联
                    Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                    police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                    police_casetouserinfo_.setCreatetime(new Date());
                    police_casetouserinfo_.setCasessid(casessid);
                    police_casetouserinfo_.setUserssid(userssid);
                    police_casetouserinfo_.setUsertotypessid(usertotypessid);
                    int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                    LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
                }
            }
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
        List<Case> cases=police_caseMapper.getCase(caseParam);
        if (null!=cases&&cases.size()==1){
                Case case_=cases.get(0);
                EntityWrapper ewuserinfo=new EntityWrapper<>();
                ewuserinfo.eq("ctu.casessid",case_.getSsid());
                List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                if (null!=userInfos&&userInfos.size()>0){
                    for (UserInfo userInfo : userInfos) {
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

                        ////获取该用户全部的证件
                        EntityWrapper ewcard=new EntityWrapper<>();
                        ewcard.eq("u.ssid",userInfo.getSsid());
                        List<UserInfoAndCard> cards=police_userinfoMapper.getCardByUser(ewcard);
                        userInfo.setCards(cards);
                    }
                    case_.setUserInfos(userInfos);
                }
            getCaseBySsidVO.setCase_(case_);
            result.setData(getCaseBySsidVO);
            changeResultToSuccess(result);
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
        }else {
            //默认使用身份证
            cardtypesssid=PropertiesListenerConfig.getProperty("cardtype_default");
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
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
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

    public void changeboolCase(RResult result,ReqParam<ChangeboolCaseParam> param,HttpSession session){
        ChangeboolCaseVO vo=new ChangeboolCaseVO();

        ChangeboolCaseParam changeboolCaseParam=param.getParam();
        if (null==changeboolCaseParam){
            result.setMessage("参数为空");
            return;
        }

        String ssid=changeboolCaseParam.getSsid();
        Integer bool=changeboolCaseParam.getBool();
        Police_case police_case=new Police_case();
        if (bool==2||bool==-1){
            //案件归档{
            //归档，判断下面是否有进行中的笔录
            EntityWrapper ewarraignment=new EntityWrapper();
            ewarraignment.eq("cr.casessid",ssid);
            ewarraignment.ne("r.recordbool",-1);//笔录不为删除状态
            List<String> recordbools=new ArrayList<>();
            recordbools.add("1");
            ewarraignment.in("r.recordbool",recordbools);
            List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
            if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                result.setMessage("请先结束进行中的笔录");
                return;
            }
            police_case.setEndtime(new Date());//归档修改结束时间

            //案件删除
            if (bool==-1){
                //将该案件下的笔录全部改为删除状态
                EntityWrapper ewarraignment_=new EntityWrapper();
                ewarraignment_.eq("cr.casessid",ssid);
                ewarraignment_.ne("r.recordbool",-1);//笔录不为删除状态
                List<ArraignmentAndRecord> arraignmentAndRecords_ = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment_);
                if (null!=arraignmentAndRecords_&&arraignmentAndRecords_.size()>0) {
                    for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords_) {
                        Police_record record = new Police_record();
                        record.setSsid(arraignmentAndRecord.getRecordssid());
                        record = police_recordMapper.selectOne(record);
                        if (null != record) {
                            //案件下笔录全改为删除
                            EntityWrapper entityWrapper = new EntityWrapper();
                            entityWrapper.eq("ssid", arraignmentAndRecord.getRecordssid());
                            record.setSsid(arraignmentAndRecord.getRecordssid());
                            record.setRecordbool(-1);
                            int police_recordMapper_updatebool = police_recordMapper.update(record, entityWrapper);
                            LogUtil.intoLog(this.getClass(), "police_recordMapper_updatebool__" + police_recordMapper_updatebool);
                        }
                    }
                }
            }

        }

        if (bool==3){
            //该案件正在休庭，需要改为进行中
            bool=1;//改为进行中


            //查找该案件下所有提讯的笔录，找到暂停中的笔录并且生成一份新的笔录，状态为未开始;旧的改为已完成
            EntityWrapper ewarraignment=new EntityWrapper();
            ewarraignment.eq("cr.casessid",ssid);
            ewarraignment.eq("r.recordbool",3);
            ewarraignment.orderBy("a.createtime",false);
            List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);//不出意外一般只存有一条数据
            if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                //修改笔录状态为已完成
                for (ArraignmentAndRecord arraignmentAndRecord : arraignmentAndRecords) {

                    //开始获取该信息进行添加新的案件提讯笔录

                    RResult addCaseToArraignment_rr=new RResult();
                    ReqParam<AddCaseToArraignmentParam> addCaseToArraignment_param=new ReqParam<>();
                    AddCaseToArraignmentParam addCaseToArraignmentParam=new AddCaseToArraignmentParam();


                    //回填数据-------------------------------------------------------------------------start
                    String userssid=arraignmentAndRecord.getUserssid();
                    if (StringUtils.isNotBlank(userssid)&&StringUtils.isNotBlank(ssid)){
                        addCaseToArraignmentParam.setUserssid(userssid);
                        addCaseToArraignmentParam.setCasessid(ssid);

                        //获取案件和人员信息
                        Police_userinfo police_userinfo=new Police_userinfo();
                        police_userinfo.setSsid(arraignmentAndRecord.getUserssid());
                        police_userinfo=police_userinfoMapper.selectOne(police_userinfo);
                        UserInfo userinfo_=gson.fromJson(gson.toJson(police_userinfo),UserInfo.class);

                        police_case.setSsid(ssid);
                        police_case=police_caseMapper.selectOne(police_case);

                        if (null!=userinfo_&&null!=police_case){
                            int asknum=arraignmentAndRecord.getAsknum()==null?0:arraignmentAndRecord.getAsknum();//询问
                            addCaseToArraignmentParam.setAdminssid(arraignmentAndRecord.getAdminssid());
                            addCaseToArraignmentParam.setOtheradminssid(arraignmentAndRecord.getOtheradminssid());
                            addCaseToArraignmentParam.setRecordadminssid(arraignmentAndRecord.getRecordadminssid());
                            addCaseToArraignmentParam.setRecordtypessid(arraignmentAndRecord.getRecordtypessid());
                            addCaseToArraignmentParam.setRecordplace(arraignmentAndRecord.getRecordplace());
                            addCaseToArraignmentParam.setMultifunctionbool(arraignmentAndRecord.getMultifunctionbool());
                            addCaseToArraignmentParam.setAskobj(arraignmentAndRecord.getAskobj());
                            addCaseToArraignmentParam.setAsknum(asknum);
                            addCaseToArraignmentParam.setMtmodelssid(arraignmentAndRecord.getMtmodelssid());
                            addCaseToArraignmentParam.setSkipCheckbool(1);//默认跳过检测
                            addCaseToArraignmentParam.setSkipCheckCasebool(1);//默认跳过检测
                            addCaseToArraignment_param.setParam(addCaseToArraignmentParam);

                            String mtmodelssidname="";
                            if (StringUtils.isNotEmpty(arraignmentAndRecord.getMtmodelssid())){
                                List<Avstmt_modelAll> modelAlls=new ArrayList<>();
                                GetMc_modelParam_out getMc_modelParam_out=new GetMc_modelParam_out();
                                getMc_modelParam_out.setMcType(MCType.AVST);
                                getMc_modelParam_out.setModelssid(arraignmentAndRecord.getMtmodelssid());
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
                            String recordname=userinfo_.getUsername()+"《"+police_case.getCasename().trim()+"》"+mtmodelssidname+"_"+arraignmentAndRecord.getRecordtypename()+"_第"+(Integer.valueOf(asknum)+1)+"次";
                            recordname=recordname==null?"":recordname.replace(" ", "").replace("\"", "");//笔录名称
                            if (null!=arraignmentAndRecord.getMultifunctionbool()&&arraignmentAndRecord.getMultifunctionbool()==1){
                                recordname=arraignmentAndRecord.getRecordname()+"_第"+(Integer.valueOf(asknum)+1)+"次";
                            }
                            addCaseToArraignmentParam.setRecordname(recordname);

                            addCaseToArraignmentParam.setAddUserInfo(userinfo_);
                            addCaseToArraignmentParam.setAddPolice_case(police_case);
                            //回填数据-------------------------------------------------------------------------end
                            addCaseToArraignment(addCaseToArraignment_rr,addCaseToArraignment_param,session);
                            if (null!=addCaseToArraignment_rr&&addCaseToArraignment_rr.getActioncode().equals(Code.SUCCESS.toString())){
                                if (null!=addCaseToArraignment_rr.getData()){
                                    //获取到返回的笔录ssid
                                    vo.setAddcasetoarraignmentvo_data(JSON.toJSONString(addCaseToArraignment_rr.getData()));
                                }
                            }
                        }else {
                            LogUtil.intoLog(this.getClass(),"案件暂停继续回填案件人员信息为空__userinfo__"+userinfo_+"__police_case__"+police_case);
                        }

                    }else {
                        LogUtil.intoLog(this.getClass(),"案件暂停继续回填案件人员信息参数错误__casessid_"+ssid+"__userssid__"+userssid);
                    }

                    Police_record record=new Police_record();
                    String recordssid=arraignmentAndRecord.getRecordssid();
                    if (StringUtils.isNotBlank(recordssid)){
                        record.setSsid(recordssid);
                        record.setRecordbool(3);
                        record=police_recordMapper.selectOne(record);
                        if (null!=record){
                            EntityWrapper entityWrapper=new EntityWrapper();
                            entityWrapper.eq("ssid",recordssid);
                            record.setSsid(recordssid);
                            record.setRecordbool(2);
                            int  police_recordMapper_updatebool=police_recordMapper.update(record,entityWrapper);
                            if (police_recordMapper_updatebool>0){
                                LogUtil.intoLog(this.getClass(),"changeboolCase__police_recordMapper.update__暂停笔录状态改为已完成__police_recordMapper_updatebool__"+police_recordMapper_updatebool);
                            }
                        }else {
                            LogUtil.intoLog(this.getClass(),"changeboolCase__police_recordMapper.selectOne__未找到笔录信息__recordssid__"+recordssid);
                        }
                    }
                }
            }
        }

        police_case.setCasebool(bool);
        EntityWrapper ew=new EntityWrapper();
        ew.eq("ssid",ssid);
       int police_caseMapper_updatebool = police_caseMapper.update(police_case,ew);
       if (police_caseMapper_updatebool>0){
           result.setData(vo);
           changeResultToSuccess(result);
       }
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

    public boolean checkRecordForUser(RResult result, CheckStartRecordParam param, String session_adminssid,Integer[] recordbools ){
        CheckStartRecordVO vo=new CheckStartRecordVO();

        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(this.getClass(),"checkRecordForUser__参数为空");
            return false;
        }

        //当前
        String mtmodelssid=param.getMtmodel_ssid();//会议模板
        List<String> admininfos=param.getAdmininfos_ssid();//询问人集合
        String userinfo=param.getUserinfo_ssid();//被询问人



        //已参与
        List<String> userssidList=new ArrayList<>();//全部被询问人
        List<String> adminssidList=new ArrayList<>();//全部询问人
        List<String> mtmodelssidList=new ArrayList<>();//全部会议模板


        //开始收集所有正在参与制作笔录的人员（询问人和被询问人，记录人员暂不计算）
        EntityWrapper recordparam=new EntityWrapper();
        if (null!=recordbools&&recordbools.length==1){
            recordparam.eq("recordbool",recordbools[0]);
         }else if(null!=recordbools&&recordbools.length==2){
        recordparam.eq("recordbool",recordbools[0]).or().eq("recordbool",recordbools[1]);
         }

        List<Police_record> list=police_recordMapper.selectList(recordparam);
        if (null!=list&&list.size()>0){
            for (Police_record police_record : list) {
                String recordssid=police_record.getSsid();
                //收集被询问人
                try {
                    EntityWrapper caseParam=new EntityWrapper();
                    caseParam.eq("r.ssid",recordssid);

                    Case case_ = police_caseMapper.getCaseByRecordSsid(caseParam);
                    if (null!=case_){
                        EntityWrapper ewuserinfo=new EntityWrapper<>();
                        ewuserinfo.eq("ctu.casessid",case_.getSsid());
                        List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                        if (null!=userInfos&&userInfos.size()>0){
                            for (UserInfo userInfo : userInfos) {
                                userssidList.add(userInfo.getSsid());
                            }
                            case_.setUserInfos(userInfos);
                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }

                //收集询问人
                try {
                    Police_arraignment police_arraignment=new Police_arraignment();
                    police_arraignment.setRecordssid(recordssid);
                    police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                    if (null!=police_arraignment){
                        adminssidList.add(police_arraignment.getAdminssid());
                        adminssidList.add(police_arraignment.getOtheradminssid());
                        mtmodelssidList.add(police_arraignment.getMtmodelssid());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String gnlist=getSQEntity.getGnlist();
            //服务端版本可存在多个进行中
            if (gnlist.indexOf(SQVersion.S_E)!= -1){
                //法院的只需要检测模板
                //开始检测会议模板是否已经被使用
                if (null!=mtmodelssid){
                    if (null!=mtmodelssidList&&mtmodelssidList.size()>0){
                        LogUtil.intoLog(this.getClass(),"mtmodelssidList__"+mtmodelssidList.toString());
                        for (String s : mtmodelssidList) {
                            if(s.equals(mtmodelssid)){
                                vo.setMtmodel_ssid(mtmodelssid);
                                vo.setMsg("*所选模板已被占用");
                                result.setData(vo);
                                LogUtil.intoLog(this.getClass(),"该模板已在笔录中__"+mtmodelssid);
                                return false;
                            }
                        }
                    }
                }
            }else {
                //开始检测被询问人是否已经被使用
                if (null!=userinfo){
                    if (null!=userssidList&&userssidList.size()>0){
                        LogUtil.intoLog(this.getClass(),"userssidList__"+userssidList.toString());
                        for (String s : userssidList) {
                            if(s.equals(userinfo)){
                                //获取该人员姓名
                                Police_userinfo police_userinfo=new Police_userinfo();
                                police_userinfo.setSsid(userinfo);
                                police_userinfo=police_userinfoMapper.selectOne(police_userinfo);
                                vo.setUserinfo_ssid(userinfo);
                                String username=police_userinfo.getUsername()==null?"":police_userinfo.getUsername();
                                vo.setMsg("*"+username+"正在被讯问中");
                                result.setData(vo);
                                LogUtil.intoLog(this.getClass(),"该被询问人已在笔录中__"+userinfo);
                                return false;
                            }
                        }
                    }
                }

                //开始检测询问人是否已经被使用
                if (null!=admininfos&&admininfos.size()>0){
                    if (null!=adminssidList&&adminssidList.size()>0){
                        LogUtil.intoLog(this.getClass(),"adminssidList__"+adminssidList.toString());
                        List<String> admininfos_ssid=new ArrayList<>();
                        for (String s : adminssidList) {
                            for (String admininfo : admininfos) {
                                if(null!=admininfo&&s!=null&&s.equals(admininfo)){
                                    if (session_adminssid.equals(admininfo)){
                                        vo.setMsg("*您有一份笔录/审讯正在制作中");
                                    }else {
                                        Base_admininfo base_admininfo=new Base_admininfo();
                                        base_admininfo.setSsid(admininfo);
                                        base_admininfo = base_admininfoMapper.selectOne(base_admininfo);
                                        String username=base_admininfo.getUsername()==null?"":base_admininfo.getUsername();
                                        vo.setMsg("*"+username+"已被选用");
                                    }
                                    admininfos_ssid.add(admininfo);
                                    vo.setAdmininfos_ssid(admininfos_ssid);
                                    result.setData(vo);
                                    LogUtil.intoLog(this.getClass(),"该询问人已在笔录中__"+admininfo);
                                    return false;
                                }
                            }
                        }
                    }
                }


                //开始检测会议模板是否已经被使用
                if (null!=mtmodelssid){
                    if (null!=mtmodelssidList&&mtmodelssidList.size()>0){
                        LogUtil.intoLog(this.getClass(),"mtmodelssidList__"+mtmodelssidList.toString());
                        for (String s : mtmodelssidList) {
                            if(s.equals(mtmodelssid)){
                                vo.setMtmodel_ssid(mtmodelssid);
                                vo.setMsg("*所选模板已被占用");
                                result.setData(vo);
                                LogUtil.intoLog(this.getClass(),"该模板已在笔录中__"+mtmodelssid);
                                return false;
                            }
                        }
                    }
                }
            }
        }else {
            return true;
        }
        return true;
    }

    /**
     * 主要用于案件编号
     * @param numberType 前缀
     * @param numberNo 从哪个开始
     * @return
     */
    public static String getNumberNo(String numberType, String numberNo){
        String newNumberNo = "00001";//默认5位
        if(numberNo != null && !numberNo.isEmpty()){
            int newEquipment = Integer.parseInt(numberNo) + 1;
            newNumberNo = String.format(numberType + "%05d", newEquipment);
        }
        return newNumberNo;
    }

    public void updateCaseToUser(RResult result,ReqParam<UpdateCaseToUserParam> paramReqParam){
        UpdateCaseToUserVO vo=new UpdateCaseToUserVO();

        UpdateCaseToUserParam param=paramReqParam.getParam();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

         UserInfo userInfo=param.getUserInfo();
         Police_case case_=param.getCase_();
         Police_arraignment arraignment=param.getArraignment();
         String recordssid=param.getRecordssid();
         String recordname=param.getRecordname();

        if (StringUtils.isBlank(recordssid)){
            result.setMessage("系统异常");
            LogUtil.intoLog(3,this.getClass(),"updateCaseToUser_recordssid is null");
            return;
        }


         if (StringUtils.isNotBlank(recordname)){
             EntityWrapper recordname_ew=new EntityWrapper();
             recordname_ew.eq("recordname",recordname);
             recordname_ew.ne("ssid",recordssid);
             recordname_ew.ne("recordbool",-1);
             List<Police_record> police_records=police_recordMapper.selectList(recordname_ew);
             if (null!=police_records&&police_records.size()>0){
                 result.setMessage("笔录名称不能重复");
                 LogUtil.intoLog(3,this.getClass(),"updateCaseToUser_笔录名称不能重复");
                 return;
             }
         }

         Police_record police_record=new Police_record();
         police_record.setRecordname(recordname);
         EntityWrapper record_ew=new EntityWrapper();
         record_ew.eq("ssid",recordssid);
         int police_recordMapper_update_bool=police_recordMapper.update(police_record,record_ew);
         LogUtil.intoLog(1,this.getClass(),"police_recordMapper_update_bool___"+police_recordMapper_update_bool);




         //根据笔录ssid获取提讯信息
        Police_arraignment police_arraignment=new Police_arraignment();
        police_arraignment.setRecordssid(recordssid);
        police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
        if (null!=police_arraignment){
            String arraignmentssid=police_arraignment.getSsid();//提讯ssid
            String userssid=police_arraignment.getUserssid();//被询问人ssid

            if (StringUtils.isBlank(arraignmentssid)||StringUtils.isBlank(userssid)){
                result.setMessage("系统异常");
                LogUtil.intoLog(3,this.getClass(),"updateCaseToUser_arraignmentssid or userssid is null__userssid:"+userssid+"__arraignmentssid:"+arraignmentssid);
                return;
            }


            //修改提讯信息
            EntityWrapper arraignmentupdate_ew=new EntityWrapper();
            arraignmentupdate_ew.eq("ssid",arraignmentssid);
            police_arraignment.setAskobj(arraignment.getAskobj());
            police_arraignment.setAsknum(arraignment.getAsknum());
            police_arraignment.setRecordplace(arraignment.getRecordplace());
            police_arraignment.setRecordplace(arraignment.getRecordplace());
            police_arraignment.setOtheradminssid(arraignment.getOtheradminssid());
            police_arraignment.setRecordadminssid(arraignment.getRecordadminssid());
            int police_arraignmentMapper_update_bool=police_arraignmentMapper.update(police_arraignment,arraignmentupdate_ew);
            LogUtil.intoLog(1,this.getClass(),"police_arraignmentMapper_update_bool___"+police_arraignmentMapper_update_bool);


            //修改被询问人信息
            Police_userinfo police_userinfo=new Police_userinfo();
            police_userinfo.setSsid(userssid);
            police_userinfo=police_userinfoMapper.selectOne(police_userinfo);
            if (null!=police_userinfo){
                EntityWrapper userinfoupdate_ew=new EntityWrapper();
                userinfoupdate_ew.eq("ssid",userssid);
                police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                int police_userinfoMapper_update_bool=police_userinfoMapper.update(police_userinfo,userinfoupdate_ew);
                LogUtil.intoLog(1,this.getClass(),"police_userinfoMapper_update_bool___"+police_userinfoMapper_update_bool);
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


        //生成初始化word头文件
        RResult exportwordhead_rr=new RResult();
        ExportWordParam exportwordheadParam=new ExportWordParam();
        exportwordheadParam.setRecordssid(recordssid);
        exportwordheadParam.setWordheadbool(true);
        ReqParam reqParam=new ReqParam();
        reqParam.setParam(exportwordheadParam);
        recordService2.exportWord(exportwordhead_rr, reqParam);
        if (null != exportwordhead_rr && exportwordhead_rr.getActioncode().equals(Code.SUCCESS.toString())) {
            LogUtil.intoLog(this.getClass(),"recordService.exportWord笔录结束时exportWord__成功__保存问答");
        }else{
            LogUtil.intoLog(this.getClass(),"recordService.exportWord笔录结束时exportWord__出错__"+exportwordhead_rr.getMessage());
        }

       result.setData(1);
        changeResultToSuccess(result);
        return;
    }

}
