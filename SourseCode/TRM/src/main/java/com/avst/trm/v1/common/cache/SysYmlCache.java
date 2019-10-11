package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.cache.param.SysYmlParam;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;

/**
 * avstyml文件
 */
public class SysYmlCache {
    private static SysYmlParam sysYmlParam=null;

    public static synchronized SysYmlParam getSysYmlParam() {
        if(null == sysYmlParam){
            initSysYmlParam();
        }
        return sysYmlParam;
    }

    public static synchronized void setSysYmlParam(SysYmlParam sysYmlParam) {
        SysYmlCache.sysYmlParam = sysYmlParam;
    }


    public static synchronized void initSysYmlParam(){
        MainService mainService= SpringUtil.getBean(MainService.class);
        RResult result_=new RResult();
        mainService.getNavList(result_);
    }
}
