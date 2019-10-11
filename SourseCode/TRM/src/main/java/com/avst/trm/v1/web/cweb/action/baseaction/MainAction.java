package com.avst.trm.v1.web.cweb.action.baseaction;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.SysYmlCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.cache.param.SysYmlParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.web.cweb.req.basereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.sweb.service.baseservice.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cweb/base/main")
public class MainAction extends BaseAction {
    @Autowired
    private  MainService mainService;

    @Autowired
    private UserService userService;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    @RequestMapping(value = "/{pageid}")
    public ModelAndView gotomain(Model model,@PathVariable("pageid")String pageid) {
        return new ModelAndView(pageid, pageid, model);
    }


    /*
     * 客户端管理员登录
     * @return
     */
    @RequestMapping(value = "/userlogin")
    @ResponseBody
    public RResult userlogin(@RequestBody ReqParam<UserloginParam> param,HttpServletRequest request) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.userlogin(result,param,request);
            AppCache.delAppCacheParam();
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
            AppCache.delAppCacheParam();
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 修改用户密码
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePassWord")
    @ResponseBody
    public RResult updatePassWord(@RequestBody ReqParam<updatePassWordParam> param){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.updatePassWord(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 修改个人信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePersonInfo")
    @ResponseBody
    public RResult updatePersonInfo(@RequestBody ReqParam<updatePersonInfoParam> param, HttpServletRequest request) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            mainService.updatePersonInfo(result, param, request);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 查询单个人信息
     * @param ssid
     * @return
     */
    @RequestMapping(value = "/getUserBySsid")
    @ResponseBody
    public RResult getUserBySsid(String ssid){
        RResult result=createNewResultOfFail();
        if (null==ssid){
            result.setMessage("参数为空");
        }else{
            userService.getUserBySsid(result,ssid);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    /**
     * 修改服务器配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateServerconfig")
    @ResponseBody
    public RResult updateServerconfig(ReqParam param,@RequestParam(value="client_url",required=false) MultipartFile multipartfile){
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.updateServerconfig(result,param,multipartfile);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 获取服务器配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/getServerconfig")
    @ResponseBody
    public RResult getServerconfig(@RequestBody ReqParam param){
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
     * 获取全部国籍
     * @param param
     * @return
     */
    @RequestMapping("/getNationalitys")
    @ResponseBody
    public RResult getNationalitys(@RequestBody  ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getNationalitys(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部民族
     * @param param
     * @return
     */
    @RequestMapping("/getNationals")
    @ResponseBody
    public RResult getNationals(@RequestBody  ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getNationals(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取首页数据
     * @param param
     * @return
     */
    @RequestMapping("/getHome")
    @ResponseBody
    public RResult getHome(@RequestBody  ReqParam<GetHomeParam> param, HttpSession session){
        RResult result=this.createNewResultOfFail();

        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getHome(result,param,session);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取导航栏目
     * @return
     */
    @RequestMapping("/getNavList")
    @ResponseBody
    public  RResult getNavList(){
        RResult result=this.createNewResultOfFail();
        mainService.getNavList(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 心跳
     * @return
     */
    @RequestMapping("/getPant")
    @ResponseBody
    public  RResult getPant(){
        RResult result=this.createNewResultOfFail();
        result.setData("心跳正常");
        changeResultToSuccess(result);
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取全部管理员
     */
    @RequestMapping("/getAdminList")
    @ResponseBody
    public  RResult getAdminList(@RequestBody ReqParam<GetAdminListParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getAdminList(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取默认的会议模板类型
     * @param param
     * @return
     */
    @RequestMapping("/getDefaultMtModelssid")
    @ResponseBody
    public RResult getDefaultMtModelssid(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getDefaultMtModelssid(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 获取全部单位
     * @param param
     * @return
     */
    @RequestMapping("/getWorkunits")
    @ResponseBody
    public RResult getWorkunits(@RequestBody ReqParam param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            mainService.getWorkunits(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }




    /***
     * 检测关键字
     */
    @RequestMapping("/checkKeyword")
    @ResponseBody
    public  RResult checkKeyword(@RequestBody ReqParam<CheckKeywordParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            mainService.checkKeyword(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 插件下载列表
     * @param param
     * @return
     */
    @RequestMapping("/getPackdownList")
    @ResponseBody
    public  RResult getPackdownList(@RequestBody ReqParam<GetPackdownListParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            mainService.getPackdownList(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 上传插件
     * @param
     * @return
     */
    @RequestMapping("/uploadPackdown")
    @ResponseBody
    public  RResult uploadPackdown(ReqParam param,@RequestParam(value="file",required=false) MultipartFile multipartfile){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            mainService.uploadPackdown(result, param,multipartfile);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 删除插件
     * @param
     * @return
     */
    @RequestMapping("/changeboolPackdown")
    @ResponseBody
    public  RResult changeboolPackdown(@RequestBody ReqParam<ChangeboolPackdownParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else {
            mainService.changeboolPackdown(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }

    /**
     * 跳转==》修改个人信息页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoupdatePersonInfo")
    public ModelAndView gotoupdatePersonInfo(Model model){
        model.addAttribute("title","个人信息");
        return  new ModelAndView("client_web/base/updatePersonInfo","updatePersonInfoModel", model);
    }

    /**
     * 跳转==》修改用户密码页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoupdatePassword")
    public ModelAndView gotoupdatePassword(Model model){
        model.addAttribute("title","修改密码");
        return  new ModelAndView("client_web/base/updatePassword","updatePasswordModel", model);
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
     * 跳转==》登录页
     */
    @RequestMapping(value = "/gotologin")
    public ModelAndView gotologin(Model model){
        AppCacheParam param = AppCache.getAppCacheParam();
        if(StringUtils.isBlank(param.getTitle()) || StringUtils.isBlank(param.getGuidepageUrl())){
            this.getNavList();
            param = AppCache.getAppCacheParam();
            Base_serverconfig base_serverconfig = base_serverconfigMapper.selectById(1);
            if (null != base_serverconfig) {
                param.setTitle(base_serverconfig.getClientname());
            }
        }
        model.addAttribute("title", "欢迎使用" + param.getTitle());
        model.addAttribute("serviceName",  param.getTitle());
        model.addAttribute("guidepageUrl",  param.getGuidepageUrl());
        return  new ModelAndView("client_web/base/login","loginModel", model);
    }

    /**
     * 跳转==》主页
     */
    @RequestMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model){
        AppCacheParam param = AppCache.getAppCacheParam();
        if(StringUtils.isBlank(param.getTitle()) || StringUtils.isBlank(param.getGuidepageUrl())){
            this.getNavList();
            param = AppCache.getAppCacheParam();
            Base_serverconfig base_serverconfig = base_serverconfigMapper.selectById(1);
            if (null != base_serverconfig) {
                param.setTitle(base_serverconfig.getClientname());
            }
        }
        Map<String, Object> data = param.getData();
        model.addAttribute("homeurl",(String) data.get("home-url"));
        model.addAttribute("title",param.getTitle());
        model.addAttribute("guidepageUrl",  param.getGuidepageUrl());
        //通过授权判断，返回笔录系统
        return  new ModelAndView("client_web/base/main","mainModel", model);
    }

    /**
     * 跳转==》主页
     */
    @RequestMapping(value = "/gotohome")
    public ModelAndView gotohome(Model model){
        model.addAttribute("title","智能提讯系统");
        SysYmlParam sysYmlParam=SysYmlCache.getSysYmlParam();
        if (null!=sysYmlParam){
            Map<String, Object> branchYml=sysYmlParam.getBranchYml();
            if (null!=branchYml){
                model.addAttribute("home",branchYml.get("home"));//分支特性
            }
        }
        return  new ModelAndView("client_web/base/home","homeModel", model);
    }

    /**
     * 跳转==》插件列表
     */
    @GetMapping("gotopackdownList")
    public ModelAndView gotopackdownList(Model model,String parentbool){
        model.addAttribute("title","插件下载");
        model.addAttribute("parentbool",parentbool);//用于控制页面跳转
        return new ModelAndView("client_web/base/packdownList", "packdownListModel", model);
    }








    /**
     * 检测是否授权
     * @return
     */
    public boolean checkToken( String token){
        String clientkey=CommonCache.getClientKey();
      if (StringUtils.isEmpty(token)||StringUtils.isEmpty(clientkey)){
            return  false;
        }
        if (!token.trim().equals(clientkey.trim())){
            return  false;
        }
        return  true;
    }



}



