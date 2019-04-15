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


    public void initClient(InitVO initvo){

        //判断是否生成隐性的ini文件
        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        String serverip=serverconfig.getServerip();
        String serverport=serverconfig.getServerport();
        if(StringUtils.isEmpty(serverip)||StringUtils.isEmpty(serverport)){
            initvo.setCode(CodeForSQ.ERROR100002);
            initvo.setMsg("服务器配置访问IP/端口异常");
            return;
        }
        int authorizebool=serverconfig.getAuthorizebool();
        if(authorizebool!=1){//还没有生成隐性授权文件
            boolean bool=AnalysisSQ.createClientini(base_serverconfigMapper,serverconfig);
            System.out.println("initClient authorizebool:"+bool);
        }

        int bool=AnalysisSQ.checkUseTime();
        if(bool > -1){
            initvo.setCode(CodeForSQ.TRUE);
            initvo.setMsg("使用正常");
        }else{
            if(bool == -100001){
                initvo.setCode(CodeForSQ.ERROR100001);
            }else if(bool == -100002){
                initvo.setCode(CodeForSQ.ERROR100002);
            }else{
                initvo.setCode(CodeForSQ.ERROR100003);
            }
            initvo.setMsg("使用异常");
            return;
        }

        initvo.setBaseUrl(CommonCache.getClientBaseurl());
        initvo.setKey(CommonCache.getClientKey());
        initvo.setServiceType(CommonCache.getCurrentServerType());
        List<PageVO> pageList=new ArrayList<PageVO>();
        PageVO pageVO=new PageVO();
        pageVO.setPageid("ase4351dfw");
        List<ActionVO> actionList=new ArrayList<ActionVO>();
        ActionVO actionVO=new ActionVO();
        actionVO.setActionId("2wer4");
        actionVO.setGotopageOrRefresh(2);
        actionVO.setNextPageId("23erd");
        actionVO.setReqURL("init");
        actionList.add(actionVO);
        actionVO=new ActionVO();
        actionVO.setActionId("54wer4");
        actionVO.setGotopageOrRefresh(1);
        actionVO.setNextPageId("231rd");
        actionVO.setReqURL("init");
        actionList.add(actionVO);
        pageVO.setActionList(actionList);
        pageList.add(pageVO);

        pageVO=new PageVO();
        pageVO.setPageid("123fw");
        actionList=new ArrayList<ActionVO>();
        actionVO=new ActionVO();
        actionVO.setActionId("1er4");
        actionVO.setGotopageOrRefresh(2);
        actionVO.setNextPageId("2erd");
        actionVO.setReqURL("init2");
        actionList.add(actionVO);
        actionVO=new ActionVO();
        actionVO.setActionId("54wer1");
        actionVO.setGotopageOrRefresh(1);
        actionVO.setNextPageId("231rd1");
        actionVO.setReqURL("init3");
        actionList.add(actionVO);
        pageVO.setActionList(actionList);
        pageList.add(pageVO);
        initvo.setPageList(pageList);

    }

    public void  userlogin(RResult result, ReqParam param, HttpSession httpSession){
        UserloginVO userloginVO=new UserloginVO();
        String type= CommonCache.getCurrentServerType();
        UserloginParam userloginParam=new UserloginParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            userloginParam =gson.fromJson(parameter, UserloginParam.class);
        }
        if (null==userloginParam){
            result.setMessage("参数为空");
            return;
        }
        String loginaccount1=userloginParam.getLoginaccount();
        String password1=userloginParam.getPassword();

        if (StringUtils.isBlank(type)){
            System.out.println("系统异常--"+type);
            result.setMessage("系统异常");
            return;
        }
        userloginVO.setType(type);

        if (StringUtils.isBlank(loginaccount1)||StringUtils.isBlank(password1)){
            System.out.println("账号:"+loginaccount1+"密码:"+password1+"不能为空--");
            result.setMessage("账号密码不能为空");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        ew.eq("loginaccount",loginaccount1);
        List<Base_admininfo> users= base_admininfoMapper.selectList(ew);
        if (null!=users&&users.size()>0){
            if (users.size()==1){
                    Base_admininfo user=users.get(0);
                    String loginaccount=user.getLoginaccount();//账号
                    String password=user.getPassword();//密码
                    Integer adminbool=user.getAdminbool();//状态

                     if (!password.trim().equals(password1)){
                        System.out.println("账户:"+loginaccount1+"用户密码不正确--"+loginaccount1);
                         result.setMessage("密码错误");
                        return;
                     }
                     if (StringUtils.isNotBlank(loginaccount)&&loginaccount.equals(loginaccount1)&&StringUtils.isNotBlank(password)&&password.equals(password1)){
                            if (null!=adminbool&&adminbool!=1){
                                 System.out.println("账户:"+loginaccount1+"用户状态:"+adminbool+"--");
                                 result.setMessage("用户状态异常");
                                 return;
                            }
                            System.out.println("账户:"+loginaccount1+"登陆成功--");
                            //修改最后一次登陆时间
                            user.setLastlogintime(new Date());
                            int updateById_bool=base_admininfoMapper.updateById(user);
                            System.out.println("updateById_bool--"+updateById_bool);
                            httpSession.setAttribute("user", user);
                            if (type.equals("police")){
                                System.out.println("准备跳转police页面");
                                result.setNextpageid("police_index");
                            }else if (type.equals("court")){
                                System.out.println("准备跳转court页面");
                            }else if (type.equals("jw")){
                                System.out.println("准备跳转jw页面");
                            }else if (type.equals("meeting")){
                                System.out.println("准备跳转meeting页面");
                            }
                        }
            }else{
                System.out.println("多个用户异常--"+loginaccount1);
                result.setMessage("系统异常");
                return;
            }
        }else{
            System.out.println("用户不存在--"+loginaccount1);
            result.setMessage("用户不存在");
            return;
        }
        result.setData(userloginVO);
        changeResultToSuccess(result);
        return;
    }

    public  void setServerconfig(RResult result,ReqParam param){
        Base_serverconfig serverconfig=new Base_serverconfig();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            serverconfig =gson.fromJson(parameter, Base_serverconfig.class);
        }

        if (null==serverconfig){
            result.setMessage("参数为空");
            return;
        }

        if (null==serverconfig.getId()){
            result.setMessage("未找到该配置信息");
            return;
        }
        int updateById_bool=base_serverconfigMapper.updateById(serverconfig);
        if (updateById_bool>0){
            result.setData(updateById_bool);
            changeResultToSuccess(result);
            return;
        }
    }


}
