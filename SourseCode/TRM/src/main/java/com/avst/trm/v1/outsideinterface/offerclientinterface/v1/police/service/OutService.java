package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.conf.socketio.MessageEventHandler;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_userinfoMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.MeetingControl;
import com.avst.trm.v1.feignclient.req.GetMCAsrTxtBackParam_out;
import com.avst.trm.v1.feignclient.req.StartMCParam_out;
import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRercordAsrTxtBackVO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OutService  extends BaseService {
    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    private Gson gson = new Gson();

    public static SocketIOServer socketIoServer;



    public RResult startRercord(RResult result, ReqParam<StartMCParam_out> param) {
        if (null == param) {
            System.out.println("参数为空__");
            result.setMessage("参数为空");
            return result;
        }
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

    public RResult overRercord(RResult result, ReqParam param) {
        if (null == param) {
            System.out.println("参数为空__");
            result.setMessage("参数为空");
            return result;
        }
        try {
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
      //  GetRercordAsrTxtBackVO getRercordAsrTxtBackVO=new GetRercordAsrTxtBackVO();

        //请求参数转换
        AsrTxtParam_toout asrTxtParam_toout = param.getParam();
        if (null==asrTxtParam_toout){
            System.out.println("参数为空");
            return false;
        }

        try {
        //    getRercordAsrTxtBackVO = gson.fromJson(gson.toJson(asrTxtParam_toout), GetRercordAsrTxtBackVO.class);
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

}
