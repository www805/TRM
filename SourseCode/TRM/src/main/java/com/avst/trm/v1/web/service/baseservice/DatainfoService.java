package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DatainfoAndType;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DownserverAndDatainfo;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.GetdataInfosParam;
import com.avst.trm.v1.web.vo.basevo.GetdataInfosVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("datainfoService")
public class DatainfoService extends BaseService {
    @Autowired
    private Base_datainfoMapper base_datainfoMapper;

    public void getdataInfos(RResult result, GetdataInfosParam param){
        GetdataInfosVO getdataInfosVO=new GetdataInfosVO();

        EntityWrapper ew=new EntityWrapper();
        if (null!=param.getDataname_cn()){
            ew.like(true,"d.dataname_cn",param.getDataname_cn().trim());
        }
        int count = base_datainfoMapper.countgetdataInfos(ew);
        param.setRecordCount(count);

        Page<DatainfoAndType> page=new Page<DatainfoAndType>(param.getCurrPage(),param.getPageSize());
        List<DatainfoAndType> list=base_datainfoMapper.getdataInfos(page,ew);
        getdataInfosVO.setPageparam(param);

        if (null!=list&&list.size()>0){
            getdataInfosVO.setPagelist(list);
        }
        result.setData(getdataInfosVO);
        changeResultToSuccess(result);
        return;
    }
}
