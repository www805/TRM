package com.avst.trm.v1.common.util;

import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * java后台调用打开某一个EXE程序
 * 测试说明，暂时可以打开EXE、bat、lnk3中类型
 */
public class OpenEXE {

    public static boolean openEXE(String exePath){

        if(StringUtils.isEmpty(exePath)){
            LogUtil.intoLog(4,OpenEXE.class,"想要打开的程序的路径为空，exePath："+exePath);
            return false;
        }
        File file=new File(exePath);
        if(null!=file&&file.isFile()&&!file.isDirectory()){
            String cmd= "cmd /c start " + exePath;
            String cmdrr=ExecuteCMD.executeCMD(cmd);
            LogUtil.intoLog(1,OpenEXE.class,"执行打开程序命令的返回："+cmdrr);
        }else{
            LogUtil.intoLog(4,OpenEXE.class,"想要打开的程序的路径是一个不是文件的路径，exePath："+exePath);
            return false;
        }
       return true;
    }


    public static void main(String[] args) {
        System.out.println(openEXE("C:\\Users\\Administrator\\Desktop\\common\\notepad++.lnk"));
    }

}
