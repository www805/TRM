package com.avst.trm.v1.common.util.uploadfile;

import com.avst.trm.v1.common.util.uploadfile.param.FileParam;
import com.avst.trm.v1.common.util.uploadfile.param.UploadFileParam;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UploadFileCache {

    private static List<UploadFileParam> uploadFileList=null;


    public static UploadFileParam getUploadFile(String ssid){

        if(null!=uploadFileList&&uploadFileList.size() > 0){
            for(UploadFileParam param:uploadFileList){
                if(StringUtils.isNotEmpty(param.getSsid())&&param.getSsid().equals(ssid)){
                    return param;
                }
            }
        }
        return null;
    }

    public static FileParam getUploadFile(String ssid,String filepath){

        UploadFileParam uploadFileParam=getUploadFile(ssid);
        if(null!=uploadFileParam&&uploadFileParam.getFileList()!=null){
            List<FileParam> fileParamList=uploadFileParam.getFileList();
            for(FileParam fileParam:fileParamList){
                if(fileParam.getFilePath().equals(filepath)){
                    return fileParam;
                }
            }
        }
        return null;
    }

    public static List<FileParam> getFileList(String ssid){

        UploadFileParam uploadFileParam=getUploadFile(ssid);
        if(null!=uploadFileParam&&uploadFileParam.getFileList()!=null){
            return uploadFileParam.getFileList();
        }
        return null;
    }


    public static void setUploadFile(UploadFileParam uploadFileParam){

        if(null==uploadFileList){
            uploadFileList=new ArrayList<UploadFileParam>();
        }

        boolean add_bool=true;
        if(uploadFileList.size() > 0){
            for(int i=0;i<uploadFileList.size();i++){
                UploadFileParam param=uploadFileList.get(i);
                if(StringUtils.isNotEmpty(param.getSsid())
                        &&StringUtils.isNotEmpty(uploadFileParam.getSsid())
                        &&param.getSsid().equals(uploadFileParam.getSsid())){
                    param=uploadFileParam;
                    add_bool=false;
                    break;
                }
            }
        }
        if(add_bool){
            uploadFileList.add(uploadFileParam);
        }
    }


    public static void setFileParam(String ssid,FileParam fileParam){

        List<FileParam>  filelist=getFileList(ssid);
        if(null==filelist){
            filelist=new ArrayList<FileParam>();
        }
        boolean add_bool=true;
        if(filelist.size() > 0){
            for(int i=0;i<filelist.size();i++){
                FileParam param=filelist.get(i);
                if(StringUtils.isNotEmpty(param.getFilePath())
                        &&StringUtils.isNotEmpty(fileParam.getFilePath())
                        &&param.getFilePath().equals(fileParam.getFilePath())){
                    param=fileParam;
                    add_bool=false;
                    break;
                }
            }
        }
        if(add_bool){
            filelist.add(fileParam);
        }

        setFileParamList(ssid,filelist);
    }

    public static void setFileParamList(String ssid,List<FileParam> fileList){

        UploadFileParam uploadFileParam=getUploadFile(ssid);

        if(null!=uploadFileParam&&fileList!=null){
            uploadFileParam.setFileList(fileList);
        }
    }

    public static boolean checkBool(String ssid,boolean bool,String filepath){

        if(null==uploadFileList||uploadFileList.size() == 0){
            return false;
        }

        FileParam fileParam = getUploadFile(ssid,filepath);
        if(null!=fileParam){
            if((fileParam.isBool()&&bool)
            ||(!fileParam.isBool()&&!bool)){//只有不同的时候才会改变bool，这里说明相同，就不需要

            }else{

                fileParam.setBool(bool);
                setFileParam(ssid,fileParam);

                //计数，完成数
                changeDoneNum(ssid,bool);

            }
        }

        return true;
    }

    private static void changeDoneNum(String ssid,boolean bool){

        UploadFileParam param=getUploadFile(ssid);
        if(null!=param){//这一步其实是多余的
            int num=param.getDoneFileCount();
            if(bool){//新完成了一个
                num++;
            }else{//检测，上传失败了一个，一般情况不会出现
                num--;
            }
            param.setDoneFileCount(num);
            param.setUploadNum(param.getUploadNum()+1);
            setUploadFile(param);
        }
    }


    public static boolean delUploadFile(String ssid){

        if(null!=uploadFileList&&uploadFileList.size() > 0){
            for(int i=0;i<uploadFileList.size();i++){
                UploadFileParam param=uploadFileList.get(i);
                if(StringUtils.isNotEmpty(param.getSsid())&&param.getSsid().equals(ssid)){

                    uploadFileList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
}
