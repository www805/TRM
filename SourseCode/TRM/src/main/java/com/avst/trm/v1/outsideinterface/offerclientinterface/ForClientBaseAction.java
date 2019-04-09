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

        initVO.setCode(CodeForSQ.TRUE);
        initVO.setBaseUrl("http://192.168.17.175/forClient");
        initVO.setKey("1234567890");
        initVO.setServiceType("court");
        List<PageVO> pageList=new ArrayList<PageVO>();
        PageVO pageVO=new PageVO();
        pageVO.setPageid("ase4351dfw");
        List<ActionVO> actionList=new ArrayList<ActionVO>();
        ActionVO actionVO=new ActionVO();
        actionVO.setActionId("2wer4");
        actionVO.setGotopageOrRefresh(2);
        actionVO.setNextPageId("23erd");
        actionVO.setReqURL("init");
        actionList.add(actionVO);
        actionVO=new ActionVO();
        actionVO.setActionId("54wer4");
        actionVO.setGotopageOrRefresh(1);
        actionVO.setNextPageId("231rd");
        actionVO.setReqURL("init");
        actionList.add(actionVO);
        pageVO.setActionList(actionList);
        pageList.add(pageVO);

        pageVO=new PageVO();
        pageVO.setPageid("123fw");
        actionList=new ArrayList<ActionVO>();
        actionVO=new ActionVO();
        actionVO.setActionId("1er4");
        actionVO.setGotopageOrRefresh(2);
        actionVO.setNextPageId("2erd");
        actionVO.setReqURL("init2");
        actionList.add(actionVO);
        actionVO=new ActionVO();
        actionVO.setActionId("54wer1");
        actionVO.setGotopageOrRefresh(1);
        actionVO.setNextPageId("231rd1");
        actionVO.setReqURL("init3");
        actionList.add(actionVO);
        pageVO.setActionList(actionList);
        pageList.add(pageVO);
        initVO.setPageList(pageList);

        return initVO;
    }

}
