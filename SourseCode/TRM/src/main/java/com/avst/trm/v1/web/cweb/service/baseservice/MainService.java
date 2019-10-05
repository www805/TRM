package com.avst.trm.v1.web.cweb.service.baseservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.datasourse.base.entity.*;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetToOutFlushbonadingListParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.web.cweb.cache.KeywordCache;
import com.avst.trm.v1.web.cweb.req.basereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.vo.basevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckKeywordVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("mainService")
public class MainService extends BaseService {

    private Gson gson = new Gson();

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    @Autowired
    private Base_nationalityMapper base_nationalityMapper;

    @Autowired
    private Base_nationalMapper base_nationalMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Police_templateMapper police_templateMapper;

    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

    @Autowired
    private Base_keywordMapper base_keywordMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private EquipmentControl equipmentControl;

    public InitVO initClient(InitVO initvo){
        return  CommonCache.getinit_CLIENT();
    }

    public void  userlogin(RResult result, ReqParam<UserloginParam> param, HttpServletRequest request){
        UserloginVO userloginVO=new UserloginVO();
        String type= CommonCache.getCurrentServerType();

        //请求参数转换
        UserloginParam userloginParam= param.getParam();
        if (null==userloginParam){
            result.setMessage("参数为空");
            return;
        }

        if (StringUtils.isBlank(type)){
            LogUtil.intoLog(this.getClass(),"系统异常--"+type);
            result.setMessage("系统异常");
            return;
        }
        userloginVO.setType(type);


        String loginaccount1=userloginParam.getLoginaccount();
        String password1=userloginParam.getPassword();
        if (StringUtils.isBlank(loginaccount1)||StringUtils.isBlank(password1)){
            LogUtil.intoLog(this.getClass(),"账号:"+loginaccount1+"密码:"+password1+"不能为空--");
            result.setMessage("账号密码不能为空");
            return;
        }

        Subject subject =  SecurityUtils.getSubject();

        //检查用户登录
        EntityWrapper ew=new EntityWrapper();
        ew.eq("BINARY  loginaccount",loginaccount1);//BINARY区分大小写
        List<AdminAndWorkunit> users= base_admininfoMapper.getAdminListAndWorkunit(ew);
        if (null!=users&&users.size()>0){
            if (users.size()==1){
                AdminAndWorkunit user=users.get(0);
                String loginaccount=user.getLoginaccount().trim();//账号
                String password=user.getPassword().trim();//密码
                Integer adminbool=user.getAdminbool();//状态

                if (!password.equals(password1.trim())){
                    LogUtil.intoLog(this.getClass(),"账户:"+loginaccount1+"用户密码不正确--"+password1);
                    result.setMessage("密码错误");
                    return;
                }
                if (StringUtils.isNotBlank(loginaccount)&&loginaccount.equals(loginaccount1.trim())&&StringUtils.isNotBlank(password)&&password.equals(password1.trim())){
                    if (null!=adminbool&&adminbool!=1){
                        LogUtil.intoLog(this.getClass(),"账户:"+loginaccount1+"用户状态:"+adminbool+"--");
                        result.setMessage("用户状态异常");
                        return;
                    }



                    subject.login( new UsernamePasswordToken(loginaccount, password,false));   //完成登录
                    LogUtil.intoLog(this.getClass(),"用户是否登录："+subject.isAuthenticated());
                    if(!subject.isPermitted("userlogin")&&subject.isAuthenticated()) {
                        result.setMessage("不好意思~您没有权限登录，请联系管理员");
                        return;
                    }


                    if (null!=user.getTemporaryaskbool()&&user.getTemporaryaskbool()==1){
                        result.setMessage("临时询问人不可登录");
                        return;
                    }

                    userloginVO.setSsid(user.getSsid());

                    //检测是否为第一次登陆
                    Integer firstloginbool=-1;
                    if (null==user.getLastlogintime()){
                        firstloginbool=1;//需要初始化密码
                        userloginVO.setFirstloginbool(firstloginbool);

                        result.setData(userloginVO);
                        result.setMessage("密码需要重置");
                        LogUtil.intoLog(this.getClass(),"账户:"+loginaccount1+"需要重置密码--");
                        return;
                    }

                    //session存储
                    request.getSession().setAttribute(Constant.MANAGE_CLIENT,user);


                    //登录成功
                    LogUtil.intoLog(this.getClass(),"账户:"+loginaccount1+"登录成功--");
                    result.setMessage("登录成功");





                    //修改最后一次登录时间
                    user.setLastlogintime(new Date());
                    int updateById_bool=base_admininfoMapper.updateById(user);
                    LogUtil.intoLog(this.getClass(),"updateById_bool--"+updateById_bool);


                    result.setData(userloginVO);
                    changeResultToSuccess(result);
                    return;
                }else {
                    LogUtil.intoLog(this.getClass(),"登录异常了--账户"+loginaccount1+"--密码--"+password);
                }
            }else{
                LogUtil.intoLog(this.getClass(),"多个用户异常--"+loginaccount1);
                result.setMessage("系统异常");
                return;
            }
        }else{
            LogUtil.intoLog(this.getClass(),"用户不存在--"+loginaccount1);
            result.setMessage("没有找到该用户");
            return;
        }
        return;
    }



