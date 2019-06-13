package com.avst.trm.test;

import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.LogUtil;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

public class Test {

   public static void main(String[] args) {

      upload();
   }


   public static void upload(){

      String actionUrl="http://192.168.17.175:8080/forDownServer/synchronizedata_file";
      String uploadFilePath="F:\\wubin\\笔录管理系统\\系统设计\\ceshi.mp4";


      Map<String , String> map =new HashMap<String , String>();
      map.put("sqNum","1234567");
//      LogUtil.intoLog(Test.class,HttpRequest.uploadToUrl(uploadFilePath,actionUrl,map));

      LogUtil.intoLog(Test.class,HttpRequest.uploadFile(actionUrl,uploadFilePath,map));

   }


}
