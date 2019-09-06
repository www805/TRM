package com.avst.trm.v1.web.cweb.service.baseservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
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
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.web.cweb.req.basereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.vo.basevo.*;
import com.avst.trm.v1.web.cweb.vo.policevo.CheckKeywordVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
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

    public InitVO initClient(InitVO initvo){
        return  CommonCache.getinit_CLIENT();
    }

    public void  userlogin(RResult result, ReqParam<UserloginParam> param, HttpSession httpSession){
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
        if (subject.isAuthenticated()){
            subject.logout();
        }

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
                        subject.logout();
                        return;
                    }


                    if (null!=user.getTemporaryaskbool()&&user.getTemporaryaskbool()==1){
                        result.setMessage("临时询问人不可登录");
                        return;
                    }

                    //session存储
                    httpSession.setAttribute(Constant.MANAGE_CLIENT,user);

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
                ServerconfigAndFilesave serverconfig=gson.fromJson(gson.toJson(list.get(0)), ServerconfigAndFilesave.class);
                serverconfig.setSyslogo_downurl(serverconfig.getSyslogo_downurl());
                serverconfig.setClient_downurl(serverconfig.getClient_downurl());
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

    public void  getHome(RResult result, ReqParam<GetHomeParam> param){
        GetHomeVO getHomeVO=new GetHomeVO();

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



        Integer record_num=police_recordMapper.selectCount(null);
        Integer case_num= police_caseMapper.selectCount(null);
        Integer template_num=police_templateMapper.selectCount(null);//模板数量
        Integer userinfo_num=police_userinfoMapper.selectCount(null);




        EntityWrapper recordparam1=new EntityWrapper();
        recordparam1.where(" date_format(createtime,'%Y')={0}",years);
        recordparam1.eq("recordbool",1);//进行中
        Integer record_unfinishnum_y= police_recordMapper.selectCount(recordparam1);

        EntityWrapper recordparam2=new EntityWrapper();
        recordparam2.eq("recordbool",2);//已完成的
        recordparam2.where(" date_format(createtime,'%Y')={0}",years);
        Integer record_finishnum_y =police_recordMapper.selectCount(recordparam2);

        Integer case_startnum_y=police_caseMapper.getCase_startnum(years);//案件开始提讯数量
        Integer case_endnum_y=police_caseMapper.Getcase_endnum(years);//案件未开始提讯数量


        EntityWrapper recordparam3=new EntityWrapper();
        recordparam3.where(" date_format(createtime,'%Y')={0}",years);
        Integer record_num_y=police_recordMapper.selectCount(recordparam3);//笔录总数

        EntityWrapper caseparam=new EntityWrapper();
        caseparam.where(" date_format(occurrencetime,'%Y')={0}",years);
        Integer case_num_y=police_caseMapper.selectCount(caseparam);//案件总数

        EntityWrapper templateparam=new EntityWrapper();
        templateparam.where(" date_format(createtime,'%Y')={0}",years);
        Integer template_num_y=police_templateMapper.selectCount(templateparam);//模板总数

        EntityWrapper userinfoparam=new EntityWrapper();
        userinfoparam.where(" date_format(createtime,'%Y')={0}",years);
        Integer userinfo_num_y=police_userinfoMapper.selectCount(userinfoparam);//人员总数


        List<Integer> case_monthnum_y=police_caseMapper.getCase_monthnum_y(years);//12月案件
        List<Integer> record_monthnum_y=police_recordMapper.getRecord_monthnum_y(years);//12月笔录



        getHomeVO.setTemplate_num(template_num==null?0:template_num);//
        getHomeVO.setCase_num(case_num==null?0:case_num);//
        getHomeVO.setUserinfo_num(userinfo_num==null?0:userinfo_num);//
        getHomeVO.setRecord_num(record_num==null?0:record_num);//


        getHomeVO.setTemplate_num_y(template_num_y==null?0:template_num_y);
        getHomeVO.setCase_num_y(case_num_y==null?0:case_num_y);
        getHomeVO.setUserinfo_num_y(userinfo_num_y==null?0:userinfo_num_y);
        getHomeVO.setRecord_num_y(record_num_y==null?0:record_num_y);


        getHomeVO.setRecord_finishnum_y(record_finishnum_y==null?0:record_finishnum_y);
        getHomeVO.setRecord_unfinishnum_y(record_unfinishnum_y==null?0:record_unfinishnum_y);
        getHomeVO.setCase_startnum_y(case_startnum_y==null?0:case_startnum_y);
        getHomeVO.setCase_endnum_y(case_endnum_y==null?0:case_endnum_y);


        getHomeVO.setRecord_monthnum_y(record_monthnum_y);//
        getHomeVO.setCase_monthnum_y(case_monthnum_y);//

        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
        getHomeVO.setSqEntity(getSQEntity);

//        Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);
//        if (null != serverconfig) {
//            getHomeVO.setWorkdays(serverconfig.getWorkdays());
//        }

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
        getHomeVO.setSqgnList(gnArrayList);

        getHomeVO.setDq_y(years);
        result.setData(getHomeVO);
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
            AdminAndWorkunit user = (AdminAndWorkunit) request.getSession().getAttribute(Constant.MANAGE_CLIENT);
            user.setUsername(paramParam.getUsername());
            request.getSession().setAttribute(Constant.MANAGE_CLIENT, user);
        }

        result.setData(update);
        changeResultToSuccess(result);
    }

    public void getNavList(RResult result) {
        String nav_file_name=PropertiesListenerConfig.getProperty("nav.file.name");

        AppCacheParam cacheParam = AppCache.getAppCacheParam();
        String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";
        if(null == cacheParam.getData()){
            FileInputStream fis = null;
            try {
                Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);

                if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
                    Base_filesave filesaveSyslogo = new Base_filesave();
                    filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
                    Base_filesave syslogo = base_filesaveMapper.selectOne(filesaveSyslogo);
                    if (null!=syslogo){
                        cacheParam.setSyslogoimage(syslogo.getRecorddownurl());
                    }
                }

                if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
                    Base_filesave filesaveClientlogo = new Base_filesave();
                    filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
                    Base_filesave clientlogo = base_filesaveMapper.selectOne(filesaveClientlogo);
                    if (null!=clientlogo){
                        cacheParam.setClientimage(clientlogo.getRecorddownurl());
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

        CheckKeywordVO vo=new CheckKeywordVO();
        CheckKeywordParam checkKeywordParam=param.getParam();
        if(null==checkKeywordParam){
            result.setMessage("参数为空");
            return;
        }

        String txt=checkKeywordParam.getTxt();
        String defaultstyle="color:#fff;background-color:red";//默认样式
        if (StringUtils.isNotBlank(txt)){

            //开始检测文本
            //1、获取关键字列表
            EntityWrapper keywords_ew=new EntityWrapper();
            List<Base_keyword> keywords= base_keywordMapper.selectList(keywords_ew);
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
                vo.setTxt(txt);
                result.setData(vo);
                changeResultToSuccess(result);
                return;
            }else {
                LogUtil.intoLog(this.getClass(),"关键字检测：检测列表为空__");
            }
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


}