    public void userloginout(RResult result,ReqParam param,HttpSession session){
            session.setAttribute(Constant.MANAGE_CLIENT,null);
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            LogUtil.intoLog(this.getClass(),"退出成功");
            result.setMessage("退出成功");
        changeResultToSuccess(result);
        return;
    }

    public  void updateServerconfig(RResult result, ReqParam param, MultipartFile multipartfile){
        UpdateServerconfigVO updateServerconfigVO=new UpdateServerconfigVO();
        String Stringparam=(String)param.getParam();
        //请求参数转换
        UpdateServerconfigParam updateServerconfigParam=gson.fromJson(Stringparam, UpdateServerconfigParam.class);
        if (null==updateServerconfigParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==updateServerconfigParam.getSsid()){
            result.setMessage("参数为空");
            return;
        }

        boolean isip = OpenUtil.isIp(updateServerconfigParam.getServerip());
        if(isip == false){
            result.setMessage("设备IP不是一个正确的IP");
            return;
        }

        //old数据
        EntityWrapper ew=new EntityWrapper();
        ew.eq("b.ssid",updateServerconfigParam.getSsid());
        List<ServerconfigAndFilesave> list=base_serverconfigMapper.getServerconfig(ew);
        ServerconfigAndFilesave serverconfig=new ServerconfigAndFilesave();
        if (null!=list&&list.size()==1){
            serverconfig=gson.fromJson(gson.toJson(list.get(0)), ServerconfigAndFilesave.class);
        }

        String client_filesavessid=serverconfig.getClient_filesavessid();
        if (null!=multipartfile){
            try {

                String oldfilepath=serverconfig.getClient_realurl();//旧地址
                String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
                String savePath=PropertiesListenerConfig.getProperty("spring.images.filePath");
                String qg=PropertiesListenerConfig.getProperty("file.qg");

                //D:/trmfile/upload/server/{sortnum}/2019/4/13  拼接地址
                String getServerconfig=serverconfig.getAuthorizesortnum()==null?null:serverconfig.getAuthorizesortnum()+"/";
                if (StringUtils.isNotBlank(getServerconfig)){
                    savePath+=getServerconfig;
                }else{
                    savePath+="default/";
                }

                if (StringUtils.isNotBlank(oldfilepath)){
                    File oldfile=new File(oldfilepath);
                    if (oldfile.exists()) {
                        oldfile.delete();
                        LogUtil.intoLog(this.getClass(),"删除原有客户logo:"+oldfilepath);
                    }
                }


                String oldfilename=multipartfile.getOriginalFilename();
                String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                String filename = DateUtil.getSeconds()+"."+suffix;

                String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                LogUtil.intoLog(this.getClass(),"客户端logo真实地址："+realurl);
                multipartfile.transferTo(new File(realurl));
                String downurl =uploadpath+OpenUtil.strMinusBasePath(qg, realurl) ;
                LogUtil.intoLog(this.getClass(),"客户端logo下载地址："+downurl);


                if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                    Base_filesave base_filesave=new Base_filesave();
                    base_filesave.setDatassid(serverconfig.getSsid());
                    base_filesave.setUploadfilename(oldfilename);
                    base_filesave.setRealfilename(filename);
                    base_filesave.setRecordrealurl(realurl);
                    base_filesave.setRecorddownurl(downurl);
                    if (StringUtils.isNotBlank(oldfilepath)){
                        //修改
                        EntityWrapper filesaveparam = new EntityWrapper();
                        filesaveparam.eq("ssid",client_filesavessid);
                        int filesaveupdate_bool=base_filesaveMapper.update(base_filesave,filesaveparam);
                        LogUtil.intoLog(this.getClass(),"filesaveupdate_bool__"+filesaveupdate_bool);
                    }else{
                        //新增
                        base_filesave.setSsid(OpenUtil.getUUID_32());
                        int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                        LogUtil.intoLog(this.getClass(),"filesaveinsert_bool__"+filesaveinsert_bool);
                        LogUtil.intoLog(this.getClass(),"新增的文件ssid"+base_filesave.getSsid());
                        client_filesavessid=base_filesave.getSsid();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //修改配置数据
        EntityWrapper serverconfigparam = new EntityWrapper();
        serverconfigparam.eq("ssid",updateServerconfigParam.getSsid());
        updateServerconfigParam.setClient_filesavessid(client_filesavessid);
        int updateById_bool=base_serverconfigMapper.update(updateServerconfigParam,serverconfigparam);//没有任何需要修改值的时候会报错
        LogUtil.intoLog(this.getClass(),"updateById_bool"+updateById_bool);
        updateServerconfigVO.setBool(updateById_bool);
        result.setData(updateServerconfigVO);
        if (updateById_bool<1){
            result.setMessage("修改异常");
            return;
        }
        //清空导航栏缓存
        AppCache.delAppCacheParam();
        changeResultToSuccess(result);
        return;
    }

    public void getServerconfig(RResult result,ReqParam param){
        GetServerconfigVO getServerconfigVO=new GetServerconfigVO();

        EntityWrapper ew=new EntityWrapper();
        ew.eq("type","police");

        List<ServerconfigAndFilesave> list=base_serverconfigMapper.getServerconfig(ew);
        if (null!=list&&list.size()>0){

            if (list.size()==1){
                String myIP = NetTool.getMyIP();
                ServerconfigAndFilesave serverconfig=gson.fromJson(gson.toJson(list.get(0)), ServerconfigAndFilesave.class);
                serverconfig.setSyslogo_downurl("http://" + myIP + serverconfig.getSyslogo_downurl());
                serverconfig.setClient_downurl("http://" + myIP + serverconfig.getClient_downurl());
                getServerconfigVO.setServerconfigAndFilesave(serverconfig);
                result.setData(getServerconfigVO);
                changeResultToSuccess(result);
                return;
            }else{
                LogUtil.intoLog(this.getClass(),"多个系统配置异常--");
                result.setMessage("系统异常");
                return;
            }
        }else {
            result.setMessage("系统异常");
        }
        return;
    }

    public void getNationalitys(RResult result, ReqParam param){
        List<Base_nationality> list=base_nationalityMapper.selectList(null);
        result.setData(list);
        changeResultToSuccess(result);
        return;
    }

    public void getNationals(RResult result, ReqParam param){
        List<Base_national> list=base_nationalMapper.selectList(null);
        result.setData(list);
        changeResultToSuccess(result);
        return;
    }

    public void  getHome(RResult result, ReqParam<GetHomeParam> param, HttpSession session){
        GetHomeVO vo=new GetHomeVO();

        GetHomeParam getHomeParam=param.getParam();
        if (null==getHomeParam){
            result.setMessage("参数为空");
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
        Calendar c = Calendar.getInstance();

        String years=getHomeParam.getYearstype();
        System.out.println(years);
        if (!StringUtils.isNotBlank(years)){
            years=df.format(new Date());
        }

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




        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
        vo.setSqEntity(getSQEntity);


        //授权功能
        List gnArrayList = new ArrayList();
        String gnlist = getSQEntity.getGnlist();
        String[] strings = gnlist.split("\\|");
        if (null != strings && strings.length > 0) {

            for (int i = 0; i < strings.length; i++) {
                String str = strings[i];
                if("asr".equals(str)){
                    gnArrayList.add(SQGN.asr);
                }else if("fd".equals(str)){
                    gnArrayList.add(SQGN.fd);
                }else if("ph".equals(str)){
                    gnArrayList.add(SQGN.ph);
                }else if("record".equals(str)){
                    gnArrayList.add(SQGN.record);
                }else if("tts".equals(str)){
                    gnArrayList.add(SQGN.tts);
                }
            }
        }
        vo.setSqgnList(gnArrayList);

        vo.setDq_y(years);



        Integer stateSQ=-1;
        if (null!=CommonCache.getServerSQCode()&&!CommonCache.getServerSQCode().equals("-1")&&CommonCache.clientSQbool)
        {
            stateSQ=1;//系统正常
        }
        vo.setStateSQ(stateSQ);

        //获取首页视频地址
        String liveurl=null;
        ReqParam<GetToOutFlushbonadingListParam> param_ = new ReqParam<>();
        GetToOutFlushbonadingListParam listParam = new GetToOutFlushbonadingListParam();
        listParam.setFdType(FDType.FD_AVST);
        param_.setParam(listParam);
        RResult result_ = equipmentControl.getToOutDefaulturl(param_);
        if (null != result_ && result_.getActioncode().equals(Code.SUCCESS.toString())) {
            liveurl= String.valueOf(result_.getData());
        }else{
            LogUtil.intoLog(this.getClass(),"请求equipmentControl.getToOutDefaulturl__出错");
        }
        vo.setLiveurl(liveurl);

        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }



    public void getAdminList(RResult result, ReqParam<GetAdminListParam> param){
        GetAdminListParam getAdminListParam=param.getParam();
        if (null==getAdminListParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper adminparam=new EntityWrapper();
        adminparam.eq("a.adminbool",1);//正常人
        adminparam.orderBy("a.registerTime",false);
        List<AdminAndWorkunit> adminList=base_admininfoMapper.getAdminListAndWorkunit(adminparam);
        if (null!=adminList&&adminList.size()>0){
            result.setData(adminList);
            changeResultToSuccess(result);
        }
        return;
    }

    public void getDefaultMtModelssid(RResult result,ReqParam param){
        String modelssid=null;
        Base_type base_type=new Base_type();
        base_type.setType(CommonCache.getCurrentServerType());
        base_type=base_typeMapper.selectOne(base_type);
        if (null!=base_type){
            modelssid=base_type.getMtmodelssid();
            result.setData(modelssid);
            changeResultToSuccess(result);
            LogUtil.intoLog(this.getClass(),"获取到默认的会议模板ssid__"+modelssid);
        }
        return;
    }

    public void getWorkunits(RResult result,ReqParam param){
        try {
            EntityWrapper ew=new EntityWrapper();
            List<Police_workunit> list=police_workunitMapper.selectList(ew);
            if (null!=list&&list.size()>0){
                result.setData(list);
            }
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePassWord(RResult result, ReqParam<updatePassWordParam> param) {

        updatePassWordParam paramParam = param.getParam();

        if (null == paramParam) {
            result.setMessage("参数为空");
            return;
        }

        if (StringUtils.isBlank(paramParam.getSsid())) {
            result.setMessage("ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(paramParam.getOldpassword())) {
            result.setMessage("旧密码不能为空");
            return;
        }
        if (StringUtils.isBlank(paramParam.getNewpassword())) {
            result.setMessage("新密码不能为空");
            return;
        }
        if (StringUtils.isBlank(paramParam.getPassword())) {
            result.setMessage("确认密码不能为空");
            return;
        }
        if (!paramParam.getNewpassword().equals(paramParam.getPassword())) {
            result.setMessage("两次确认密码必须一样");
            return;
        }



        //查询旧密码是否正确
        Base_admininfo base_admininfo = new Base_admininfo();
        base_admininfo.setSsid(paramParam.getSsid());
        base_admininfo.setPassword(paramParam.getOldpassword());
        Base_admininfo baseAdmininfo = base_admininfoMapper.selectOne(base_admininfo);

        if (null == baseAdmininfo) {
            result.setMessage("旧密码错误");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        ew.eq("ssid", paramParam.getSsid());

        Base_admininfo admininfo = new Base_admininfo();
        admininfo.setPassword(paramParam.getNewpassword());

        Integer firstloginbool=paramParam.getFirstloginbool();
        if (null!=firstloginbool&&firstloginbool==1){
            //修改最后登录密码
            admininfo.setLastlogintime(new Date());
        }

        Integer update = base_admininfoMapper.update(admininfo, ew);
        if (update != 1) {
            result.setMessage("修改错误");
        }

        result.setData(update);
        changeResultToSuccess(result);
    }

    public void updatePersonInfo(RResult result, ReqParam<updatePersonInfoParam> param, HttpServletRequest request) {

        updatePersonInfoParam paramParam = param.getParam();

        if (null == paramParam) {
            result.setMessage("参数为空");
            return;
        }

        if (StringUtils.isBlank(paramParam.getSsid())) {
            result.setMessage("ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(paramParam.getUsername())) {
            result.setMessage("用户名称不能为空");
            return;
        }
        if (StringUtils.isBlank(paramParam.getWorkunitssid())) {
            result.setMessage("工作单位不能为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        ew.eq("ssid", paramParam.getSsid());

        Base_admininfo admininfo = new Base_admininfo();
        admininfo.setUsername(paramParam.getUsername());
        admininfo.setWorkunitssid(paramParam.getWorkunitssid());

        Integer update = base_admininfoMapper.update(admininfo, ew);
        if (update != 1) {
            result.setMessage("修改错误");
        } else {
            //重新查询并且更新session
            EntityWrapper session_ew = new EntityWrapper();
            session_ew.eq("a.ssid", paramParam.getSsid());
            List<AdminAndWorkunit> users= base_admininfoMapper.getAdminListAndWorkunit(session_ew);
            if (null!=users&&users.size()==1){
                AdminAndWorkunit user=users.get(0);
                request.getSession().setAttribute(Constant.MANAGE_CLIENT, user);
            }
        }

        result.setData(update);
        changeResultToSuccess(result);
    }

    public void getNavList(RResult result) {
        String nav_file_name=PropertiesListenerConfig.getProperty("nav.file.name");

        AppCacheParam cacheParam = AppCache.getAppCacheParam();
        String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";
        if(null == cacheParam.getData()){
            String myIP = NetTool.getMyIP();
            FileInputStream fis = null;
            try {
                Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);

                if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
                    Base_filesave filesaveSyslogo = new Base_filesave();
                    filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
                    Base_filesave syslogo = base_filesaveMapper.selectOne(filesaveSyslogo);
                    if (null!=syslogo){
                        cacheParam.setSyslogoimage("http://" + myIP + syslogo.getRecorddownurl());
                    }
                }

                if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
                    Base_filesave filesaveClientlogo = new Base_filesave();
                    filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
                    Base_filesave clientlogo = base_filesaveMapper.selectOne(filesaveClientlogo);
                    if (null!=clientlogo){
                        cacheParam.setClientimage("http://" + myIP + clientlogo.getRecorddownurl());
                    }
                }

                fis = new FileInputStream(path);

                Yaml yaml = new Yaml();
                Map<String,Object> map = yaml.load(fis);

                String cwebFile=PropertiesListenerConfig.getProperty("nav.file.client");
                String application_name=PropertiesListenerConfig.getProperty("spring.application.name");

                Map<String,Object> avstYml = (Map<String, Object>) map.get(application_name);
                Map<String,Object> fileYml = (Map<String, Object>) avstYml.get(cwebFile);
                Map<String,Object> zkYml = (Map<String, Object>) map.get("zk");
                Map<String,Object> guidepage = (Map<String, Object>) zkYml.get("guidepage");
                String guidepageUrl = (String) guidepage.get("url");
                fileYml.put("bottom", map.get("bottom"));
                String hostAddress = NetTool.getMyIP();

                cacheParam.setData(fileYml);
                cacheParam.setGuidepageUrl("http://" + hostAddress + guidepageUrl);

            } catch (IOException e) {
                LogUtil.intoLog(4, this.getClass(), "没找到外部配置文件：" + path);
            }finally {
                if(null != fis){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        result.setData(cacheParam);
        changeResultToSuccess(result);
    }


    public  void checkKeyword(RResult result, ReqParam<CheckKeywordParam> param){
        long starttime=new Date().getTime();

        CheckKeywordVO vo=new CheckKeywordVO();
        CheckKeywordParam checkKeywordParam=param.getParam();
        if(null==checkKeywordParam){
            result.setMessage("参数为空");
            return;
        }

        String txt=checkKeywordParam.getTxt();
        String defaultstyle="color:#fff;background-color:red";//默认样式
        if (StringUtils.isNotBlank(txt)){

            //2.获取ansj列表（二次过滤）
            List<String> word=getkeywords(txt);
            if (null!=word&&word.size()>0){
                for (String text : word) {
                    if (txt.indexOf(text)>=0){
                        String replacetext= "<text style='"+defaultstyle+"'>"+text+"</text>";
                        txt=txt.replaceAll(text,replacetext);
                    }
                }
            }


            //开始检测文本
            //1、获取关键字列表(一次过滤)
            List<Base_keyword> keywords= KeywordCache.getKeywordList();
            if (null!=keywords&&keywords.size()>0)
            {
                for (Base_keyword keyword : keywords) {
                    String keywordtext=keyword.getText();
                    if (txt.indexOf(keywordtext)>=0){
                         String color=keyword.getColor()==null?"":keyword.getColor();
                         String backgroundcolor=keyword.getBackgroundcolor()==null?"":keyword.getBackgroundcolor();
                         String replacetext=keyword.getReplacetext();
                         Integer shieldbool=keyword.getShieldbool(); // 是否屏蔽：1屏蔽/-1不屏蔽；默认-1


                        if(StringUtils.isNotBlank(color)||StringUtils.isNotBlank(backgroundcolor)){
                            defaultstyle="color:"+color+";background-color:"+backgroundcolor; //指定样式
                        }
                        //需要屏蔽
                         if (null!=shieldbool&&shieldbool==1&&StringUtils.isNotBlank(replacetext)){
                                 replacetext="<text style='"+defaultstyle+"'>"+replacetext+"</text>";
                         }else {
                             //文本替换
                                 replacetext="<text style='"+defaultstyle+"'>"+keywordtext+"</text>";
                         }
                        txt=txt.replaceAll(keywordtext,replacetext);
                        LogUtil.intoLog(this.getClass(),"关键字过滤后的文本__"+txt);
                    }
                }
            }else {
                LogUtil.intoLog(this.getClass(),"关键字检测：检测列表为空__");
            }



            vo.setTxt(txt);
            result.setData(vo);
            changeResultToSuccess(result);
            long endtime=new Date().getTime();
           /* System.out.println("开始检测关键字当前时间___txt___"+txt+"__start___"+starttime+"____end___"+endtime+"___相差___"+(endtime-starttime));*/
            return;
        }else {
            LogUtil.intoLog(this.getClass(),"关键字检测：没有要检测的文本__");
        }

        return;
    }


    public void getPackdownList(RResult result,ReqParam<GetPackdownListParam> param){
        GetPackdownListVO vo=new GetPackdownListVO();
        GetPackdownListParam getPackdownListParam=param.getParam();
        if(null==getPackdownListParam){
            result.setMessage("参数为空");
            return;
        }

        String filename=getPackdownListParam.getFilename();
        EntityWrapper getpackdownList_ew=new EntityWrapper();
        if (StringUtils.isNotBlank(filename)){
            getpackdownList_ew.like("uploadfilename",filename);
        }
        getpackdownList_ew.eq("datassid","packdown8520");//插件标识packdown8520*/
        getpackdownList_ew.eq("filebool",1);//插件状态正常的插件
        int count = base_filesaveMapper.countgetfilesavePage(getpackdownList_ew);
        getPackdownListParam.setRecordCount(count);

        Page<Base_filesave> page=new Page<Base_filesave>(getPackdownListParam.getCurrPage(),getPackdownListParam.getPageSize());
        List<Base_filesave> list=base_filesaveMapper.getfilesavePage(page,getpackdownList_ew);
        vo.setPageparam(getPackdownListParam);

        if (null!=list&&list.size()>0){
            String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");
            for (Base_filesave base_filesave : list) {
                String downurl=base_filesave.getRecorddownurl();
                if (StringUtils.isNotBlank(downurl)){
                    base_filesave.setRecorddownurl(uploadbasepath+downurl);
                }
            }
        }

        vo.setPagelist(list);
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }

    public void uploadPackdown(RResult result,ReqParam param, MultipartFile multipartfile){
        if (null==multipartfile){
            result.setMessage("请先选择插件(exe)进行上传");
            return;
        }
        try {
            String savePath=PropertiesListenerConfig.getProperty("file.packdown");
            String qg=PropertiesListenerConfig.getProperty("file.qg");

            String originalfilename=multipartfile.getOriginalFilename();

            if(!originalfilename.endsWith(".exe")){
                result.setMessage("请先选择插件(exe)进行上传");
                return;
            }

            String realurl = OpenUtil.createpath_fileByBasepath(savePath, originalfilename);
            LogUtil.intoLog(this.getClass(),"插件上传的真实地址__："+realurl);
            multipartfile.transferTo(new File(realurl));
            String downurl =OpenUtil.strMinusBasePath(qg, realurl) ;
            LogUtil.intoLog(this.getClass(),"插件上传的下载地址__："+downurl);

            if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                //添加数据库
                Base_filesave base_filesave=new Base_filesave();
                base_filesave.setDatassid("packdown8520");
                base_filesave.setUploadfilename(originalfilename);
                base_filesave.setRealfilename(originalfilename);
                base_filesave.setRecordrealurl(realurl);
                base_filesave.setRecorddownurl(downurl);
                base_filesave.setSsid(OpenUtil.getUUID_32());
                int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                LogUtil.intoLog(this.getClass(),"filesaveinsert_bool__"+filesaveinsert_bool);
                if (filesaveinsert_bool>0){
                    result.setData(filesaveinsert_bool);
                    changeResultToSuccess(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void changeboolPackdown(RResult result,ReqParam<ChangeboolPackdownParam> param){
        ChangeboolPackdownParam changeboolPackdownParam=param.getParam();
        if(null==changeboolPackdownParam){
            result.setMessage("参数为空");
            return;
        }

        String ssid=changeboolPackdownParam.getSsid();
        Integer filebool=changeboolPackdownParam.getFilebool();
        LogUtil.intoLog(this.getClass(),"插件删除的ssid__delPackdown__"+ssid);
        if (StringUtils.isBlank(ssid)||null==filebool){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper filesaves_ew=new EntityWrapper();
        filesaves_ew.eq("ssid",ssid);
        List<Base_filesave> filesaves_=base_filesaveMapper.selectList(filesaves_ew);
        if (null!=filesaves_&&filesaves_.size()==1){
            Base_filesave base_filesave_=filesaves_.get(0);
            base_filesave_.setFilebool(filebool);
            //删除记录
            int base_filesaveMapper_update_bool=base_filesaveMapper.update(base_filesave_,filesaves_ew);
            LogUtil.intoLog(this.getClass(),"base_filesaveMapper_update_bool__"+base_filesaveMapper_update_bool);
            if (base_filesaveMapper_update_bool>0){
                result.setData(base_filesaveMapper_update_bool);
                changeResultToSuccess(result);
            }
        }else {
            LogUtil.intoLog(this.getClass(),"插件删除查询是否存在该文件：该记录不存在或者查询出多条");
        }
        return;
    }

    public static List<String> getkeywords(String text){
        List<String> keywords=new ArrayList<>();
        if (StringUtils.isNotBlank(text)){
            //词性过滤

            //词性筛选
            Set<String> expectedNature = new HashSet<String>();
            expectedNature.add("nr");//人名
            expectedNature.add("ns");//数组
            expectedNature.add("t");//时间词
            expectedNature.add("m");//数值
            Result result = ToAnalysis.parse(text);

            List<Term> terms = result.getTerms(); //拿到terms
            for(int i=0; i<terms.size(); i++) {
                String word = terms.get(i).getName(); //拿到词
                String natureStr = terms.get(i).getNatureStr(); //拿到词性
                if(expectedNature.contains(natureStr)) {
                    if ("m".equals(natureStr)) {
                        //检测电话号码
                        if (word.length() == 11) {
                            String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
                            Pattern p = Pattern.compile(regExp);
                            Matcher m = p.matcher(word);
                            if (m.matches()) {
                                keywords.add(word);
                            }
                        }
                        //检测身份证
                        if (word.length() == 18) {
                            keywords.add(word);
                        } else if (word.length() == 17) {
                            Term to = terms.get(i).to();
                            if ("x".equals(to.getName())) {
                                terms.get(i).merage(to);
                                to.setName(null);
                                keywords.add( terms.get(i).getName());
                            }
                        }
                    }else {
                        keywords.add(word);
                    }

                }
            }

            //检测车牌号
            String regEx_1="[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
            Pattern p_1 = Pattern.compile(regEx_1);
            Matcher m_1= p_1.matcher(text);
            while (m_1.find()) {
                keywords.add(m_1.group());
            }
        }
        return keywords;
    }


    public static void main(String[] args) {
        String text="昨天我叫刘德华，男，初中文化，电话号码是17534789453身份证号码是13282619771220503X户籍所在地萍乡市湘东区东关镇红星村粤B12345，粤B54321现住址萍乡市开发区平安小小区3栋3单元501车牌号粤B12345";
        List<String> texts=getkeywords(text);
        for (String s : texts) {
            System.out.println(s);
        }

    }
}
