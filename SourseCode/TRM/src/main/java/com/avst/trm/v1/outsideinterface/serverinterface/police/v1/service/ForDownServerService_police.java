package com.avst.trm.v1.outsideinterface.serverinterface.police.v1.service;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_upserver;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_upserver;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.GetSynchronizedDataSheet_UpServer;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasheet_upserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_upserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.outsideinterface.conf.OutSideIntegerfaceCache;
import com.avst.trm.v1.outsideinterface.reqparam.BaseReqParam;
import com.avst.trm.v1.outsideinterface.serverinterface.ForDownServerBaseServiceInterface;
import com.avst.trm.v1.outsideinterface.serverinterface.common.cache.SynchronizedataCache;
import com.avst.trm.v1.outsideinterface.serverinterface.common.reqparam.StartSynchronizedata_1_Param;
import com.avst.trm.v1.outsideinterface.serverinterface.common.reqparam.StartSynchronizedata_2_Param;
import com.avst.trm.v1.outsideinterface.serverinterface.common.reqparam.SynchronizedataParam;
import com.avst.trm.v1.outsideinterface.serverinterface.common.vo.Param.StartSynchronizedataVOParam;
import com.avst.trm.v1.outsideinterface.serverinterface.common.vo.StartSynchronizedataVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ForDownServerService_police extends BaseService implements ForDownServerBaseServiceInterface {

    @Autowired
    private Base_datasynchroni_upserverMapper base_datasynchroni_upserverMapper;

    @Autowired
    private Base_datasheet_upserverMapper base_datasheet_upserverMapper;

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;


    @Override
    public void initsynchronizeddata(String sqCode,String sqNum,RResult result) {

        //验证sqNum是否已经有token，
        String token=OutSideIntegerfaceCache.getToupserverTBToken(sqNum);
        SQEntity sqEntity=AnalysisSQ.getSQEntity(sqCode);
        if(null==sqEntity){//授权有问题
            result.setMessage("授权有问题");
            result.setActioncode(CodeForSQ.ERROR100002);
        }else{

            if(sqNum.equals(sqEntity.getSortNum())){//一定要对应下级服务器的序号

                //单位编号对比，同单位才能同步
                //UnitCode有自己的生成规则
                if(sqEntity.getUnitCode().startsWith(AnalysisSQ.getSQEntity().getUnitCode())){

                    if(StringUtils.isEmpty(token)){//如果服务器中有设备的同步token，
                        result.setMessage("有没有同步完成的任务，请先同步完成");
                    }else{
                        result.setMessage("有没有同步完成的任务，请先同步完成");
                        //生成新的token
                        token=AnalysisSQ.getClientKey();
                    }
                    result.setData(token);
                    changeResultToSuccess(result);
                }else{
                    result.setMessage("授权码中的单位编号与上级服务器不同，请联系管理员");
                    result.setActioncode(CodeForSQ.ERROR100002);
                }
            }else{
                result.setMessage("授权码不是该序号的服务器的授权");
                result.setActioncode(CodeForSQ.ERROR100002);
            }
        }

    }

    @Override
    public void startSynchronizedata(BaseReqParam param, RResult result) {

        //先查看该序号的在缓存中有没有未完成的任务，没有就新增对应缓存并开始比较，有的话直接比较再添加
        String data=param.getData();
        String sortnum=param.getSqNum();
        if(StringUtils.isEmpty(data)){
            result.setMessage("同步的参数为空");
            return ;
        }
        List<StartSynchronizedata_2_Param> datalis=
                (List<StartSynchronizedata_2_Param>)JacksonUtil.stringToObjebt_2(data, new StartSynchronizedata_2_Param());

        if(null==datalis||datalis.size() == 0){
            result.setMessage("同步的参数为空");
            return ;
        }

        boolean initbool=true;//是否是没有一次全新的同步，如果有上次未同步完成的自动加进去，继续同步
        String token=param.getToken();
        StartSynchronizedata_1_Param startSynchronizedata_1_param=
                SynchronizedataCache.getSynchronizeData(token);
        List<StartSynchronizedata_2_Param> startSynchronizedata_2_paramlist=
                new ArrayList<StartSynchronizedata_2_Param>();
        if(null==startSynchronizedata_1_param){
            startSynchronizedata_1_param=new StartSynchronizedata_1_Param();
        }else{
            initbool=false;
            startSynchronizedata_2_paramlist=startSynchronizedata_1_param.getDatalist();
        }

        //查询该序号服务器的同步记录，对比同步数据表，找出本次同步真正需要同步的数据
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("dd.unitsort",param.getSqNum());
        //获取上级服务器的同步表中关于这个序号的同步记录
        List<GetSynchronizedDataSheet_UpServer> list=base_datasynchroni_upserverMapper.getSynchronizedDataSheet_UpServer(entityWrapper);

        //获取同步次数
        int sdnum=0;
        //写入本次同步缓存，
        if(null==list||list.size() == 0){
            startSynchronizedata_2_paramlist.addAll(datalis);
            SynchronizedataCache.addSynchronizedataListBySortnum(token,datalis);
        }else{
            sdnum=list.size();
            for(StartSynchronizedata_2_Param p:datalis){//下级传过来的等待同步的数据
                boolean bool=true;
                for(GetSynchronizedDataSheet_UpServer sd:list){//下级在上级中已经同步的数据
                    //判断下级服务器需要同步的ssid跟上级服务器中该序号的服务器已经同步过来的ssid进行对比
                    if(sd.getDatassid().equals(p.getDatassid())){
                        bool=false;
                        break;
                    }
                }
                if(bool){
                    SynchronizedataCache.addSynchronizedataBySortnum(token,p);
                }
            }
        }

        //返回给下级服务器
        List<StartSynchronizedata_2_Param> cachelist=SynchronizedataCache.getSynchronizeData(token).getDatalist();
        if(null!=cachelist&&cachelist.size() > 0){

            //同步表数据新增
            String newssid = OpenUtil.getUUID_32();
            Base_datasynchroni_upserver base_datasynchroni_upserver=new Base_datasynchroni_upserver();
            base_datasynchroni_upserver.setUploadcount((sdnum+1));
            base_datasynchroni_upserver.setSsid(newssid);
            base_datasynchroni_upserver.setUnitsort(Integer.parseInt(param.getSqNum()));
            base_datasynchroni_upserver.setLastuploadtime(new Date());
            int insert=base_datasynchroni_upserverMapper.insert(base_datasynchroni_upserver);
            LogUtil.intoLog(this.getClass(),"base_datasynchroni_upserverMapper.insert bool:"+insert);
            //写入缓存
            if(insert>=0){
                startSynchronizedata_1_param.setSdTableSsid(newssid);
                SynchronizedataCache.setSynchronizedataBySortnum(token,startSynchronizedata_1_param);
            }else{//如果不成功，直接删除缓存
                SynchronizedataCache.delSynchronizedataBySortnum(token,sortnum);
                result.setMessage("上级服务器新增同步数据关系失败");
                return ;
            }

            StartSynchronizedataVO vo=new StartSynchronizedataVO();
            List<StartSynchronizedataVOParam> datalist=new ArrayList<StartSynchronizedataVOParam>();
            vo.setTotal(cachelist.size());
            vo.setDatalist(cachelist);
            result.setData(vo);
            changeResultToSuccess(result);

        }else{
            result.setMessage("没有需要同步的数据");
        }
    }

    @Override
    public void synchronizedata(BaseReqParam param,RResult result,MultipartFile file,int servertype) {

        String token=param.getToken();
        String data=param.getData();
        if(StringUtils.isEmpty(data)){
            result.setMessage("同步的参数为空");
            return ;
        }

        SynchronizedataParam synchronizedataParam=(SynchronizedataParam)JacksonUtil.stringToObjebt_2(data, new SynchronizedataParam());

        if(null==synchronizedataParam||null==synchronizedataParam.getT()){
            result.setMessage("同步的参数为空");
            return ;
        }

        //查看缓存中是否有这一条同步数据
        StartSynchronizedata_1_Param startSynchronizedata_1_Param=SynchronizedataCache.getSynchronizeData(token);
        if(null==startSynchronizedata_1_Param){
            result.setMessage("本次同步任务不存在，请重试");
            return ;
        }
        List<StartSynchronizedata_2_Param> startSynchronizedata_2_paramslist=startSynchronizedata_1_Param.getDatalist();
        String datassid=synchronizedataParam.getSsid();
        StartSynchronizedata_2_Param startSynchronizedata_2_param = SynchronizedataCache.getSynchronizeData2(token,datassid);
        if(null==startSynchronizedata_2_param){
            result.setMessage("本次同步任务不存在，请重试");
            return ;
        }

        String tablename=synchronizedataParam.getDataname();
        int type=synchronizedataParam.getType();

        //开始执行新增数据操作，有文件的先同步文件，再同步数据库
        //同步文件
        String filepath="";
        if(servertype==2){//说明有文件同步
            String basefilepath= PropertiesListenerConfig.getProperty("spring.images.filePath");//后期再加上日期路径
            filepath=synchronizedata_file(file,basefilepath);
            if(StringUtils.isEmpty(filepath)){
                result.setMessage("文件数据同步失败，请注意");
                return ;
            }
        }
        //插入到指定的表中
        BaseMapper baseMapper=(BaseMapper) SpringUtil.getBean(tablename+"Mapper");
        int insert=synchronizedata_data(synchronizedataParam,baseMapper,datassid,filepath);

        if(insert==1){//插入成功

            // 修改同步表,这里不会有文件同步新增，文件同步新增
            Base_datasheet_upserver base_datasheet_upserver=new Base_datasheet_upserver();
            base_datasheet_upserver.setDatassid(datassid);
            base_datasheet_upserver.setDataname(tablename);
            if(type==2){//同步文件
                base_datasheet_upserver.setFiletype(1);
                base_datasheet_upserver.setFilename("");
            }else{
                base_datasheet_upserver.setFiletype(-1);
            }
            base_datasheet_upserver.setSsid(OpenUtil.getUUID_32());
            base_datasheet_upserver.setUpserverssid(startSynchronizedata_1_Param.getSdTableSsid());
            int insertdatasheet=base_datasheet_upserverMapper.insert(base_datasheet_upserver);
            if(insertdatasheet>=0){

                //修改缓存，
                startSynchronizedata_2_param.setOverwork(true);
                SynchronizedataCache.setSynchronizedataBySortnum(token,startSynchronizedata_2_param);

                result.setData(true);
                this.changeResultToSuccess(result);
            }else{

                //删除已同步的数据表
                EntityWrapper entityWrapper=new EntityWrapper();
                entityWrapper.eq("ssid",datassid);
                int delete=baseMapper.delete(entityWrapper);
                LogUtil.intoLog(this.getClass(),delete+":delete--baseMapper.delete");
                result.setMessage("数据同步失败已删除这条同步数据");
            }
        }else{
            result.setMessage("新增数据到指定的表中失败");
        }
    }

    private String synchronizedata_file( MultipartFile file, String basepath) {

        String filepath="";
        //先做文件的处理
        InputStream in=null;
        FileOutputStream fos=null;
        int lengthCount=0;//接受的文件长度统计
        try {
            in=file.getInputStream();
            String name=file.getName();
            String filename=file.getOriginalFilename();

            LogUtil.intoLog(this.getClass(),filename+"-----filename----name:"+name);
            fos = new FileOutputStream(basepath+filename);

            byte[] b = new byte[1024];
            int length=0;
            while((length = in.read(b))>0){
                lengthCount+=length;
                fos.write(b,0,length);
            }
            filepath=basepath+filename;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filepath;

    }

    /**
     * 真正的处理数据库同步的方法
     * @param synchronizedataParam
     */
    private int synchronizedata_data(SynchronizedataParam synchronizedataParam,BaseMapper baseMapper,
                                     String datassid,String filepath) {

        int bool=baseMapper.insert(synchronizedataParam.getT());//不用担心会把id插入到表里去，mybatis plus 自动把id去掉了
        if(bool>=0){//修改文件的存储路径
            Base_filesave base_filesave=new Base_filesave();
            base_filesave.setSsid(datassid);
            base_filesave=base_filesaveMapper.selectOne(base_filesave);//不确定可以用
            if(null!=base_filesave){
                String qg="";//切割的路径字段，跟下载路径有关系，后期补上
                base_filesave.setRecordrealurl(filepath);
                base_filesave.setRecorddownurl(OpenUtil.strMinusBasePath(qg,filepath));
            }
        }


        return bool;
    }


    @Override
    public void overSynchronizedata(BaseReqParam param, RResult result) {

        String token=param.getToken();
        String sortnum=param.getSqNum();

        StartSynchronizedata_1_Param startSynchronizedata_1_param=SynchronizedataCache.getSynchronizeData(token);

        if(null==startSynchronizedata_1_param||null==startSynchronizedata_1_param.getDatalist()
                ||startSynchronizedata_1_param.getDatalist().size() == 0){
            result.setMessage("没有找到对应的缓存，可以已经被干掉了，下级服务器直接关闭本次同步");
            SynchronizedataCache.delSynchronizedataBySortnum(token,sortnum);//关闭本次同步
            changeResultToSuccess(result);
            return ;
        }

        int finishednum=startSynchronizedata_1_param.getFinishednum();
        int totalnum=startSynchronizedata_1_param.getTotalnum();
        if(finishednum >= totalnum){

            SynchronizedataCache.delSynchronizedataBySortnum(token,sortnum);//关闭本次同步
            changeResultToSuccess(result);
        }else{
            List<StartSynchronizedata_2_Param> list= startSynchronizedata_1_param.getDatalist();
            List<StartSynchronizedata_2_Param> rrlist=new ArrayList<>();
            for(StartSynchronizedata_2_Param data:list){
                if(!data.isOverwork()){
                    rrlist.add(data);
                }
            }
            if(rrlist.size()==0){
                SynchronizedataCache.delSynchronizedataBySortnum(token,sortnum);//关闭本次同步
                changeResultToSuccess(result);
            }else{
                result.setData(rrlist);
            }
        }

    }

    public void overSynchronizedata_must(BaseReqParam param, RResult result){

        String mustOver=param.getData();
        String sortnum=param.getSqNum();
        if(mustOver.equals("false")){
            overSynchronizedata(param,result);
        }else{//强制结束本次同步
            String token=param.getToken();
            StartSynchronizedata_1_Param startSynchronizedata_1_param=SynchronizedataCache.getSynchronizeData(token);

            if(null==startSynchronizedata_1_param||null==startSynchronizedata_1_param.getDatalist()
                    ||startSynchronizedata_1_param.getDatalist().size() == 0){
                result.setMessage("没有找到对应的缓存，可以已经被干掉了，下级服务器直接关闭本次同步");
            }
            SynchronizedataCache.delSynchronizedataBySortnum(token,sortnum);//关闭本次同步
            changeResultToSuccess(result);
        }



    };
}
