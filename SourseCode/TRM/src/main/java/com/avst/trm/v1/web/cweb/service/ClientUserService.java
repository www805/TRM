package com.avst.trm.v1.web.cweb.service;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.GetCaseByIdParam;
import com.avst.trm.v1.web.cweb.req.GetUserByCardParam;
import com.avst.trm.v1.web.cweb.vo.GetCaseByIdVO;
import com.avst.trm.v1.web.cweb.vo.GetUserByCardVO;
import org.springframework.stereotype.Service;

@Service("clientUserService")
public class ClientUserService extends BaseService {


    public void getUserByCard(RResult result, ReqParam<GetUserByCardParam> param){
        GetUserByCardVO getUserByCardVO=new GetUserByCardVO();

        GetUserByCardParam getUserByCardParam=param.getParam();
        if (null==getUserByCardParam){
            result.setMessage("参数为空");
            return;
        }

        return;
    }

    public void getCaseById(RResult result, ReqParam<GetCaseByIdParam> param){
        GetCaseByIdVO getCaseByIdVO=new GetCaseByIdVO();

        GetCaseByIdParam getCaseByIdParam=param.getParam();
        if (null==getCaseByIdParam){
            result.setMessage("参数为空");
            return;
        }


        return;
    }


}
