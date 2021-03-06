package com.avst.trm.v1.feignclient.ec;


import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.*;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphAnalysisParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphRealTimeImageParam;
import com.avst.trm.v1.feignclient.ec.req.tts.Str2TtsParam;
import com.avst.trm.v1.feignclient.ec.vo.GetSavepathVO;
import com.avst.trm.v1.feignclient.ec.vo.GetURLToPlayVO;
import com.avst.trm.v1.feignclient.ec.vo.SaveFile_localParam;
import com.avst.trm.v1.feignclient.ec.vo.ph.GetPolygraphAnalysisVO;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpALLParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetServerIpParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient("ec")
public interface EquipmentControl {


    @RequestMapping("/ss/v1/checkRecordFileState")
    @ResponseBody
    public RResult checkRecordFileState(@RequestBody CheckRecordFileStateParam param);

    @RequestMapping("/ss/v1/getURLToPlay")
    @ResponseBody
    public RResult<GetURLToPlayVO> getURLToPlay(@RequestBody GetURLToPlayParam param);

    @RequestMapping("/ss/v1/getSavePath")
    @ResponseBody
    public RResult<GetSavepathVO> getSavePath(@RequestBody GetSavePathParam  param);

    @RequestMapping("/ss/v1/saveFile_local")
    @ResponseBody
    public RResult saveFile_local(@RequestBody SaveFile_localParam param);

    /**
     * 获取存储服务器的对应iid的存储地址的路径
     * @param param
     * @return
     */
    @RequestMapping("/ss/v1/getSaveFilePath_local")
    @ResponseBody
    public RResult getSaveFilePath_local(@RequestBody GetSaveFilePath_localParam param);

    @RequestMapping("/ss/v1/getSaveFilesPathByiid")
    @ResponseBody
    public RResult getSaveFilesPathByiid(@RequestBody GetSaveFilesPathByiidParam param);

    @RequestMapping("/storage/v1/getDefaultSaveInfo")
    @ResponseBody
    public RResult getDefaultSaveInfo(@RequestBody GetDefaultSaveInfoParam param);

    @RequestMapping("/storage/v1/reStartFTPServer")
    @ResponseBody
    public RResult reStartFTPServer(@RequestBody ReStartFTPServerParam param);




//设备
    @RequestMapping("/flushbonading/v1/getFDListByFdid")
    @ResponseBody
    public RResult getFDListByFdid(@RequestBody GetFDListByFdidParam param);

    @RequestMapping("/flushbonading/v1/getFDLog")
    @ResponseBody
    public RResult getFDLog(@RequestBody GetFDLogParam param);

//ph测谎仪

    /**
     * 检测测谎仪状态
     * @param param
     * @return
     */
    @RequestMapping("/ph/v1/checkPolygraphState")
    @ResponseBody
    public RResult checkPolygraphState(@RequestBody ReqParam<CheckPolygraphStateParam> param);

    /**
     * //获取测谎心里分析数据
     * @param param
     * @return
     */
    @RequestMapping("/ph/v1/getPolygraphAnalysis")
    @ResponseBody
    public RResult<GetPolygraphAnalysisVO> getPolygraphAnalysis(@RequestBody  ReqParam<GetPolygraphAnalysisParam> param);

    /**
     * //获取测谎仪心理分析的实时图像
     * @param param
     * @return
     */
    @RequestMapping("/ph/v1/getPolygraphRealTimeImage")
    @ResponseBody
    public RResult getPolygraphRealTimeImage(@RequestBody  ReqParam<GetPolygraphRealTimeImageParam> param);


    @RequestMapping("/tts/v1/str2Tts")
    @ResponseBody
    public RResult str2Tts(@RequestBody  ReqParam<Str2TtsParam> param);


    /**
     * 获取所有存储服务的信息
     * @param param
     * @return
     */
    @RequestMapping("/storage/v1/getToOutFileSpaceList")
    @ResponseBody
    public RResult getToOutFileSpaceList(@RequestBody ReqParam<GetToOutStorageListParam> param);

    //通过ssid查询文件管理
    @RequestMapping("/storage/v1/getToOutFileSpaceByssid")
    @ResponseBody
    public RResult getToOutFileSpaceByssid(@RequestBody ReqParam<GetToOutStorageListParam> param);

    //查询路径下的所有文件
    @RequestMapping("/storage/v1/getToOutFileSpaceAll")
    @ResponseBody
    public RResult getToOutFileSpaceAll(@RequestBody ReqParam<GetToOutStorageListParam> param);

    //删除当前路径下的所有文件
    @RequestMapping("/storage/v1/delToOutFileSpaceAll")
    @ResponseBody
    public RResult delToOutFileSpaceAll(@RequestBody ReqParam<GetToOutStorageListParam> param);

    //删除单个文件
    @RequestMapping("/storage/v1/delToOutFileSpaceByPath")
    @ResponseBody
    public RResult delToOutFileSpaceByPath(@RequestBody ReqParam<GetToOutStorageListParam> param);


    /**
     * 获取其他全部设备IP
     * @param param
     * @return
     */
    @RequestMapping("/base/v1/main/getServerIpALL")
    @ResponseBody
    public RResult getServerIpALL(@RequestBody ReqParam<GetServerIpALLParam> param);

