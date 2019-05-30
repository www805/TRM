package com.avst.trm.v1.feignclient.ec;


import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.ec.req.CheckRecordFileStateParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDListByFdidParam;
import com.avst.trm.v1.feignclient.ec.req.GetURLToPlayParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient(value="ec",url="localhost:8081/")
public interface EquipmentControl {


    @RequestMapping("/ss/v1/checkRecordFileState")
    @ResponseBody
    public RResult checkRecordFileState(@RequestBody CheckRecordFileStateParam param);

    @RequestMapping("/ss/v1/getURLToPlay")
    @ResponseBody
    public RResult getURLToPlay(@RequestBody GetURLToPlayParam param);

    @RequestMapping("/flushbonading/v1/getFDListByFdid")
    @ResponseBody
    public RResult getFDListByFdid(@RequestBody GetFDListByFdidParam param);

}
