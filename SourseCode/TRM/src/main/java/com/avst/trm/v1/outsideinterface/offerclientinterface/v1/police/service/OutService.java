package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.conf.ASRType;
import com.avst.trm.v1.common.conf.MCType;
import com.avst.trm.v1.common.conf.socketio.MessageEventHandler;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.MeetingControl;
import com.avst.trm.v1.feignclient.req.GetMCParam_out;
import com.avst.trm.v1.feignclient.req.OverMCParam_out;
import com.avst.trm.v1.feignclient.req.StartMCParam_out;
import com.avst.trm.v1.feignclient.req.TdAndUserAndOtherParam;
import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;
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


    private Gson gson = new Gson();

    public static SocketIOServer socketIoServer;



    public RResult startRercord(RResult result, ReqParam<StartMCParam_out> param) {
        StartMCParam_out startMCParam_out=gson.fromJson(gson.toJson(param.getParam()), StartMCParam_out.class);

        if (null == startMCParam_out) {
            System.out.println("参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        //获取
        Base_type base_type=new Base_type();
        base_type.setType(CommonCache.getCurrentServerType());
        base_type=base_typeMapper.selectOne(base_type);
        startMCParam_out.setMcType(MCType.AVST);
        startMCParam_out.setModelbool(1);
        startMCParam_out.setMtmodelssid(base_type.getMtmodelssid());//查询会议模板ssid
        startMCParam_out.setYwSystemType("TRM_AVST");
        List<TdAndUserAndOtherParam> tdList=startMCParam_out.getTdList();
        if (null!=tdList&&tdList.size()>0){
            for (TdAndUserAndOtherParam tdAndUserAndOtherParam : tdList) {
                tdAndUserAndOtherParam.setAsrtype(ASRType.AVST);
            }
            startMCParam_out.setTdList(tdList);
        }
        param.setParam(startMCParam_out);
        try {
            result = meetingControl.startMC(param);
            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                System.out.println("startMC开启成功__");
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
                System.out.println("overMC关闭成功__");
            }else{
                System.out.println("overMC关闭失败__");
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
}
