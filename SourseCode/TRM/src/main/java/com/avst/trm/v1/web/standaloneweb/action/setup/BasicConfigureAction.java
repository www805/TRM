package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDNetWorkParam_out;
import com.avst.trm.v1.web.standaloneweb.req.*;
import com.avst.trm.v1.web.standaloneweb.service.BasicConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统设置action
 */
@Controller
@RequestMapping("/cweb/setUp/basicConfigure")
public class BasicConfigureAction extends BaseAction {

    @Autowired
    private BasicConfigureService basicConfigureService;

    /**
     * 获取默认设备的ssid
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFDssid")
    @ResponseBody
    public RResult getFDssid (@RequestBody ReqParam param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.getFDssid(result);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 设置NTP同步
     * @param param
     * @return
     */
    @RequestMapping(value = "/setNTP")
    @ResponseBody
    public RResult setNTP (@RequestBody ReqParam<SetNTPParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.setNTP(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 同步本机时间
     * @param param
     * @return
     */
    @RequestMapping(value = "/setEcSystemTimeSync")
    @ResponseBody
    public RResult setEcSystemTimeSync (@RequestBody ReqParam<SetEcSystemTimeSyncParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.setEcSystemTimeSync(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 设置x85系统时间
     * @param param
     * @return
     */
    @RequestMapping(value = "/setEcSystemTime")
    @ResponseBody
    public RResult setEcSystemTime (@RequestBody ReqParam<SetEcSystemTimeParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.setEcSystemTime(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取网络配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFDNetWork")
    @ResponseBody
    public RResult getFDNetWork(@RequestBody ReqParam<GetFDNetWorkParam_out> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.getFDNetWork(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 设置网络配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/setNetworkConfigure")
    @ResponseBody
    public RResult setNetworkConfigure(@RequestBody ReqParam<GetNetworkConfigureParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.setNetworkConfigure(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }




    /**
     * 软硬件信息数据获取
     * 硬件信息、软件信息、授权信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSystemInfo")
    @ResponseBody
    public RResult getSystemInfo(@RequestBody ReqParam param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.getSystemInfo(result);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 基本配置界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoBasicConfigure")
    public ModelAndView gotoBasicConfigure(Model model){
        model.addAttribute("title","基本配置");
        return  new ModelAndView("standalone_web/setup/basicConfigure/basicConfigure","basicConfigureModel", model);
    }

    /**
     * 软硬件信息
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoSoftAndHardInfo")
    public ModelAndView gotoSoftAndHardInfo(Model model){
        model.addAttribute("title","软硬件信息");
        return  new ModelAndView("standalone_web/setup/basicConfigure/softAndHardInfo","softAndHardInfoModel", model);
    }

    /**
     * 网络配置界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoNetworkConfigure")
    public ModelAndView gotoNetwork(Model model){
        model.addAttribute("title","设备网络配置");
        return  new ModelAndView("standalone_web/setup/basicConfigure/networkConfigure","networkConfigureModel", model);
    }

    /**
     * 日期时间界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoDateTime")
    public ModelAndView gotoDateTime(Model model){
        model.addAttribute("title","日期时间");
        return  new ModelAndView("standalone_web/setup/basicConfigure/dateTime","dateTimeModel", model);
    }





}



