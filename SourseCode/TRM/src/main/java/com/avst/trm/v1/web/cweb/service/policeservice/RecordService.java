package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_national;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_nationalMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_nationalityMapper;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Recordreal;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
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
    private  Police_recordtemplatetoproblemMapper police_recordtemplatetoproblemMapper;

    @Autowired
    private Police_answerMapper police_answerMapper;


    public void getRecords(RResult result, ReqParam<GetRecordsParam> param){
        GetRecordsVO getRecordsVO=new GetRecordsVO();

        //请求参数转换
        GetRecordsParam getRecordsParam = param.getParam();
        if (null==getRecordsParam){
            result.setMessage("参数为空");
            return;
        }

         String recordname=getRecordsParam.getRecordname();//笔录名
         String recordtypessid=getRecordsParam.getRecordtypessid();//笔录类型

        EntityWrapper recordparam=new EntityWrapper();
        if (StringUtils.isNotBlank(recordtypessid)){
            recordparam.eq("r.recordtypessid",recordtypessid);
        }
        if (StringUtils.isNotBlank(recordname)){
            recordparam.like("r.recordname",recordname);
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
                List<Problem> problems = police_recordtemplatetoproblemMapper.getProblemByRecordSsid(probleparam);
                if (null!=problems&&problems.size()>0){
                    //根据题目和笔录查找对应答案
                    for (Problem problem : problems) {
                        String problemssid=problem.getSsid();
                        if (StringUtils.isNotBlank(problem.getSsid())){
                            EntityWrapper answerParam=new EntityWrapper();
                            answerParam.eq("a.problemssid",problemssid);
                            answerParam.eq("a.recordssid",recordssid);
                            answerParam.orderBy("a.ordernum",true);
                            answerParam.orderBy("a.createtime",true);
                            List<Police_answer> answers=police_answerMapper.getAnswerByProblemSsidAndRecordSsid(answerParam);
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

    public void addRecord(RResult result, ReqParam param){
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
        try {
            EntityWrapper recordParam=new EntityWrapper();
            recordParam.eq("r.ssid",recordssid);
            Record record=police_recordMapper.getRecordBySsid(recordParam);

            if (null!=record){
                //获取题目
                EntityWrapper ew=new EntityWrapper();
                ew.eq("r.ssid",record.getSsid());
                ew.orderBy("p.ordernum",true);
                ew.orderBy("p.createtime",true);
                List<Problem> problems = police_recordtemplatetoproblemMapper.getProblemByRecordSsid(ew);
                if (null!=problems&&problems.size()>0){
                    //根据题目和笔录查找对应答案
                    for (Problem problem : problems) {
                        String problemssid=problem.getSsid();
                        if (StringUtils.isNotBlank(problemssid)){
                            EntityWrapper answerParam=new EntityWrapper();
                            answerParam.eq("a.problemssid",problemssid);
                            answerParam.eq("a.recordssid",record.getSsid());
                            ew.orderBy("a.ordernum",true);
                            ew.orderBy("a.createtime",true);
                            List<Police_answer> answers=police_answerMapper.getAnswerByProblemSsidAndRecordSsid(answerParam);
                            if (null!=answers&&answers.size()>0){
                                problem.setAnswers(answers);
                            }
                        }
                    }
                    record.setProblems(problems);
                }


                getRecordByIdVO.setRecord(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据笔录ssid获取案件信息
        try {
            EntityWrapper caseParam=new EntityWrapper();
            caseParam.eq("r.ssid",recordssid);
            CaseAndUserInfo caseAndUserInfo = police_caseMapper.getCaseByRecordSsid(caseParam);
            if (null!=caseAndUserInfo){
                getRecordByIdVO.setCaseAndUserInfo(caseAndUserInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
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


        //添加笔录信息
        Police_record record=new Police_record();
        record.setSsid(OpenUtil.getUUID_32());
        record.setCreatetime(new Date());
        record.setRecordbool(2);//1进行中2未开始
        record.setRecordtypessid(addCaseToArraignmentParam.getRecordtypessid());
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
            arraignment.setAsknum(addCaseToArraignmentParam.getAsknum());
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
            //添加笔录提讯信息
            Police_casetoarraignment casetoarraignment=new Police_casetoarraignment();
            casetoarraignment.setCreatetime(new Date());
            casetoarraignment.setSsid(OpenUtil.getUUID_32());
            casetoarraignment.setArraignmentssid(arraignment.getSsid());
            casetoarraignment.setCasessid(addCaseToArraignmentParam.getCasessid());
            int insertcasetoarraignment_bool=police_casetoarraignmentMapper.insert(casetoarraignment);
            System.out.println("insertcasetoarraignment_bool__"+insertcasetoarraignment_bool);
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
        List<Police_userinfo> userinfos=police_userinfoMapper.getUserByCard(userparam);
        if (null==userinfos||userinfos.size()<1){
            result.setMessage("未找到该人员信息");
            return;
        }

        if (null!=userinfos&&userinfos.size()>0){
            if (userinfos.size()==1){
                Police_userinfo police_userinfo=userinfos.get(0);
                getUserByCardVO.setUserinfo(police_userinfo);


                //根据用户userssid查询案件列表
                EntityWrapper caseparam=new EntityWrapper();
                caseparam.eq("userssid",police_userinfo.getSsid());
                caseparam.orderBy("occurrencetime",false);
                List<Police_case> cases=police_caseMapper.selectList(caseparam);
                if (null!=cases&&cases.size()>0){
                    getUserByCardVO.setCases(cases);
                }

                //询问人等
                Base_admininfo admininfo=(Base_admininfo)httpSession.getAttribute(Constant.MANAGE_CLIENT);

                EntityWrapper otheruserinfosparam=new EntityWrapper();
                otheruserinfosparam.ne("a.ssid",admininfo.getSsid());
                otheruserinfosparam.eq("a.adminbool",1);//正常人
                List<AdminAndWorkunit> otheruserinfos=base_admininfoMapper.getAdminListAndWorkunit(otheruserinfosparam);
                if (null!=cases&&cases.size()>0){
                    getUserByCardVO.setOtheruserinfos(otheruserinfos);
                }

                //其他在场人员信息关联：疑问？其他在场人员应该是关联案件和人员ssid 的：数据库需要修改。。。


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

        String casessid=getCaseByIdParam.getCasessid();
        if (StringUtils.isBlank(casessid)){
            result.setMessage("参数为空");
            return;
        }
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


    public static void main(String[] args) {



    }




}
