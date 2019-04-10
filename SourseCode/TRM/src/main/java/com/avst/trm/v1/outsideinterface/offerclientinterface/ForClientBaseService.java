package com.avst.trm.v1.outsideinterface.offerclientinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForClientBaseService extends BaseService {


    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;


    public void initClient(InitVO initvo){

        //判断是否生成隐性的ini文件
        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        String serverip=serverconfig.getServerip();
        String serverport=serverconfig.getServerport();
        if(StringUtils.isEmpty(serverip)||StringUtils.isEmpty(serverport)){
            initvo.setCode(CodeForSQ.ERROR100002);
            initvo.setMsg("服务器配置访问IP/端口异常");
            return;
        }
        int authorizebool=serverconfig.getAuthorizebool();
        if(authorizebool!=1){//还没有生成隐性授权文件
            boolean bool=AnalysisSQ.createClientini(base_serverconfigMapper,serverconfig);
            System.out.println("initClient authorizebool:"+bool);
        }

        int bool=AnalysisSQ.checkUseTime();
        if(bool > -1){
            initvo.setCode(CodeForSQ.TRUE);
            initvo.setMsg("使用正常");
        }else{
            if(bool == -100001){
                initvo.setCode(CodeForSQ.ERROR100001);
            }else if(bool == -100002){
                initvo.setCode(CodeForSQ.ERROR100002);
            }else{
                initvo.setCode(CodeForSQ.ERROR100003);
            }
            initvo.setMsg("使用异常");
            return;
        }

        initvo.setBaseUrl(CommonCache.getClientBaseurl());
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
