package com.avst.trm.v1.outsideinterface.serverinterface;

import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.outsideinterface.reqparam.BaseReqParam;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ForDownServerBaseServiceInterface {

    public void initsynchronizeddata(String sqCode,String sqNum,RResult result);

    public void startSynchronizedata(BaseReqParam param, RResult result);

    /**
     * 同步数据库
     * @param param
     * @param result
     */
    public void synchronizedata(BaseReqParam param, RResult result,MultipartFile file,int type);

    public void overSynchronizedata(BaseReqParam param, RResult result);

    public void overSynchronizedata_must(BaseReqParam param, RResult result);


}
