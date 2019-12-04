package com.avst.trm.v1.web.cweb.service.policeservice;


import com.alibaba.fastjson.JSON;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
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
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMc_modelParam_out;
import com.avst.trm.v1.feignclient.mc.vo.Avstmt_modelAll;
import com.avst.trm.v1.web.cweb.conf.UserinfogradeType;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.param.ArrUserExpandParam;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.avst.trm.v1.common.cache.CommonCache.getSQEntity;

@Service("arraignmentService")
public class ArraignmentService extends BaseService {

    private Gson gson = new Gson();

    @Autowired
    private Police_wordtemplateMapper police_wordtemplateMapper;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Police_recordtypeMapper police_recordtypeMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Police_userinfototypeMapper police_userinfototypeMapper;

    @Autowired
    private Police_casetouserinfoMapper police_casetouserinfoMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_usertoMapper police_usertoMapper;

    @Autowired
    private RecordService2 recordService2;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_cardtypeMapper police_cardtypeMapper;




    //=================================================关于提讯用户等其他====================================================================start
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
        Integer skipCheckCaseNumbool=addCaseToArraignmentParam.getSkipCheckCaseNumbool();//是否跳过检测案件编号检测 1跳过-1不跳过 跳过检测
        Integer multifunctionbool=addCaseToArraignmentParam.getMultifunctionbool();//功能类型
        Integer custommsgbool=addCaseToArraignmentParam.getCustommsgbool();//是否需要自定义信息
        String mtmodelssid=addCaseToArraignmentParam.getMtmodelssid();//会议模板ssid
        String mtmodelssidname=addCaseToArraignmentParam.getMtmodelssidname();//会议模板名称
        String wordtemplatessid=addCaseToArraignmentParam.getWordtemplatessid();//笔录模板ssid
        //笔录信息
        String recordtypessid=addCaseToArraignmentParam.getRecordtypessid()==null?PropertiesListenerConfig.getProperty("recordtype_default"):addCaseToArraignmentParam.getRecordtypessid();//笔录类型
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

        //授权功能信息
        String gnlist=getSQEntity.getGnlist();

