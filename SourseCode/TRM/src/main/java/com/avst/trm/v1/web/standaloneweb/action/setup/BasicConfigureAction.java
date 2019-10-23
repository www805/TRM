package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDAudioConfParam_out;
import com.avst.trm.v1.feignclient.ec.req.GetFDNetWorkParam_out;
import com.avst.trm.v1.feignclient.ec.req.SetFDAudioVolumeParam_out;
import com.avst.trm.v1.web.standaloneweb.req.GetNetworkConfigureParam;
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
     * 获得设备音频配置
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFDAudioConf")
    @ResponseBody
    public RResult getFDAudioConf(@RequestBody ReqParam<GetFDAudioConfParam_out> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.getFDAudioConf(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 设置设备某一个通道的通道音量
     * @param param
     * @return
     */
    @RequestMapping(value = "/setFDAudioVolume")
    @ResponseBody
    public RResult setFDAudioVolume(@RequestBody ReqParam<SetFDAudioVolumeParam_out> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            basicConfigureService.setFDAudioVolume(result,param.getParam());
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



