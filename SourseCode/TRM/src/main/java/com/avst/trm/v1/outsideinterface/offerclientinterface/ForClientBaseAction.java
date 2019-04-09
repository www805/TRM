package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.web.service.CeshiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

}
