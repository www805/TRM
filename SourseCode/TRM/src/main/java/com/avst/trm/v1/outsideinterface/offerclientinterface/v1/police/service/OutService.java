package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

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
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OutService  extends BaseService {
    @Autowired
    private MeetingControl meetingControl;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    private Gson gson = new Gson();


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

    public void setRercordAsrTxtBack(RResult result, ReqParam<AsrTxtParam_toout> param){
        GetRercordAsrTxtBackVO getRercordAsrTxtBackVO=new GetRercordAsrTxtBackVO();

        //请求参数转换
        AsrTxtParam_toout asrTxtParam_toout = param.getParam();
        if (null==asrTxtParam_toout){
            result.setMessage("参数为空");
            return;
        }

        try {
            getRercordAsrTxtBackVO = gson.fromJson(gson.toJson(asrTxtParam_toout), GetRercordAsrTxtBackVO.class);
            if(null!=getRercordAsrTxtBackVO){
                //开始处理返回数据
                //时间毫秒级处理显示
                String asrtime = getRercordAsrTxtBackVO.getAsrtime();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.valueOf(asrtime));
                asrtime = df.format(date);


                if (StringUtils.isNotBlank(asrtime)){
                    getRercordAsrTxtBackVO.setAsrtime(asrtime);
                }

                //判断会议人员类型
                Police_userinfo userinfo=new Police_userinfo();//被询问人
                Base_admininfo admininfo=new Base_admininfo();//询问人
                String userssid=getRercordAsrTxtBackVO.getUserssid();

                userinfo.setSsid(userssid);
                admininfo.setSsid(userssid);
                userinfo=police_userinfoMapper.selectOne(userinfo);
                admininfo=base_admininfoMapper.selectOne(admininfo);

                if (null!=userinfo&&userinfo.getId()!=null){
                    getRercordAsrTxtBackVO.setUsername(userinfo.getUsername());
                    getRercordAsrTxtBackVO.setUsertype(2);
                }else if(null!=admininfo&&admininfo.getId()!=null){
                    getRercordAsrTxtBackVO.setUsername(admininfo.getUsername());
                    getRercordAsrTxtBackVO.setUsertype(1);
                }else{
                    System.out.println("未找到会议用户__");
                    result.setMessage("系统异常");
                    return ;
                }

                result.setData(true);
                changeResultToSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(getRercordAsrTxtBackVO.toString());
        return;
    }

}
