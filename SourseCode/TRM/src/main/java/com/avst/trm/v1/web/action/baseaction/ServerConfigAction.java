package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.policereq.ServerconfigParam;
import com.avst.trm.v1.web.service.policeservice.ServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/web/config")
public class ServerConfigAction extends BaseAction{

    @Autowired
    private ServerConfigService serverConfigService;

    /**
     * 系统配置
     * @param model
     * @return
     */
    @GetMapping(value = "/getServerConfig")
    public ModelAndView getServerConfig(Model model) {

        RResult rResult = createNewResultOfFail();
        serverConfigService.getServerConfigById(rResult);

        model.addAttribute("RResult", rResult);
        model.addAttribute("title", "系统配置");
        return new ModelAndView("police/serverconfig", "configModel", model);
    }

    /**
     * 修改配置
     * @return
     */
    @PostMapping(value = "/getServerConfig",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RResult UpdateServerConfig(ServerconfigParam serverconfigParam) {

        RResult rResult = createNewResultOfFail();
        serverConfigService.UpdateServerConfig(rResult, serverconfigParam);

        return rResult;
    }

    /***
     * 图片上传接口
     * @return
     */
    @PostMapping(value = "/uploadByImg")
    @ResponseBody
    public RResult uploadByImg(@RequestParam("file") MultipartFile file) {
        RResult rResult = createNewResultOfFail();
        serverConfigService.uploadByImg(rResult, file);
        return rResult;
    }

}
