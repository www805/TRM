package com.avst.trm.v1.common.util.sq;


import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.wb.deencode.EncodeUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 *  创建授权文件
 */
public class CreateSQ {

//    @Value("${Javakeyname}")
    private static String javakeyname="javatrm.ini";

    /**
     * 只有总站才会有的，本地运行都可以，不能部署到服务上去的类
     * 授权文件生成
     * @param sqEntity
     * @param basepath
     * @return
     */
    public static boolean deSQ(SQEntity sqEntity,String basepath){

        try {
            String sqcode=sqEntity.toString();
            System.out.println("授权创建前 sqcode:"+sqcode);
            String rr=EncodeUtil.encoderByDES(sqcode);
            System.out.println("授权创建后 rr:"+rr);

            String path=basepath+"\\"+ DateUtil.getSeconds() +"_"+javakeyname;
            ReadWriteFile.writeTxtFile(rr,path);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {

        SQEntity sqEntity= new SQEntity();
        sqEntity.setUnitCode("avst");
        sqEntity.setSqDay(10000);
        sqEntity.setSortNum(1);
        sqEntity.setServerType("police");
        sqEntity.setForeverBool(true);
        sqEntity.setClientName("吴斌客户端服务器");
        sqEntity.setCpuCode(NetTool.getLocalMac());
        sqEntity.setStartTime(DateUtil.getDateAndMinute());

        System.out.println(deSQ(sqEntity,"E:\\trmshouquan"));

    }

}
