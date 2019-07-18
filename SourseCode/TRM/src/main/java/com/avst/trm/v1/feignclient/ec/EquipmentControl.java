package com.avst.trm.v1.feignclient.ec;


import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.req.CheckRecordFileStateParam;
import com.avst.trm.v1.feignclient.ec.req.GetFDListByFdidParam;
import com.avst.trm.v1.feignclient.ec.req.GetSavePathParam;
import com.avst.trm.v1.feignclient.ec.req.GetURLToPlayParam;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphAnalysisParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphRealTimeImageParam;
import com.avst.trm.v1.feignclient.ec.req.tts.Str2TtsParam;
import com.avst.trm.v1.feignclient.ec.vo.GetSavepathVO;
import com.avst.trm.v1.feignclient.ec.vo.GetURLToPlayVO;
import com.avst.trm.v1.feignclient.ec.vo.SaveFile_localParam;
import com.avst.trm.v1.feignclient.ec.vo.ph.GetPolygraphAnalysisVO;
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
    public RResult<GetURLToPlayVO> getURLToPlay(@RequestBody GetURLToPlayParam param);

    @RequestMapping("/ss/v1/getSavePath")
    @ResponseBody
    public RResult<GetSavepathVO> getSavePath(@RequestBody GetSavePathParam  param);

    @RequestMapping("/ss/v1/saveFile_local")
    @ResponseBody
    public RResult saveFile_local(@RequestBody SaveFile_localParam param);

//设备
    @RequestMapping("/flushbonading/v1/getFDListByFdid")
    @ResponseBody
    public RResult getFDListByFdid(@RequestBody GetFDListByFdidParam param);

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


}
