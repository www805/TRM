package com.avst.trm.v1.feignclient.mc;

import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.mc.req.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备控制的代理
 */
@FeignClient(value="mc",url="localhost:8082/")
public interface MeetingControl {

    @RequestMapping( value = "/mt/v1/getMCAsrTxtBack")
    @ResponseBody
    public RResult getMCAsrTxtBack(@RequestBody ReqParam<GetMCAsrTxtBackParam_out> param);

    @RequestMapping("/mt/v1/startMC")
    @ResponseBody
    public RResult startMC(@RequestBody ReqParam<StartMCParam_out> param);

    @RequestMapping("/mt/v1/overMC")
    @ResponseBody
    public RResult overMC(@RequestBody ReqParam<OverMCParam_out> param);

    @RequestMapping("/mt/v1/getMC")
    @ResponseBody
    public RResult getMC(@RequestBody ReqParam<GetMCParam_out> param);

    @RequestMapping("/mt/v1/getMCaLLUserAsrTxtList")
    @ResponseBody
    public RResult getMCaLLUserAsrTxtList(@RequestBody ReqParam<GetMCaLLUserAsrTxtListParam_out> param);


    @RequestMapping("/mt/v1/getMCState")
    @ResponseBody
    public RResult getMCState(@RequestBody ReqParam<GetMCStateParam_out> param);


}
