package com.avst.trm.v1.web.sweb.action.baseaction;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import com.avst.trm.v1.web.sweb.req.basereq.UpdateIpParam;
import com.avst.trm.v1.web.sweb.service.policeservice.ServerIpService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sweb/base/ip")
public class ServerIpAction extends BaseAction{

    @Autowired
    private ServerIpService serverIpService;

    /**
     * 服务器IP配置
     * @param model
     * @return
     */
    @GetMapping(value = "/getServerIp")
    public ModelAndView getServerIp(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getServerIp")) {
            model.addAttribute("title", "服务器IP配置");
            return new ModelAndView("server_web/base/serverip", "ipModel", model);
        } else {
            return new ModelAndView("redirect:/sweb/base/home/unauth");
        }
    }

    /**
     * 修改配置
     * @return
     */
    @PostMapping(value = "/updateServerIp",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult updateServerIp(@RequestBody GetServerIpParam getServerIpParam) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateServerIp")) {
            serverIpService.updateServerIp(rResult, getServerIpParam);
        }else{
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /**
     * 修改网卡里指定的IP
     * @return
     */
    @PostMapping(value = "/updateIp",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult updateIp(@RequestBody UpdateIpParam updateIpParam) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("updateIp")) {
            serverIpService.updateIp(rResult, updateIpParam);
        }else{
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /**
     * 获取全部模板及通道信息
     * @return
     */
    @PostMapping(value = "/getServerIpList",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult getServerIpList() {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getServerIpList")) {
            serverIpService.getServerIp(rResult);
        }else{
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

    /**
     * 获取其他全部设备IP
     * @return
     */
    @PostMapping(value = "/getServerIpALL", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult getServerIpALL(GetServerIpALLParam param) {
        RResult rResult = createNewResultOfFail();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isPermitted("getServerIpALL")) {
            serverIpService.getServerIpALL(rResult, param);
        }else{
            rResult.setMessage("权限不足");
        }
        return rResult;
    }

}
