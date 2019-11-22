package com.avst.trm.v1.web.cweb.service.policeservice;

import com.alibaba.fastjson.JSON;
import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.conf.type.MCType;
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
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMc_modelParam_out;
import com.avst.trm.v1.feignclient.mc.vo.Avstmt_modelAll;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 * 关于案件的写这里
 */
@Service("caseService")
public class CaseService extends BaseService {

    private Gson gson=new Gson();

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Police_userinfototypeMapper police_userinfototypeMapper;

    @Autowired
    private Police_casetouserinfoMapper police_casetouserinfoMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private ArraignmentService arraignmentService;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;


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
        if (StringUtils.isBlank(years)){
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

    public void getCaseById(RResult result, ReqParam<GetCaseByIdParam> param, HttpSession session){
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

    public void getCases(RResult result, ReqParam<GetCasesParam> param, HttpSession session){
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

    public void addCase(RResult result, ReqParam<AddCaseParam> param, HttpSession session){
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
                    userparam.eq("ut.cardnum",cardnum);
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

           /* //删除案件的全部的关联
            EntityWrapper delew=new EntityWrapper();
            delew.eq("casessid",casessid);
            int police_casetouserinfoMapper_delete_bool=police_casetouserinfoMapper.delete(delew);
            LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_delete_bool__"+police_casetouserinfoMapper_delete_bool);*/




            //案件多用户
            List<UserInfo> userInfos=updateCaseParam.getUserInfos();
            if (null!=userInfos&&userInfos.size()>0){

                //对于存在的判断该人在改案件中是否已经存在提讯，存在不可删除
                EntityWrapper casetouserinfoParam1=new EntityWrapper();
                casetouserinfoParam1.eq("casessid",casessid);
                List<Police_casetouserinfo> police_casetouserinfos=police_casetouserinfoMapper.selectList(casetouserinfoParam1);
                if (null!=police_casetouserinfos&&police_casetouserinfos.size()>0) {
                    for (Police_casetouserinfo police_casetouserinfo : police_casetouserinfos) {
                        String userssid=police_casetouserinfo.getUserssid();
                        EntityWrapper ewarraignment=new EntityWrapper();
                        ewarraignment.eq("cr.casessid",casessid);
                        ewarraignment.eq("a.userssid",userssid);
                        ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                        List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                        if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                            //判断本次传过来是否有这个人的
                            int usernum=0;
                            for (UserInfo userInfo : userInfos) {
                                String ussid=userInfo.getSsid();
                                if (StringUtils.isNotEmpty(ussid)&&StringUtils.isNotEmpty(userssid)&&ussid.equals(userssid)){
                                    //本次带的也有
                                    usernum++;
                                }
                            }
                            if (usernum<1){
                                LogUtil.intoLog(this.getClass(),"案件修改存在用户已有提讯不允许删除__userssid_"+userssid);
                                result.setMessage("存在用户已被提讯不允许更改");
                                return;
                            }
                        }else {
                            EntityWrapper delew=new EntityWrapper();
                            delew.eq("casessid",casessid);
                            delew.eq("userssid",userssid);
                            delew.eq("usertotypessid",police_casetouserinfo.getUsertotypessid());
                            int police_casetouserinfoMapper_delete_bool=police_casetouserinfoMapper.delete(delew);
                            LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_delete_bool__"+police_casetouserinfoMapper_delete_bool);
                        }
                    }
                }



                for (UserInfo userInfo : userInfos) {
                    String cardtypessid=userInfo.getCardtypessid();
                    String cardnum=userInfo.getCardnum();
                    String userssid=userInfo.getSsid();
                    String usertotypessid=null;

                    //根据证件类型和证件号码，检测证件是否存在存在修改；不存在新增
                    EntityWrapper userparam=new EntityWrapper();
                    userparam.eq("ut.cardtypessid",cardtypessid);
                    if (StringUtils.isNotEmpty(userssid)){
                        userparam.eq("u.ssid",userssid);//userssid存在
                    }else {
                        userparam.eq("ut.cardnum",cardnum);
                    }
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
                        if (updateuserinfo_bool>0){
                            Police_userinfototype police_userinfototype=new Police_userinfototype();
                            police_userinfototype.setCardnum(userInfo.getCardnum());
                            EntityWrapper ew2=new EntityWrapper();
                            ew2.eq("cardtypessid",cardtypessid);
                            ew2.eq("userssid",userssid);
                            int police_userinfototypeMapper_update_bool=police_userinfototypeMapper.update(police_userinfototype,ew2);
                            LogUtil.intoLog(1,this.getClass(),"police_userinfototypeMapper_update_bool__"+police_userinfototypeMapper_update_bool);
                        }
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

                    LogUtil.intoLog(this.getClass(),"案件人员数据__casessid_"+casessid+"__userssid_"+userssid+"___usertotypessid__"+usertotypessid);
                    if (StringUtils.isNotEmpty(casessid)&&StringUtils.isNotEmpty(userssid)&&StringUtils.isNotEmpty(usertotypessid)){
                        //判断是否需要新增删除关联
                        EntityWrapper casetouserinfoParam=new EntityWrapper();
                        casetouserinfoParam.eq("casessid",casessid);
                        casetouserinfoParam.eq("userssid",userssid);
                        casetouserinfoParam.eq("usertotypessid",usertotypessid);
                        List<Police_casetouserinfo> police_casetouserinfos_=police_casetouserinfoMapper.selectList(casetouserinfoParam);
                        if (null==police_casetouserinfos_||police_casetouserinfos_.size()<1){
                            //不存在关系需要新增
                            //添加用户与案件的关联
                            Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                            police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                            police_casetouserinfo_.setCreatetime(new Date());
                            police_casetouserinfo_.setCasessid(casessid);
                            police_casetouserinfo_.setUserssid(userssid);
                            police_casetouserinfo_.setUsertotypessid(usertotypessid);
                            int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                            LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
                        }/*else {
                            //对于存在的判断该人在改案件中是否已经存在提讯，存在不可删除
                            EntityWrapper ewarraignment=new EntityWrapper();
                            ewarraignment.eq("cr.casessid",casessid);
                            ewarraignment.eq("a.userssid",userssid);
                            ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                            List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                            if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                                LogUtil.intoLog(this.getClass(),"案件修改存在用户已有提讯不允许删除__userssid_"+userssid);
                                result.setMessage("部分用户已被提讯不允许删除");
                                return;
                            }else {
                                EntityWrapper delew=new EntityWrapper();
                                delew.eq("casessid",casessid);
                                delew.eq("userssid",userssid);
                                delew.eq("usertotypessid",usertotypessid);
                                int police_casetouserinfoMapper_delete_bool=police_casetouserinfoMapper.delete(delew);
                                LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_delete_bool__"+police_casetouserinfoMapper_delete_bool);
                            }
                        }*/
                    }
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

                    EntityWrapper ewarraignment=new EntityWrapper();
                    ewarraignment.eq("cr.casessid",casessid);
                    ewarraignment.eq("a.userssid",userInfo.getSsid());
                    ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                    List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                    if (null!=arraignmentAndRecords){
                        userInfo.setArraignment_num(arraignmentAndRecords.size());
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
                            arraignmentService.addCaseToArraignment(addCaseToArraignment_rr,addCaseToArraignment_param,session);
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

    public void getCasesByCasenum(RResult result, GetCasesByCasenumParam param){
        GetCasesByCasenumVO vo=new GetCasesByCasenumVO();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        String casenum=param.getCasenum();
        LogUtil.intoLog(1,this.getClass(),"根据案件编号查询案件__casenum："+casenum);
        if (null==casenum){
            result.setMessage("参数为空");
            return;
        }



        //根据案件编号查询获取案件信息==1
        Case case_=new Case();
        EntityWrapper ew=new EntityWrapper();
        ew.eq("c.casenum",casenum);
        List<Case> cases=police_caseMapper.getCase(ew);
        if (null!=cases&&cases.size()>0){
            case_=cases.get(0);

            //根据案件ssid获取最后一次提讯的相关信息==2
            String casessid=case_.getSsid();
            ArraignmentAndRecord arraignmentAndRecord=new ArraignmentAndRecord();
            if (StringUtils.isNotEmpty(casessid)){
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",casessid);
                ewarraignment.ne("r.recordbool",-1);
                ewarraignment.orderBy("a.createtime",false);//时间排序获取最近时间的
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    arraignmentAndRecord=arraignmentAndRecords.get(0);
                    case_.setArraignments(arraignmentAndRecords);
                    vo.setArraignmentAndRecord(arraignmentAndRecord);

                    //获取相关人员信息
                    String  arraignmentssid=arraignmentAndRecord.getSsid();
                    if (StringUtils.isNotEmpty(arraignmentssid)){
                        EntityWrapper recorduserinfosParam=new EntityWrapper();
                        recorduserinfosParam.eq("a.ssid",arraignmentssid);
                        RecordUserInfos recordUserInfos=police_recordMapper.getRecordUserInfosByRecordSsid(recorduserinfosParam);
                        if (null!=recordUserInfos){
                            vo.setRecordUserInfos(recordUserInfos);
                        }


                        //获取相关人员：暂时只针对法院===
                        List<Usergrade> usergrades=new ArrayList<>();
                        EntityWrapper arre=new EntityWrapper();
                        arre.eq("arraignmentssid",arraignmentssid);
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
                      /*  Police_userinfo police_userinfo=new Police_userinfo();
                        police_userinfo.setSsid(userssid);
                        police_userinfo=police_userinfoMapper.selectOne(police_userinfo);*/
                                    String cardtypessid=PropertiesListenerConfig.getProperty("cardtype_default");//默认使用身份证类型
                                    EntityWrapper userparam=new EntityWrapper();
                                    userparam.eq("ut.cardtypessid",cardtypessid);
                                    userparam.eq("u.ssid",userssid);
                                    List<UserInfo> userInfos=police_userinfoMapper.getUserByCard(userparam);
                                    UserInfo police_userinfo=new UserInfo();
                                    if (null!=userInfos&&userInfos.size()==1){
                                        police_userinfo=userInfos.get(0);
                                    }

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
                                            usergrade.setUserinfo(police_userinfo);
                                            usergrades.add(usergrade);
                                        }else if (null!=admininfo){
                                            usergrade.setUsername(admininfo.getUsername());
                                            usergrades.add(usergrade);
                                        }
                                    }
                                }
                            }
                            if (null!=usergrades&&usergrades.size()>0){
                                vo.setUsergrades(usergrades);
                            }
                        }
                    }
                }
                vo.setCase_(case_);
            }
        }
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }

}
