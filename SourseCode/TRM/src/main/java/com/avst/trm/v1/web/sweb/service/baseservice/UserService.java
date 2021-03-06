package com.avst.trm.v1.web.sweb.service.baseservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.shiro.param.ShiroRealm;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admintorole;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admintoroleMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_workunitMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.cweb.conf.CheckPasswordKey;
import com.avst.trm.v1.web.sweb.req.basereq.ChangeboolUserParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.sweb.req.basereq.ResetPasswordParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetUserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Base_admintoroleMapper base_admintoroleMapper;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

    public void getUserList(RResult result,GetUserListParam param){
        GetUserListVO getUserListVO=new GetUserListVO();

        //请求参数组合
        EntityWrapper ew=new EntityWrapper();
        if (null!=param.getLoginaccount()){
            ew.like(true,"a.loginaccount",param.getLoginaccount().trim());
        }
        if (StringUtils.isNotBlank(param.getUsername())){
            ew.like(true,"a.username",param.getUsername().trim());
        }
        if (StringUtils.isNotBlank(param.getWorkunitssid())){
            ew.eq(true,"a.workunitssid",param.getWorkunitssid().trim());
        }
        if (StringUtils.isNotBlank(param.getRolessid())){
            ew.eq(true,"ar.rolessid",param.getRolessid().trim());
        }
        if (null!=param.getAdminbool()){
            ew.eq(true,"a.adminbool",param.getAdminbool());
        }
        if (null!=param.getTemporaryaskbool()){
            ew.eq(true,"a.temporaryaskbool",param.getTemporaryaskbool());
        }

        //筛选剔除已删除用户：
        ew.ne(true,"a.adminbool",-1);
        //筛选剔除临时询问用户：
        //ew.ne(true,"a.temporaryaskbool",1);

       int count = base_admininfoMapper.countgetUserList(ew);
       param.setRecordCount(count);

        ew.orderBy("a.superbool",false);
        ew.orderBy("a.temporaryaskbool",true);
       ew.orderBy("a.unitsort",true);
       ew.orderBy("a.registertime",false);
        ew.orderBy("a.lastlogintime",false);
       Page<AdminAndWorkunit> page=new Page<AdminAndWorkunit>(param.getCurrPage(),param.getPageSize());
       List<AdminAndWorkunit> list=base_admininfoMapper.getUserList(page,ew);
       getUserListVO.setPageparam(param);

       if (null!=list&&list.size()>0){
           //绑定状态正常的角色
           for (AdminAndWorkunit adminAndWorkunit : list) {
               EntityWrapper ewrole=new EntityWrapper();
               ewrole.eq("ar.adminssid",adminAndWorkunit.getSsid());
               List<Base_role> roles = base_admintoroleMapper.getRolesByAdminSsid(ewrole);
               if (null!=roles&&roles.size()>0){
                   adminAndWorkunit.setRoles(roles);
               }
           }
           getUserListVO.setPagelist(list);
       }
       result.setData(getUserListVO);
       changeResultToSuccess(result);
       return;
    }

    public void  getWorkunits(RResult result){
        try {
            List<Police_workunit> list=police_workunitMapper.selectList(null);
            if (null!=list&&list.size()>0){
                result.setData(list);
            }
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  changeboolUser(RResult result, ChangeboolUserParam param){
        String ssid=param.getSsid();
        Integer adminbool=param.getAdminbool();
        Integer temporaryaskbool=param.getTemporaryaskbool();

        if (StringUtils.isBlank(ssid)||(null==adminbool&&temporaryaskbool==null)){
            result.setMessage("参数为空");
            return;
        }

        try {
            EntityWrapper ew=new EntityWrapper();
            ew.eq("ssid",ssid);
            Base_admininfo admininfo=new Base_admininfo();
            if (null!=adminbool){
                admininfo.setAdminbool(adminbool);
            }
            if (null!=temporaryaskbool){
                admininfo.setTemporaryaskbool(temporaryaskbool);
            }
            int delete_bool= base_admininfoMapper.update(admininfo,ew);
            LogUtil.intoLog(this.getClass(),"delete_bool__"+delete_bool);
            if (delete_bool<1){
                result.setMessage("系统异常");
                return;
            }
            result.setData(delete_bool);
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void getUserBySsid(RResult result,String ssid){
        EntityWrapper ew=new EntityWrapper();
        ew.eq(true,"ssid",ssid);
        List<AdminAndWorkunit> list = base_admininfoMapper.selectList(ew);
        if (null==list||list.size()<1){
            result.setMessage("未找到该用户");
            return;
        }
        //查询角色
        if (null!=list&&list.size()>0){
            if (list.size()==1){
                AdminAndWorkunit adminAndWorkunit= gson.fromJson(gson.toJson(list.get(0)), AdminAndWorkunit.class);
                EntityWrapper ewrole=new EntityWrapper();
                ewrole.eq("ar.adminssid",adminAndWorkunit.getSsid());
                List<Base_role> roles = base_admintoroleMapper.getRolesByAdminSsid(ewrole);
                if (null!=roles&&roles.size()>0){
                    adminAndWorkunit.setRoles(roles);
                }

                //查找工作单位
                Police_workunit police_workunit=new Police_workunit();
                police_workunit.setSsid(adminAndWorkunit.getWorkunitssid());
                police_workunit=police_workunitMapper.selectOne(police_workunit);
                if (null!=police_workunit){
                    adminAndWorkunit.setWorkname(police_workunit.getWorkname());
                }


                result.setData(adminAndWorkunit);
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"系统异常：多个用户");
                result.setMessage("系统异常");
                return;
            }
        }
        return;
    }

    public void addUser(RResult result, AdminAndWorkunit param, HttpSession session){
        String loginaccount=param.getLoginaccount();
        if (StringUtils.isBlank(loginaccount)){
            result.setMessage("请输入登录账号");
            return;
        }

        EntityWrapper base_admininfos_param=new EntityWrapper();
        base_admininfos_param.eq("loginaccount",loginaccount);
        List<Base_admininfo> base_admininfos_=base_admininfoMapper.selectList(base_admininfos_param);
        if (null!=base_admininfos_&&base_admininfos_.size()>0){
            result.setMessage("登录账号已存在,请重新输入");
            return;
        }


        try {
            //新增用户
            Base_admininfo admininfo = gson.fromJson(gson.toJson(param), Base_admininfo.class);
            admininfo.setSsid(OpenUtil.getUUID_32());
            admininfo.setRegistertime(new Date());

            AdminAndWorkunit user = gson.fromJson(gson.toJson(session.getAttribute(Constant.MANAGE_WEB)), AdminAndWorkunit.class);

            admininfo.setCreator(user.getSsid());
            admininfo.setTemporaryaskbool(-1);//非临时询问人
            int insert_bool=base_admininfoMapper.insert(admininfo);
            LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
            if (insert_bool>0){
                //添加角色关联数据
                List<Base_role> roles=param.getRoles();
                if (null!=roles&&roles.size()>0){
                    for (Base_role role : roles) {
                        Base_admintorole admintorole=new Base_admintorole();
                        admintorole.setAdminssid(admininfo.getSsid());
                        admintorole.setRolessid(role.getSsid());
                        admintorole.setCreatetime(new Date());
                       int admintorole_insertbool = base_admintoroleMapper.insert(admintorole);
                        LogUtil.intoLog(this.getClass(),"admintorole_insertbool__"+admintorole_insertbool+"添加的角色为："+role.getRolename());
                    }
                }
                result.setData(insert_bool);
                changeResultToSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void updateUser(RResult result,AdminAndWorkunit param) {
        //新增用户
        EntityWrapper ew=new EntityWrapper();
        ew.eq("ssid",param.getSsid());
        if (StringUtils.isBlank(param.getSsid())){
            result.setMessage("参数为空");
            return;
        }

        String loginaccount=param.getLoginaccount();
        if (StringUtils.isBlank(loginaccount)){
            result.setMessage("请输入登录账号");
            return;
        }

        EntityWrapper base_admininfos_param=new EntityWrapper();
        base_admininfos_param.eq("loginaccount",loginaccount);
        base_admininfos_param.ne("ssid",param.getSsid());
        List<Base_admininfo> base_admininfos_=base_admininfoMapper.selectList(base_admininfos_param);
        if (null!=base_admininfos_&&base_admininfos_.size()>0){
            result.setMessage("登录账号已存在,请重新输入");
            return;
        }


        param.setUpdatetime(new Date());
        Base_admininfo base_admininfo = gson.fromJson(gson.toJson(param), Base_admininfo.class);
        int update_bool = base_admininfoMapper.update(base_admininfo,ew);
        LogUtil.intoLog(this.getClass(),"update_bool__" + update_bool);
        if (update_bool > 0) {
            //删除原有角色关联数据
            EntityWrapper ewadmintorole=new EntityWrapper();
            ewadmintorole.eq("adminssid",param.getSsid());
            int delete_bool=base_admintoroleMapper.delete(ewadmintorole);
            LogUtil.intoLog(this.getClass(),"delete_bool__"+delete_bool);
            //添加角色关联数据
            List<Base_role> roles = param.getRoles();
            if (null != roles && roles.size() > 0) {
                for (Base_role role : roles) {
                    Base_admintorole admintorole = new Base_admintorole();
                    admintorole.setAdminssid(base_admininfo.getSsid());
                    admintorole.setRolessid(role.getSsid());
                    admintorole.setCreatetime(new Date());
                    admintorole.setSsid(OpenUtil.getUUID_32());
                    int admintorole_insertbool = base_admintoroleMapper.insert(admintorole);
                    LogUtil.intoLog(this.getClass(),"admintorole_insertbool__" + admintorole_insertbool );
                }
            }
            ShiroRealm.reloadAuthorizing();
            result.setData(update_bool);
            changeResultToSuccess(result);
            return;
        }
    }

    public void resetPassword(RResult result, ResetPasswordParam param, MultipartFile multipartfile) {
        if (null == param) {
            result.setMessage("参数错误");
            return;
        }


        //获取用户
        String userssid = param.getUserssid();
        String init_password = param.getInit_password();
        if (StringUtils.isBlank(userssid)) {
            result.setMessage("未找到该用户");
            LogUtil.intoLog(3,this.getClass(),"重置密码验证用户__userssid__"+userssid);
            return;
        }
        EntityWrapper ew = new EntityWrapper();
        ew.eq(true, "ssid", userssid);
        List<Base_admininfo> list = base_admininfoMapper.selectList(ew);
        if (null == list || list.size() < 1) {
            result.setMessage("未找到该用户");
            LogUtil.intoLog(3,this.getClass(),"重置密码验证用户__user is null");
            return;
        }
        Base_admininfo admininfo=new Base_admininfo();
        if (null != list && list.size() == 1) {
            if (list.size() == 1) {
                if (null != admininfo) {
                    admininfo = list.get(0);
                }
            }
        }else {
            result.setMessage("系统异常");
            LogUtil.intoLog(3,this.getClass(),"重置密码验证用户__找到多个用户");
            return;
        }

        if (null != multipartfile) {
            String filename = multipartfile.getOriginalFilename();
            if (!filename.endsWith(".ini")) {
                result.setMessage("请上传后缀为ini的文件");
                return;
            }
            //收集验证数据
            Map<String, String> decryptionMap = new HashMap<>();
            decryptionMap.put("ssid", admininfo.getSsid());
            decryptionMap.put("loginaccount", admininfo.getLoginaccount());
            decryptionMap.put("registertime", String.valueOf(admininfo.getRegistertime().getTime()));

            String decryptiontext="";
            try {
                InputStream inputStream = multipartfile.getInputStream();
                BufferedReader bufread=null;
                InputStreamReader in = new InputStreamReader(inputStream,"utf-8");//字节流字符流转化的桥梁
                bufread = new BufferedReader(in);//以字符流方式读入
                try {
                    StringBuffer sb = new StringBuffer();
                    String read="";
                    while ((read = bufread.readLine()) != null) {
                        sb.append(read);
                    }
                    decryptiontext=sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    bufread.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean CheckKeybool = CheckPasswordKey.CheckKey(decryptiontext, decryptionMap);
            if (!CheckKeybool) {
                result.setMessage("key验证失败");
                LogUtil.intoLog(3,this.getClass(),"重置密码验证用户__key验证失败");
                return;
            }
        }

        //验证成功
        //开始修改重置用户
        EntityWrapper base_admininfos_param=new EntityWrapper();
        base_admininfos_param.eq("ssid",admininfo.getSsid());
        List<Base_admininfo> base_admininfos_=base_admininfoMapper.selectList(base_admininfos_param);
        if (null!=base_admininfos_&&base_admininfos_.size()==1){
            Base_admininfo base_admininfo = base_admininfos_.get(0);
           /* base_admininfo.setLastlogintime(null);*///重置后用户登录不需要再修改了
            base_admininfo.setPassword(init_password);
            int update_bool = base_admininfoMapper.update(base_admininfo,ew);
            LogUtil.intoLog(this.getClass(),"重置密码验证用户__update_bool__" + update_bool);
            if (update_bool>0){
                changeResultToSuccess(result);
                return;
            }
        }

        return;
    }



}
