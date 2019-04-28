package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.CloseddownServerParam;
import com.avst.trm.v1.web.req.basereq.GetdownServersParam;
import com.avst.trm.v1.web.req.basereq.StartdownServerParam;
import com.avst.trm.v1.web.service.baseservice.DownServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/*
下级服务器同步
 */
@Controller
@RequestMapping("/web/downServer")
public class DownServerAction extends BaseAction {

    @Autowired
    private DownServerService downServerService;



    /**
     * 跳转同步数据页面
     * @param model
     * @return
     */
    @GetMapping(value = "/todownServer")
    public ModelAndView todownServer(Model model) {
        model.addAttribute("title","同步数据");
        return new ModelAndView("police/downServer", "downServerModel", model);
    }

    /**
     * 获取同步列表
     * @return
     */
    @RequestMapping(value = "/getdownServers")
    @ResponseBody
    public RResult getdownServers(GetdownServersParam param){
        RResult result=createNewResultOfFail();
        if (null==param){
               result.setMessage("参数为空");
        }else{
            downServerService.getdownServers(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 开始同步
     * @return
     */
    @RequestMapping(value = "/startdownServer")
    @ResponseBody
    public RResult startdownServer(@RequestBody  StartdownServerParam param){
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else {
            downServerService.startdownServer(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 强制关闭同步
     * @return
     */
    @RequestMapping(value = "/closeddownServer")
    @ResponseBody
    public RResult closeddownServer(@RequestBody  CloseddownServerParam param){
        RResult result=createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else {
            downServerService.closeddownServer(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }










}
