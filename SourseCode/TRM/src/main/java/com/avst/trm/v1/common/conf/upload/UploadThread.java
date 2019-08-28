package com.avst.trm.v1.common.conf.upload;

import java.util.ArrayList;
import java.util.List;

public class UploadThread implements Runnable {

    private List<UploadParam> uploadList;

    public UploadThread(List<String> pathlist){
        uploadList=new ArrayList<UploadParam>();
        for(String path:pathlist){
            UploadParam uploadParam=new UploadParam();
            uploadParam.setPath(path);
            uploadList.add(uploadParam);
        }
    }

    @Override
    public void run() {

        synchronized (uploadList){
            while (uploadList.size()> 0){

                for(int i=0;i<uploadList.size();i++){
                    UploadParam upload=uploadList.get(i);
                    if(upload.getState()==0){//开始上传



                        //上传成功才会消除掉这条数据

                    }

                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}
