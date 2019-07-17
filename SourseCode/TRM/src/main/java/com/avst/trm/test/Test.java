package com.avst.trm.test;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Test {

   public static void main(String[] args) {

//      upload();
//
//      yaxml();

      String xmSoursePath = OpenUtil.getXMSoursePath();
      System.out.println(xmSoursePath);
   }


   public static void yaxml(){

      FileInputStream fis = null;

      try {

         fis = new FileInputStream("D:\\java\\TRM\\SourseCode\\hao.yml");

         AppCacheParam param = new AppCacheParam();

//         Map<String, Object> map = YamlUtils.yamlHandler(fis);
//
//         System.out.println(map);
//
//         Iterator<String> iterator = map.keySet().iterator();
//         while (iterator.hasNext()) {
//            String next = iterator.next();
//            System.out.println(next);
//         }

         //单文件处理
         Yaml yaml = new Yaml();
         Object object = yaml.load(fis);
         //这里只是简单处理，需要多个方式可以自己添加
         if (object instanceof Map) {
            Map map = (Map) object;
            System.out.println(map);

            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
               String next = iterator.next();
               System.out.println(next);

               Object re = map.get(next);
               System.out.println(re);



               if (re instanceof List) {

                  System.out.println("数组------------");
                  System.out.println(re);

                  List arr = (List) re;

                  for (Object o : arr) {
                     Map<String, Object> a = (Map<String, Object>) o;
                     Set<String> strings = a.keySet();
                     for (String string : strings) {
                        String name = (String) a.get("name");

                        System.out.println(name);
                        if ("list".equals(string)) {
                           List listString = (List) a.get("list");
                           System.out.println(listString);
                           for (Object o1 : listString) {
                              System.out.println(o1);
                           }
                        }
                     }



                  }


               }if(re instanceof String){
                  System.out.println("字符串------------");
                  System.out.println(re);

                  param.setTitle((String) re);
               }

            }

         }

      } catch (IOException e) {
         e.printStackTrace();
      }finally {
         if(null != fis){
            try {
               fis.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }




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
