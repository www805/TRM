package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.vo.UpdateServerconfigVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.req.UpdateServerconfigParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.vo.UserloginVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.req.UserloginParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ForClientBaseService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;


    public InitVO initClient(InitVO initvo){

        return  CommonCache.getinit_CLIENT();
    }

    public void  userlogin(RResult result, ReqParam param, HttpSession httpSession){
        UserloginVO userloginVO=new UserloginVO();
        String type= CommonCache.getCurrentServerType();

        //请求参数转换
        UserloginParam userloginParam=new UserloginParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            userloginParam =gson.fromJson(parameter, UserloginParam.class);
        }

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
                        System.out.println("账户:"+loginaccount1+"用户密码不正确--"+loginaccount1);
                         result.setMessage("密码错误");
                        return;
                     }
                     if (StringUtils.isNotBlank(loginaccount)&&loginaccount.equals(loginaccount1.trim())&&StringUtils.isNotBlank(password)&&password.equals(password1.trim())){
                            if (null!=adminbool&&adminbool!=1){
                                 System.out.println("账户:"+loginaccount1+"用户状态:"+adminbool+"--");
                                 result.setMessage("用户状态异常");
                                 return;
                            }
                            System.out.println("账户:"+loginaccount1+"登陆成功--");
                            result.setMessage("登陆成功");
                            //修改最后一次登陆时间
                            user.setLastlogintime(new Date());
                            int updateById_bool=base_admininfoMapper.updateById(user);
                            System.out.println("updateById_bool--"+updateById_bool);

                            //session存储
                            httpSession.setAttribute("user", user);

                            if (type.equals("police")){
                                result.setNextpageid("police_index");
                            }else if (type.equals("court")){
                                result.setNextpageid("court_index");
                            }else if (type.equals("jw")){
                                result.setNextpageid("jw_index");
                            }else if (type.equals("meeting")){
                                result.setNextpageid("meeting_index");
                            }
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
        result.setData(userloginVO);
        changeResultToSuccess(result);
        return;
    }



    public void userloginout(RResult result,ReqParam param,HttpSession session){
        session.setMaxInactiveInterval(1000*60*60);
        if (null!=session.getAttribute("user")){
            session.removeAttribute("user");
            System.out.println("登出成功");
            result.setMessage("登出成功");
        }
        result.setNextpageid("userloginout_index");
        changeResultToSuccess(result);
        return;
    }

    public  void updateServerconfig(RResult result,ReqParam param){
        UpdateServerconfigVO updateServerconfigVO=new UpdateServerconfigVO();

        //请求参数转换
        UpdateServerconfigParam serverconfig=new UpdateServerconfigParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            serverconfig =gson.fromJson(parameter, UpdateServerconfigParam.class);
        }

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


        result.setNextpageid("updateServerconfig_index");
        changeResultToSuccess(result);
        return;
    }


}
