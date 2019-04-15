package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.vo.UserloginVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.req.UserloginParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 对客户端开放服务的基础接口,基本流程上的接口都在这里展现
 */
@RestController
@RequestMapping("/forClient")
public class ForClientBaseAction extends BaseAction {

    @Autowired
    private ForClientBaseService forClientBaseService;

    @GetMapping(value = "/init",produces = MediaType.APPLICATION_XML_VALUE)
    public InitVO initClient() {

        InitVO initVO=new InitVO();

        forClientBaseService.initClient(initVO);

        return initVO;
    }

    /**
     * 客户端管理员登陆
     * @return
     */
    @GetMapping(value = "/userlogin",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult userlogin(ReqParam param, HttpSession httpSession) {
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey=CommonCache.getClientKey();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token,clientkey)){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.userlogin(result,param,httpSession);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 修改服务器配置
     * @param param
     * @return
     */
    @GetMapping(value = "/setServerconfig",produces = MediaType.APPLICATION_XML_VALUE)
    public RResult setServerconfig(ReqParam param){
        RResult result=this.createNewResultOfFail();
        String token=param.getToken();
        String clientkey=CommonCache.getClientKey();
        if(null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(token,clientkey)){
            result.setMessage("授权异常");
        }else{
            forClientBaseService.setServerconfig(result,param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return  result;
    }



    /**
     * 检测是否授权
     * @return
     */
    public boolean checkToken( String token,String clientkey){
        System.out.println("token:"+token+"------clientkey:"+clientkey);
        if (StringUtils.isEmpty(token)||StringUtils.isEmpty(clientkey)){
            return  false;
        }
        if (!token.trim().equals(clientkey.trim())){
            return  false;
        }
        return  true;
    }




}
