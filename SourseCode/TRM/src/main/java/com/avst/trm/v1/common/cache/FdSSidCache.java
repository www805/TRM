package com.avst.trm.v1.common.cache;


import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetToOutFlushbonadingListParam;
import com.avst.trm.v1.feignclient.ec.vo.fd.Flushbonadinginfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 默认设备ssid缓存
 */
public class FdSSidCache {

    private static String fdssid=null;

    public static synchronized String getFdSSidCache() {
        if(null == fdssid){
            initFdSSidCache();
        }
        return fdssid;
    }

    public static synchronized void setFdSSidCache(String ssid) {
        FdSSidCache.fdssid = ssid;
    }

    public static synchronized void initFdSSidCache(){

        try {
            EquipmentControl equipmentControl = SpringUtil.getBean(EquipmentControl.class);

            ReqParam param = new ReqParam();
            GetToOutFlushbonadingListParam listParam = new GetToOutFlushbonadingListParam();
            listParam.setFdType(FDType.FD_AVST);
            param.setParam(listParam);
            RResult result = equipmentControl.getToOutDefault(param);

            Gson gson = new Gson();

            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())&&null!=result.getData()) {
                Flushbonadinginfo flushbonadinginfo=gson.fromJson(gson.toJson(result.getData()), Flushbonadinginfo.class);
                if (null!=flushbonadinginfo&&null!=flushbonadinginfo.getLivingurl()){
                    FdSSidCache.fdssid= flushbonadinginfo.getSsid();
                }
            }
        } catch (JsonSyntaxException e) {
            LogUtil.intoLog(FdSSidCache.class,"com.avst.trm.v1.common.cache.FdSSidCache  请求设备默认ssid失败....");
        }



    }


}
