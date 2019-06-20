package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ToOutVO;
import org.springframework.stereotype.Service;

@Service
public class ToOutService extends BaseService {

    public RResult checkClient(RResult rresult, ReqParam param){
        ToOutVO toOutVO=new ToOutVO();
        toOutVO.setTotal_item(1);
        toOutVO.setUse_item(1);
        rresult.setData(toOutVO);
        changeResultToSuccess(rresult);
        return  rresult;
    }



}
