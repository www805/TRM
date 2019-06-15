package com.avst.trm.v1.feignclient.zk;

import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.zk.util.ClientResult;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "zk", url = "localhost:8079/", fallback = ClientResult.class)
public interface ZkControl {

    @RequestMapping("/info/getControlInfo")
    @ResponseBody
    public RResult getControlInfo();

}
