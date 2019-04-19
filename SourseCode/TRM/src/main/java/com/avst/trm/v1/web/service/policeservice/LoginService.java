package com.avst.trm.v1.web.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.LoginParam;
import com.avst.trm.v1.web.vo.AdminManage_session;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

        if(null==result){
            result=new RResult<AdminManage_session>();
        }
        try {
            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
            ew.eq("a.loginaccount", loginParam.getLoginaccount()).eq("a.password", loginParam.getPassword());

            System.out.println(ew.getSqlSegment()+"---------");
            AdminManage_session adminManage=admininfoMapper.getAdminAndAdmintorole(ew);
            result.setData(adminManage);

            if(null != adminManage){
                request.getSession().setAttribute(Constant.MANAGE_WEB,adminManage);
                this.changeResultToSuccess(result);
            }else{
                result.setMessage("用户名或密码错误");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

}
