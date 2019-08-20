package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.GetdataInfosParam;
import com.avst.trm.v1.web.sweb.service.baseservice.DatainfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/*
下级服务器同步
 */
@Controller
@RequestMapping("/sweb/base/datainfo")
public class DatainfoAction extends BaseAction {
    @Autowired
    private DatainfoService datainfoService;



    /**
     * 跳转表单页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/tostartDownServer")
    public ModelAndView tostartDownServer(Model model,String lastIP) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("tostartDownServer")) {
            model.addAttribute("title","表单列表");
            if (StringUtils.isNotBlank(lastIP)){
                model.addAttribute("lastIP",lastIP);
            }
            return new ModelAndView("server_web/base/startDownServer", "startDownServerModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }

    }

    /**
     * 获取表单数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/getdataInfos")
    @ResponseBody
    public RResult getdataInfos(GetdataInfosParam param){
        RResult result=createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getdataInfos")) {
            if (null==param){
                result.setMessage("参数为空");
            }else {
                datainfoService.getdataInfos(result, param);
            }
        }else {
            result.setMessage("权限不足");
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


}
