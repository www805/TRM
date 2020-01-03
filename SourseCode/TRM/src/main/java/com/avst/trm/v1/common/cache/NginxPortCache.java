package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import java.util.List;

/**
 * nginx端口缓存
 */
public class NginxPortCache {

    private static String nginxPort = null;

    public static synchronized String getNginxPort() {
        if(null == nginxPort){
            initNginxPort();
        }
        return nginxPort;
    }

    public static synchronized void setNginxPort(String port) {
        NginxPortCache.nginxPort = port;
    }

    public static synchronized void delNginxPort() {
        nginxPort = null;
    }

    public static synchronized void initNginxPort(){

        String sysBasepath = PropertiesListenerConfig.getProperty("sysBasepath");
        String fileBasepath = sysBasepath.endsWith("/") ? sysBasepath : (sysBasepath + "/");
        String basepath = "";

        //NGINX。conf
        String nginxconfpath = fileBasepath + "other/nginx-1.8.1/nginx-1.8.1/conf/nginx.conf";
        List<String> nginxlist = ReadWriteFile.readTxtFileToList(nginxconfpath, "utf8");
        if (null != nginxlist && nginxlist.size() > 0) {
            for (String str : nginxlist) {
                if (str.trim().startsWith("listen ")) {
                    basepath = str.replace("listen ", "").replace(";","").trim();
                    break;
                }
            }
        }

        nginxPort = basepath;

    }


}
