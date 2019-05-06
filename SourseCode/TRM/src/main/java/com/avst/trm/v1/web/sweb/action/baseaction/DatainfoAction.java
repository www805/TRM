package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.GetdataInfosParam;
import com.avst.trm.v1.web.sweb.service.baseservice.DatainfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/web/datainfo")
public class DatainfoAction extends BaseAction {
    @Autowired
    private DatainfoService datainfoService;



    /**
     * 跳转表单页面
     * @param model
     * @return
     */
    @RequiresPermissions("tostartDownServer")
    @GetMapping(value = "/tostartDownServer")
    public ModelAndView tostartDownServer(Model model,String lastIP) {
        model.addAttribute("title","表单列表");
        if (StringUtils.isNotBlank(lastIP)){
            model.addAttribute("lastIP",lastIP);
        }
        return new ModelAndView("server_web/police/startDownServer", "startDownServerModel", model);
    }

    /**
     * 获取表单数据
     * @param param
     * @return
     */
    @RequiresPermissions("getdataInfos")
    @RequestMapping(value = "/getdataInfos")
    @ResponseBody
    public RResult getdataInfos(GetdataInfosParam param){
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else {
            datainfoService.getdataInfos(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


}
