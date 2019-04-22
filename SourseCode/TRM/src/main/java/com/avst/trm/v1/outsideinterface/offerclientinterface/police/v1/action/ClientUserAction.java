package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.action;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseAction;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetCaseByIdParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetUserByCardParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service.ClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于人员
 */
@RestController
@RequestMapping("/v1/police/clientUser")
public class ClientUserAction extends ForClientBaseAction {
    @Autowired
    private ClientUserService clientUserService;

    /**
     * ①根据证件Id联查：/v1/police/clientUser/getUserByCard
     * ②根据案件id联查: /v1/police/clientUser/getCaseById
     */

    /**
     * 根据证件id联查
     * @param param
     * @return
     */
    @PostMapping(value = "/getUserByCard",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getUserByCard(@RequestBody  ReqParam<GetUserByCardParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            clientUserService.getUserByCard(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 根据案件id联查
     * @param param
     * @return
     */
    @PostMapping(value = "/getCaseById",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult getCaseById(@RequestBody  ReqParam<GetCaseByIdParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            clientUserService.getCaseById(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }



}
