package com.avst.trm.v1.feignclient.mc;

import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.mc.req.*;
import com.avst.trm.v1.feignclient.mc.vo.PhDataParam_toout;
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
    public RResult getMC(@RequestBody ReqParam<GetPhssidByMTssidParam_out> param);

    @RequestMapping("/mt/v1/getMCaLLUserAsrTxtList")
    @ResponseBody
    public RResult getMCaLLUserAsrTxtList(@RequestBody ReqParam<GetMCaLLUserAsrTxtListParam_out> param);


    @RequestMapping("/mt/v1/getMCState")
    @ResponseBody
    public RResult getMCState(@RequestBody ReqParam<GetMCStateParam_out> param);


    /**
     * 这里通过会议ssid获取测谎仪ssid是可能的，但是一旦一个会议由多个测谎仪就有问题，这里需要在会议的对应接口中处理
     * 根据会议获取测谎仪ssid
     * @param param
     * @return
     */
    @RequestMapping("/mt/v1/getPhssidByMTssid")
    @ResponseBody
    public RResult getPhssidByMTssid(@RequestBody ReqParam<GetPhssidByMTssidParam_out> param);

    /**
     * 身心监护缓存推送数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/mt/v1/getPHData")
    @ResponseBody
    public RResult getPHData(@RequestBody ReqParam<GetPHDataParam_out> param);


    /**
     * 身心检测回放数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/mt/v1/getPHDataBack")
    @ResponseBody
    public RResult getPHDataBack(@RequestBody ReqParam<GetPHDataParam_out> param);


    /**
     * 根据会议获取直播开始时间
     * @param param
     * @return
     */
    @RequestMapping("/mt/v1/getFdrecordStarttimeByMTssid")
    @ResponseBody
    public RResult getFdrecordStarttimeByMTssid(@RequestBody ReqParam<GetFdrecordStarttimeByMTssidParam_out> param);


    @RequestMapping("/mt/v1/getTdAndUserAndOtherCacheParamByMTssid")
    @ResponseBody
    public RResult getTdAndUserAndOtherCacheParamByMTssid(@RequestBody ReqParam<GetTdAndUserAndOtherCacheParamByMTssidPara_out> param);
}
