package com.avst.trm.v1.web.cweb.conf;


import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wb.deencode.DeCodeUtil;
import com.wb.deencode.EncodeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户密码key
 */
public class CheckPasswordKey {

    private static String key_name="resetuser.ini";
    public static String key_path=OpenUtil.getJDKorJREPath() +key_name;//检测文件夹地址


    /**
     * 创建key
     * @param encryptedtext 加密文本
     * @return
     */
    public static boolean CreateKey(String encryptedtext){

        File file = new File(key_path);
        if (file.exists()&& !file.isDirectory()) {
            LogUtil.intoLog(1,CheckPasswordKey.class,"key存在不需要创建："+encryptedtext);
            return true;
        }
        if (StringUtils.isNotBlank(encryptedtext)){
            //检测是否存在key
            LogUtil.intoLog(1,CheckPasswordKey.class,"密码加密___加密文本__前："+encryptedtext);
             encryptedtext= EncodeUtil.encoderByDES(encryptedtext);
             if (null!=encryptedtext){
                 LogUtil.intoLog(1,CheckPasswordKey.class,"密码加密___加密文本__后："+encryptedtext);
                 ReadWriteFile.writeTxtFile(encryptedtext,key_path,"utf8");
                 return true;
             }
        }
        return false;
    }


    /**
     * 检测key
     * @param decryptiontext 文字
     * @param objectMap 对应的对象
     * @return
     */
    public static boolean CheckKey( String decryptiontext, Map<String,String> objectMap){
        if (null==objectMap){
            LogUtil.intoLog(4,CheckPasswordKey.class,"密码解密失败___解密对象objectMap__"+objectMap);
            return false;
        }

        if (StringUtils.isBlank(decryptiontext)){
            LogUtil.intoLog(4,CheckPasswordKey.class,"密码解密失败___解密文本decryptiontext__"+decryptiontext);
            return false;
        }

        LogUtil.intoLog(1,CheckPasswordKey.class,"密码解密___解密文本__前："+decryptiontext);
        decryptiontext= DeCodeUtil.decoderByDES(decryptiontext);
        LogUtil.intoLog(1,CheckPasswordKey.class,"密码解密___解密文本__后："+decryptiontext);
        Gson gson=new Gson();
        Map<String,String> decryptionMap = null;


        try {
            //解密对象
            decryptionMap = gson.fromJson(decryptiontext, Map.class);
        } catch (JsonSyntaxException e) {
            return false;
        }


        if (null==decryptionMap){
                return false;
         }
            //盘对值是否相同
            boolean contain = false;
            for (Object o : objectMap.keySet()) {

                contain = decryptionMap.containsKey(o);
                if (contain) {
                    contain = objectMap.get(o).equals(decryptionMap.get(o));
                }
                if (!contain) {
                    return false;
                }
            }
            return true;


    }

    public static void main(String[] args) {
        Map<String,String> encryptedMap=new HashMap<>();
        encryptedMap.put("ssid","admininfo1");
        encryptedMap.put("loginaccount","admin");
        encryptedMap.put("registertime","1555995200000");
        Gson gson=new Gson();
        String encryptedtext = gson.toJson(encryptedMap);
        CheckPasswordKey.CreateKey(encryptedtext);


        Map<String,String> encryptedMap2=new HashMap<>();
        encryptedMap2.put("ssid","admininfo1");
        encryptedMap2.put("loginaccount","admin");
        encryptedMap2.put("registertime","1555995200000");

        CheckPasswordKey.CheckKey("E:\\resetuser.ini",encryptedMap2);
    }

}
