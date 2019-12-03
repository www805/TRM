package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.socketio.SocketIOConfig;
import com.avst.trm.v1.common.conf.type.*;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordUserInfos;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Usergrade;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.tts.Str2TtsParam;
import com.avst.trm.v1.feignclient.ec.vo.CheckRecordFileStateVO;
import com.avst.trm.v1.feignclient.ec.vo.GetURLToPlayVO;
import com.avst.trm.v1.feignclient.ec.vo.param.FDCacheParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;
import com.avst.trm.v1.feignclient.ec.vo.ph.CheckPolygraphStateVO;
import com.avst.trm.v1.feignclient.ec.vo.ph.GetPolygraphAnalysisVO;
import com.avst.trm.v1.feignclient.ec.vo.tts.Str2ttsVO;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.*;
import com.avst.trm.v1.feignclient.mc.vo.*;
import com.avst.trm.v1.feignclient.mc.vo.param.PHDataBackVoParam;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.*;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.req.policereq.CheckStartRecordParam;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.cweb.service.policeservice.ArraignmentService;
import com.avst.trm.v1.web.cweb.service.policeservice.EquipmentService;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckKeywordVO;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckStartRecordVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.corundumstudio.socketio.SocketIOClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.avst.trm.v1.common.cache.CommonCache.getSQEntity;

@Service
public class OutService  extends BaseService {
    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private ZkControl zkControl;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private MainService mainService;

    @Autowired
    private ArraignmentService arraignmentService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private Police_arraignmentexpandMapper police_arraignmentexpandMapper;

    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;


    private Gson gson = new Gson();


