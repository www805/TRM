package com.avst.trm.v1.web.cweb.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.param.InitVO;
import com.avst.trm.v1.web.cweb.req.GetServerconfigParam;
import com.avst.trm.v1.web.cweb.req.UpdateServerconfigParam;
import com.avst.trm.v1.web.cweb.req.UserloginParam;
import com.avst.trm.v1.web.cweb.vo.GetServerconfigVO;
import com.avst.trm.v1.web.cweb.vo.UpdateServerconfigVO;
import com.avst.trm.v1.web.cweb.vo.UserloginVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service("mainService")
public class MainService extends BaseService {

    private Gson gson = new Gson();

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;


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
        List<Base_admininfo> users= base_admininfoMapper.selectList(ew);
        if (null!=users&&users.size()>0){
            if (users.size()==1){
                Base_admininfo user=users.get(0);
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
        if (null!=session.getAttribute(Constant.MANAGE_WEB)){
            session.removeAttribute(Constant.MANAGE_WEB);
            System.out.println("登出成功");
            result.setMessage("登出成功");
        }
        changeResultToSuccess(result);
        return;
    }

    public  void updateServerconfig(RResult result,ReqParam<UpdateServerconfigParam> param){
        UpdateServerconfigVO updateServerconfigVO=new UpdateServerconfigVO();

        //请求参数转换
        UpdateServerconfigParam serverconfig= param.getParam();
        if (null==serverconfig){
            result.setMessage("参数为空");
            return;
        }

        if (null==serverconfig.getId()){
            result.setMessage("参数为空");
            return;
        }

        //修改配置数据
        int updateById_bool=base_serverconfigMapper.updateById(serverconfig);//没有任何需要修改值的时候会报错
        System.out.println("updateById_bool"+updateById_bool);
        updateServerconfigVO.setBool(updateById_bool);
        result.setData(updateServerconfigVO);
        if (updateById_bool<1){
            result.setMessage("修改异常");
            return;
        }
        //修改之后处理***

        changeResultToSuccess(result);
        return;
    }

    public void getServerconfig(RResult result,ReqParam<GetServerconfigParam> param){
        GetServerconfigVO getServerconfigVO=new GetServerconfigVO();

        GetServerconfigParam getServerconfigParam=param.getParam();
        if (null==getServerconfigParam){
            result.setMessage("参数为空");
            return;
        }

        if (StringUtils.isBlank(getServerconfigParam.getType())){
            result.setMessage("参数为空");
            return;
        }
        EntityWrapper ew=new EntityWrapper();
        ew.eq("type",getServerconfigParam.getType());
        List<Base_serverconfig> list=base_serverconfigMapper.selectList(ew);
        if (null!=list&&list.size()>0){
            if (list.size()==1){
                Serverconfig serverconfig=gson.fromJson(gson.toJson(list.get(0)), Serverconfig.class);
                getServerconfigVO.setServerconfig(serverconfig);
                result.setData(getServerconfigVO);
                changeResultToSuccess(result);
                return;
            }else{
                System.out.println("多个系统配置异常--");
                result.setMessage("系统异常");
                return;
            }
        }
        return;
    }
}