        //快速笔录，添加默认数据
        if (null!=custommsgbool&&custommsgbool==1){
            String defaulttitle="快速笔录";
            String defaultrecordname="默认笔录";
            //一键谈话：默认使用会议的谈话模板ssid
            String cardtypessid= PropertiesListenerConfig.getProperty("cardtype_default");//默认使用身份证
            String time=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            //用户信息使用默认
            UserInfo newaddUserInfo=new UserInfo();
            newaddUserInfo.setUsername("未知_"+time);
            newaddUserInfo.setCardnum("未知_"+time);
            newaddUserInfo.setCardtypessid(cardtypessid);
            //案件信息默认
            Police_case newaddPolice_case=new Police_case();
            newaddPolice_case.setOccurrencetime(new Date());
            newaddPolice_case.setStarttime(new Date());


            //根据hk版本：
            if (null!=multifunctionbool&&multifunctionbool==1){
                defaulttitle="快速谈话";
                defaultrecordname="审讯笔录";
            }

            //根据nx版本：
            if (gnlist.indexOf(SQVersion.NX_O)!= -1){
                defaulttitle="快速庭审";
                newaddPolice_case.setCasenum(addPolice_case.getCasenum()==null?"":addPolice_case.getCasenum());

                //此处需要加入默认的拓展表人员数据====================================================
                //外部人员默认4个
                if (null==arraignmentexpand||arraignmentexpand.size()<1){
                    String[] arraignmentexpand_ = new String[] {UserinfogradeType.USERINFOGRADE1,UserinfogradeType.USERINFOGRADE2,UserinfogradeType.USERINFOGRADE3,UserinfogradeType.USERINFOGRADE8};
                    arraignmentexpand=new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        String username_time=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                        UserInfo userInfo=new UserInfo();
                        userInfo.setUsername("未知_"+username_time+"_"+i);
                        userInfo.setUserinfogradessid(arraignmentexpand_[i]);
                        arraignmentexpand.add(userInfo);
                    }
                }

                //内部人员也默认四个：除开书记员其他三个为新增的零时的用户
                if (null==arrUserExpandParams||arrUserExpandParams.size()<1){
                    String[] arrUserExpandParams_ = new String[] {UserinfogradeType.USERINFOGRADE4,UserinfogradeType.USERINFOGRADE6,UserinfogradeType.USERINFOGRADE7};
                    arrUserExpandParams=new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        //添加该管理员
                        String newadminssid=OpenUtil.getUUID_32();
                        String username_time=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                        Base_admininfo base_admininfo=new Base_admininfo();
                        base_admininfo.setSsid(newadminssid);
                        base_admininfo.setTemporaryaskbool(1);//是否为临时询问人1是
                        base_admininfo.setCreator(user.getSsid());
                        base_admininfo.setWorkunitssid(user.getWorkunitssid());
                        base_admininfo.setLoginaccount(OpenUtil.getUUID_32());
                        base_admininfo.setUsername("临时_"+username_time+"_"+i);
                        base_admininfo.setRegistertime(new Date());
                        int base_admininfoMapper_insertbool=base_admininfoMapper.insert(base_admininfo);
                        LogUtil.intoLog(this.getClass(),"base_admininfoMapper_insertbool__"+base_admininfoMapper_insertbool);
                        if (base_admininfoMapper_insertbool>0){
                            ArrUserExpandParam arrUserExpandParam=new ArrUserExpandParam();
                            arrUserExpandParam.setUserinfogradessid(arrUserExpandParams_[i]);
                            arrUserExpandParam.setUserssid(newadminssid);
                            arrUserExpandParams.add(arrUserExpandParam);
                        }
                    }
                    //书记员默认为登陆者不新增
                    ArrUserExpandParam arrUserExpandParam=new ArrUserExpandParam();
                    arrUserExpandParam.setUserinfogradessid(UserinfogradeType.USERINFOGRADE5);
                    if (StringUtils.isBlank(adminssid)){adminssid=user.getSsid();}
                    arrUserExpandParam.setUserssid(adminssid);
                    arrUserExpandParams.add(arrUserExpandParam);
                }
                //此处需要加入默认的拓展表人员数据====================================================end
            }
            String conversationmsg=defaulttitle+"_"+time;
            newaddPolice_case.setCasename("案件名_"+conversationmsg);


            //笔录名称
            recordname=defaultrecordname+"【"+defaulttitle+"】_"+time;
            askobj="询问对象_"+conversationmsg;

            if (StringUtils.isBlank(adminssid)){
                adminssid=user.getSsid();
            }
            addUserInfo=newaddUserInfo;
            addPolice_case=newaddPolice_case;
        }
        addCaseToArraignmentVO.setMultifunctionbool(multifunctionbool);
        addCaseToArraignmentVO.setCustommsgbool(custommsgbool);


        //整理模板
        if (null!=multifunctionbool&&(multifunctionbool==1||multifunctionbool==2 )){//||单组件时候
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
        if (null!=skipCheckCasebool&&skipCheckCasebool==-1&&StringUtils.isNotBlank(casessid)&&StringUtils.isNotBlank(userssid)){
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
        if (null!=skipCheckbool&&skipCheckbool==-1){
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

        //检测案件号码
        LogUtil.intoLog(1,this.getClass(),"【开始笔录】检测案件号码__skipCheckCaseNumbool:"+skipCheckCaseNumbool);
        if (null!=skipCheckCaseNumbool&&skipCheckCaseNumbool==-1&&StringUtils.isNotEmpty(addPolice_case.getCasenum())){
               String casenum=addPolice_case.getCasenum();
               if (StringUtils.isNotEmpty(casenum)){
                   EntityWrapper police_cases_param=new EntityWrapper();
                   police_cases_param.eq("casenum",casenum);
                   if (StringUtils.isNotEmpty(casessid)){
                       police_cases_param.ne("ssid",casessid);
                   }
                   police_cases_param.ne("casebool",-1);
                   List<Police_case> police_cases_=police_caseMapper.selectList(police_cases_param);
                   if (null!=police_cases_&&police_cases_.size()>0){
                       addCaseToArraignmentVO.setCasenumingbool(true);
                       addCaseToArraignmentVO.setCasessid(police_cases_.get(0).getSsid());
                       result.setData(addCaseToArraignmentVO);
                       return;
                   }
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
            if (null!=police_recordtype){
                recordname=""+addUserInfo.getUsername()+"《"+addPolice_case.getCasename().trim()+"》"+mtmodelssidname+"_"+police_recordtype.getTypename().replace(" ", "")+"_第"+(Integer.valueOf(asknum)+1)+"次";

            }
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
        if (StringUtils.isNotEmpty(userssid)){
            checkuserparam.eq("u.ssid",userssid);
            checkuserinfos=police_userinfoMapper.getUserByCard(checkuserparam);
        }else if (StringUtils.isNotEmpty(cardnum)&&(null==checkuserinfos||checkuserinfos.size()<1)){
                checkuserparam.eq("ut.cardnum",cardnum);
                checkuserinfos=police_userinfoMapper.getUserByCard(checkuserparam);
        }
        String time=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        if (StringUtils.isBlank(cardnum)&&StringUtils.isEmpty(userssid)){
            cardnum="未知_"+time;
            addUserInfo.setCardnum(cardnum);//默认身份证号码
        }

        if ((null==checkuserinfos||checkuserinfos.size()<1)&&StringUtils.isBlank(userssid)){
            LogUtil.intoLog(this.getClass(),"【开始笔录】需要新增人员____");
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
            if (updateuserinfo_bool>0){
                //此处需要修改身份证号码
                //先判断重复
                if (StringUtils.isNotEmpty(addUserInfo.getCardnum())&&StringUtils.isNotEmpty(addUserInfo.getCardtypessid())&&StringUtils.isNotEmpty(userssid)){
                    EntityWrapper ew1=new EntityWrapper();
                    ew1.eq("cardnum",addUserInfo.getCardnum());
                    ew1.eq("cardtypessid",addUserInfo.getCardtypessid());
                    ew1.ne("userssid",userssid);
                    List<Police_userinfototype> police_userinfototypes=police_userinfototypeMapper.selectList(ew1);
                    if (null!=police_userinfototypes&&police_userinfototypes.size()>0){
                        result.setMessage("身份证号码已存在");
                        LogUtil.intoLog(1,this.getClass(),"身份证号码不能重复__"+userInfo.getCardnum());
                        return;
                    }
                    Police_userinfototype police_userinfototype=new Police_userinfototype();
                    police_userinfototype.setCardnum(addUserInfo.getCardnum());
                    EntityWrapper ew2=new EntityWrapper();
                    ew2.eq("cardtypessid",addUserInfo.getCardtypessid());
                    ew2.eq("userssid",userssid);
                    int police_userinfototypeMapper_update_bool=police_userinfototypeMapper.update(police_userinfototype,ew2);
                    LogUtil.intoLog(1,this.getClass(),"police_userinfototypeMapper_update_bool__"+police_userinfototypeMapper_update_bool);
                    if (police_userinfototypeMapper_update_bool>0){
                        userInfo.setCardnum(addUserInfo.getCardnum());
                        userInfo.setCardtypessid(addUserInfo.getCardtypessid());
                    }
                }

                usertotypessid=userInfo.getUsertotypessid();
                if (StringUtils.isEmpty(usertotypessid)){
                    EntityWrapper userparam=new EntityWrapper();
                    userparam.eq("ut.cardtypessid",userInfo.getCardtypessid());
                    userparam.eq("ut.cardnum",userInfo.getCardnum());
                    List<UserInfo> userInfos_=police_userinfoMapper.getUserByCard(userparam);
                    if (null!=userInfos_&&userInfos_.size()==1){
                        UserInfo userInfo_=userInfos_.get(0);
                        usertotypessid=userInfo_.getUsertotypessid();
                    }
                }
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
                    casetouserinfoParam.eq("usertotypessid",usertotypessid);
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
        record.setRecordtypessid(recordtypessid);//默认谈话办案
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
                    if (updateuserinfo_bool>0){
                        //此处需要修改身份证号码
                    }
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


        LogUtil.intoLog(1,this.getClass(),"【开始笔录】wordtemplatessid__"+wordtemplatessid);
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
                    if (StringUtils.isNotEmpty(userinfogradessid)){
                        String userInfossid=null;
                        String usertotypessid_=null;
                        if (userinfogradessid.equals(UserinfogradeType.USERINFOGRADE2)&&StringUtils.isNotEmpty(userInfo.getUsername())&&username.equals(userInfo.getUsername())){//默认参数//对应被告==被询问人==>可能多个哎哎哎判断是否跟被询问人一样一样不用新增
                            userInfossid=userssid;//被询问人的，以免重复增加
                            usertotypessid_=usertotypessid;
                        }else {
                            userInfossid=userInfo.getSsid();
                            if (StringUtils.isBlank(userInfo.getCardtypessid())){
                                userInfo.setCardtypessid(PropertiesListenerConfig.getProperty("cardtype_default"));
                            }

                            String userInfocardnum = userInfo.getCardnum();//人员证件号码
                            String userInfousername = userInfo.getUsername();//人员名称
                            List<UserInfo> checkuserInfoinfos=new ArrayList<>();
                            EntityWrapper checkuserInfoparam=new EntityWrapper();
                            checkuserInfoparam.eq("ut.cardtypessid",userInfo.getCardtypessid());

                            if (StringUtils.isNotEmpty(userInfossid)){
                                checkuserInfoparam.eq("u.ssid",userInfossid);
                                checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                            }else if (StringUtils.isNotEmpty(userInfocardnum)&&(null==checkuserinfos||checkuserinfos.size()<1)){
                                checkuserInfoparam.eq("ut.cardnum",userInfocardnum);
                                checkuserInfoinfos=police_userinfoMapper.getUserByCard(checkuserInfoparam);
                            }

                            String time2=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                            if (StringUtils.isBlank(userInfocardnum)){
                                userInfocardnum="未知_"+time2;
                                userInfo.setCardnum(userInfocardnum);//默认身份证号码
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
                                    usertotypessid_=police_userinfototype.getSsid();
                                }
                            }else if (checkuserInfoinfos.size()==1){
                                UserInfo userinfo_=checkuserInfoinfos.get(0);
                                //修改用户信息
                                EntityWrapper updateuserinfoParam=new EntityWrapper();
                                updateuserinfoParam.eq("ssid",userinfo_.getSsid());
                                Police_userinfo police_userinfo=gson.fromJson(gson.toJson(userInfo),Police_userinfo.class);
                                int updateuserinfo_bool = police_userinfoMapper.update(police_userinfo,updateuserinfoParam);
                                LogUtil.intoLog(this.getClass(),"updateuserinfo_bool__"+updateuserinfo_bool);
                                if (updateuserinfo_bool>0){
                                    //先判断重复
                                    if (StringUtils.isNotEmpty(userInfo.getCardnum())&&StringUtils.isNotEmpty(userInfo.getCardtypessid())&&StringUtils.isNotEmpty(userinfo_.getSsid())){
                                        //重复了忽略----------
                                        EntityWrapper ew1=new EntityWrapper();
                                        ew1.eq("cardnum",userInfo.getCardnum());
                                        ew1.eq("cardtypessid",userInfo.getCardtypessid());
                                        ew1.ne("userssid",userinfo_.getSsid());
                                        List<Police_userinfototype> police_userinfototypes=police_userinfototypeMapper.selectList(ew1);
                                        if (null==police_userinfototypes||police_userinfototypes.size()<1){
                                            Police_userinfototype police_userinfototype=new Police_userinfototype();
                                            police_userinfototype.setCardnum(userInfo.getCardnum());
                                            EntityWrapper ew2=new EntityWrapper();
                                            ew2.eq("cardtypessid",userInfo.getCardtypessid());
                                            ew2.eq("userssid",userinfo_.getSsid());
                                            int police_userinfototypeMapper_update_bool=police_userinfototypeMapper.update(police_userinfototype,ew2);
                                            LogUtil.intoLog(1,this.getClass(),"police_userinfototypeMapper_update_bool__"+police_userinfototypeMapper_update_bool);
                                        }
                                    }
                                    userInfossid=userinfo_.getSsid();
                                    usertotypessid_=userinfo_.getUsertotypessid();
                                }
                            }
                            LogUtil.intoLog(this.getClass(),"【开始笔录】拓展表关联案件信息userInfossid____"+userInfossid+"___usertotypessid___"+usertotypessid_+"__casessid__"+casessid);
                            if (userinfogradessid.equals(UserinfogradeType.USERINFOGRADE2)&&StringUtils.isNotEmpty(usertotypessid_)){
                                //添加案件人员表
                                Police_casetouserinfo police_casetouserinfo_=new Police_casetouserinfo();
                                police_casetouserinfo_.setSsid(OpenUtil.getUUID_32());
                                police_casetouserinfo_.setCreatetime(new Date());
                                police_casetouserinfo_.setCasessid(casessid);
                                police_casetouserinfo_.setUserssid(userInfossid);
                                police_casetouserinfo_.setUsertotypessid(usertotypessid_);
                                int police_casetouserinfoMapper_insert_bool=police_casetouserinfoMapper.insert(police_casetouserinfo_);
                                LogUtil.intoLog(this.getClass(),"police_casetouserinfoMapper_insert_bool__"+police_casetouserinfoMapper_insert_bool);
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
        base_admininfo.setTemporaryaskbool(1);//添加临时询问人
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

        if (StringUtils.isBlank(userInfo.getCardnum())){
            result.setMessage("请输入居民身份证号码");
            LogUtil.intoLog(3,this.getClass(),"updateCaseToUser_userInfo.getCardnum() is null");
            return;
        }

        String cardtypesssid=userInfo.getCardtypessid();
        if (StringUtils.isBlank(cardtypesssid)){
            PropertiesListenerConfig.getProperty("cardtype_default");//支持居民身份证
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
                if (police_userinfoMapper_update_bool>0&&null!=cardtypesssid&&null!=userInfo.getCardnum()){
                    //先判断重复
                    EntityWrapper ew1=new EntityWrapper();
                    ew1.eq("cardnum",userInfo.getCardnum());
                    ew1.eq("cardtypessid",cardtypesssid);
                    ew1.ne("userssid",userssid);
                    List<Police_userinfototype> police_userinfototypes=police_userinfototypeMapper.selectList(ew1);
                    if (null!=police_userinfototypes&&police_userinfototypes.size()>0){
                        result.setMessage("身份证号码已存在");
                        LogUtil.intoLog(1,this.getClass(),"身份证号码不能重复__"+userInfo.getCardnum());
                        return;
                    }


                    Police_userinfototype police_userinfototype=new Police_userinfototype();
                    police_userinfototype.setCardnum(userInfo.getCardnum());
                    EntityWrapper ew2=new EntityWrapper();
                    ew2.eq("cardtypessid",cardtypesssid);
                    ew2.eq("userssid",userssid);
                    int police_userinfototypeMapper_update_bool=police_userinfototypeMapper.update(police_userinfototype,ew2);
                    LogUtil.intoLog(1,this.getClass(),"police_userinfototypeMapper_update_bool__"+police_userinfototypeMapper_update_bool);
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


    //=================================================关于提讯用户等其他=====================================================================end


}
