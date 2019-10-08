package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统设置action
 */
@Controller
@RequestMapping("/cweb/setUp/audioAndVideo")
public class AudioAndVideoAction extends BaseAction {


    /*
     *查询日志列表
     * @return
     */
//    @RequestMapping(value = "/getLogListByType")
//    @ResponseBody
//    public RResult getLogListByType(@RequestBody ReqParam<GetLogListByTypeParam> param) {
//        RResult result=this.createNewResultOfFail();
//        if(null==param){
//            result.setMessage("参数为空");
//        }else if (!checkToken(param.getToken())){
//            result.setMessage("授权异常");
//        }else{
//            logService.getLogListByType(result,param.getParam());
//        }
//        result.setEndtime(DateUtil.getDateAndMinute());
//        return result;
//    }
//
//
//    /*
//     *查询日志类型列表
//     * @return
//     */
//    @RequestMapping(value = "/getLogTypeList")
//    @ResponseBody
//    public RResult getLogTypeList(@RequestBody ReqParam<GetLogTypeListParam> param) {
//        RResult result=this.createNewResultOfFail();
//        if(null==param){
//            result.setMessage("参数为空");
//        }else if (!checkToken(param.getToken())){
//            result.setMessage("授权异常");
//        }else{
//            logService.getLogTypeList(result,param.getParam());
//        }
//        result.setEndtime(DateUtil.getDateAndMinute());
//        return result;
//    }



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



