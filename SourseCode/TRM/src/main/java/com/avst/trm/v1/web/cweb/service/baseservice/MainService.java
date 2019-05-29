package com.avst.trm.v1.web.cweb.service.baseservice;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.*;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_caseMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_templateMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_userinfoMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.web.cweb.req.basereq.GetHomeParam;
import com.avst.trm.v1.web.cweb.req.basereq.UpdateServerconfigParam;
import com.avst.trm.v1.web.cweb.req.basereq.UserloginParam;
import com.avst.trm.v1.web.cweb.vo.basevo.GetHomeVO;
import com.avst.trm.v1.web.cweb.vo.basevo.GetServerconfigVO;
import com.avst.trm.v1.web.cweb.vo.basevo.UpdateServerconfigVO;
import com.avst.trm.v1.web.cweb.vo.basevo.UserloginVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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



    @Value("${spring.images.filePath}")
    private String imagesfilePath;
    @Value("${upload.basepath}")
    private String uploadbasepath;

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
            System.out.println("系统异常--"+type);
            result.setMessage("系统异常");
            return;
        }
        userloginVO.setType(type);


        String loginaccount1=userloginParam.getLoginaccount();
        String password1=userloginParam.getPassword();
        if (StringUtils.isBlank(loginaccount1)||StringUtils.isBlank(password1)){
            System.out.println("账号:"+loginaccount1+"密码:"+password1+"不能为空--");
            result.setMessage("账号密码不能为空");
            return;
        }

        //检查用户登陆
        EntityWrapper ew=new EntityWrapper();
        ew.eq("loginaccount",loginaccount1);
        List<AdminAndWorkunit> users= base_admininfoMapper.getAdminListAndWorkunit(ew);
        if (null!=users&&users.size()>0){
            if (users.size()==1){
                AdminAndWorkunit user=users.get(0);
                String loginaccount=user.getLoginaccount().trim();//账号
                String password=user.getPassword().trim();//密码
                Integer adminbool=user.getAdminbool();//状态

                if (!password.equals(password1.trim())){
                    System.out.println("账户:"+loginaccount1+"用户密码不正确--"+password1);
                    result.setMessage("密码错误");
                    return;
                }
                if (StringUtils.isNotBlank(loginaccount)&&loginaccount.equals(loginaccount1.trim())&&StringUtils.isNotBlank(password)&&password.equals(password1.trim())){
                    if (null!=adminbool&&adminbool!=1){
                        System.out.println("账户:"+loginaccount1+"用户状态:"+adminbool+"--");
                        result.setMessage("用户状态异常");
                        return;
                    }
                    //登陆成功
                    System.out.println("账户:"+loginaccount1+"登陆成功--");
                    result.setMessage("登陆成功");
                    //修改最后一次登陆时间
                    user.setLastlogintime(new Date());
                    int updateById_bool=base_admininfoMapper.updateById(user);
                    System.out.println("updateById_bool--"+updateById_bool);

                    //session存储
                    httpSession.setAttribute(Constant.MANAGE_CLIENT,user);
                    result.setData(userloginVO);
                    changeResultToSuccess(result);
                    return;
                }
            }else{
                System.out.println("多个用户异常--"+loginaccount1);
                result.setMessage("系统异常");
                return;
            }
        }else{
            System.out.println("用户不存在--"+loginaccount1);
            result.setMessage("没有找到该用户");
            return;
        }
        return;
    }



    public void userloginout(RResult result,ReqParam param,HttpSession session){
        if (null!=session.getAttribute(Constant.MANAGE_CLIENT)){
            session.removeAttribute(Constant.MANAGE_CLIENT);
            System.out.println("登出成功");
            result.setMessage("登出成功");
        }
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
                String uploadpath=uploadbasepath;
                String savePath=imagesfilePath;
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
                        System.out.println("删除原有客户logo:"+oldfilepath);
                    }
                }


                String oldfilename=multipartfile.getOriginalFilename();
                String suffix =oldfilename.substring(oldfilename.lastIndexOf(".") + 1);
                String filename = DateUtil.getSeconds()+"."+suffix;

                String realurl = OpenUtil.createpath_fileByBasepath(savePath, filename);
                System.out.println("客户端logo真实地址："+realurl);
                multipartfile.transferTo(new File(realurl));
                String downurl =uploadpath+OpenUtil.strMinusBasePath(qg, realurl) ;
                System.out.println("客户端logo下载地址："+downurl);


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
                        System.out.println("filesaveupdate_bool__"+filesaveupdate_bool);
                    }else{
                        //新增
                        base_filesave.setSsid(OpenUtil.getUUID_32());
                        int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                        System.out.println("filesaveinsert_bool__"+filesaveinsert_bool);
                        System.out.println("新增的文件ssid"+base_filesave.getSsid());
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
        System.out.println("updateById_bool"+updateById_bool);
        updateServerconfigVO.setBool(updateById_bool);
        result.setData(updateServerconfigVO);
        if (updateById_bool<1){
            result.setMessage("修改异常");
            return;
        }
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
                getServerconfigVO.setServerconfigAndFilesave(serverconfig);
                result.setData(getServerconfigVO);
                changeResultToSuccess(result);
                return;
            }else{
                System.out.println("多个系统配置异常--");
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

        String years=null;
        Integer yearstype=getHomeParam.getYearstype();
        if (null==yearstype){
            yearstype=1;
        }
        if (yearstype==1){
            years=df.format(new Date());
        }else if (yearstype==2){
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            Date y = c.getTime();
            years=df.format(y);
        }

        Integer record_num=police_recordMapper.selectCount(null);
        Integer case_num= police_caseMapper.selectCount(null);
        Integer template_num=police_templateMapper.selectCount(null);//模板数量
        Integer userinfo_num=police_userinfoMapper.selectCount(null);




        EntityWrapper recordparam1=new EntityWrapper();
        recordparam1.where(" date_format(createtime,'%Y')={0}",years);
        recordparam1.eq("recordbool",1);//进行中
        Integer record_finishnum_y = police_recordMapper.selectCount(recordparam1);

        EntityWrapper recordparam2=new EntityWrapper();
        recordparam2.eq("recordbool",2);//已完成的
        recordparam2.where(" date_format(createtime,'%Y')={0}",years);
        Integer record_unfinishnum_y=police_recordMapper.selectCount(recordparam2);

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

        getHomeVO.setDq_y(years);
        result.setData(getHomeVO);
        changeResultToSuccess(result);
        return;
    }
}
