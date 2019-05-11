package com.avst.trm.v1.report.toupserver.common.conf;

import com.avst.trm.v1.common.datasourse.base.entity.BaseEntity;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.GetSynchronizedDataSheet_DownServer;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizedataParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 专门为查找对应表明的所有数据
 */
public class AddDataToSynchronizeDataConf {

    /**
     * 检测数据库表中所有数据添加到list中
     * 需要再次优化
     * @return
     */
    public static List<StartSynchronizedata_2_Param> addDataToSynchronizeData(
            List<StartSynchronizedata_2_Param> datalist,String datatablename){

        StartSynchronizedata_2_Param data=new StartSynchronizedata_2_Param();
        BaseMapper<BaseEntity> mapper=(BaseMapper)SpringUtil.getBean(datatablename);
        EntityWrapper<BaseEntity> entityWrapper=new EntityWrapper<BaseEntity>();
        List<BaseEntity> list=mapper.selectList(entityWrapper);

        //不需要去重
        if(null!=list&&list.size() > 0){

            if(null==datalist){
                datalist=new ArrayList<StartSynchronizedata_2_Param>();
            }
            Base_filesaveMapper base_filesaveMapper=(Base_filesaveMapper)SpringUtil.getBean(Base_filesaveMapper.class);
            for(BaseEntity entity:list){
                StartSynchronizedata_2_Param p=new StartSynchronizedata_2_Param(datatablename,1,entity.getSsid());
                datalist.add(p);

                //现阶段只有2张表才有文件同步 ，但还是全部表查找
                addFileToSynchronizeData(datalist,p,base_filesaveMapper);

            }
        }
        return datalist;
    }

    /**
     * 检测数据库表中所有数据添加到list中
     * 需要再次优化
     * @return
     */
    public static List<StartSynchronizedata_2_Param> addDataToSynchronizeData(
            List<StartSynchronizedata_2_Param> datalist,List<Base_datainfo> tablelist){

        if(null!=tablelist&&tablelist.size() > 0){
            for(Base_datainfo data:tablelist){
                datalist=addDataToSynchronizeData(datalist,data.getDataname());
            }
        }

        return datalist;
    }



    /**
     * 检测数据中是否有文件，有的话就添加到list中
     * 需要再次优化
     * @return
     */
    public static List<StartSynchronizedata_2_Param> addFileToSynchronizeData(
            List<StartSynchronizedata_2_Param> datalist,StartSynchronizedata_2_Param param,
            Base_filesaveMapper base_filesaveMapper){

        int type=param.getType();

        if(1==type){//只有数据中才有文件路径才会进行添加文件
            String datassid=param.getDatassid();
            //现阶段只有2张表需要文件同步
            EntityWrapper<Base_filesave> ew = new EntityWrapper();
            ew.eq("datassid",datassid);
            List<Base_filesave> list=base_filesaveMapper.selectList(ew);
            if(null!=list&&list.size() > 0){
                for(Base_filesave file:list){
                    StartSynchronizedata_2_Param p=new StartSynchronizedata_2_Param(
                            "base_filesave",2,file.getSsid());//文件保存表的数据
                    datalist.add(p);
                }
            }
        }

        //执行数据去重
        if(null!=datalist&&datalist.size() > 0){
            System.out.println(datalist.size()+"个数据--去重开始时间："+ DateUtil.getSeconds());
            datalist=QCList(datalist);
            System.out.println(datalist.size()+"个数据--去重开始时间："+ DateUtil.getSeconds());
        }

        return datalist;
    }

    /**
     * StartSynchronizedata_2_Param对象去重
     * @param list
     * @return
     */
    public static List<StartSynchronizedata_2_Param> QCList(List<StartSynchronizedata_2_Param> list){
        if(null==list||list.size() == 0){
            return list;
        }
        HashMap<String,StartSynchronizedata_2_Param> tempMap = new HashMap<>();
        for (StartSynchronizedata_2_Param StartSynchronizedata_2_Param : list) {
            String key = StartSynchronizedata_2_Param.getDatassid();
            tempMap.put(key, StartSynchronizedata_2_Param);
        }
        List<StartSynchronizedata_2_Param> tempList = new ArrayList<>();
        for(String key : tempMap.keySet()){
            tempList.add(tempMap.get(key));
        }
        return tempList;
    }
    

    /**
     * 对比等待同步的数据和已同步的数据得到真正需要同步到该IP的上级服务器中
     * @param datalist
     * @param list
     * @return
     */
    public static List<StartSynchronizedata_2_Param> checkDataToSynchronizeData(
            List<StartSynchronizedata_2_Param> datalist, List<GetSynchronizedDataSheet_DownServer> list){

        if(null==list||list.size()==0){
            for(GetSynchronizedDataSheet_DownServer sdd:list){//已同步的数据
                for(int i=0;i<datalist.size();i++){//所有等待同步的数据
                    StartSynchronizedata_2_Param sdw=datalist.get(i);
                    if(sdw.getDatassid().equals(sdd.getSsid())){//一旦有相同的就删除带同步的数据
                        datalist.remove(i);
                        break;
                    }
                }
            }
        }
        return datalist;
    }

    /**
     * 给指定的表进行数据库查询并赋值
     * 数据库base_filesave的同步要不要在base_datainfo中写4个，对应4中同步类型
     * @param param
     * @return
     */
    public static SynchronizedataParam getSynchronizedataParam(SynchronizedataParam param,String datassid){

        String tablename=param.getDataname();
        BaseMapper mapper=(BaseMapper)SpringUtil.getBean(tablename+"Mapper");
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("id",datassid);
        param.setT(mapper.selectList(entityWrapper).get(0));//把数据用泛型的方式进去





        //上级服务器转成对应的实体类需要使用
//        switch (tablename){
//            case SynDataTableConstant.tablepolice_answer:
//
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_arraignment:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_case:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_casetoarraignment:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_problem:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_problemtotype:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_problemtype:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_record:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_workunit:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_StartSynchronizedata_2_Paramto:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_StartSynchronizedata_2_Paraminfo:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_templatetype:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_templatetotype:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_templatetoproblem:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_template:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_recordtype:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_recordtemplatetoproblem:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_recordtemplate:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablepolice_recordreal:
//                param.setT(null);
//                break;
//            case SynDataTableConstant.tablebase_filesave:
//                param.setT(null);
//                break;
//
//            default:;
//        }

        return param;
    }




}
