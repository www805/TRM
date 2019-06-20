package com.avst.trm.v1.feignclient.zk;


import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value="zk",url="localhost:8079/")
public interface ZkControl {

    @RequestMapping( value = "/zk/getControlInfoAll")
    @ResponseBody
    public RResult getControlInfoAll();

}
