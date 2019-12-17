package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;

import java.util.List;

/**
 * 系统修改IP
 */
public class ChangeIP {


    /**
     * 外网访问
     * 客户端修改IP，trm配置文件改IP
     *
     * @param localip
     * @param fileBasepath
     * @return
     */
    public static boolean otherPCConnectChangeIP(String fileBasepath, String localip) {

        if (null == fileBasepath || fileBasepath.trim().equals("")) {
            String sysBasepath = PropertiesListenerConfig.getProperty("sysBasepath");
            fileBasepath = sysBasepath;
        }
        fileBasepath = fileBasepath.endsWith("/") ? fileBasepath : (fileBasepath + "/");
        if (null == localip || localip.trim().equals("")) {
            localip = ServerIpCache.getServerIp();
        }
        boolean client_bool = false;

        String clientconfpath = fileBasepath + "桌面式应用/客户端/resources/app/main.js";
        List<String> clientlist = ReadWriteFile.readTxtFileToList(clientconfpath, "utf8");
        if (null != clientlist && clientlist.size() > 0) {
            String writetxt = "mainWindow.loadURL('http://" + localip + ":8080/cweb/');";
            String newfiletxt = "";
            for (String str : clientlist) {
                if (str.indexOf("mainWindow.loadURL") > -1) {
                    str = writetxt;
                }
                newfiletxt += str;
            }
            client_bool = ReadWriteFile.writeTxtFile(newfiletxt, clientconfpath);
            LogUtil.intoLog(1, ChangeIP.class, client_bool + ":client_bool,修改客户端IP是否成功");
        }

        boolean staticPath_bool = false;
        String staticconfpath = fileBasepath + "桌面式应用/客户端/page/index.html";
        List<String> pathlist = ReadWriteFile.readTxtFileToList(staticconfpath, "utf8");
        if (null != pathlist && pathlist.size() > 0) {
            String writetxt = "var url = \"http://" + localip + ":8080/cweb/\";//笔录系统首页地址";
            String newfiletxt = "";
            for (String str : pathlist) {
                if (str.indexOf("var url = \"http://") > -1) {
                    str = writetxt;
                }
                newfiletxt += str + "\n";
            }
            staticPath_bool = ReadWriteFile.writeTxtFile(newfiletxt, staticconfpath);
            LogUtil.intoLog(1, ChangeIP.class, staticPath_bool + ":client_bool,修改静态页面url地址是否成功");
        }

        String trmconfpath = fileBasepath + "WORKJAR/trm.properties";
        List<String> trmlist = ReadWriteFile.readTxtFileToList(trmconfpath, "utf8");
        boolean trmbool = false;
        if (null != trmlist && trmlist.size() > 0) {

            String writetxt1 = "upload.basepath=http://" + localip;
            String writetxt2 = "socketio.server.host=" + localip;

            String newfiletxt = "";
            for (String str : trmlist) {
                if (str.indexOf("upload.basepathL") > -1) {
                    str = writetxt1;
                }
                if (str.indexOf("socketio.server.host") > -1) {
                    str = writetxt2;
                }
                newfiletxt += str;
            }
            trmbool = ReadWriteFile.writeTxtFile(newfiletxt, trmconfpath);
            LogUtil.intoLog(1, ChangeIP.class, trmbool + ":trmbool,修改trm配置文件是否成功");
        }

        String ecconfpath = fileBasepath + "WORKJAR/ec.properties";
        List<String> eclist = ReadWriteFile.readTxtFileToList(ecconfpath, "utf8");
        boolean ecbool = false;
        if (null != eclist && eclist.size() > 0) {

            String writetxt1 = "httpbasestaticpath=http://" + localip;

            String newfiletxt = "";
            for (String str : eclist) {
                if (str.indexOf("httpbasestaticpath") > -1) {
                    str = writetxt1;
                }
                newfiletxt += str;
            }
            ecbool = ReadWriteFile.writeTxtFile(newfiletxt, ecconfpath);
            LogUtil.intoLog(1, ChangeIP.class, ecbool + ":ecbool,修改ec配置文件是否成功");
        }

        LogUtil.intoLog(1, ChangeIP.class, trmbool + ":trmbool,client_bool:" + client_bool + ",ecbool:" + ecbool);
        if (trmbool && client_bool && ecbool) {
            return true;
        }


        return false;
    }

    public static void main(String[] args) {
        System.out.println(NetTool.getMyIP());
    }

    //替换IP,笔录服务器IP、嵌入式设备IP、测谎仪IP、语音识别模型IP
    //数据库中修改IP，系统中的文件修改IP

    /**
     * param blip         笔录客户端IP
     * param fdip         录像主机（嵌入式 ）ip
     * param phip         测谎仪IP
     * param asrip        语音识别模型IP
     * @param fileBasepath 安装主目录地址
     * @return
     */
    public static boolean updateIp(GetServerIpParam getServerIpParam, String fileBasepath) {

        String blip = getServerIpParam.getTrmip();
        String asrip = getServerIpParam.getAsrip().getEtip();

        //先修改数据库数据
        EquipmentControl ec = SpringUtil.getBean(EquipmentControl.class);

        RResult rResult = ec.updateServerIp(getServerIpParam);

        fileBasepath = fileBasepath.endsWith("/") ? fileBasepath : (fileBasepath + "/");
        //系统文件修改
        boolean outbool = otherPCConnectChangeIP(fileBasepath, blip);
        //asrconf修改
        String asrconfpath = fileBasepath + "other/asr-http/asr/audioconfig.xml";
        String asrtxt = ReadWriteFile.readTxtFileToStr(asrconfpath);
        boolean asrbool = false;
        if (null != asrtxt && !asrtxt.trim().equals("")) {
            asrtxt = "<root><audioip>" + asrip + "</audioip><audioport>12000</audioport><localport>8000</localport><debug>false</debug><console>false</console></root>";
            asrbool = ReadWriteFile.writeTxtFile(asrtxt, asrconfpath);
            LogUtil.intoLog(1, ChangeIP.class, asrbool + ":asrbool,修改asr配置文件是否成功");
        }

        LogUtil.intoLog(1, ChangeIP.class, asrbool + ":asrbool,outbool:" + outbool);
        if (asrbool && outbool) {
            return true;
        }
        return false;
    }


}