    public RResult startRercord(RResult result, ReqParam<StartRercordParam> param, HttpSession session) {
        StartRercordVO vo=new StartRercordVO();


        StartRercordParam startRercordParam=gson.fromJson(gson.toJson(param.getParam()), StartRercordParam.class);
        if (null == startRercordParam) {
            LogUtil.intoLog(this.getClass(),"参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_CLIENT)), AdminAndWorkunit.class);
        String recordssid=startRercordParam.getRecordssid();
        String mtmodelssid=null;//会议模板ssid
        StartRecordAndCaseParam startRecordAndCaseParam=startRercordParam.getStartRecordAndCaseParam();


        RecordUserInfos recordUserInfos=new RecordUserInfos();
        if (StringUtils.isNotBlank(recordssid)){
            EntityWrapper recorduserinfosParam = new EntityWrapper();
            recorduserinfosParam.eq("a.recordssid", recordssid);
            recordUserInfos = police_recordMapper.getRecordUserInfosByRecordSsid(recorduserinfosParam);
        }

        if (null!=recordUserInfos){
            //判断是否开过会议
            Police_arraignment police_arraignment=new Police_arraignment();
            police_arraignment.setRecordssid(recordssid);
            police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                if (null!=police_arraignment){
                    String mtssid=police_arraignment.getMtssid();
                    mtmodelssid=police_arraignment.getMtmodelssid();
                    if (StringUtils.isNotBlank(mtssid)){
                        vo.setRecordbool(true);
                        result.setData(vo);
                        result.setMessage("该案件已开启过会议");
                        return result;
                    }
                }

            //未指定模板使用默认模板
            if (StringUtils.isBlank(mtmodelssid)){
                Base_type base_type=new Base_type();
                base_type.setType(CommonCache.getCurrentServerType());
                base_type=base_typeMapper.selectOne(base_type);
                if (null!=base_type){
                    mtmodelssid=base_type.getMtmodelssid();
                }
            }

            LogUtil.intoLog(this.getClass(),"开启笔录使用的会议模板ssid_"+mtmodelssid);

            //先检测是否可以开始笔录
            List<String> adminssids=new ArrayList<>();
            if (StringUtils.isNotEmpty(recordUserInfos.getOtheradminssid())){
                adminssids.add(recordUserInfos.getOtheradminssid());
            }
            if (StringUtils.isNotEmpty(recordUserInfos.getAdminssid())){
                adminssids.add(recordUserInfos.getAdminssid());
            }

            CheckStartRecordParam checkStartRecordParam=new CheckStartRecordParam();
            checkStartRecordParam.setMtmodel_ssid(mtmodelssid);
            checkStartRecordParam.setUserinfo_ssid(recordUserInfos.getUserssid());
            checkStartRecordParam.setAdmininfos_ssid(adminssids);
            RResult checkrecordforuser_rr=new RResult();
            Integer[] recordbools=new Integer[]{1};
            boolean bool = arraignmentService.checkRecordForUser(checkrecordforuser_rr,checkStartRecordParam,user.getSsid(),recordbools);
            if (!bool){
                CheckStartRecordVO checkStartRecordVO=gson.fromJson(gson.toJson(checkrecordforuser_rr.getData()),CheckStartRecordVO.class);
                vo.setCheckStartRecordVO(checkStartRecordVO);
                result.setData(vo);
                return result;
            }


            //开始进行片头叠加
            PtdjParam_out ptdjParam_out=startRercordParam.getPtdjParam_out();
            if (null!=ptdjParam_out){
                ptdjParam_out.setFdType(FDType.FD_AVST);
                RResult ptdj_rr=new RResult();
                ReqParam ptdj_param=new ReqParam();
                ptdj_param.setParam(ptdjParam_out);
                equipmentService.ptdj(ptdj_rr,ptdj_param);
                if (null!=ptdj_rr&&ptdj_rr.getActioncode().equals(Code.SUCCESS.toString())){
                    LogUtil.intoLog(this.getClass(),"recordService.ptdj片头叠加成功__请求成功__");
                }else {
                    String msg=ptdj_rr==null?"":ptdj_rr.getMessage();
                    LogUtil.intoLog(this.getClass(),"recordService.ptdj片头叠加成功__请求失败__"+msg);
                }
            }

            //收集参数进行笔录
            //根据模板获取模板信息
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
                    LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__成功");
                }else{
                    LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__失败"+rr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            List<Avstmt_modeltdAll> avstmt_modeltdAlls=new ArrayList<>();
            List<TdAndUserAndOtherParam> tdList=new ArrayList<>();
            StartMCParam_out startMCParam_out=new StartMCParam_out();
            startMCParam_out.setMcType(MCType.AVST);
            startMCParam_out.setYwSystemType(YWType.RECORD_TRM);
            startMCParam_out.setAsrtype(ASRType.AVST);
            startMCParam_out.setModelbool(1);//默认使用模板
            startMCParam_out.setMeetingtype(2);//会议类型，1视频/2音频
            startMCParam_out.setMtmodelssid(mtmodelssid);//查询会议模板ssid
            startMCParam_out.setStartRecordAndCaseParam(startRecordAndCaseParam);



            //是否需要录像
            Integer userecord=-1;
            if (null!=modelAlls&&modelAlls.size()==1) {
                Avstmt_modelAll modelAll = modelAlls.get(0);
                userecord = modelAll.getUserecord();
            }

            //查询提讯拓展表，查找其他角色
            String gnlist=getSQEntity.getGnlist();
            if (gnlist.indexOf(SQVersion.FY_T)!= -1){
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


                            //查找用户:人员表
                            Police_userinfo police_userinfo=new Police_userinfo();
                            police_userinfo.setSsid(userssid);
                            police_userinfo=police_userinfoMapper.selectOne(police_userinfo);

                            //查找用户：管理员表
                            Base_admininfo admininfo=new Base_admininfo();
                            admininfo.setSsid(userssid);
                            admininfo=base_admininfoMapper.selectOne(admininfo);

                            if (null!=police_userinfograde){
                                Integer useasr=-1;
                                Integer usepolygraph=-1;
                                if (null!=avstmt_modeltdAlls&&avstmt_modeltdAlls.size()>0) {
                                    for (Avstmt_modeltdAll avstmt_modeltdAll : avstmt_modeltdAlls) {
                                        if (avstmt_modeltdAll.getGrade()==police_userinfograde.getGrade()){
                                            useasr=avstmt_modeltdAll.getUseasr();
                                            usepolygraph=avstmt_modeltdAll.getUsepolygraph();
                                        }
                                    }
                                }

                                TdAndUserAndOtherParam tdAndUserAndOtherParam=new TdAndUserAndOtherParam();
                                tdAndUserAndOtherParam.setGrade(police_userinfograde.getGrade());
                                tdAndUserAndOtherParam.setUsepolygraph(usepolygraph);//使用测谎仪
                                tdAndUserAndOtherParam.setUseasr(useasr);//使用语音识别
                                if (null!=police_userinfo){
                                    tdAndUserAndOtherParam.setUserssid(police_userinfo.getSsid());
                                    tdAndUserAndOtherParam.setUsername(police_userinfo.getUsername());
                                    tdList.add(tdAndUserAndOtherParam);
                                }else if (null!=admininfo){
                                    tdAndUserAndOtherParam.setUserssid(admininfo.getSsid());
                                    tdAndUserAndOtherParam.setUsername(admininfo.getUsername());
                                    tdList.add(tdAndUserAndOtherParam);
                                }
                            }
                        }
                    }
                }
            }else {
                //非法院版本
                //只判断主要被询问人和主要询问人
                Integer useasr1=-1;
                Integer usepolygraph1=-1;
                Integer useasr2=-1;
                Integer usepolygraph2=-1;
                if (null!=modelAlls&&modelAlls.size()==1){
                    Avstmt_modelAll modelAll=modelAlls.get(0);
                    avstmt_modeltdAlls=modelAll.getAvstmt_modeltdAlls();
                    if (null!=avstmt_modeltdAlls&&avstmt_modeltdAlls.size()>0){
                        for (Avstmt_modeltdAll avstmt_modeltdAll : avstmt_modeltdAlls) {
                            if (avstmt_modeltdAll.getGrade()==1){
                                useasr1=avstmt_modeltdAll.getUseasr();
                                usepolygraph1=avstmt_modeltdAll.getUsepolygraph();
                            }else if (avstmt_modeltdAll.getGrade()==2){
                                useasr2=avstmt_modeltdAll.getUseasr();
                                usepolygraph2=avstmt_modeltdAll.getUsepolygraph();
                            }
                        }
                    }
                }
                TdAndUserAndOtherParam tdAndUserAndOtherParam1=new TdAndUserAndOtherParam();
                tdAndUserAndOtherParam1.setGrade(1);//主麦：默认询问人一
                tdAndUserAndOtherParam1.setUserssid(recordUserInfos.getAdminssid());
                tdAndUserAndOtherParam1.setUsername(recordUserInfos.getAdminname());
                tdAndUserAndOtherParam1.setUsepolygraph(usepolygraph1);//使用测谎仪
                tdAndUserAndOtherParam1.setUseasr(useasr1);//使用语音识别

                TdAndUserAndOtherParam tdAndUserAndOtherParam2=new TdAndUserAndOtherParam();
                tdAndUserAndOtherParam2.setGrade(2);//副麦：默认被询问人
                tdAndUserAndOtherParam2.setUserssid(recordUserInfos.getUserssid());
                tdAndUserAndOtherParam2.setUsername(recordUserInfos.getUsername());
                tdAndUserAndOtherParam2.setUsepolygraph(usepolygraph2);//使用测谎仪
                tdAndUserAndOtherParam2.setUseasr(useasr2);//使用语音识别

                tdList.add(tdAndUserAndOtherParam1);
                tdList.add(tdAndUserAndOtherParam2);
            }


            if (null!=tdList&&tdList.size()>0){
                //此处需要筛选：将相同通道的只保留一个
                LogUtil.intoLog(1,this.getClass(),"会议人员（主要针对法院多角色）应该为__________________________________________去重前"+tdList.size());
                tdList=tdList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(TdAndUserAndOtherParam::getGrade))),ArrayList::new));
                LogUtil.intoLog(1,this.getClass(),"会议人员（主要针对法院多角色）应该为__________________________________________去重后"+tdList.size());
                for (TdAndUserAndOtherParam tdAndUserAndOtherParam : tdList) {
                    tdAndUserAndOtherParam.setAsrtype(ASRType.AVST);
                    tdAndUserAndOtherParam.setFdtype(FDType.FD_AVST);
                    tdAndUserAndOtherParam.setPolygraphtype(PHType.CMCROSS);
                    tdAndUserAndOtherParam.setUserecord(userecord);//使用录像
                }
                startMCParam_out.setTdList(tdList);
            }else {
                LogUtil.intoLog(1,this.getClass(),"startMC开启失败__人员为空___请注意___"+tdList.size());
                result.setMessage("人员为空");
                return result;
            }

            ReqParam<StartMCParam_out> param1=new ReqParam<>();
            param1.setParam(startMCParam_out);
            try {
                RResult rr = meetingControl.startMC(param1);
                if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                    StartMCVO startMCVO=gson.fromJson(gson.toJson(rr.getData()), StartMCVO.class);
                    String mtssid=startMCVO.getMtssid();

                    //根据recordssid获取提讯
                    if (null!=police_arraignment){
                        police_arraignment.setMtssid(mtssid);
                        int arraignmentupdateById_bool = police_arraignmentMapper.updateById(police_arraignment);
                        LogUtil.intoLog(this.getClass(),"arraignmentupdateById_bool__"+arraignmentupdateById_bool);
                    }

                    //修改笔录状态为进行中1
                    if (StringUtils.isNotBlank(recordssid)){
                        EntityWrapper recordew=new EntityWrapper();
                        recordew.eq("ssid",recordssid);
                        Police_record record=new Police_record();
                        record.setRecordbool(1);
                        int record_updatebool= police_recordMapper.update(record,recordew);
                        LogUtil.intoLog(this.getClass(),"record_updatebool__"+record_updatebool);
                    }

                    LogUtil.intoLog(this.getClass(),"startMC开启成功__");

                    vo.setStartMCVO(startMCVO);
                    result.setData(vo);
                    changeResultToSuccess(result);
                }else{
                    LogUtil.intoLog(this.getClass(),"startMC开启失败__");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //笔录用户信息未获取
            LogUtil.intoLog(this.getClass(),"startRercord__RecordUserInfos__笔录用户信息未获取到__");
        }
        return result;
    }


    public void pauseOrContinueRercord(RResult result, ReqParam<PauseOrContinueRercordParam> param){
        PauseOrContinueRercordVO vo=new  PauseOrContinueRercordVO();

        PauseOrContinueRercordParam pauseOrContinueRercordParam=gson.fromJson(gson.toJson(param.getParam()), PauseOrContinueRercordParam.class);
        if (null == pauseOrContinueRercordParam) {
            LogUtil.intoLog(this.getClass(),"pauseOrContinueRercord__参数为空__");
            result.setMessage("参数为空");
            return;
        }

        String mtssid=pauseOrContinueRercordParam.getMtssid();
        Integer pauseOrContinue=pauseOrContinueRercordParam.getPauseOrContinue();
        if (StringUtils.isBlank(mtssid)||null==pauseOrContinue){
            result.setMessage("参数为空");
            return;
        }


        PauseOrContinueMCParam_out pauseOrContinueMCParam_out=new PauseOrContinueMCParam_out();
        pauseOrContinueMCParam_out.setMcType(MCType.AVST);
        pauseOrContinueMCParam_out.setMtssid(mtssid);
        pauseOrContinueMCParam_out.setPauseOrContinue(pauseOrContinue);
        ReqParam reqParam=new ReqParam();
        reqParam.setParam(pauseOrContinueMCParam_out);
        RResult rr=meetingControl.pauseOrContinueMC(reqParam);
        if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
            LogUtil.intoLog(this.getClass(),"会议暂停或者继续识别__meetingControl.pauseOrContinueMC请求成功");
            vo=gson.fromJson(gson.toJson(rr.getData()), PauseOrContinueRercordVO.class);
            changeResultToSuccess(result);
        }else{
            String msg=rr==null?"":rr.getMessage();
            LogUtil.intoLog(this.getClass(),"会议暂停或者继续识别__meetingControl.pauseOrContinueMC请求失败__"+msg);
            msg=pauseOrContinue==1?"笔录暂停失败":"笔录重启失败";
            result.setMessage(msg);
        }
        vo.setPauseOrContinue(pauseOrContinue);
        result.setData(vo);
        return;
    }


    public RResult overRercord(RResult result, ReqParam<OverMCParam_out> param) {
        OverMCParam_out overMCParam_out=gson.fromJson(gson.toJson(param.getParam()), OverMCParam_out.class);

        if (null == overMCParam_out) {
            LogUtil.intoLog(this.getClass(),"参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        overMCParam_out.setMcType(MCType.AVST);
        try {
            param.setParam(overMCParam_out);
            result = meetingControl.overMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                LogUtil.intoLog(this.getClass(),"overMC关闭成功__"+overMCParam_out.getMtssid());
            }else{
                LogUtil.intoLog(this.getClass(),"overMC关闭失败__"+overMCParam_out.getMtssid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setRercordAsrTxtBack(ReqParam<SetMCAsrTxtBackVO> param, HttpSession session){
        //请求参数转换
        SetMCAsrTxtBackVO setMCAsrTxtBackVO=param.getParam();
        if (null==setMCAsrTxtBackVO){
            LogUtil.intoLog(this.getClass(),"参数为空");
            return false;
        }
        LogUtil.intoLog(this.getClass(),setMCAsrTxtBackVO.toString());

        try {
            if(null!=setMCAsrTxtBackVO){
                //开始处理返回数据
                //时间毫秒级处理显示

                String asrtime = setMCAsrTxtBackVO.getAsrtime();
                String starttime = setMCAsrTxtBackVO.getStarttime();
                String asrstartime = setMCAsrTxtBackVO.getAsrstartime();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if (StringUtils.isNotBlank(asrtime)){
                    asrtime = df.format(new Date(Long.valueOf(asrtime)));
                    setMCAsrTxtBackVO.setAsrtime(asrtime);
                }

                if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                    asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                    setMCAsrTxtBackVO.setAsrstartime(asrstartime);
                }

                String txt=setMCAsrTxtBackVO.getTxt();
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
                        setMCAsrTxtBackVO.setKeyword_txt(keyword_txt);
                    }
                }else {
                    setMCAsrTxtBackVO.setKeyword_txt(keyword_txt);
                }


                List<SocketIOClient> clients = SocketIOConfig.clients;
                LogUtil.intoLog(this.getClass(),"SocketIOClient__"+clients);
                if (null!=clients&&clients.size()>0){
                    for (SocketIOClient client : clients) {
                        try {
                            client.sendEvent("getback", setMCAsrTxtBackVO);
                        } catch (Exception e) {
                            LogUtil.intoLog(4,this.getClass(),"client.sendEvent数据填充到socket失败");
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public RResult getRecord(RResult result, ReqParam<GetPhssidByMTssidParam_out> param){
        //请求参数转换
        GetPhssidByMTssidParam_out getMCParam_out = param.getParam();
        if (null==getMCParam_out){
            LogUtil.intoLog(4,this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCParam_out.setMcType(MCType.AVST);
        GetMCVO getMCVO=new GetMCVO();
        try {
            result = meetingControl.getMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                 getMCVO=gson.fromJson(gson.toJson(result.getData()),GetMCVO.class);
                if (null!=getMCVO){
                    List<AsrTxtParam_toout>  asrTxtParam_toouts=getMCVO.getList();
                    if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                        for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                            String asrtime = asrTxtParam_toout.getAsrtime();
                            String starttime = asrTxtParam_toout.getStarttime();
                            String asrstartime = asrTxtParam_toout.getAsrstartime();
                            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            if (StringUtils.isNotBlank(asrtime)){
                                asrtime = df.format(new Date(Long.valueOf(asrtime)));
                                asrTxtParam_toout.setAsrtime(asrtime);
                            }

                            if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                                asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                                asrTxtParam_toout.setAsrstartime(asrstartime);
                            }
                        }
                        LogUtil.intoLog(this.getClass(),"排序后时间2：——————"+asrTxtParam_toouts.get(0).getStarttime());
                      /*  Collections.sort(asrTxtParam_toouts, (s1, s2) -> s2.getStarttime().toString().compareTo(s1.getStarttime().toString()));*/
                        asrTxtParam_toouts.sort((o1, o2) -> o1.getAsrstartime().compareTo(o2.getAsrstartime()));
                        LogUtil.intoLog(this.getClass(),"排序后时间2：——————"+asrTxtParam_toouts.get(0).getStarttime());
                        getMCVO.setList(asrTxtParam_toouts);
                        result.setData(getMCVO);
                    }
                }
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMC__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public RResult getRecordrealing(RResult result, ReqParam<GetMCaLLUserAsrTxtListParam_out> param){
        GetRecordrealingVO getRecordrealingVO=new GetRecordrealingVO();
        //请求参数转换
        GetMCaLLUserAsrTxtListParam_out getMCaLLUserAsrTxtListParam_out = param.getParam();
        if (null==getMCaLLUserAsrTxtListParam_out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }

        //获取会议直播实时数据
        getMCaLLUserAsrTxtListParam_out.setMcType(MCType.AVST);
        List<AsrTxtParam_toout> asrTxtParam_toouts=new ArrayList<AsrTxtParam_toout>();
        try {
            RResult rr=new RResult<>();
            rr = meetingControl.getMCaLLUserAsrTxtList(param);
            if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                asrTxtParam_toouts=gson.fromJson(gson.toJson(rr.getData()), new TypeToken<List<AsrTxtParam_toout>>(){}.getType());
                if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                    //时间排序
                    for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {

                        String asrtime = asrTxtParam_toout.getAsrtime();
                        String starttime = asrTxtParam_toout.getStarttime();
                        String asrstartime = asrTxtParam_toout.getAsrstartime();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        if (StringUtils.isNotBlank(asrtime)){
                            asrtime = df.format(new Date(Long.valueOf(asrtime)));
                            asrTxtParam_toout.setAsrtime(asrtime);
                        }

                        if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                            asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                            asrTxtParam_toout.setAsrstartime(asrstartime);
                        }

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
                    LogUtil.intoLog(this.getClass(),"排序后时间1：——————"+asrTxtParam_toouts.get(0).getAsrstartime());
                    asrTxtParam_toouts.sort((o1, o2) -> o1.getAsrstartime().compareTo(o2.getAsrstartime()));
                    LogUtil.intoLog(this.getClass(),"排序后时间1：——————"+asrTxtParam_toouts.get(0).getAsrstartime());

                    getRecordrealingVO.setList(asrTxtParam_toouts);
                }
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMCaLLUserAsrTxtList__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取会议直播地址
        List<FDCacheParam> fdCacheParams=new ArrayList<>();
        try {
            RResult rr2=new RResult<>();
            GetFDListByFdidParam getFDListByFdidParam=new GetFDListByFdidParam();
            getFDListByFdidParam.setFdType(FDType.FD_AVST);
            getFDListByFdidParam.setFdid(getMCaLLUserAsrTxtListParam_out.getMtssid());
            rr2 = equipmentControl.getFDListByFdid(getFDListByFdidParam);
            if (null != rr2 && rr2.getActioncode().equals(Code.SUCCESS.toString())) {
                fdCacheParams=gson.fromJson(gson.toJson(rr2.getData()), new TypeToken<List<FDCacheParam>>(){}.getType());
                getRecordrealingVO.setFdCacheParams(fdCacheParams);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getFDListByFdid__出错");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        result.setData(getRecordrealingVO);
        changeResultToSuccess(result);
        return  result;
    }

    public RResult getRecordState(RResult result, ReqParam<GetMCStateParam_out> param){
        //请求参数转换
        GetMCStateParam_out getMCStateParam_out = param.getParam();
        if (null==getMCStateParam_out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCStateParam_out.setMcType(MCType.AVST);
        Integer mtstate=null;
        try {
            result = meetingControl.getMCState(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                mtstate= (Integer) result.getData();
                result.setData(mtstate);
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMCState__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public void getPolygraphdata(RResult result, ReqParam<GetPolygraphdataParam> param){
        //请求参数转换
        GetPolygraphdataParam getPolygraphdataParam = param.getParam();
        if (null==getPolygraphdataParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }
        String mtssid=getPolygraphdataParam.getMtssid();
        if (StringUtils.isBlank(mtssid)){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }

        ReqParam<GetPHDataParam_out> phparam=new ReqParam<GetPHDataParam_out>();
        GetPHDataParam_out getPHDataParam_out=new GetPHDataParam_out();
        getPHDataParam_out.setMcType(MCType.AVST);
        getPHDataParam_out.setMtssid(mtssid);
        phparam.setParam(getPHDataParam_out);
        RResult rr1=meetingControl.getPHData(phparam);
        if (null != rr1 && rr1.getActioncode().equals(Code.SUCCESS.toString())) {
            PhDataParam_toout vo=gson.fromJson(gson.toJson(rr1.getData()),PhDataParam_toout.class);
            if (null!=vo){
                GetPolygraphAnalysisVO getPolygraphAnalysisVO=new GetPolygraphAnalysisVO();
                getPolygraphAnalysisVO.setT(vo.getT());
                result.setData(getPolygraphAnalysisVO);
                changeResultToSuccess(result);
                return;
            }else{
            }
        }else {
            LogUtil.intoLog(this.getClass(),"身心监测数据获取异常 mtssid："+mtssid);
            changeResultToSuccess(result);
            return;
        }
        return;
    }


    public void getPHDataBack(RResult result,ReqParam<GetPHDataBackParam> param){
        GetPHDataBackParam getPHDataBackParam = param.getParam();
        if (null==getPHDataBackParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return;
        }

        String mtssid=getPHDataBackParam.getMtssid();

        GetPHDataParam_out getPHDataParam_out=new GetPHDataParam_out();
        getPHDataParam_out.setMcType(MCType.AVST);
        getPHDataParam_out.setMtssid(mtssid);
        ReqParam reqParam=new ReqParam();
        reqParam.setParam(getPHDataParam_out);
        RResult getPHDataBack_rr =  meetingControl.getPHDataBack(reqParam);
        if (null != getPHDataBack_rr && getPHDataBack_rr.getActioncode().equals(Code.SUCCESS.toString())) {
            LogUtil.intoLog(this.getClass(),"meetingControl.getPHDataBack__成功");
            List<PHDataBackVoParam> phDataBackVoParams=gson.fromJson(gson.toJson(getPHDataBack_rr.getData()), new TypeToken<List<PHDataBackVoParam>>(){}.getType());
            result.setData(phDataBackVoParams);
            changeResultToSuccess(result);
        }else {
            LogUtil.intoLog(this.getClass(),"meetingControl.getPHDataBack__出错");
        }
        return;
    }


    //-------------------------------------------------------------------------------------------------------------------


    public void checkPlayFileState(RResult result, CheckRecordFileStateParam param){
        param.setSsType(SSType.AVST);

        return;
    }


    public void getPlayUrl(RResult result,GetURLToPlayParam  param){
        GetPlayUrlVO getPlayUrlVO =new GetPlayUrlVO();
        //先判断数据存储状态2
        String iid=param.getIid();

        if (StringUtils.isBlank(iid)){
            result.setMessage("参数为空");
            return;
        }

        getPlayUrlVO.setIid(iid);

        //1.获取文件状态
        RResult rr=new RResult();
        CheckRecordFileStateParam checkRecordFileStateParam=new CheckRecordFileStateParam();
        checkRecordFileStateParam.setSsType(SSType.AVST);
        checkRecordFileStateParam.setIid(iid);
        rr= equipmentControl.checkRecordFileState(checkRecordFileStateParam);

        CheckRecordFileStateVO checkRecordFileStateVO=new CheckRecordFileStateVO();
        Integer state=null;
        if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
            checkRecordFileStateVO=gson.fromJson(gson.toJson(rr.getData()),CheckRecordFileStateVO.class );
            List<RecordFileParam> recordFileParams=checkRecordFileStateVO.getRecordList();
            if (null!=recordFileParams&&recordFileParams.size()>0){
                for (RecordFileParam recordFileParam : recordFileParams) {
                    state=recordFileParam.getState();
                }
            }
            LogUtil.intoLog(this.getClass(),"数据存储状态__"+state);
            getPlayUrlVO.setRecordFileParams(recordFileParams);
           if (null!=state&&state==2){
               LogUtil.intoLog(this.getClass(),"数据存储正常开始获取地址__"+state);

               //2.说去文件地址
               RResult<GetURLToPlayVO> rr2=new RResult<GetURLToPlayVO>();
               GetURLToPlayParam getURLToPlayParam=new GetURLToPlayParam();
               getURLToPlayParam.setSsType(SSType.AVST);
               getURLToPlayParam.setIid(iid);
               rr2=equipmentControl.getURLToPlay(getURLToPlayParam);

               GetURLToPlayVO getURLToPlayVO=new GetURLToPlayVO();
               if (null != rr2 && rr2.getActioncode().equals(Code.SUCCESS.toString())) {
                   getURLToPlayVO=rr2.getData();
                   if(null!=getURLToPlayVO&&null!=getURLToPlayVO.getRecordList()){
                       List<RecordPlayParam> recordPlayParams =getURLToPlayVO.getRecordList();
                       if (null!=recordPlayParams&&recordPlayParams.size()>0){
                           for (RecordPlayParam recordPlayParam : recordPlayParams) {
                               LogUtil.intoLog(this.getClass(),"直播地址__"+recordPlayParam.getPlayUrl());
                           }
                       }
                       getPlayUrlVO.setRecordPlayParams(recordPlayParams);
                   }
               }else{
                   LogUtil.intoLog(this.getClass(),"请求getURLToPlay__出错");
               }
               result.setData(getPlayUrlVO);
               changeResultToSuccess(result);
           }else{
               LogUtil.intoLog(this.getClass(),"数据存储状态异常__"+state);

              /* 0文件未获取，等待中；1文件正常，生成请求地址中；2文件可以正常使用；-1文件未正常获取，需强制获取；-2文件请求地址有误，需重新生成*/
               String msgtext="点播获取失败";
                if(null!=state)   {
                    msgtext=state==0?"点播文件未获取...请等待":(state==1?"点播文件正常，生成请求地址中...请等待":"点播获取失败");
                }
               result.setMessage(msgtext);
               return;
           }
        }else{
            LogUtil.intoLog(this.getClass(),"请求checkRecordFileState__出错");
        }

       /* */
        return;
    }


    public void getEquipmentsState(RResult result,ReqParam<GetEquipmentsStateParam> param){
        GetEquipmentsStateVO getEquipmentsStateVO=new GetEquipmentsStateVO();
        //请求参数转换
        GetEquipmentsStateParam getEquipmentsStateParam = param.getParam();
        if (null==getEquipmentsStateParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }
        String mtssid=getEquipmentsStateParam.getMtssid();
        if (StringUtils.isBlank(mtssid)){
            LogUtil.intoLog(this.getClass(),"getEquipmentsState参数为空mtssid____"+mtssid);
            result.setMessage("参数为空");
            return ;
        }

        Integer fdrecord=getEquipmentsStateParam.getFdrecord();//是否需要录像，1使用，-1 不使用
        Integer usepolygraph=getEquipmentsStateParam.getUsepolygraph();//是否使用测谎仪，1使用，-1 不使用
        Integer useasr=getEquipmentsStateParam.getUseasr();//是否使用语言识别，1使用，-1 不使用

        Integer recordnum=getEquipmentsStateParam.getRecordnum();//本次会议开启的录音/像个数
        Integer asrnum=getEquipmentsStateParam.getAsrnum();//本次会议开启的语音识别个数
        Integer polygraphnum=getEquipmentsStateParam.getPolygraphnum();//本次会议开启的测谎仪个数



        //状态： -1异常 1正常  0未启动
        Integer AsrState=0;//语音识别状态
        Integer PolygraphState=0;//身心检测状态
        Integer LiveState=0;//直播状态
        Integer MtState=0;//会议状态  //会议状态 0初始化，1进行中，2已结束，3暂停
        Integer PlayState=0;//点播文件状态

        //获取会议状态
        ReqParam<GetMCStateParam_out> mt_param=new ReqParam<>();//会议参数
        GetMCStateParam_out getMCStateParam_out=new GetMCStateParam_out();
        getMCStateParam_out.setMcType(MCType.AVST);
        getMCStateParam_out.setMtssid(mtssid);
        mt_param.setParam(getMCStateParam_out);
        RResult mt_rr=new RResult();//会议返回
        try {
            mt_rr=meetingControl.getMCState(mt_param);
            if (null != mt_rr && mt_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                Integer mtstate2=(Integer) mt_rr.getData();
                MtState=mtstate2==3?3:1;
                if(null!=asrnum&&asrnum.intValue()>0&&null!=useasr&&useasr.intValue()==1){
                    AsrState=1;
                }
                if(null!=recordnum&&recordnum.intValue()>0&&null!=fdrecord&&fdrecord.intValue()==1){
                    LiveState=1;
                }
                //设置状态
            }else{
                //设置状态
                MtState= -1;
                if(null!=asrnum&&asrnum.intValue()>0&&null!=useasr&&useasr.intValue()==1){
                    AsrState=-1;
                }
                if(null!=recordnum&&recordnum.intValue()>0&&null!=fdrecord&&fdrecord.intValue()==1){
                    LiveState=-1;
                }
            }
        } catch (Exception e) {
            MtState= -1;
            LiveState=-1;
            AsrState=-1;
            e.printStackTrace();
        }




        //获取身心检测的状态
        if (null!=usepolygraph&&usepolygraph.intValue()==1&&null!=polygraphnum&&polygraphnum.intValue()>0){
            //为使用状态时
            String polygraphssid=null;//身心检测的ssid
            ReqParam<GetPhssidByMTssidParam_out> MCdata_param=new ReqParam<>();
            GetPhssidByMTssidParam_out getMCdataParam_out=new GetPhssidByMTssidParam_out();
            getMCdataParam_out.setMcType(MCType.AVST);
            getMCdataParam_out.setMtssid(mtssid);
            MCdata_param.setParam(getMCdataParam_out);
            RResult  rResult=new RResult();
            try {
                rResult=meetingControl.getPhssidByMTssid(MCdata_param);
                if (null != rResult && rResult.getActioncode().equals(Code.SUCCESS.toString())) {
                    String phssid = rResult.getData().toString();
                    if (StringUtils.isNotEmpty(phssid)){
                        polygraphssid=phssid;
                    }else{
                    }
                }else{
                }
            } catch (Exception e) {
                PolygraphState=-1;
                e.printStackTrace();
            }
            if (null==polygraphssid){
                PolygraphState=0;
                //设置状态
            }else {
                ReqParam<CheckPolygraphStateParam> Polygraph_param=new ReqParam<>();
                CheckPolygraphStateParam checkPolygraphStateParam=new CheckPolygraphStateParam();
                checkPolygraphStateParam.setPhType(PHType.CMCROSS);
                checkPolygraphStateParam.setPolygraphssid(polygraphssid);
                Polygraph_param.setParam(checkPolygraphStateParam);
                RResult<CheckPolygraphStateVO> Polygraph_rr=new RResult();
                try {
                    Polygraph_rr=equipmentControl.checkPolygraphState(Polygraph_param);
                    if (null != Polygraph_rr && Polygraph_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                        PolygraphState=1;
                        //设置状态
                    }else{
                        //设置状态
                        PolygraphState=-1;
                    }
                } catch (Exception e) {
                    PolygraphState=-1;
                    e.printStackTrace();
                }
            }
        }


        getEquipmentsStateVO.setAsrState(AsrState);
        getEquipmentsStateVO.setPolygraphState(PolygraphState);
        getEquipmentsStateVO.setLiveState(LiveState);
        getEquipmentsStateVO.setMtState(MtState);
        getEquipmentsStateVO.setPlayState(PlayState);
        result.setData(getEquipmentsStateVO);
        changeResultToSuccess(result);
        return;
    }

    public void str2Tts(RResult result, ReqParam<Str2TtsParam> param){
        Str2ttsVO vo=new Str2ttsVO();

        Str2TtsParam str2TtsParam = param.getParam();
        if (null==str2TtsParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }
        String text=str2TtsParam.getText();
        if (StringUtils.isBlank(text)){
            LogUtil.intoLog(this.getClass(),"str2Tts参数为空text____");
            result.setMessage("参数为空");
            return ;
        }

        LogUtil.intoLog(this.getClass(),"tts文本____"+text);

        ReqParam reqParam=new ReqParam();
        str2TtsParam.setTtsType(TTSType.AVST);
        reqParam.setParam(str2TtsParam);
        RResult str2tts_rr = equipmentControl.str2Tts(reqParam);
        if (null!=str2tts_rr&&str2tts_rr.getActioncode().equals(Code.SUCCESS.toString())){
            String uploadpath= (String) str2tts_rr.getData();
            vo.setUploadpath(uploadpath);
            result.setData(vo);
            changeResultToSuccess(result);
            LogUtil.intoLog(this.getClass(),"str2Tts请求__成功");
        }else {
            LogUtil.intoLog(this.getClass(),"str2Tts请求__出错");
        }
        return;
    }



    public void getMc_model(RResult result, ReqParam<GetMc_modelParam_out> param){
        GetMc_modelParam_out getMc_modelParam_out=param.getParam();
        getMc_modelParam_out.setMcType(MCType.AVST);

        ReqParam reqParam=new ReqParam();
        reqParam.setParam(getMc_modelParam_out);

        try {
            RResult rr = meetingControl.getMc_model(reqParam);
            if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
                List<Avstmt_modelAll> modelAlls=gson.fromJson(gson.toJson(rr.getData()), new TypeToken<List<Avstmt_modelAll>>(){}.getType());
                result.setData(modelAlls);
                changeResultToSuccess(result);
                LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__成功");
            }else{
                LogUtil.intoLog(this.getClass(),"meetingControl.getMc_modeltd请求__失败"+rr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void getTdByModelSsid(RResult result,ReqParam<GetTdByModelSsidParam_out> param){
        GetTdByModelSsidParam_out getTdByModelSsidParam_out=param.getParam();
        if (null==getTdByModelSsidParam_out){
            LogUtil.intoLog(this.getClass(),"getTdByModelSsid参数为空getTdByModelSsidParam_out____");
            result.setMessage("参数为空");
            return ;
        }
        String modelssid=getTdByModelSsidParam_out.getModelssid();
        if (StringUtils.isBlank(modelssid)){
            LogUtil.intoLog(this.getClass(),"getTdByModelSsid参数为空modelssid____");
            result.setMessage("参数为空");
            return ;
        }
        getTdByModelSsidParam_out.setMcType(MCType.AVST);
        getTdByModelSsidParam_out.setModelssid(modelssid);
        ReqParam reqParam=new ReqParam<>();
        reqParam.setParam(getTdByModelSsidParam_out);
        RResult rr =  meetingControl.getTdByModelSsid(reqParam);
        if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
            GetTdByModelSsidVO vo=gson.fromJson(gson.toJson(rr.getData()),GetTdByModelSsidVO.class);
            if (null!=vo){
                result.setData(vo);
                changeResultToSuccess(result);
                return;
            }
            LogUtil.intoLog(this.getClass(),"getTdByModelSsid请求meetingControl.getTdByModelSsid___请求成功");
        }else {
            String msg=rr.getMessage()==null?"":rr.getMessage();
            LogUtil.intoLog(this.getClass(),"getTdByModelSsid请求meetingControl.getTdByModelSsid___请求失败"+msg);
        }
            return;
    }

    public void getMCCacheParamByMTssid(RResult result,ReqParam<GetMCCacheParamByMTssidParam_out> param){
        GetMCCacheParamByMTssidVO vo=new GetMCCacheParamByMTssidVO();

        GetMCCacheParamByMTssidParam_out out=param.getParam();
        if (null==out){
            LogUtil.intoLog(this.getClass(),"getMCCacheParamByMTssid参数为空GetMCCacheParamByMTssidParam_out____");
            result.setMessage("参数为空");
            return ;
        }
        String mtssid=out.getMtssid();
        if (StringUtils.isBlank(mtssid)){
            LogUtil.intoLog(this.getClass(),"getMCCacheParamByMTssid参数为空mtssid____");
            result.setMessage("参数为空");
            return ;
        }
        out.setMcType(MCType.AVST);
        out.setMtssid(mtssid);
        ReqParam reqParam=new ReqParam<>();
        reqParam.setParam(out);
        RResult rr =  meetingControl.getMCCacheParamByMTssid(reqParam);
        if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
            vo=gson.fromJson( gson.toJson(rr.getData()),GetMCCacheParamByMTssidVO.class);
            if (null!=vo){
                result.setData(vo);
                changeResultToSuccess(result);
                return;
            }
            LogUtil.intoLog(this.getClass(),"getMCCacheParamByMTssid请求meetingControl.getMCCacheParamByMTssid___请求成功");
        }else {
            String msg=rr.getMessage()==null?"":rr.getMessage();
            LogUtil.intoLog(this.getClass(),"getMCCacheParamByMTssid请求meetingControl.getMCCacheParamByMTssid___请求失败");
        }
        return;
    }

    public void getTDCacheParamByMTssid(RResult result,ReqParam<GetTDCacheParamByMTssidParam_out> param){
        GetTDCacheParamByMTssidParam_out out = param.getParam();
        if (null==out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return;
        }
        String mtssid=out.getMtssid();
        String userssid=out.getUserssid();
        if (StringUtils.isBlank(mtssid)||StringUtils.isBlank(userssid)){
            LogUtil.intoLog(this.getClass(),"getTDCacheParamByMTssid参数为空mtssid____");
            result.setMessage("参数为空");
            return ;
        }

        ReqParam reqParam=new ReqParam();
        out.setMcType(MCType.AVST);
        out.setMtssid(mtssid);
        out.setUserssid(userssid);
        reqParam.setParam(out);
        RResult rr = meetingControl.getTDCacheParamByMTssid(reqParam);
        if (null!=rr&&rr.getActioncode().equals(Code.SUCCESS.toString())){
            GetTDCacheParamByMTssidVO vo=gson.fromJson(gson.toJson(rr.getData()),GetTDCacheParamByMTssidVO.class );
            result.setData(vo);
            changeResultToSuccess(result);
            LogUtil.intoLog(this.getClass(),"getTDCacheParamByMTssid请求__成功");
        }else {
            LogUtil.intoLog(this.getClass(),"getTDCacheParamByMTssid请求__出错");
        }
        return;
    }



    public void getClient(RResult rresult,ReqParam param){
        RResult zk_rr = zkControl.getControlInfoAll();
        List<ControlInfoParamVO> list=new ArrayList<>();
        if (null != zk_rr && zk_rr.getActioncode().equals(Code.SUCCESS.toString())) {
            list= gson.fromJson(gson.toJson(zk_rr.getData()), new TypeToken<List<ControlInfoParamVO>>(){}.getType());
            if (null!=list&&list.size()>0){
                rresult.setData(list);
                changeResultToSuccess(rresult);
                return;
            }
        }else{
            LogUtil.intoLog(this.getClass(),"getClient请求__出错");
        }
          return;
    }


}
