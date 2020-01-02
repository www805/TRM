package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
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

    //登录后台
    public RResult getLoginUser(RResult rresult, ReqParam param) {



        return null;
    }

    public RResult getUserPwd(RResult rresult, ReqParam param) {

        ControlInfoParamVO controlInfoParamVO = new ControlInfoParamVO();

        Base_admininfo admininfo = CommonCache.getAdmininfo();
        controlInfoParamVO.setLoginusername(admininfo.getLoginaccount());//登录账号
        controlInfoParamVO.setLoginpassword(admininfo.getPassword());//登录密码

        rresult.setData(controlInfoParamVO);
        changeResultToSuccess(rresult);

        return rresult;
    }
}
