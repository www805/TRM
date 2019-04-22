package com.avst.trm.v1.outsideinterface.serverinterface;

import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.outsideinterface.reqparam.BaseReqParam;
import org.springframework.stereotype.Service;
@Service
public interface ForDownServerBaseServiceInterface {

    public void initsynchronizeddata(String sqCode,String sqNum);

    public void startSynchronizedata(BaseReqParam param, RResult result);

    public void synchronizedata(BaseReqParam param, RResult result);

    public void overSynchronizedata(BaseReqParam param, RResult result);

}
