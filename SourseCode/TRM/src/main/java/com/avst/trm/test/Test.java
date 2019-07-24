package com.avst.trm.test;

import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class Test {

   public static void main(String[] args) {

//      upload();
//
//      yaxml();

//      String xmSoursePath = OpenUtil.getXMSoursePath();
//      System.out.println(xmSoursePath);

      getini();
   }


   public static void getini(){
      try {
         //ini文件的存放位置
//            String filepath = "D:\\initest.ini";
         String filepath = OpenUtil.getXMSoursePath() + "\\pfconfig.ini";
         //创建文件输入流
         FileInputStream fis = new FileInputStream(filepath);

         InputStreamReader reader = new InputStreamReader(fis,"GBK");
         //创建文件输出流
//         OutputStream opt = null;
         //创建Properties属性对象用来接收ini文件中的属性
         Properties pps = new Properties();
         //从文件流中加载属性
         pps.load(reader);
         System.out.println(pps);
//            //通过getProperty("属性名")获取key对应的值
//            System.out.println(pps.getProperty("url1"));
//            System.out.println(pps.getProperty("url11"));
//            //加载读取文件流
//            opt = new FileOutputStream(filepath);
//            //通过setProperty(key,value)赋值，会覆盖相同key的值
//            pps.setProperty("url2", "v2");
//            pps.setProperty("url1", "v1");
//            //修改值 (必不可少)
//            pps.store(opt, null);
         reader.close();
         fis.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
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
