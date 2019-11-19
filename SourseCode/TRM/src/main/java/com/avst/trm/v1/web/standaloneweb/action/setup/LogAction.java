package com.avst.trm.v1.web.standaloneweb.action.setup;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.basereq.*;
import com.avst.trm.v1.web.cweb.req.policereq.CheckKeywordParam;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.standaloneweb.req.GetLogListByTypeParam;
import com.avst.trm.v1.web.standaloneweb.req.GetLogTypeListParam;
import com.avst.trm.v1.web.standaloneweb.service.LogService;
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

/**
 * 系统设置action
 */
@Controller
@RequestMapping("/cweb/setUp/log")
public class LogAction extends BaseAction {
    @Autowired
    private LogService logService;




    /*
     *查询日志列表
     * @return
     */
    @RequestMapping(value = "/getLogListByType")
    @ResponseBody
    public RResult getLogListByType(@RequestBody ReqParam<GetLogListByTypeParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            logService.getLogListByType(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /*
     *查询日志类型列表
     * @return
     */
    @RequestMapping(value = "/getLogTypeList")
    @ResponseBody
    public RResult getLogTypeList(@RequestBody ReqParam<GetLogTypeListParam> param) {
        RResult result=this.createNewResultOfFail();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            logService.getLogTypeList(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    /**
     * 进入日志查询界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoLogShow")
    public ModelAndView gotoLogShow(Model model){
        model.addAttribute("title","日志查看");
        return  new ModelAndView("standalone_web/setup/logShow","logModel", model);
    }









}



