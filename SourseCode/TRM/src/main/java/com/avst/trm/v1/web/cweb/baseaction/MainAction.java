package com.avst.trm.v1.web.cweb.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.GetServerconfigParam;
import com.avst.trm.v1.web.cweb.req.UpdateServerconfigParam;
import com.avst.trm.v1.web.cweb.req.UserloginParam;
import com.avst.trm.v1.web.cweb.service.MainService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cweb/base/main")
public class MainAction extends BaseAction {
    @Autowired
    private MainService mainService;

    @RequestMapping(value = "/{pageid}")
    public ModelAndView gotomain(Model model,@PathVariable("pageid")String pageid) {
        return new ModelAndView(pageid, pageid, model);
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
            mainService.userlogin(result,param,httpSession);
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
            mainService.userloginout(result,param,httpSession);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 修改服务器配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateServerconfig",produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public RResult updateServerconfig(@RequestBody ReqParam<UpdateServerconfigParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.updateServerconfig(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取服务器配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/getServerconfig",produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public RResult getServerconfig(@RequestBody ReqParam<GetServerconfigParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getServerconfig(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 跳转==》修改服务器配置页面
     */
    @RequestMapping(value = "/gotoupdateServerconfig")
    public ModelAndView gotoupdateServerconfig(Model model){
        model.addAttribute("title","系统配置");
        return  new ModelAndView("client_web/base/updateServerconfig","updateServerconfigModel", model);
    }

    /**
     * 跳转==》登陆页
     */
    @RequestMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model){
        model.addAttribute("title","欢迎来到智能提讯系统");
        return  new ModelAndView("client_web/base/login","loginModel", model);
    }

    /**
     * 跳转==》主页
     */
    @RequestMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model){
        model.addAttribute("title","智能提讯系统");
        return  new ModelAndView("client_web/base/main","mainModel", model);
    }

    /**
     * 跳转==》主页
     */
    @RequestMapping(value = "/gotohome")
    public ModelAndView gotohome(Model model){
        model.addAttribute("title","智能提讯系统");
        return  new ModelAndView("client_web/base/home","homeModel", model);
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
