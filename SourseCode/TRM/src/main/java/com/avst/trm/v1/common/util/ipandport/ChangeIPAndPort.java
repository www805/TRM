package com.avst.trm.v1.common.util.ipandport;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQVersion;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 系统修改IP或者端口
 */
public class ChangeIPAndPort {


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

        //获取授权信息
        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
        String gnlist = getSQEntity.getGnlist();

        boolean staticPape_bool = false;
        String staticconfpath = fileBasepath + "桌面式应用/客户端/page/index.html";
        List<String> pathlist = ReadWriteFile.readTxtFileToList(staticconfpath, "utf8");
        if (null != pathlist && pathlist.size() > 0) {
            String serverport=PropertiesListenerConfig.getProperty("server.port");
            if(StringUtils.isEmpty(serverport)){
                serverport="6060";
            }
            String writetxt = "var url = \"http://" + localip + ":"+serverport+"/cweb/\";//笔录系统首页地址";
            String newfiletxt = "";
            for (String str : pathlist) {
                if (str.indexOf("var url = \"http://") > -1) {
                    str = writetxt;
                }
                newfiletxt += str + "\n";
            }
            staticPape_bool = ReadWriteFile.writeTxtFile(newfiletxt, staticconfpath);
            LogUtil.intoLog(1, ChangeIPAndPort.class, staticPape_bool + ":staticPape_bool,修改静态页面url地址是否成功");
        }

