package com.avst.trm.v1.web.cweb.conf;

import com.avst.trm.v1.common.util.OpenUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 用户密码key
 */
public class CheckPasswordKey {

    public static String key_path="";//文件地址


    /**
     * 创建key
     * @param str
     * @return
     */
    public static String CreateKey(String str){
        if (StringUtils.isNotBlank(str)){
            //检测是否存在key
        }
        return null;
    }


    /**
     * 检测key
     * @param key_path
     * @return
     */
    public static boolean CheckKey(String key_path){
        if (StringUtils.isNotBlank(key_path)){

        }
        return false;
    }


}
