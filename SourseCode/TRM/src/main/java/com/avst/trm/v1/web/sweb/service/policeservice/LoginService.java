package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.AppServiceCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.cweb.conf.CheckPasswordKey;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService extends BaseService {

    @Autowired
    private Base_admininfoMapper admininfoMapper;

    private Gson gson=new Gson();

    /***
     * 登录验证
     * @param result
     * @param loginParam
     */
    public void gotologin(RResult<AdminManage_session> result, HttpServletRequest request, LoginParam loginParam){
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


            EntityWrapper ew=new EntityWrapper();
            ew.eq("loginaccount", loginParam.getLoginaccount());

            LogUtil.intoLog(this.getClass(),ew.getSqlSegment()+"---------");
            List<Base_admininfo> adminManage=admininfoMapper.selectList(ew);
            if (null==adminManage||adminManage.size()<1){
                result.setMessage("未找到该用户");
                return;
            }

            Subject subject =  SecurityUtils.getSubject();

            if (null!=adminManage&&adminManage.size()>0){
                    if (adminManage.size()==1){
                        Base_admininfo base_admininfo=adminManage.get(0);


                        String password1=base_admininfo.getPassword();
                        if (null==password1||!password.equals(password1)){
                            result.setMessage("请输入正确的密码");
                            return;
                        }
                        if (base_admininfo.getAdminbool()!=1){
                            result.setMessage("用户异常");
                            return;
                        }


                        subject.login( new UsernamePasswordToken(loginaccount, password,false));   //完成登录
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

                        request.getSession().setAttribute(Constant.MANAGE_WEB,base_admininfo);

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
