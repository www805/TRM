package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.sq.NetTool;
import org.apache.commons.lang.StringUtils;

/**
 * 本机IP缓存
 */
public class ServerIpCache {

    private static String serverIp = null;

    public static synchronized String getServerIp() {
        if(null == serverIp){
            initServerIp();
        }
        return serverIp;
    }

    public static synchronized void setServerIp(String ip) {
        serverIp = ip;
    }

    public static synchronized void delServerIp() {
        serverIp = null;
    }

    public static synchronized void initServerIp(){
        Base_serverconfigMapper mapper = SpringUtil.getBean(Base_serverconfigMapper.class);
        Base_serverconfig base_serverconfig = mapper.selectById(1);
        if (null == base_serverconfig || null != base_serverconfig && StringUtils.isBlank(base_serverconfig.getServerip())) {
            serverIp = NetTool.getMyIP();//如果找不到，就用获取的ip
        } else {
            serverIp = base_serverconfig.getServerip();
        }
    }


}
