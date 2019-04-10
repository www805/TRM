package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.PropertiesConf;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForClientBaseService extends BaseService {

    @Autowired
    private PropertiesConf propertiesConf;


    public void initClient(InitVO initvo){

        initvo.setCode(CodeForSQ.TRUE);
        initvo.setBaseUrl(propertiesConf.getBaseurl());
        initvo.setKey(CommonCache.getClientKey());
        initvo.setServiceType(CommonCache.getCurrentServerType());
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
        initvo.setPageList(pageList);

    }


}
