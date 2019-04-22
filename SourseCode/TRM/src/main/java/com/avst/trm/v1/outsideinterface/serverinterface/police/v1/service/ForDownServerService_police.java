package com.avst.trm.v1.outsideinterface.serverinterface.police.v1.service;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.outsideinterface.reqparam.BaseReqParam;
import com.avst.trm.v1.outsideinterface.serverinterface.ForDownServerBaseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForDownServerService_police extends BaseService implements ForDownServerBaseServiceInterface {

    @Override
    public void initsynchronizeddata(String sqCode,String sqNum) {

    }

    @Override
    public void startSynchronizedata(BaseReqParam param, RResult result) {

    }

    @Override
    public void synchronizedata(BaseReqParam param, RResult result) {

    }

    @Override
    public void overSynchronizedata(BaseReqParam param, RResult result) {

    }
}