        String trmconfpath = fileBasepath + "WORKJAR/trm.properties";
        List<String> trmlist = ReadWriteFile.readTxtFileToList(trmconfpath, "utf8");
        boolean trmbool = false;
        if (null != trmlist && trmlist.size() > 0) {

            String writetxt1 = "upload.basepath=http://" + localip;
            String writetxt2 = "socketio.server.host=" + localip;
            String serverport=PropertiesListenerConfig.getProperty("server.port");
            if(StringUtils.isEmpty(serverport)){
                serverport="6060";
            }
            String writetxt3 = "re.basepath=http://" + localip + ":"+serverport+"/toup";

            //取出授权缓存判断如果是客户端版就用localhost
            if (gnlist.indexOf(SQVersion.S_E) == -1) {
                writetxt1 = "upload.basepath=http://localhost" ;
                writetxt2 = "socketio.server.host=localhost" ;
            }

            String newfiletxt = "";
            for (String str : trmlist) {
                if (str.indexOf("upload.basepathL") > -1) {
                    str = writetxt1;
                }
                if (str.indexOf("socketio.server.host") > -1) {
                    str = writetxt2;
                }
                if (str.indexOf("re.basepath") > -1) {
                    str = writetxt3;
                }
                newfiletxt += str + "\n";
            }
            trmbool = ReadWriteFile.writeTxtFile(newfiletxt, trmconfpath);
            LogUtil.intoLog(1, ChangeIPAndPort.class, trmbool + ":trmbool,修改trm配置文件是否成功");
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
                newfiletxt += str + "\n";
            }
            ecbool = ReadWriteFile.writeTxtFile(newfiletxt, ecconfpath);
            LogUtil.intoLog(1, ChangeIPAndPort.class, ecbool + ":ecbool,修改ec配置文件是否成功");
        }

        LogUtil.intoLog(1, ChangeIPAndPort.class, trmbool + ":trmbool,staticPape_bool:" + staticPape_bool + ",ecbool:" + ecbool);
        if (trmbool && staticPape_bool && ecbool) {
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
     * getServerIpParam
     * @param fileBasepath 安装主目录地址
     * @return
     */
    public static boolean updateIp(GetServerIpParam getServerIpParam, String fileBasepath) {

        String blip = getServerIpParam.getTrmip();

        //先修改数据库数据
        EquipmentControl ec = SpringUtil.getBean(EquipmentControl.class);

        boolean ecupdateipbool=false;
        RResult rResult = ec.updateServerIp(getServerIpParam);
        if(null!=rResult&&rResult.getActioncode().equals(Code.SUCCESS.toString())){
            ecupdateipbool=true;
        }

        fileBasepath = fileBasepath.endsWith("/") ? fileBasepath : (fileBasepath + "/");
        //系统文件修改
        boolean outbool = otherPCConnectChangeIP(fileBasepath, blip);

        LogUtil.intoLog(1, ChangeIPAndPort.class, ecupdateipbool+":ecupdateipbool----outbool:" + outbool);
        if (outbool&&ecupdateipbool) {
            return true;
        }
        return false;
    }

    /**
     * 修改平台端口
     * @return
     */
    public static boolean updatePort(UpdatePortParam updatePortParam){

        //trm/ec/mc/zk配置文件
        //nginx,conf的配置文件
        //数据库，ec的asr_etinfo中的backtxtinterface
        //数据库，ec的ss_saveinfo中的port

        String fileBasepath=updatePortParam.getAnzhuangpath();
        if (null == fileBasepath || fileBasepath.trim().equals("")) {
            String sysBasepath = PropertiesListenerConfig.getProperty("sysBasepath");
            fileBasepath = sysBasepath;
        }
        fileBasepath = fileBasepath.endsWith("/") ? fileBasepath : (fileBasepath + "/");

        int ecserverport=updatePortParam.getEcserverport();
        //ec.priperties
        if(ecserverport>0){
            //ec
            String ecconfpath = fileBasepath + "WORKJAR/ec.properties";
            List<String> eclist = ReadWriteFile.readTxtFileToList(ecconfpath, "utf8");
            boolean ecbool = false;
            if (null != eclist && eclist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : eclist) {
                    if (str.indexOf("server.port") > -1) {
                        str="server.port="+ecserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    ecbool = ReadWriteFile.writeTxtFile(newfiletxt, ecconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, ecbool + ":ecbool,修改ec配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"ec.properties没有读到数据");
            }

            //数据库修改
            //数据库，ec的asr_etinfo中的backtxtinterface的端口修改

        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,ecserverport+":ecserverport ,端口必须是大于0的才会进行修改");
        }

        int trmserverport=updatePortParam.getTrmserverport();
        //trm.priperties
        if(trmserverport>0){
            String trmconfpath = fileBasepath + "WORKJAR/trm.properties";
            List<String> trmlist = ReadWriteFile.readTxtFileToList(trmconfpath, "utf8");
            boolean trmbool = false;
            if (null != trmlist && trmlist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : trmlist) {
                    if (str.indexOf("server.port") > -1) {
                        str="server.port="+trmserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    trmbool = ReadWriteFile.writeTxtFile(newfiletxt, trmconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, trmbool + ":trmbool,修改trm配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"trm.properties没有读到数据");
            }
        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,trmserverport+":trmserverport ,端口必须是大于0的才会进行修改");
        }

        int zkserverport=updatePortParam.getZkserverport();
        //zk.priperties
        if(zkserverport>0){
            String zkconfpath = fileBasepath + "WORKJAR/zk.properties";
            List<String> zklist = ReadWriteFile.readTxtFileToList(zkconfpath, "utf8");
            boolean zkbool = false;
            if (null != zklist && zklist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : zklist) {
                    if (str.indexOf("server.port") > -1) {
                        str="server.port="+zkserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    zkbool = ReadWriteFile.writeTxtFile(newfiletxt, zkconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, zkbool + ":zkbool,修改trm配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"zk.properties没有读到数据");
            }
        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,zkserverport+":zkserverport ,端口必须是大于0的才会进行修改");
        }

        int mcserverport=updatePortParam.getMcserverport();
        //mc.priperties
        if(mcserverport>0){
            String mcconfpath = fileBasepath + "WORKJAR/mc.properties";
            List<String> mclist = ReadWriteFile.readTxtFileToList(mcconfpath, "utf8");
            boolean mcbool = false;
            if (null != mclist && mclist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : mclist) {
                    if (str.indexOf("server.port") > -1) {
                        str="server.port="+mcserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    mcbool = ReadWriteFile.writeTxtFile(newfiletxt, mcconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, mcbool + ":mcbool,修改mc配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"mc.properties没有读到数据");
            }
        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,mcserverport+":mcserverport ,端口必须是大于0的才会进行修改");
        }

        int ftpserverport=updatePortParam.getFtpserverport();
        if(ftpserverport>0) {
            //ec
            String ecconfpath = fileBasepath + "WORKJAR/ec.properties";
            List<String> eclist = ReadWriteFile.readTxtFileToList(ecconfpath, "utf8");
            boolean ecbool = false;
            if (null != eclist && eclist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool = true;//如果是false就不修改文件
                for (String str : eclist) {
                    if (str.indexOf("ftpport") > -1) {
                        str = "ftpport=" + ftpserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if (updatebool) {
                    ecbool = ReadWriteFile.writeTxtFile(newfiletxt, ecconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, ecbool + ":ecbool,修改ec配置文件是否成功");
                }
            } else {
                LogUtil.intoLog(4, ChangeIPAndPort.class, "ec.properties没有读到数据");
            }

            //数据库，ec的ss_saveinfo中的port
            //设备集中控制ftp的port也要修改
        }

        int nginxserverport=updatePortParam.getNginxserverport();
        //修改ec的httpbasestaticpath和trm的upload.basepath
        if(nginxserverport>0){
            //ec
            String ecconfpath = fileBasepath + "WORKJAR/ec.properties";
            List<String> eclist = ReadWriteFile.readTxtFileToList(ecconfpath, "utf8");
            boolean ecbool = false;
            if (null != eclist && eclist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : eclist) {
                    if (str.indexOf("httpbasestaticpath") > -1) {

                        String[] arr=str.split(":");
                        if(null!=arr&&arr.length>1){
                            if(arr.length==2||arr.length==3){
                                str=arr[0]+":"+arr[1]+":"+nginxserverport;
                            }else{
                                LogUtil.intoLog(4, ChangeIPAndPort.class, str + ":httpbasestaticpath的配置异常，请检查");
                                updatebool=false;
                                break;
                            }
                        }else{
                            LogUtil.intoLog(4, ChangeIPAndPort.class, str + ":httpbasestaticpath的配置异常，请检查");
                            updatebool=false;
                            break;
                        }
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    ecbool = ReadWriteFile.writeTxtFile(newfiletxt, ecconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, ecbool + ":ecbool,修改ec配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"ec.properties没有读到数据");
            }

            //trm
            String trmconfpath = fileBasepath + "WORKJAR/trm.properties";
            List<String> trmlist = ReadWriteFile.readTxtFileToList(trmconfpath, "utf8");
            boolean trmbool = false;
            if (null != trmlist && trmlist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : trmlist) {
                    if (str.indexOf("upload.basepath") > -1) {

                        String[] arr=str.split(":");
                        if(null!=arr&&arr.length>1){
                            if(arr.length==2||arr.length==3){
                                str=arr[0]+":"+arr[1]+":"+nginxserverport;
                            }else{
                                LogUtil.intoLog(4, ChangeIPAndPort.class, str + ":upload.basepath的配置异常，请检查");
                                updatebool=false;
                                break;
                            }
                        }else{
                            LogUtil.intoLog(4, ChangeIPAndPort.class, str + ":upload.basepath的配置异常，请检查");
                            updatebool=false;
                            break;
                        }
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    trmbool = ReadWriteFile.writeTxtFile(newfiletxt, trmconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, trmbool + ":trmbool,修改trm配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"trm.properties没有读到数据");
            }

            //NGINX。conf
            String nginxconfpath = fileBasepath + "other/nginx-1.8.1/nginx-1.8.1/conf/nginx.conf";
            List<String> nginxlist = ReadWriteFile.readTxtFileToList(nginxconfpath, "utf8");
            boolean nginxbool = false;
            if (null != nginxlist && nginxlist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : nginxlist) {
                    if (str.trim().startsWith("listen ")) {
                        str="listen   "+nginxserverport+";";
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    trmbool = ReadWriteFile.writeTxtFile(newfiletxt, nginxconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, nginxbool + ":nginxbool,修改nginx配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"nginx.conf没有读到数据");
            }
        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,ecserverport+":ecserverport ,端口必须是大于0的才会进行修改");
        }

        int socketioserverport=updatePortParam.getSocketioserverport();
        //trm
        if(socketioserverport>0){
            String trmconfpath = fileBasepath + "WORKJAR/trm.properties";
            List<String> trmlist = ReadWriteFile.readTxtFileToList(trmconfpath, "utf8");
            boolean trmbool = false;
            if (null != trmlist && trmlist.size() > 0) {
                String newfiletxt = "";
                boolean updatebool=true;//如果是false就不修改文件
                for (String str : trmlist) {
                    if (str.indexOf("socketio.server.port=") > -1) {
                        str="socketio.server.port="+socketioserverport;
                    }
                    newfiletxt += str + "\n";
                }
                if(updatebool){
                    trmbool = ReadWriteFile.writeTxtFile(newfiletxt, trmconfpath);
                    LogUtil.intoLog(1, ChangeIPAndPort.class, trmbool + ":trmbool,修改trm配置文件是否成功");
                }
            }else{
                LogUtil.intoLog(4,ChangeIPAndPort.class,"trm.properties没有读到数据");
            }
        }else{
            LogUtil.intoLog(3,ChangeIPAndPort.class,trmserverport+":trmserverport ,端口必须是大于0的才会进行修改");
        }



        return false;
    }


}
