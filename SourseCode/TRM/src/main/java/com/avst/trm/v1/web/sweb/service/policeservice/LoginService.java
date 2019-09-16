package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.LoginParam;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class LoginService extends BaseService {

    @Autowired
    private Base_admininfoMapper admininfoMapper;

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
            LogUtil.intoLog(this.getClass(),"LogAction gotologin loginParam is null");
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
                        LogUtil.intoLog(this.getClass(),"用户是否登录："+subject.isAuthenticated());
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


                        //修改用户最后一次登录
                        base_admininfo.setLastlogintime(new Date());
                        int updateById_bool=admininfoMapper.updateById(base_admininfo);
                        LogUtil.intoLog(this.getClass(),"updateById_bool__"+updateById_bool);
                        this.changeResultToSuccess(result);


                    }else{
                        LogUtil.intoLog(this.getClass(),"系统异常--登录账号多个："+loginaccount);
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

}
