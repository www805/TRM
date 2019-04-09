package com.avst.trm.v1.common.util.sq;


import com.avst.trm.v1.common.util.ReadWriteFile;
import com.wb.deencode.EncodeUtil;

/**
 *  创建授权文件
 */
public class CreateSQ {


    /**
     * 只有总站才会有的，本地运行都可以，不能部署到服务上去的类
     * 授权文件生成
     * @param sqEntity
     * @param basepath
     * @return
     */
    public static boolean deSQ(SQEntity sqEntity,String basepath){

        try {
            String unitname=sqEntity.getClientName();
            String sqcode=sqEntity.toString();
            System.out.println("授权创建前 sqcode:"+sqcode);
            String rr=EncodeUtil.encoderByDES(sqcode);
            System.out.println("授权创建后 rr:"+rr);

            String path=basepath+"\\"+unitname+".ini";
            ReadWriteFile.readTxtFileToStr(path,"utf8");

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
