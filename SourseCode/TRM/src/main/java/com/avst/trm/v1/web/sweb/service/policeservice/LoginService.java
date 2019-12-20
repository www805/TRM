package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.AppServiceCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.socketio.param.LoginConstant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admintoroleMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.LoginParam;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class LoginService extends BaseService {

    @Autowired
    private Base_admininfoMapper admininfoMapper;

    @Autowired
    private Base_admintoroleMapper base_admintoroleMapper;

    private Gson gson=new Gson();

    /***
     * 登录验证
     * @param result
     * @param loginParam
     */
    public void gotologin(RResult<AdminManage_session> result, HttpServletRequest request, HttpServletResponse response, LoginParam loginParam){
        String loginaccount=loginParam.getLoginaccount();
        String password=loginParam.getPassword();


        if(!StringUtils.isNotBlank(loginaccount) || !StringUtils.isNotBlank(password)){
            result.setMessage("用户名密码不能为空");
            LogUtil.intoLog(4,this.getClass(),"LogAction gotologin loginParam is null");
            return;
        }

        if(null==result){
            result=new RResult<AdminManage_session>();
        }
        try {

            //检查用户登录
            EntityWrapper ew=new EntityWrapper();
            ew.eq("BINARY  loginaccount",loginParam.getLoginaccount());//BINARY区分大小写
            LogUtil.intoLog(this.getClass(),ew.getSqlSegment()+"---------");
            List<AdminAndWorkunit> adminManage= admininfoMapper.getAdminListAndWorkunit(ew);

            if (null==adminManage||adminManage.size()<1){
                result.setMessage("未找到该用户");
                return;
            }

            Subject subject =  SecurityUtils.getSubject();

            if (null!=adminManage&&adminManage.size()>0){
                    if (adminManage.size()==1){
                        AdminAndWorkunit base_admininfo=adminManage.get(0);


                        String password1=base_admininfo.getPassword();
                        if (null==password1||!password.equals(password1)){
                            result.setMessage("请输入正确的密码");
                            return;
                        }
                        if (base_admininfo.getAdminbool()!=1){
                            result.setMessage("用户异常");
                            return;
                        }

                        boolean rememberpassword=loginParam.isRememberpassword();
                        LogUtil.intoLog(1,this.getClass(),"后台登录账号："+loginaccount+"__是否需要记住密码__"+rememberpassword);
                        subject.login( new UsernamePasswordToken(loginaccount, password,rememberpassword));   //完成登录
                        LogUtil.intoLog(1,this.getClass(),"用户是否登录："+subject.isAuthenticated(),base_admininfo.getUsername());
                      if(!subject.isPermitted("checklogin")&&subject.isAuthenticated()) {
                            result.setMessage("不好意思~您没有权限登录，请联系管理员");
                            subject.logout();
                            return;
                        }

                        if (null!=base_admininfo.getTemporaryaskbool()&&base_admininfo.getTemporaryaskbool()==1){
                            result.setMessage("临时询问人不可登录");
                            return;
                        }

                        EntityWrapper ewrole=new EntityWrapper();
                        ewrole.eq("ar.adminssid",base_admininfo.getSsid());
                        List<Base_role> roles = base_admintoroleMapper.getRolesByAdminSsid(ewrole);
                        if (null!=roles&&roles.size()>0){
                            base_admininfo.setRoles(roles);
                            for (Base_role role : roles) {
                                if (StringUtils.isNotEmpty(role.getSsid())&&role.getSsid().equals("role1")){//role1超管
                                    base_admininfo.setSuperrolebool(1);
                                    break;
                                }
                            }
                        }

                        request.getSession().setAttribute(Constant.MANAGE_WEB,base_admininfo);

                        if (rememberpassword){
                            Cookie server_loginaccount=new Cookie(LoginConstant.SERVER_LOGINACCOUNT,loginaccount);
                            server_loginaccount.setMaxAge(60*60*24*7);
                            server_loginaccount.setPath("/");
                            Cookie server_rememberme=new Cookie(LoginConstant.SERVER_REMEMBERME,"YES");
                            server_rememberme.setMaxAge(60*60*24*7);
                            server_rememberme.setPath("/");
                            response.addCookie(server_loginaccount);
                            response.addCookie(server_rememberme);
                        }else {
                            Cookie server_loginaccount=new Cookie(LoginConstant.SERVER_LOGINACCOUNT,null);
                            server_loginaccount.setMaxAge(0);
                            server_loginaccount.setPath("/");
                            Cookie server_rememberme=new Cookie(LoginConstant.SERVER_REMEMBERME,null);
                            server_rememberme.setMaxAge(0);
                            server_rememberme.setPath("/");
                            response.addCookie(server_loginaccount);
                            response.addCookie(server_rememberme);
                        }





                        //修改用户最后一次登录：服务器端是否需要判断第一次登录重置密码？
                       /* base_admininfo.setLastlogintime(new Date());*/
                        int updateById_bool=admininfoMapper.updateById(base_admininfo);
                        LogUtil.intoLog(this.getClass(),"updateById_bool__"+updateById_bool);
                        this.changeResultToSuccess(result);

                        //写入缓存用户
                        AdminManage_session admin=new AdminManage_session();
                        admin.setAdminbool(base_admininfo.getAdminbool());
                        admin.setLastlogintime(base_admininfo.getLastlogintime());
                        admin.setLoginaccount(base_admininfo.getLoginaccount());
                        admin.setWorkunitname(base_admininfo.getWorkunitssid());//这里没有去查工作单位，暂时不用这个参数
                        admin.setUsername(base_admininfo.getUsername());
                        admin.setUpdatetime(base_admininfo.getUpdatetime());
                        admin.setUnitsort(base_admininfo.getUnitsort());
                        admin.setSsid(base_admininfo.getSsid());
                        admin.setRegistertime(base_admininfo.getRegistertime());
                        CommonCache.setAdminManage_session(admin);


                    }else{
                        LogUtil.intoLog(3,this.getClass(),"系统异常--登录账号多个,登录名："+loginaccount,loginaccount);
                        result.setMessage("系统异常");
                        return;
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
        }
        return;
    }

    public void logout(RResult rResult, HttpServletRequest request) {
        this.changeResultToSuccess(rResult);
        AppServiceCache.delAppServiceCache();//清空logo导航栏缓存
        rResult.setMessage("退出成功");
        request.getSession().setAttribute(Constant.MANAGE_WEB, null);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
