package com.avst.trm.v1.common.util.sq;


import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.ReadWriteFile;
import com.wb.deencode.EncodeUtil;

/**
 *  创建授权文件
 *  授权的UnitCode一定是有规则的，例如：最上面的服务器是hb,下一级hb_wh,hb_wh_hk,最下级的客户端服务器也是hb_wh_hk；
 *   当前的节点服务器和该节点的下级服务器（客户端服务器）UnitCode一致，只是SortNum不同，节点是0，其他自动在上一个数值上加1
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
            LogUtil.intoLog(CreateSQ.class,"授权创建前 sqcode:"+sqcode);
            String rr=EncodeUtil.encoderByDES(sqcode);
            LogUtil.intoLog(CreateSQ.class,"授权创建后 rr:"+rr);

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
        //授权的UnitCode一定是有规则的，例如：最上面的服务器是hb,下一级hb_wh,hb_wh_hk,最下级的客户端服务器也是hb_wh_hk；
        // 当前的节点服务器和该节点的下级服务器（客户端服务器）UnitCode一致，只是SortNum不同，节点是0，其他自动在上一个数值上加1
        sqEntity.setUnitCode("avst");
        sqEntity.setSqDay(10000);
        sqEntity.setSortNum(1);
        sqEntity.setServerType("police");
        sqEntity.setForeverBool(true);
        sqEntity.setClientName("吴斌客户端服务器");
        sqEntity.setCpuCode("7774727242454549724A4544");
        sqEntity.setGnlist("record|asr|tts|fd|ph");
        sqEntity.setStartTime(DateUtil.getDateAndMinute());

        LogUtil.intoLog(CreateSQ.class,deSQ(sqEntity,"E:\\trmshouquan"));


    }

}
