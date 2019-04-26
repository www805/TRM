package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.util.baseaction.RRParam;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizeDataTypeParam;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizedataParam;

/**
 * 处理同步请求的接口
 */
public interface ToUpServerBaseReqInterface {

    public RRParam synchronizedata(String urle, StartSynchronizedata_2_Param param,String downserverssid);

}
