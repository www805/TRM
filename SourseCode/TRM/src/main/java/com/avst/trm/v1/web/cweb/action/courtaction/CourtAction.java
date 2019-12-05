package com.avst.trm.v1.web.cweb.action.courtaction;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.courtreq.*;
import com.avst.trm.v1.web.cweb.service.courtService.CourtService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/cweb/court/court")
public class CourtAction extends BaseAction {
    @Autowired
    private CourtService courtService;


    /**
     *
     * @param param
     * @param
     * @return
     */
    @RequestMapping(value = "/getUserinfogradePage")
    public RResult getUserinfogradePage(@RequestBody ReqParam<GetUserinfogradePageParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.getUserinfogradePage(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/getUserinfogradeByssid")
    public RResult getUserinfogradeByssid(@RequestBody ReqParam<GetUserinfogradeByssidParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.getUserinfogradeByssid(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/addUserinfograde")
    public RResult addUserinfograde(@RequestBody ReqParam<AddUserinfogradeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.addUserinfograde(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/updateUserinfograde")
    public RResult updateUserinfograde(@RequestBody ReqParam<UpdateUserinfogradeParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.updateUserinfograde(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 导入模板ue
     * @return
     */
    @RequestMapping(value = "/importtemplate_ue")
    public RResult importtemplate_ue(@RequestParam(value="param",required=false)String ReqParam, @RequestParam(value="file",required=false) MultipartFile multipartfile){
        RResult result=this.createNewResultOfFail();
        if (StringUtils.isEmpty(ReqParam)){
            result.setMessage("参数为空");
        }else {
            Gson gson=new Gson();
            ReqParam param=gson.fromJson(ReqParam, ReqParam.class);
            if (null==param){
                result.setMessage("参数为空");
            }else if (!checkToken(param.getToken())){
                result.setMessage("授权异常");
            }else{
                courtService.importtemplate_ue(result,param,multipartfile);
            }
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 导出模板ue
     * @param param
     * @return
     */
    @RequestMapping(value = "/exporttemplate_ue")
    public RResult exporttemplate_ue(@RequestBody ReqParam<Exporttemplate_ueParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.exporttemplate_ue(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/export_asr")
    public RResult export_asr(@RequestBody ReqParam<Export_asrParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.export_asr(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    @RequestMapping(value = "/setMCTagTxtreal")
    public RResult setMCTagTxtreal(@RequestBody ReqParam<SetMCTagTxtrealParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            courtService.setMCTagTxtreal(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }






}
