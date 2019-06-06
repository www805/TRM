package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.MCType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMCAsrTxtBackParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetMCStateParam_out;
import com.avst.trm.v1.feignclient.mc.req.OverMCParam_out;
import com.avst.trm.v1.feignclient.mc.req.StartMCParam_out;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${file.basepath}")
    private String filePath;

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
        getRecordsVO.setPagelist(records);
        result.setData(getRecordsVO);
        changeResultToSuccess(result);
        return;
    }

    public void addRecord(RResult result, ReqParam<AddRecordParam> param){

        if (addRecordbool){
            System.out.println("---addRecordbool---Start---");
            result.setMessage("保存中,请稍等...");
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
        List<RecordToProblem> recordToProblems1=addRecordParam.getRecordToProblems();//笔录携带的题目答案集合

        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        //修改笔录状态
        Integer recordbool=addRecordParam.getRecordbool();
        System.out.println("recordbool__"+recordbool);
        EntityWrapper updaterecordParam=new EntityWrapper();
        updaterecordParam.eq("ssid",recordssid);
        Police_record record=new Police_record();
        record.setSsid(recordssid);
        record.setRecordbool(recordbool);
        int updaterecord_bool=police_recordMapper.update(record,updaterecordParam);
        System.out.println("updaterecord_bool__"+updaterecord_bool);

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
                System.out.println("answerdelete_bool__"+answerdelete_bool);
            }
             int recordtoproblemdelete_bool=police_recordtoproblemMapper.delete(recordToProblemsParam);
             System.out.println("recordtoproblemdelete_bool__"+recordtoproblemdelete_bool);
        }else{
            System.out.println("该笔录没有任何题目答案__1");
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
                System.out.println("recordtoprobleminsert_bool__"+recordtoprobleminsert_bool);
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
                            System.out.println("answerinsert_bool__"+answerinsert_bool);
                        }
                    }
                }
            }
        }else{
            System.out.println("该笔录没有任何题目答案__2");
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
                try {
                    Police_arraignment police_arraignment=new Police_arraignment();
                    police_arraignment.setRecordssid(recordssid);
                    police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                    Integer mtstate=null;
                    if (null!=police_arraignment){
                        if (StringUtils.isNotBlank(police_arraignment.getMtssid())){
                            ReqParam<GetMCStateParam_out> getMCStateParam_outReqParam=new ReqParam<>();
                            GetMCStateParam_out getMCStateParam_out=new GetMCStateParam_out();
                            getMCStateParam_out.setMcType(MCType.AVST);
                            getMCStateParam_out.setMtssid(police_arraignment.getMtssid());
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

            }

        //获取实时数据
        result.setData(getRecordByIdVO);
        changeResultToSuccess(result);
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
                if(recordtypes.get(i).getPid()==0){
                    getRecordtypesVOParamList.add(recordtypes.get(i));
                }
                if (null!=pid){
                    getRecordtypesVOParamList.add(recordtypes.get(i));
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

        Police_recordtype police_recordtype=new Police_recordtype();
        police_recordtype.setId(getRecordtypeByIdParam.getId());
        police_recordtype =  police_recordtypeMapper.selectOne(police_recordtype);
        if (null==police_recordtype){
            System.out.println("未找到该笔录类型--");
            result.setMessage("系统错误");
        }
        result.setData(police_recordtype);
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
        System.out.println("insert_bool__"+insert_bool);
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
        System.out.println("update_bool__"+update_bool);
        if (update_bool>0){
            result.setData(update_bool);
            changeResultToSuccess(result);
        }
        return;
    }

    public void addRecordTemplate(RResult result, ReqParam param){
        return;
    }

    public void addCaseToArraignment(RResult result, ReqParam<AddCaseToArraignmentParam> param){
        AddCaseToArraignmentParam addCaseToArraignmentParam=param.getParam();
        if (null==addCaseToArraignmentParam){
            result.setMessage("参数为空");
            return;
        }

        List<Police_userto> usertos=addCaseToArraignmentParam.getUsertos();//其他在场人员信息


        //添加笔录信息
        Police_record record=new Police_record();
        record.setSsid(OpenUtil.getUUID_32());
        record.setCreatetime(new Date());
        record.setRecordbool(1);//1进行中2未开始
        record.setRecordtypessid(addCaseToArraignmentParam.getRecordtypessid());
        record.setRecordname(addCaseToArraignmentParam.getRecordname().replace(" ", "").replace("\"", ""));
        int insertrecord_bool=police_recordMapper.insert(record);
        System.out.println("insertrecord_bool__"+insertrecord_bool);
        if (insertrecord_bool<0){
            result.setMessage("系统异常");
            return;
        }

            //添加提讯数据
            Police_arraignment arraignment=new Police_arraignment();
            arraignment.setSsid(OpenUtil.getUUID_32());
            arraignment.setCreatetime(new Date());
            arraignment.setAdminssid(addCaseToArraignmentParam.getAdminssid());
            arraignment.setAsknum(addCaseToArraignmentParam.getAsknum()+1);
            arraignment.setAskobj(addCaseToArraignmentParam.getAskobj());
            arraignment.setRecordadminssid(addCaseToArraignmentParam.getRecordadminssid());
            arraignment.setRecordplace(addCaseToArraignmentParam.getRecordplace());
            arraignment.setOtheradminssid(addCaseToArraignmentParam.getOtheradminssid());
            arraignment.setRecordssid(record.getSsid());
            int insertarraignment_bool=police_arraignmentMapper.insert(arraignment);
            System.out.println("insertarraignment_bool__"+insertarraignment_bool);

            if (insertarraignment_bool<0){
                result.setMessage("系统异常");
                return;
            }

            if (StringUtils.isNotBlank(addCaseToArraignmentParam.getCasessid())){
                //添加案件提讯信息
                Police_casetoarraignment casetoarraignment=new Police_casetoarraignment();
                casetoarraignment.setCreatetime(new Date());
                casetoarraignment.setSsid(OpenUtil.getUUID_32());
                casetoarraignment.setArraignmentssid(arraignment.getSsid());
                casetoarraignment.setCasessid(addCaseToArraignmentParam.getCasessid());
                int insertcasetoarraignment_bool=police_casetoarraignmentMapper.insert(casetoarraignment);
                System.out.println("insertcasetoarraignment_bool__"+insertcasetoarraignment_bool);
            }

        //添加其他
        if (null!=addCaseToArraignmentParam.getUsertos()&&addCaseToArraignmentParam.getUsertos().size()>0){
            for (Police_userto userto : usertos) {
              Police_userto  userto1=new Police_userto();
                userto1.setSsid(OpenUtil.getUUID_32());
                userto1.setCreatetime(new Date());
                userto1.setArraignmentssid(arraignment.getSsid());
                userto1.setUserssid(addCaseToArraignmentParam.getUserssid());
                userto1.setLanguage(userto.getLanguage());
                userto1.setOtheruserssid(userto.getOtheruserssid());
                userto1.setRelation(userto.getRelation());
                userto1.setUsertitle(userto.getUsertitle());
                userto1.setUsertype(userto.getUsertype());
                int insertuserto_bool= police_usertoMapper.insert(userto1);
                System.out.println("insertuserto_bool__"+insertuserto_bool);
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
        System.out.println("证件类型："+cardtypesssid);
        System.out.println("证件号码："+cardnum);

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

                //询问人等
                Base_admininfo admininfo=(Base_admininfo)httpSession.getAttribute(Constant.MANAGE_CLIENT);

                EntityWrapper otheruserinfosparam=new EntityWrapper();
                otheruserinfosparam.ne("a.ssid",admininfo.getSsid());
                otheruserinfosparam.eq("a.adminbool",1);//正常人
                List<AdminAndWorkunit> otheruserinfos=base_admininfoMapper.getAdminListAndWorkunit(otheruserinfosparam);
                if (null!=otheruserinfos&&otheruserinfos.size()>0){
                    getUserByCardVO.setOtheruserinfos(otheruserinfos);
                }

                result.setData(getUserByCardVO);
                changeResultToSuccess(result);
            }else{
                System.out.println("人员用户找到多个--"+cardnum);
                result.setMessage("系统异常");
                return;
            }
        }
        return;
    }

    public void getCaseById(RResult result, ReqParam<GetCaseByIdParam> param){
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

        //根据用户userssid查询案件列表
        EntityWrapper caseparam=new EntityWrapper();
        caseparam.eq("c.userssid",userssid);
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

    public void exportPdf(RResult result, ReqParam param){


        return;
    }

    public void exportWord(RResult result, ReqParam<ExportWordParam> param, HttpServletRequest request){
        ExportWordParam exportWordParam=param.getParam();
        if (null==exportWordParam){
            result.setMessage("参数为空");
            return;
        }
        String recordssid=exportWordParam.getRecordssid();
        if (StringUtils.isBlank(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        //根据笔录ssid获取录音数据
        EntityWrapper recordParam=new EntityWrapper();
        recordParam.eq("r.ssid",recordssid);
        Record record=police_recordMapper.getRecordBySsid(recordParam);

        if (null!=record){
                String questionandanswer="";//题目答案
                EntityWrapper ew=new EntityWrapper();
                ew.eq("r.ssid",record.getSsid());
                ew.orderBy("p.ordernum",true);
                ew.orderBy("p.createtime",true);
                List<RecordToProblem> problems = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(ew);
                if (null!=problems&&problems.size()>0){
                    for (RecordToProblem problem : problems) {
                        questionandanswer+="问："+problem.getProblem()+"<w:br/>";
                        String problemssid=problem.getSsid();
                        if (StringUtils.isNotBlank(problemssid)){
                            EntityWrapper answerParam=new EntityWrapper();
                            answerParam.eq("recordtoproblemssid",problemssid);
                            answerParam.orderBy("ordernum",true);
                            answerParam.orderBy("createtime",true);
                            List<Police_answer> answers=police_answerMapper.selectList(answerParam);
                            if (null!=answers&&answers.size()>0){
                                for (Police_answer answer : answers) {
                                    questionandanswer+="答："+answer.getAnswer()+"<w:br/>";
                                }
                            }
                        }
                    }
                }

                /**
                 *   获取提讯人和被询问人
                 */
                EntityWrapper recorduserinfosParam=new EntityWrapper();
                recorduserinfosParam.eq("a.recordssid",record.getSsid());
                RecordUserInfos recordUserInfos=police_recordMapper.getRecordUserInfosByRecordSsid(recorduserinfosParam);

                String userssid=recordUserInfos.getUserssid();
                Police_userinfo police_userinfo=new Police_userinfo();
                police_userinfo.setSsid(userssid);
                police_userinfo=police_userinfoMapper.selectOne(police_userinfo);


                Police_arraignment police_arraignment=new Police_arraignment();
                police_arraignment.setRecordssid(recordssid);
                police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);


        Police_recordtype police_recordtype=new Police_recordtype();
        police_recordtype.setSsid(record.getRecordtypessid());
        police_recordtype=police_recordtypeMapper.selectOne(police_recordtype);

        String recordtypename=police_recordtype.getTypename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String recordstarttime=sdf.format(record.getCreatetime());
        String recordendtime=sdf.format(new Date());
        String recordplace=police_arraignment.getRecordplace();

        //工作单位
        Police_workunit police_workunit1=new Police_workunit();
        police_workunit1.setSsid(recordUserInfos.getWorkunitssid1());
        police_workunit1=police_workunitMapper.selectOne(police_workunit1);
        Police_workunit police_workunit2=new Police_workunit();
        police_workunit2.setSsid(recordUserInfos.getWorkunitssid2());
        police_workunit2=police_workunitMapper.selectOne(police_workunit2);
        Police_workunit police_workunit3=new Police_workunit();
        police_workunit3.setSsid(recordUserInfos.getWorkunitssid3());
        police_workunit3=police_workunitMapper.selectOne(police_workunit3);

        String workname1=police_workunit1.getWorkname();
        String workname2=police_workunit2.getWorkname();
        String workname3=police_workunit3.getWorkname();
        String username=police_userinfo.getUsername();
        String sex=police_userinfo.getSex()==1?"男":"女";
        String age=police_userinfo.getAge().toString();
        String politicsstatus=police_userinfo.getPoliticsstatus();
        String workunits=police_userinfo.getWorkunits();
        String residence=police_userinfo.getResidence();
        String phone=police_userinfo.getPhone();
        String domicile=police_userinfo.getDomicile();
         SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
        String both=sdf2.format(police_userinfo.getBoth());

        EntityWrapper userinfoparam=new EntityWrapper();
        userinfoparam.eq("u.ssid",userssid);
        List<UserInfo> userInfos=police_userinfoMapper.getUserByCard(userinfoparam);
         String cardnum=null;
        if (null!=userInfos&&userInfos.size()>0){
            cardnum=userInfos.get(0).getCardtypename()+userInfos.get(0).getCardnum();
        }

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("recordtypename", recordtypename==null?"":recordtypename);
        dataMap.put("recordstarttime", recordstarttime==null?"":recordstarttime);
        dataMap.put("recordendtime", recordendtime==null?"":recordendtime);
        dataMap.put("recordplace",recordplace==null?"":recordplace);
        dataMap.put("workname1", workname1==null?"":workname1);
        dataMap.put("workname2", workname2==null?"":workname2);
        dataMap.put("workname3", workname3==null?"":workname3);
        dataMap.put("username", username==null?"":username);
        dataMap.put("sex",sex==null?"":sex);
        dataMap.put("age", age==null?"":age);
        dataMap.put("cardnum",cardnum==null?"":cardnum);
        dataMap.put("politicsstatus", politicsstatus==null?"":politicsstatus);
        dataMap.put("workunits",workunits==null?"":workunits);
        dataMap.put("residence", residence==null?"":residence);
        dataMap.put("phone", phone==null?"":phone);
        dataMap.put("domicile",domicile==null?"":domicile);
        dataMap.put("both", both==null?"":both);
        dataMap.put("questionandanswer",questionandanswer==null?"":questionandanswer);

        try {

            /*template*/
            Configuration configuration = new Configuration(new Version("2.3.23"));
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(RecordService.class, "/config");

            //以utf-8的编码读取ftl文件
            Template template = configuration.getTemplate("askTo_wordtemplate.ftl","UTF-8");

            String filePathNew = filePath + "/zips";
            File fileMkdir = new File(filePathNew);
            if (!fileMkdir.exists()) {
                //如果不存在，就创建该目录
                fileMkdir.mkdirs();
            }
            String filename=record.getRecordname().replace(" ", "").replace("\"", "");
            String path = filePathNew + "/"+filename+".docx";

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"), 10240);
            template.process(dataMap, out);
            out.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            result.setData(uploadpath);

            changeResultToSuccess(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        }
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
                System.out.println("arraignmentupdateById_bool__"+arraignmentupdateById_bool);
                if (arraignmentupdateById_bool>0){
                    result.setData(true);
                    changeResultToSuccess(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCases(RResult result,ReqParam<GetCasesParam> param){
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
            }
            getCasesVO.setPagelist(list);
        }
        result.setData(getCasesVO);
        changeResultToSuccess(result);
    }

    public void addCase(RResult result,ReqParam<AddCaseParam> param){
        AddCaseParam addCaseParam=param.getParam();
        if (null==addCaseParam){
            result.setMessage("参数为空");
            return;
        }
        addCaseParam.setSsid(OpenUtil.getUUID_32());
        addCaseParam.setCreatetime(new Date());
        addCaseParam.setStarttime(new Date());
       int caseinsert_bool = police_caseMapper.insert(addCaseParam);
       System.out.println("caseinsert_bool__"+caseinsert_bool);
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
            System.out.println("getCaseBySsid__ssid:"+casessid);
            return;
        }

        EntityWrapper updateParam=new EntityWrapper();
        updateParam.eq("ssid",casessid);
        int caseupdate_bool = police_caseMapper.update(updateCaseParam,updateParam);
        System.out.println("caseupdate_bool__"+caseupdate_bool);
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
            System.out.println("getCaseBySsid__ssid:"+casessid);
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
            System.out.println("查找到案件数__"+caseAndUserInfos.size());
            result.setMessage("系统异常");
            return;
        }
        return;
    }

    public void getUserinfoList(RResult result,ReqParam param){
        GetUserinfoListVO getUserinfoListVO=new GetUserinfoListVO();

        //根据证件类型和证件号查询用户信息
        EntityWrapper userparam=new EntityWrapper();
        List<Police_userinfo> userinfos=police_userinfoMapper.selectList(userparam);
        if (null==userinfos||userinfos.size()<1){
            result.setMessage("未找到人员信息");
            return;
        }
        getUserinfoListVO.setUserinfos(userinfos);
        result.setData(getUserinfoListVO);
        changeResultToSuccess(result);
        return;
    }


}