    /**
     * 获取其他全部设备端口
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getToOutFtpPort")
    @ResponseBody
    public RResult getToOutFtpPort(@RequestBody ReqParam<GetToOutMiddleware_FTPParam> param);

    /**
     * 修改配置
     * @return
     */
    @PostMapping(value = "/base/v1/main/updateServerIp")
    @ResponseBody
    public RResult updateServerIp(@RequestBody GetServerIpParam getServerIpParam);

    /**
     * 修改ec数据库里的接口端口
     * @param param
     * @return
     */
    @PostMapping(value = "/flushbonading/v1/setToOutBacktxtinterface")
    @ResponseBody
    public RResult setToOutBacktxtinterface(@RequestBody ReqParam<SetToOutBacktxtinterfaceParam> param);

    /**
     * 数据库ec的ss_saveinfo中的port设备集中控制ftp的port也要修改
     * @param param
     * @return
     */
    @PostMapping(value = "/flushbonading/v1/setFtpAndSaveinfoPort")
    @ResponseBody
    public RResult setFtpAndSaveinfoPort(@RequestBody ReqParam<SetToOutBacktxtinterfaceParam> param);

//    @RequestMapping("/flushbonading/v1/getFDState")
//    @ResponseBody
//    public RResult getFDState(@RequestBody ReqParam<GetFDStateParam> param);

    /**
     * 获取默认的视频直播地址以及数据
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getToOutDefault")
    @ResponseBody
    public RResult getToOutDefault(@RequestBody ReqParam<GetToOutFlushbonadingListParam> param);

    /**
     * 获取设备状态信息
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getFDState")
    @ResponseBody
    public RResult getFDState(@RequestBody ReqParam<GetFDStateParam> param);

    /**
     * 获取当前配置片头字段
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getptdjconst")
    @ResponseBody
    public RResult getptdjconst(@RequestBody ReqParam<GetptdjconstParam_out> param);

    /**
     * 光盘出仓/进仓
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/dvdOutOrIn")
    @ResponseBody
    public RResult dvdOutOrIn(@RequestBody ReqParam<DvdOutOrInParam_out> param);

    /**
     * 开始光盘刻录
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/startRec_Rom")
    @ResponseBody
    public RResult startRec_Rom(@RequestBody ReqParam<StartRec_RomParam_out> param);

    /**
     * 结束光盘刻录
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/stopRec_Rom")
    @ResponseBody
    public RResult stopRec_Rom(@RequestBody ReqParam<StopRec_RomParam_out> param);

    /**
     * 云台控制
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/yuntaiControl")
    @ResponseBody
    public RResult yuntaiControl(@RequestBody ReqParam<YuntaiControlParam_out> param);

    /**
     * 片头叠加
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/ptdj")
    @ResponseBody
    public RResult ptdj(@RequestBody ReqParam<PtdjParam_out> param);

    /**
     * 刻录模式选择
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/changeBurnMode")
    @ResponseBody
    public RResult changeBurnMode(@RequestBody ReqParam<ChangeBurnModeParam_out> param);

    /**
     * 光盘序号
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getCDNumber")
    @ResponseBody
    public RResult getCDNumber(@RequestBody ReqParam<GetCDNumberParam_out> param);

    /**
     * 获取刻录选时
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getBurnTime")
    @ResponseBody
    public RResult getBurnTime(@RequestBody ReqParam<GetBurnTimeParam> param);

    /**
     * 修改刻录选时
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/updateBurnTime")
    @ResponseBody
    public RResult updateBurnTime(@RequestBody ReqParam<GetBurnTimeParam> param);

    /**
     * 获得 设备现场的音频振幅
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getFDAudPowerMap")
    @ResponseBody
    public RResult getFDAudPowerMap(@RequestBody GetFDAudPowerMapParam_out param);


    /**
     * 配置设备网口 IP、子网掩码、网关
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/setFDnetwork")
    @ResponseBody
    public RResult setFDnetwork(@RequestBody SetFDnetworkParam_out param);

    /**
     * 获得设备网络配置
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getFDNetWork")
    @ResponseBody
    public RResult getFDNetWork(@RequestBody GetFDNetWorkParam_out param);

    /**
     * 设置设备某一个通道的通道音量
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/setFDAudioVolume")
    @ResponseBody
    public RResult setFDAudioVolume(@RequestBody SetFDAudioVolumeParam_out param);


    /**
     * 获得设备音频配置
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getFDAudioConf")
    @ResponseBody
    public RResult getFDAudioConf(@RequestBody GetFDAudioConfParam_out param);


    /**
     * 设置设备当前时间
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/setFDTime")
    @ResponseBody
    public RResult setFDTime(@RequestBody SetFDTimeParam_out param);


    /**
     * 设置设备NTP同步
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/setFDNTP")
    @ResponseBody
    public RResult setFDNTP(@RequestBody SetFDNTPParam_out param);


    /**
     * 获取设备NTP同步设置
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/getFDNTP")
    @ResponseBody
    public RResult getFDNTP(@RequestBody GetFDNTPParam_out param);


    /**
     * 设备录像重点标记
     * @param param
     * @return
     */
    @RequestMapping("/flushbonading/v1/viewKeyMark")
    @ResponseBody
    public RResult viewKeyMark(@RequestBody ViewKeyMarkParam_out param);

}
