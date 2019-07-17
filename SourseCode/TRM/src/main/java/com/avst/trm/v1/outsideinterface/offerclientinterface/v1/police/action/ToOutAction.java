package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.ToOutService;
import com.avst.trm.v1.web.cweb.req.basereq.UserloginParam;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对外提供接口
 */
@RestController
@RequestMapping("/trm/v1")
public class ToOutAction  extends BaseAction {
    @Autowired
    private ToOutService toOutService;

    @Autowired
    private MainService mainService;

    /**
     * 提供给总控的心跳检测
     * @return
     */
    @RequestMapping("/checkClient")
    public RResult checkClient(@RequestBody ReqParam param){
        RResult rresult=createNewResultOfFail();
        rresult=toOutService.checkClient(rresult,param);
        return rresult;
    }

    /**
     * 提供给总控的登录账号密码
     * @return
     */
    @RequestMapping("/getLoginUser")
    public RResult getLoginUser(@RequestBody ReqParam<UserloginParam> param, HttpSession httpSession){
        RResult rresult=createNewResultOfFail();
        mainService.userlogin(rresult, param, httpSession);
        return rresult;
    }

}
