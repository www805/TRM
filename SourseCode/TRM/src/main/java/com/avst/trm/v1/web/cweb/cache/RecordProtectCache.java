package com.avst.trm.v1.web.cweb.cache;

import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.util.FileUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.web.cweb.cache.param.RecordProtectParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService2;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordByIdVO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wb.deencode.DeCodeUtil;
import com.wb.deencode.EncodeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存正在进行中的笔录信息：用于断电保护
 */
public class RecordProtectCache {
    private static List<RecordProtectParam> recordProtectList=null;// recordProtectList：笔录全部数据

    private static  String recordcachepath=getrecordcachepath()+File.separator+"cache"+File.separator;//文件夹位置

    private static Gson gson=new Gson();

    //获取当前目录的上一级目录
    public synchronized static String getrecordcachepath() {
        String xmsoursepath= OpenUtil.getXMSoursePath();
        if (StringUtils.isNotBlank(xmsoursepath)){
            File file = new File(xmsoursepath);
            xmsoursepath=file.getParent();
        }
        return  xmsoursepath;
    }

    /**
     * 获取缓存全部
     * @return
     */
    public synchronized static List<RecordProtectParam> getRecordProtectList() {
        if (null==recordProtectList){
            recordProtectList=new ArrayList<RecordProtectParam>();
        }
        LogUtil.intoLog(1,RecordProtectCache.class,"获取全部异常笔录地址__recordcachepath："+recordcachepath);
        List<String> filelist= FileUtil.getAllFilePath(recordcachepath,1);
        if(null!=filelist&&filelist.size() > 0) {
            for (String path_ : filelist) {
                try {
                    File file=new File(path_);
                    String filename=file.getName();
                    String recordProtectParam_string =    ReadWriteFile.readTxtFileToStr(path_);
                    if (null!=recordProtectParam_string){
                        try {
                            recordProtectParam_string= DeCodeUtil.decoderByDES(recordProtectParam_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        RecordProtectParam recordProtectParam = gson.fromJson(recordProtectParam_string, RecordProtectParam.class);//把JSON字符串转为对象
                        if(null!=recordProtectParam){
                            recordProtectList.add(recordProtectParam);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        File file=new File(path_);
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return recordProtectList;
    }


    /**
     * 根据笔录ssid获取某一份笔录
     * @param recordssid
     * @return
     */
    public static synchronized RecordProtectParam getRecordecordProtectBySsid(String recordssid){
        if(null==recordssid){
            return null;
        }
        List<String> filelist= FileUtil.getAllFilePath(recordcachepath,2);
        if(null!=filelist&&filelist.size() > 0) {
            for (String path_ : filelist) {
                File file=new File(path_);
                String filename=file.getName();
                if (StringUtils.isNotBlank(filename)&&filename.equals(recordssid)){
                    String recordProtectParam_string =    ReadWriteFile.readTxtFileToStr(path_);
                    if (null!=recordProtectParam_string){
                        try {
                            recordProtectParam_string= DeCodeUtil.decoderByDES(recordProtectParam_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        RecordProtectParam recordProtectParam = gson.fromJson(recordProtectParam_string, RecordProtectParam.class);//把JSON字符串转为对象
                        if(null!=recordProtectParam){
                            return recordProtectParam;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 更新一份缓存
     * @param
     * @param
     * @return
     */
    public static  synchronized boolean setRecordecordProtect(RecordProtectParam  recordProtectParam){
        if(null==recordProtectParam){
            return false;
        }
        try {
            String recordProtectParam_string = gson.toJson(recordProtectParam);
            if (null!=recordProtectParam_string){
                try {
                    recordProtectParam_string= EncodeUtil.encoderByDES(recordProtectParam_string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ReadWriteFile.writeTxtFile(recordProtectParam_string,recordcachepath+recordProtectParam.getRecordssid(),"utf8");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    /**
     * 删除缓存数据
     * @param recordssid
     * @return
     */
    public static synchronized  boolean delRecordecordProtect(String recordssid){
        if (null==recordssid){
            return false;
        }
        try {
            List<String> filelist= FileUtil.getAllFilePath(recordcachepath,2);
            if(null!=filelist&&filelist.size() > 0) {
                for (String path_ : filelist) {
                    File file=new File(path_);
                    String filename=file.getName();
                    if (StringUtils.isNotBlank(filename)&&filename.equals(recordssid)){
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
