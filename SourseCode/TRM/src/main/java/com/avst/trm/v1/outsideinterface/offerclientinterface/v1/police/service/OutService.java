package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.ASRType;
import com.avst.trm.v1.common.conf.FDType;
import com.avst.trm.v1.common.conf.MCType;
import com.avst.trm.v1.common.conf.socketio.MessageEventHandler;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_arraignmentMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.MeetingControl;
import com.avst.trm.v1.feignclient.req.*;
import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.StartRercordParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.StartMCVO;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OutService  extends BaseService {
    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Base_typeMapper base_typeMapper;

    @Autowired
    private Police_arraignmentMapper police_arraignmentMapper;




    private Gson gson = new Gson();

    public static SocketIOServer socketIoServer;



    public RResult startRercord(RResult result, ReqParam<StartRercordParam> param) {
       // StartMCParam_out startMCParam_out=gson.fromJson(gson.toJson(param.getParam()), StartMCParam_out.class);
        StartRercordParam startRercordParam=gson.fromJson(gson.toJson(param.getParam()), StartRercordParam.class);
        if (null == startRercordParam) {
            System.out.println("参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        //获取
        Base_type base_type=new Base_type();
        base_type.setType(CommonCache.getCurrentServerType());
        base_type=base_typeMapper.selectOne(base_type);
        StartMCParam_out startMCParam_out=new StartRercordParam();
        startMCParam_out.setMcType(MCType.AVST);
        startMCParam_out.setModelbool(1);
        startMCParam_out.setMtmodelssid(base_type.getMtmodelssid());//查询会议模板ssid
        startMCParam_out.setYwSystemType("TRM_AVST");
        List<TdAndUserAndOtherParam> tdList=startRercordParam.getTdList();
        if (null!=tdList&&tdList.size()>0){
            for (TdAndUserAndOtherParam tdAndUserAndOtherParam : tdList) {
                tdAndUserAndOtherParam.setAsrtype(ASRType.AVST);
                tdAndUserAndOtherParam.setFdtype(FDType.FD_AVST);
                tdAndUserAndOtherParam.setUserecord(1);
                tdAndUserAndOtherParam.setUseasr(1);
            }
            startMCParam_out.setTdList(tdList);
        }
        ReqParam<StartMCParam_out> param1=new ReqParam<>();
        param1.setParam(startMCParam_out);
        try {
            result = meetingControl.startMC(param1);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                StartMCVO startMCVO=gson.fromJson(gson.toJson(result.getData()), StartMCVO.class);
                String mtssid=startMCVO.getMtssid();

                //根据recordssid获取提讯
                Police_arraignment police_arraignment=new Police_arraignment();
                if (StringUtils.isNotBlank(startRercordParam.getRecordssid())){
                    police_arraignment.setRecordssid(startRercordParam.getRecordssid());
                    police_arraignment =police_arraignmentMapper.selectOne(police_arraignment);
                    if (null!=police_arraignment){
                        police_arraignment.setMtssid(mtssid);
                        int arraignmentupdateById_bool = police_arraignmentMapper.updateById(police_arraignment);
                        System.out.println("arraignmentupdateById_bool__"+arraignmentupdateById_bool);
                    }
                    System.out.println("startMC开启成功__");
                }
            }else{
                System.out.println("startMC开启失败__");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public RResult overRercord(RResult result, ReqParam<OverMCParam_out> param) {
        OverMCParam_out overMCParam_out=gson.fromJson(gson.toJson(param.getParam()), OverMCParam_out.class);

        if (null == overMCParam_out) {
            System.out.println("参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        overMCParam_out.setMcType(MCType.AVST);
        try {
            param.setParam(overMCParam_out);
            result = meetingControl.overMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                System.out.println("overMC关闭成功__"+overMCParam_out.getMtssid());
            }else{
                System.out.println("overMC关闭失败__"+overMCParam_out.getMtssid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setRercordAsrTxtBack(ReqParam<AsrTxtParam_toout> param, HttpSession session){
        //请求参数转换
        AsrTxtParam_toout asrTxtParam_toout = param.getParam();
        if (null==asrTxtParam_toout){
            System.out.println("参数为空");
            return false;
        }

        try {
            if(null!=asrTxtParam_toout){
                //开始处理返回数据
                //时间毫秒级处理显示
                String asrtime = asrTxtParam_toout.getAsrtime();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.valueOf(asrtime));
                asrtime = df.format(date);

                if (StringUtils.isNotBlank(asrtime)){
                    asrTxtParam_toout.setAsrtime(asrtime);
                }

                List<SocketIOClient> clients = MessageEventHandler.clients;
                if (null!=clients&&clients.size()>0){
                    for (SocketIOClient client : clients) {
                        client.sendEvent("getback", asrTxtParam_toout);
                    }
                }
                System.out.println(asrTxtParam_toout.toString());
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
            System.out.println("参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCParam_out.setMcType(MCType.AVST);
        List<AsrTxtParam_toout> asrTxtParam_toouts=new ArrayList<AsrTxtParam_toout>();
        try {
            result = meetingControl.getMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                asrTxtParam_toouts=gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<AsrTxtParam_toout>>(){}.getType());
                if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                    for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                        //时间转换
                        String asrtime = asrTxtParam_toout.getAsrtime();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(Long.valueOf(asrtime));
                        asrtime = df.format(date);
                        if (StringUtils.isNotBlank(asrtime)){
                            asrTxtParam_toout.setAsrtime(asrtime);
                        }
                    }
                    result.setData(asrTxtParam_toouts);
                }
                changeResultToSuccess(result);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public RResult getRecordrealing(RResult result, ReqParam<GetMCaLLUserAsrTxtListParam_out> param){
        //请求参数转换
        GetMCaLLUserAsrTxtListParam_out getMCaLLUserAsrTxtListParam_out = param.getParam();
        if (null==getMCaLLUserAsrTxtListParam_out){
            System.out.println("参数为空");
            result.setMessage("参数为空");
            return  result;
        }
        getMCaLLUserAsrTxtListParam_out.setMcType(MCType.AVST);
        List<AsrTxtParam_toout> asrTxtParam_toouts=new ArrayList<AsrTxtParam_toout>();
        try {
            result = meetingControl.getMCaLLUserAsrTxtList(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                asrTxtParam_toouts=gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<AsrTxtParam_toout>>(){}.getType());
                if (null!=asrTxtParam_toouts&&asrTxtParam_toouts.size()>0){
                    for (AsrTxtParam_toout asrTxtParam_toout : asrTxtParam_toouts) {
                        //时间转换
                        String asrtime = asrTxtParam_toout.getAsrtime();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(Long.valueOf(asrtime));
                        asrtime = df.format(date);
                        if (StringUtils.isNotBlank(asrtime)){
                            asrTxtParam_toout.setAsrtime(asrtime);
                        }
                    }
                    result.setData(asrTxtParam_toouts);
                }
                changeResultToSuccess(result);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    public RResult getRecordState(RResult result, ReqParam<GetMCStateParam_out> param){
        //请求参数转换
        GetMCStateParam_out getMCStateParam_out = param.getParam();
        if (null==getMCStateParam_out){
            System.out.println("参数为空");
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }




}
