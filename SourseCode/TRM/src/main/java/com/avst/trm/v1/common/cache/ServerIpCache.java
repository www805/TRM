package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.SystemIpUtil;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.standaloneweb.vo.GetNetworkConfigureVO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        List<String> ipList = new ArrayList<>();

        Map<String, List<GetNetworkConfigureVO>> map = SystemIpUtil.getLocalMachineInfo();
        Iterator<Map.Entry<String, List<GetNetworkConfigureVO>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<GetNetworkConfigureVO>> entry = iterator.next();
            List<GetNetworkConfigureVO> list = entry.getValue();
            for (GetNetworkConfigureVO vo : list) {
                ipList.add(vo.getIp());
            }
        }

        //如果能获取网卡就从网卡里面获取和数据库匹配，如果有一样的就用这一条
        if (ipList.size() > 0) {

            Boolean isIp = false;
            if(null != base_serverconfig && StringUtils.isNotBlank(base_serverconfig.getServerip())){
                for (String ip : ipList) {
                    if(ip.equals(base_serverconfig.getServerip())){
                        isIp = true;
                        serverIp = ip;
                        break;
                    }
                }
            }

            //如果没有就从网卡里面随意拿一条
            if(!isIp){
                serverIp = ipList.get(0);//如果数据库没有ip就随便用一个
            }

        //如果网卡获取为0就从数据库拿一条
        }else if (null != base_serverconfig && StringUtils.isNotBlank(base_serverconfig.getServerip())){
            serverIp = base_serverconfig.getServerip();
        }else{
            //如果数据库没有就从utl的方法拿一条
            serverIp = NetTool.getMyIP();//如果找不到，就用获取的ip
        }


    }


}
