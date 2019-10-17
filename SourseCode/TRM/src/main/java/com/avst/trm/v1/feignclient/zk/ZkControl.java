package com.avst.trm.v1.feignclient.zk;


import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListener;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value="zk")
public interface ZkControl {

    //获取注册的服务器
    @RequestMapping( value = "/zk/getControlInfoAll")
    @ResponseBody
    public RResult getControlInfoAll();

    //获取服务器时间
    @RequestMapping( value = "/zk/getControlTime")
    @ResponseBody
    public RResult getControlTime();

    @RequestMapping("/zk/getHeartbeat")
    public RResult getHeartbeat(@RequestBody ReqParam<ControlInfoParamVO> param);


    //获取服务器时间
    @RequestMapping("/main/getServerStatus")
    @ResponseBody
    public  RResult getServerStatus();
}
