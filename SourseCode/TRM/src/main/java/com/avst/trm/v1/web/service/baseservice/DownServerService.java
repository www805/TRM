package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_downserverMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.GetdownServersParam;
import com.avst.trm.v1.web.vo.basevo.GetdownServersVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("downServerService")
public class DownServerService extends BaseService {
    private Gson gson=new Gson();
    @Autowired
    private Base_datasynchroni_downserverMapper base_datasynchroni_downserverMapper;
    @Autowired
    private Base_datainfoMapper base_datainfoMapper;


    public void  getdownServers(RResult result, GetdownServersParam param){
        GetdownServersVO getdownServersVO=new GetdownServersVO();

        List<Base_datasynchroni_downserver> datasynchroni_downservers =  base_datasynchroni_downserverMapper.selectList(null);
        if (null!=datasynchroni_downservers&&datasynchroni_downservers.size()>0){
            getdownServersVO.setDatasynchroni_downservers(datasynchroni_downservers);
        }

        List<Base_datainfo> datainfos =  base_datainfoMapper.selectList(null);
        if (null!=datainfos&&datainfos.size()>0){
            getdownServersVO.setDatainfos(datainfos);
        }

        result.setData(getdownServersVO);
        changeResultToSuccess(result);
        return;
    }

    public void  startdownServer(RResult result){


        changeResultToSuccess(result);
        return;
    }

    public void  closeddownServer(RResult result){


        changeResultToSuccess(result);
        return;
    }


}
