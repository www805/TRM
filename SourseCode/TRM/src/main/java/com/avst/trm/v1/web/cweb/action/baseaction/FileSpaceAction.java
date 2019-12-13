package com.avst.trm.v1.web.cweb.action.baseaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.basereq.GetFileSpaceListParam;
import com.avst.trm.v1.web.cweb.service.baseservice.FileSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 磁盘文件管理
 */
@RestController
@RequestMapping("/filespace")
public class FileSpaceAction extends BaseAction {

    @Autowired
    private FileSpaceService fileSpaceService;

    //获取所有存储服务
    @RequestMapping(value = "/getFileSpaceList")
    public RResult getFileSpaceList(@RequestBody ReqParam<GetFileSpaceListParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            result = fileSpaceService.getFileSpaceList(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    //通过ssid查询文件管理
    @RequestMapping(value = "/getFileSpaceByssid")
    public RResult getFileSpaceByssid(@RequestBody ReqParam<GetFileSpaceListParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            result = fileSpaceService.getFileSpaceByssid(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    //查询路径下的所有文件
    @RequestMapping(value = "/getFileSpaceAll")
    public RResult getFileSpaceAll(@RequestBody ReqParam<GetFileSpaceListParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            result = fileSpaceService.getFileSpaceAll(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    //删除当前路径下的所有文件
    @RequestMapping(value = "/delFileSpaceAll")
    public RResult delFileSpaceAll(@RequestBody ReqParam<GetFileSpaceListParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            result = fileSpaceService.delFileSpaceAll(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    //删除单个文件
    @RequestMapping(value = "/delFileSpaceByPath")
    public RResult delFileSpaceByPath(@RequestBody ReqParam<GetFileSpaceListParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else if (!checkToken(param.getToken())) {
            result.setMessage("授权异常");
        } else {
            result = fileSpaceService.delFileSpaceByPath(result, param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



    @RequestMapping("/getFileSpacePage")
    public ModelAndView getFileSpacePage(Model model) {
        model.addAttribute("title", "磁盘文件管理");
        return new ModelAndView("client_web/base/getFileSpacePage", "getFileSpacePage", model);
    }


}
