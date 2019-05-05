package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetServerconfigParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.req.UpdateServerconfigParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.req.UserloginParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 对客户端开放服务的基础接口,基本流程上的接口都在这里展现
 */
@Controller
@RequestMapping("/forClient")
public class ForClientBaseAction extends BaseAction {

    @Autowired
    private ForClientBaseService forClientBaseService;

    @GetMapping(value = "/init",produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public InitVO initClient() {

        InitVO initVO=new InitVO();

        initVO=forClientBaseService.initClient(initVO);

        return initVO;
    }



    /*
     * 客户端管理员登陆
     * @return
     */
    @RequestMapping(value = "/userlogin")
    @ResponseBody
    public RResult userlogin(@RequestBody ReqParam<UserloginParam> param, HttpSession httpSession) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.userlogin(result,param,httpSession);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 客户端管理员登出
     * @return
     */
    @RequestMapping(value = "/userloginout")
    @ResponseBody
    public RResult userloginout(@RequestBody  ReqParam param, HttpSession httpSession) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.userloginout(result,param,httpSession);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 修改服务器配置
     * @param param
     * @return
     */
    @GetMapping(value = "/updateServerconfig",produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public RResult updateServerconfig(@RequestBody ReqParam<UpdateServerconfigParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.updateServerconfig(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取服务器配置
     * @param param
     * @return
     */
    @GetMapping(value = "/getServerconfig",produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public RResult getServerconfig(@RequestBody ReqParam<GetServerconfigParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.getServerconfig(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 跳转==》修改服务器配置页面
     */
    @GetMapping(value = "/gotoupdateServerconfig")
    public ModelAndView gotoupdateServerconfig(Model model,Integer id){
        model.addAttribute("id","id");
        return  new ModelAndView("client_web/police/updateServerconfig","updateServerconfigModel", model);
    }

    /**
     * 跳转==》登陆页
     */
    @GetMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model, HttpServletRequest request){
        model.addAttribute("title","欢迎来到智能提讯系统");
        request.getSession().setAttribute(Constant.INIT_CLIENT,CommonCache.getinit_CLIENT());
        request.getSession().setAttribute(Constant.INIT_CLIENTKEY,CommonCache.getClientKey());
        return  new ModelAndView("client_web/police/login","loginModel", model);
    }

    /**
     * 跳转==》主页
     */
    @GetMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model){
        model.addAttribute("title","智能提讯系统");
        return  new ModelAndView("client_web/police/main","mainModel", model);
    }






    /**
     * 检测是否授权
     * @return
     */
    public boolean checkToken( String token){
        String clientkey=CommonCache.getClientKey();
        System.out.println("token:"+token+"------clientkey:"+clientkey);
        if (StringUtils.isEmpty(token)||StringUtils.isEmpty(clientkey)){
            return  false;
        }
        if (!token.trim().equals(clientkey.trim())){
            return  false;
        }
        return  true;
    }






}
