package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.util.baseaction.RResult;

/**
 * 处理同步请求的接口
 */
public interface ToUpServerBaseReqInterface {

    public void initsynchronizeddata(String url);

    public void startSynchronizedata(String url);

    public void synchronizedata(String url);

    public void overSynchronizedata(String url);
}
