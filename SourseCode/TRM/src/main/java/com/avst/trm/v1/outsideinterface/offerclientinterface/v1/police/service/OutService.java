package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.*;
import com.avst.trm.v1.common.conf.socketio.MessageEventHandler;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_arraignmentMapper;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.CheckRecordFileStateParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDListByFdidParam;
import com.avst.trm.v1.feignclient.ec.req.GetURLToPlayParam;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphAnalysisParam;
import com.avst.trm.v1.feignclient.ec.vo.CheckRecordFileStateVO;
import com.avst.trm.v1.feignclient.ec.vo.GetURLToPlayVO;
import com.avst.trm.v1.feignclient.ec.vo.param.FDCacheParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordFileParam;
import com.avst.trm.v1.feignclient.ec.vo.param.RecordPlayParam;
import com.avst.trm.v1.feignclient.ec.vo.ph.CheckPolygraphStateVO;
import com.avst.trm.v1.feignclient.ec.vo.ph.GetPolygraphAnalysisVO;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.*;
import com.avst.trm.v1.feignclient.mc.vo.AsrTxtParam_toout;
import com.avst.trm.v1.feignclient.mc.vo.SetMCAsrTxtBackVO;
import com.avst.trm.v1.feignclient.mc.vo.StartMCVO;
import com.avst.trm.v1.feignclient.mc.vo.param.MCCacheParam;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.GetEquipmentsStateParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.GetPolygraphdataParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.StartRercordParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetEquipmentsStateVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetMCVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetPlayUrlVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.GetRecordrealingVO;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OutService  extends BaseService {
    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;

    @Autowired
    private EquipmentControl equipmentControl;

    @Autowired
    private ZkControl zkControl;


    private Gson gson = new Gson();

    public static SocketIOServer socketIoServer;



    public RResult startRercord(RResult result, ReqParam<StartRercordParam> param) {
        StartRercordParam startRercordParam=gson.fromJson(gson.toJson(param.getParam()), StartRercordParam.class);
        if (null == startRercordParam) {
            LogUtil.intoLog(this.getClass(),"参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        //判断是否开过会议
        Police_arraignment police_arraignment=new Police_arraignment();
        if (StringUtils.isNotBlank(startRercordParam.getRecordssid())){
            police_arraignment.setRecordssid(startRercordParam.getRecordssid());
            police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
            if (null!=police_arraignment){
                String mtssid=police_arraignment.getMtssid();
                if (StringUtils.isNotBlank(mtssid)){
                    result.setData(-1);
                    result.setMessage("该案件已开启过会议");
                    return result;
                }
            }
        }


        //获取
        Base_type base_type=new Base_type();
        base_type.setType(CommonCache.getCurrentServerType());
        base_type=base_typeMapper.selectOne(base_type);

        StartMCParam_out startMCParam_out=new StartRercordParam();
        startMCParam_out.setMcType(MCType.AVST);
        startMCParam_out.setModelbool(1);
        startMCParam_out.setMtmodelssid(base_type.getMtmodelssid());//查询会议模板ssid
        startMCParam_out.setYwSystemType(YWType.RECORD_TRM);


        List<TdAndUserAndOtherParam> tdList=startRercordParam.getTdList();
        if (null!=tdList&&tdList.size()>0){
            for (TdAndUserAndOtherParam tdAndUserAndOtherParam : tdList) {
                tdAndUserAndOtherParam.setAsrtype(ASRType.AVST);
                tdAndUserAndOtherParam.setFdtype(FDType.FD_AVST);
                tdAndUserAndOtherParam.setPolygraphtype(PHType.CMCROSS);
                tdAndUserAndOtherParam.setUserecord(1);//使用录像
                tdAndUserAndOtherParam.setUsepolygraph(1);//使用测谎仪
                tdAndUserAndOtherParam.setUseasr(1);//使用语音识别
            }
            startMCParam_out.setTdList(tdList);
        }
        ReqParam<StartMCParam_out> param1=new ReqParam<>();
        param1.setParam(startMCParam_out);
        try {
           RResult rr = meetingControl.startMC(param1);
            if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                StartMCVO startMCVO=gson.fromJson(gson.toJson(rr.getData()), StartMCVO.class);
                String mtssid=startMCVO.getMtssid();

                //根据recordssid获取提讯
                    if (null!=police_arraignment){
                        police_arraignment.setMtssid(mtssid);
                        int arraignmentupdateById_bool = police_arraignmentMapper.updateById(police_arraignment);
                        LogUtil.intoLog(this.getClass(),"arraignmentupdateById_bool__"+arraignmentupdateById_bool);
                    }
                    LogUtil.intoLog(this.getClass(),"startMC开启成功__");

                result.setData(startMCVO);
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"startMC开启失败__");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public RResult overRercord(RResult result, ReqParam<OverMCParam_out> param) {
        OverMCParam_out overMCParam_out=gson.fromJson(gson.toJson(param.getParam()), OverMCParam_out.class);

        if (null == overMCParam_out) {
            LogUtil.intoLog(this.getClass(),"参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        overMCParam_out.setMcType(MCType.AVST);
        try {
            param.setParam(overMCParam_out);
            result = meetingControl.overMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                LogUtil.intoLog(this.getClass(),"overMC关闭成功__"+overMCParam_out.getMtssid());
            }else{
                LogUtil.intoLog(this.getClass(),"overMC关闭失败__"+overMCParam_out.getMtssid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setRercordAsrTxtBack(ReqParam<SetMCAsrTxtBackVO> param, HttpSession session){
        //请求参数转换
        SetMCAsrTxtBackVO setMCAsrTxtBackVO=param.getParam();
        if (null==setMCAsrTxtBackVO){
            LogUtil.intoLog(this.getClass(),"参数为空");
            return false;
        }
        LogUtil.intoLog(this.getClass(),setMCAsrTxtBackVO.toString());

        try {
            if(null!=setMCAsrTxtBackVO){
                //开始处理返回数据
                //时间毫秒级处理显示

                String asrtime = setMCAsrTxtBackVO.getAsrtime();
                String starttime = setMCAsrTxtBackVO.getStarttime();
                String asrstartime = setMCAsrTxtBackVO.getAsrstartime();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if (StringUtils.isNotBlank(asrtime)){
                    asrtime = df.format(new Date(Long.valueOf(asrtime)));
                    setMCAsrTxtBackVO.setAsrtime(asrtime);
                }

                if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                    asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                    setMCAsrTxtBackVO.setAsrstartime(asrstartime);
                }

                List<SocketIOClient> clients = MessageEventHandler.clients;
                if (null!=clients&&clients.size()>0){
                    for (SocketIOClient client : clients) {
                        client.sendEvent("getback", setMCAsrTxtBackVO);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public RResult getRecord(RResult result, ReqParam<GetMCParam_out> param){
        //请求参数转换
        GetMCParam_out getMCParam_out = param.getParam();
        if (null==getMCParam_out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCParam_out.setMcType(MCType.AVST);
        GetMCVO getMCVO=new GetMCVO();
        try {
            result = meetingControl.getMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                 getMCVO=gson.fromJson(gson.toJson(result.getData()),GetMCVO.class);
                if (null!=getMCVO){
                    List<AsrTxtParam_toout>  asrTxtParam_toouts=getMCVO.getList();
                    if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                        for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                            String asrtime = asrTxtParam_toout.getAsrtime();
                            String starttime = asrTxtParam_toout.getStarttime();
                            String asrstartime = asrTxtParam_toout.getAsrstartime();
                            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            if (StringUtils.isNotBlank(asrtime)){
                                asrtime = df.format(new Date(Long.valueOf(asrtime)));
                                asrTxtParam_toout.setAsrtime(asrtime);
                            }

                            if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                                asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                                asrTxtParam_toout.setAsrstartime(asrstartime);
                            }
                        }
                        LogUtil.intoLog(this.getClass(),"排序后时间2：——————"+asrTxtParam_toouts.get(0).getStarttime());
                      /*  Collections.sort(asrTxtParam_toouts, (s1, s2) -> s2.getStarttime().toString().compareTo(s1.getStarttime().toString()));*/
                        asrTxtParam_toouts.sort((o1, o2) -> o1.getAsrstartime().compareTo(o2.getAsrstartime()));
                        LogUtil.intoLog(this.getClass(),"排序后时间2：——————"+asrTxtParam_toouts.get(0).getStarttime());
                        getMCVO.setList(asrTxtParam_toouts);
                        result.setData(getMCVO);
                    }
                }
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMC__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public RResult getRecordrealing(RResult result, ReqParam<GetMCaLLUserAsrTxtListParam_out> param){
        GetRecordrealingVO getRecordrealingVO=new GetRecordrealingVO();
        //请求参数转换
        GetMCaLLUserAsrTxtListParam_out getMCaLLUserAsrTxtListParam_out = param.getParam();
        if (null==getMCaLLUserAsrTxtListParam_out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }

        //获取会议直播实时数据
        getMCaLLUserAsrTxtListParam_out.setMcType(MCType.AVST);
        List<AsrTxtParam_toout> asrTxtParam_toouts=new ArrayList<AsrTxtParam_toout>();
        try {
            RResult rr=new RResult<>();
            rr = meetingControl.getMCaLLUserAsrTxtList(param);
            if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
                asrTxtParam_toouts=gson.fromJson(gson.toJson(rr.getData()), new TypeToken<List<AsrTxtParam_toout>>(){}.getType());
                if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                    //时间排序
                    for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {

                        String asrtime = asrTxtParam_toout.getAsrtime();
                        String starttime = asrTxtParam_toout.getStarttime();
                        String asrstartime = asrTxtParam_toout.getAsrstartime();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        if (StringUtils.isNotBlank(asrtime)){
                            asrtime = df.format(new Date(Long.valueOf(asrtime)));
                            asrTxtParam_toout.setAsrtime(asrtime);
                        }

                        if (StringUtils.isNotBlank(asrstartime)&&StringUtils.isNotBlank(starttime)){
                            asrstartime = df.format(new Date(Long.parseLong(asrstartime)+Long.parseLong(starttime)));
                            asrTxtParam_toout.setAsrstartime(asrstartime);
                        }


                    }
                    LogUtil.intoLog(this.getClass(),"排序后时间1：——————"+asrTxtParam_toouts.get(0).getAsrstartime());
                   /* Collections.sort(asrTxtParam_toouts, (s1, s2) -> s1.getStarttime().toString().compareTo(s2.getStarttime().toString()));*/
                    asrTxtParam_toouts.sort((o1, o2) -> o1.getAsrstartime().compareTo(o2.getAsrstartime()));
                    LogUtil.intoLog(this.getClass(),"排序后时间1：——————"+asrTxtParam_toouts.get(0).getAsrstartime());

                    getRecordrealingVO.setList(asrTxtParam_toouts);
                }
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMCaLLUserAsrTxtList__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取会议直播地址
        List<FDCacheParam> fdCacheParams=new ArrayList<>();
        try {
            RResult rr2=new RResult<>();
            GetFDListByFdidParam getFDListByFdidParam=new GetFDListByFdidParam();
            getFDListByFdidParam.setFdType(FDType.FD_AVST);
            getFDListByFdidParam.setFdid(getMCaLLUserAsrTxtListParam_out.getMtssid());
            rr2 = equipmentControl.getFDListByFdid(getFDListByFdidParam);
            if (null != rr2 && rr2.getActioncode().equals(Code.SUCCESS.toString())) {
                fdCacheParams=gson.fromJson(gson.toJson(rr2.getData()), new TypeToken<List<FDCacheParam>>(){}.getType());
                getRecordrealingVO.setFdCacheParams(fdCacheParams);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMCaLLUserAsrTxtList__出错");
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        result.setData(getRecordrealingVO);
        changeResultToSuccess(result);
        return  result;
    }

    public RResult getRecordState(RResult result, ReqParam<GetMCStateParam_out> param){
        //请求参数转换
        GetMCStateParam_out getMCStateParam_out = param.getParam();
        if (null==getMCStateParam_out){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCStateParam_out.setMcType(MCType.AVST);
        Integer mtstate=null;
        try {
            result = meetingControl.getMCState(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                mtstate= (Integer) result.getData();
                result.setData(mtstate);
                changeResultToSuccess(result);
            }else{
                LogUtil.intoLog(this.getClass(),"请求getMCState__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public void getPolygraphdata(RResult result, ReqParam<GetPolygraphdataParam> param){
        //请求参数转换
        GetPolygraphdataParam getPolygraphdataParam = param.getParam();
        if (null==getPolygraphdataParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }
        String mtssid=getPolygraphdataParam.getMtssid();
        if (StringUtils.isBlank(mtssid)){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }

        //查询会议数据获取检测仪的ssid
        String polygraphssid=null;
        ReqParam<GetMCdataParam_out> param3=new ReqParam<>();
        GetMCdataParam_out getMCdataParam_out=new GetMCdataParam_out();
        getMCdataParam_out.setMcType(MCType.AVST);
        getMCdataParam_out.setMtssid(mtssid);
        param3.setParam(getMCdataParam_out);
        RResult rr3=new RResult();
        rr3=meetingControl.getMCdata(param3);
        if (null != rr3 && rr3.getActioncode().equals(Code.SUCCESS.toString())) {
            MCCacheParam mcCacheParam = gson.fromJson(gson.toJson(rr3.getData()),MCCacheParam.class);
            if (null!=mcCacheParam&&null!=mcCacheParam.getTdList()&&mcCacheParam.getTdList().size()>0){
                polygraphssid=mcCacheParam.getTdList().get(0).getPolygraphssid();
            }
        }else{
            LogUtil.intoLog(this.getClass(),"请求getMCdata__出错");
        }

        if (null==polygraphssid){
            LogUtil.intoLog(this.getClass(),"监测仪ssid为空__"+polygraphssid);
            changeResultToSuccess(result);
            return;
        }


        //检查心跳仪器状态
        ReqParam<CheckPolygraphStateParam> param1=new ReqParam<>();
        CheckPolygraphStateParam checkPolygraphStateParam=new CheckPolygraphStateParam();
        checkPolygraphStateParam.setPhType(PHType.CMCROSS);
        checkPolygraphStateParam.setPolygraphssid(polygraphssid);
        param1.setParam(checkPolygraphStateParam);

        RResult<CheckPolygraphStateVO> rr1=new RResult();
        rr1=equipmentControl.checkPolygraphState(param1);
        Integer workstate=null;//约定一套状态指令，1成功，0初始化中，其他都为错误
        if (null != rr1 && rr1.getActioncode().equals(Code.SUCCESS.toString())) {
            CheckPolygraphStateVO checkPolygraphStateVO=gson.fromJson(gson.toJson(rr1.getData()),CheckPolygraphStateVO.class);
            if (null!=checkPolygraphStateVO){
                workstate=checkPolygraphStateVO.getWorkstate();
                LogUtil.intoLog(this.getClass(),"身心监测状态__"+workstate);
                if (null!=workstate&&workstate==1){
                    LogUtil.intoLog(this.getClass(),"身心监测状态正常开始获取数据__"+workstate);

                    //状态正常开始获取仪器数据
                    ReqParam<GetPolygraphAnalysisParam> param2=new ReqParam<>();
                    GetPolygraphAnalysisParam getPolygraphAnalysisParam=new GetPolygraphAnalysisParam();
                    getPolygraphAnalysisParam.setPhType(PHType.CMCROSS);
                    getPolygraphAnalysisParam.setPolygraphssid(polygraphssid);
                    param2.setParam(getPolygraphAnalysisParam);
                    RResult<GetPolygraphAnalysisVO> rr2=new RResult();
                    rr2=equipmentControl.getPolygraphAnalysis(param2);
                    if (null != rr2 && rr2.getActioncode().equals(Code.SUCCESS.toString())) {
                        GetPolygraphAnalysisVO getPolygraphAnalysisVO=gson.fromJson(gson.toJson(rr2.getData()),GetPolygraphAnalysisVO.class);
                        LogUtil.intoLog(this.getClass(),"getPolygraphAnalysise__"+getPolygraphAnalysisVO.toString());
                        result.setData(getPolygraphAnalysisVO);
                        changeResultToSuccess(result);
                        return;
                    }else{
                        LogUtil.intoLog(this.getClass(),"请求getPolygraphAnalysise__出错");
                    }

                }else {
                    LogUtil.intoLog(this.getClass(),"身心监测状态__"+workstate);
                    changeResultToSuccess(result);
                    return;
                }
            }
        }else{
            LogUtil.intoLog(this.getClass(),"请求checkPolygraphState__出错");
        }
        return;
    }

    //-------------------------------------------------------------------------------------------------------------------


    public void checkPlayFileState(RResult result, CheckRecordFileStateParam param){
        param.setSsType(SSType.AVST);

        return;
    }


    public void getPlayUrl(RResult result,GetURLToPlayParam  param){
        GetPlayUrlVO getPlayUrlVO =new GetPlayUrlVO();
        //先判断数据存储状态2
        String iid=param.getIid();

        if (StringUtils.isBlank(iid)){
            result.setMessage("参数为空");
            return;
        }

        getPlayUrlVO.setIid(iid);

        //1.获取文件状态
        RResult rr=new RResult();
        CheckRecordFileStateParam checkRecordFileStateParam=new CheckRecordFileStateParam();
        checkRecordFileStateParam.setSsType(SSType.AVST);
        checkRecordFileStateParam.setIid(iid);
        rr= equipmentControl.checkRecordFileState(checkRecordFileStateParam);

        CheckRecordFileStateVO checkRecordFileStateVO=new CheckRecordFileStateVO();
        Integer state=null;
        if (null != rr && rr.getActioncode().equals(Code.SUCCESS.toString())) {
            checkRecordFileStateVO=gson.fromJson(gson.toJson(rr.getData()),CheckRecordFileStateVO.class );
            List<RecordFileParam> recordFileParams=checkRecordFileStateVO.getRecordList();
            if (null!=recordFileParams&&recordFileParams.size()>0){
                for (RecordFileParam recordFileParam : recordFileParams) {
                    state=recordFileParam.getState();
                }
            }
            LogUtil.intoLog(this.getClass(),"数据存储状态__"+state);
            getPlayUrlVO.setRecordFileParams(recordFileParams);
           if (null!=state&&state==2){
               LogUtil.intoLog(this.getClass(),"数据存储正常开始获取地址__"+state);

               //2.说去文件地址
               RResult<GetURLToPlayVO> rr2=new RResult<GetURLToPlayVO>();
               GetURLToPlayParam getURLToPlayParam=new GetURLToPlayParam();
               getURLToPlayParam.setSsType(SSType.AVST);
               getURLToPlayParam.setIid(iid);
               rr2=equipmentControl.getURLToPlay(getURLToPlayParam);

               GetURLToPlayVO getURLToPlayVO=new GetURLToPlayVO();
               if (null != rr2 && rr2.getActioncode().equals(Code.SUCCESS.toString())) {
                   getURLToPlayVO=rr2.getData();
                   if(null!=getURLToPlayVO&&null!=getURLToPlayVO.getRecordList()){
                       List<RecordPlayParam> recordPlayParams =getURLToPlayVO.getRecordList();
                       if (null!=recordPlayParams&&recordPlayParams.size()>0){
                           for (RecordPlayParam recordPlayParam : recordPlayParams) {
                               LogUtil.intoLog(this.getClass(),"直播地址__"+recordPlayParam.getPlayUrl());
                           }
                       }
                       getPlayUrlVO.setRecordPlayParams(recordPlayParams);
                   }
               }else{
                   LogUtil.intoLog(this.getClass(),"请求getURLToPlay__出错");
               }
               result.setData(getPlayUrlVO);
               changeResultToSuccess(result);
           }else{
               LogUtil.intoLog(this.getClass(),"数据存储状态异常__"+state);
               result.setMessage("获取点播中...请等待");
               return;
           }
        }else{
            LogUtil.intoLog(this.getClass(),"请求checkRecordFileState__出错");
        }

       /* */
        return;
    }


    public void getEquipmentsState(RResult result,ReqParam<GetEquipmentsStateParam> param){
        GetEquipmentsStateVO getEquipmentsStateVO=new GetEquipmentsStateVO();
        //请求参数转换
        GetEquipmentsStateParam getEquipmentsStateParam = param.getParam();
        if (null==getEquipmentsStateParam){
            LogUtil.intoLog(this.getClass(),"参数为空");
            result.setMessage("参数为空");
            return ;
        }
        String mtssid=getEquipmentsStateParam.getMtssid();
        if (StringUtils.isBlank(mtssid)){
            LogUtil.intoLog(this.getClass(),"getEquipmentsState参数为空mtssid____"+mtssid);
            result.setMessage("参数为空");
            return ;
        }
        //状态： -1异常 1正常  0未启动
        Integer AsrState=0;//语音识别状态
        Integer PolygraphState=0;//身心检测状态
        Integer LiveState=0;//直播状态
        Integer MtState=0;//会议状态  //会议状态 0初始化，1进行中，2已结束，3暂停
        Integer PlayState=0;//点播文件状态

        //获取会议状态
        ReqParam<GetMCStateParam_out> mt_param=new ReqParam<>();//会议参数
        GetMCStateParam_out getMCStateParam_out=new GetMCStateParam_out();
        getMCStateParam_out.setMcType(MCType.AVST);
        getMCStateParam_out.setMtssid(mtssid);
        mt_param.setParam(getMCStateParam_out);
        RResult mt_rr=new RResult();//会议返回
        try {
            mt_rr=meetingControl.getMCState(mt_param);
            if (null != mt_rr && mt_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                MtState= 1;
                LiveState=1;
                AsrState=1;
                //设置状态
                LogUtil.intoLog(this.getClass(),"getEquipmentsState请求getMCState__成功"+MtState);
            }else{
                //设置状态
                MtState= -1;
                LiveState=-1;
                AsrState=-1;
                LogUtil.intoLog(this.getClass(),"getEquipmentsState请求getMCState__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取身心检测的状态
        String polygraphssid=null;//身心检测的ssid
        ReqParam<GetMCdataParam_out> MCdata_param=new ReqParam<>();
        GetMCdataParam_out getMCdataParam_out=new GetMCdataParam_out();
        getMCdataParam_out.setMcType(MCType.AVST);
        getMCdataParam_out.setMtssid(mtssid);
        MCdata_param.setParam(getMCdataParam_out);
        RResult  MCdata_rr=new RResult();
        try {
            MCdata_rr=meetingControl.getMCdata(MCdata_param);
            if (null != MCdata_rr && MCdata_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                MCCacheParam mcCacheParam = gson.fromJson(gson.toJson(MCdata_rr.getData()),MCCacheParam.class);
                if (null!=mcCacheParam&&null!=mcCacheParam.getTdList()&&mcCacheParam.getTdList().size()>0){
                    polygraphssid=mcCacheParam.getTdList().get(0).getPolygraphssid();
                    LogUtil.intoLog(this.getClass(),"getEquipmentsState请求getMCdata__成功");
                }
            }else{
                LogUtil.intoLog(this.getClass(),"getEquipmentsState请求getMCdata__出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null==polygraphssid){
            PolygraphState=0;
            //设置状态
        }else {
            ReqParam<CheckPolygraphStateParam> Polygraph_param=new ReqParam<>();
            CheckPolygraphStateParam checkPolygraphStateParam=new CheckPolygraphStateParam();
            checkPolygraphStateParam.setPhType(PHType.CMCROSS);
            checkPolygraphStateParam.setPolygraphssid(polygraphssid);
            Polygraph_param.setParam(checkPolygraphStateParam);
            RResult<CheckPolygraphStateVO> Polygraph_rr=new RResult();
            try {
                Polygraph_rr=equipmentControl.checkPolygraphState(Polygraph_param);
                if (null != Polygraph_rr && Polygraph_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                    PolygraphState=1;
                    //设置状态
                    LogUtil.intoLog(this.getClass(),"getEquipmentsState请求checkPolygraphState__成功");
                }else{
                    //设置状态
                    PolygraphState=-1;
                    LogUtil.intoLog(this.getClass(),"getEquipmentsState请求checkPolygraphState__出错");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        getEquipmentsStateVO.setAsrState(AsrState);
        getEquipmentsStateVO.setPolygraphState(PolygraphState);
        getEquipmentsStateVO.setLiveState(LiveState);
        getEquipmentsStateVO.setMtState(MtState);
        getEquipmentsStateVO.setPlayState(PlayState);
        result.setData(getEquipmentsStateVO);
        changeResultToSuccess(result);
        return;
    }


    public void getClient(RResult rresult,ReqParam param){
          RResult zk_rr = zkControl.getControlInfoAll();
          rresult.setData(zk_rr);
          return;
    }


}
