package com.avst.trm.test;
import	java.io.File;

import com.avst.trm.v1.common.util.FileUtil;

import java.util.List;

public class Test2  {

    public static Object object=new Object();

    public static void main(String[] args) {

//        List<String> filelist= FileUtil.getAllFilePath("d:/ftpdata",1);
//        System.out.println(filelist.size());

        String path="D:\\ftpdata\\sb3\\2019-09-02\\a08a1f4d944b489fa10dfc3eb5212b48_sxsba2\\测试案件导出.zip";
        System.out.println(new File(path).isFile());
    }

}
