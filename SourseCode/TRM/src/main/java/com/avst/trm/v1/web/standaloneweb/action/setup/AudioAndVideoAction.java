package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDAudioConfParam_out;
import com.avst.trm.v1.feignclient.ec.req.SetFDAudioVolumeParam_out;
import com.avst.trm.v1.web.standaloneweb.req.SaveFDAudioVolumeParam;
import com.avst.trm.v1.web.standaloneweb.service.AudioAndVideoService;
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
@RequestMapping("/cweb/setUp/audioAndVideo")
public class AudioAndVideoAction extends BaseAction {

    @Autowired
    private AudioAndVideoService audioAndVideoService;


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
            audioAndVideoService.getFDAudioConf(result,param.getParam());
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
            audioAndVideoService.setFDAudioVolume(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 保存声音
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveFDAudioVolume")
    @ResponseBody
    public RResult saveFDAudioVolume(@RequestBody ReqParam<SaveFDAudioVolumeParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            audioAndVideoService.saveFDAudioVolume(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    /**
     * 进入音视频界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoAudioAndVideo")
    public ModelAndView gotoAudioAndVideo(Model model){
        model.addAttribute("title","音视频");
        return  new ModelAndView("standalone_web/setup/audioandvideo/audioAndVideo","audioAndVideoModel", model);
    }

    /**
     * 信息叠加界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoInfodj")
    public ModelAndView gotoInfodj(Model model){
        model.addAttribute("title","信息叠加");
        return  new ModelAndView("standalone_web/setup/audioandvideo/infodj","infodjModel", model);
    }

    /**
     * 声音配置界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoAudioConfigure")
    public ModelAndView gotoAudioConfigure(Model model){
        model.addAttribute("title","声音配置");
        return  new ModelAndView("standalone_web/setup/audioandvideo/audioConfigure","audioConfigureModel", model);
    }





}



