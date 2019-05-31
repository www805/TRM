package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.CheckRecordFileStateParam;
import com.avst.trm.v1.feignclient.ec.req.GetURLToPlayParam;
import com.avst.trm.v1.feignclient.ec.req.ph.CheckPolygraphStateParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphAnalysisParam;
import com.avst.trm.v1.feignclient.ec.req.ph.GetPolygraphRealTimeImageParam;
import com.avst.trm.v1.feignclient.mc.req.GetMCParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetMCStateParam_out;
import com.avst.trm.v1.feignclient.mc.req.GetMCaLLUserAsrTxtListParam_out;
import com.avst.trm.v1.feignclient.mc.req.OverMCParam_out;
import com.avst.trm.v1.feignclient.mc.vo.SetMCAsrTxtBackVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.StartRercordParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/police/out")
public class OutAction extends BaseAction {
    @Autowired
    private OutService outService;

    @Autowired
    private EquipmentControl equipmentControl;

//-----------------------------------------------mc start 分割线----------------------------------------

    /**
     * 开始会议
     * @return
     */
    @RequestMapping("/ceshi")
    public RResult ceshi(int type) {
        RResult result = this.createNewResultOfFail();

        if(type==1){
            ReqParam<CheckPolygraphStateParam> param=new ReqParam<CheckPolygraphStateParam>();
            CheckPolygraphStateParam checkPolygraphStateParam=new CheckPolygraphStateParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            System.out.println(JacksonUtil.objebtToString(equipmentControl.checkPolygraphState(param)));
        }else if(type==2){
            ReqParam<GetPolygraphAnalysisParam> param=new ReqParam<GetPolygraphAnalysisParam>();
            GetPolygraphAnalysisParam checkPolygraphStateParam=new GetPolygraphAnalysisParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            System.out.println(JacksonUtil.objebtToString(equipmentControl.getPolygraphAnalysis(param)));

        }else if (type==3){
            ReqParam<GetPolygraphRealTimeImageParam> param=new ReqParam<GetPolygraphRealTimeImageParam>();
            GetPolygraphRealTimeImageParam checkPolygraphStateParam=new GetPolygraphRealTimeImageParam();
            checkPolygraphStateParam.setPhType("PH_CMCROSS");
            checkPolygraphStateParam.setPolygraphssid("212021juyts25d");
            param.setParam(checkPolygraphStateParam);
            System.out.println(JacksonUtil.objebtToString(equipmentControl.getPolygraphRealTimeImage(param)));

        }


        return result;
    }


    /**
     * 开始会议
     * @param param
     * @return
     */
    @RequestMapping("/startRercord")
    public RResult startRercord(@RequestBody ReqParam<StartRercordParam> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.startRercord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 结束会议
     * @param param
     * @return
     */
    @RequestMapping("/overRercord")
    public RResult overRercord(@RequestBody ReqParam<OverMCParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.overRercord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 会议语音识别返回
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("/outtRercordAsrTxtBack")
    public boolean setRercordAsrTxtBack(@RequestBody ReqParam<SetMCAsrTxtBackVO> param, HttpSession session) {
        if (null == param) {
            System.out.println("参数为空");
        } else {
            return outService.setRercordAsrTxtBack(param, session);
        }
        return false;
    }


    /**
     * 会议实时回放sql，
     * @param param
     * @return
     */
    @RequestMapping("/getRecord")
    public RResult getRecord(@RequestBody ReqParam<GetMCParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecord(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     *会议实时回放进行中
     * @param param
     * @return
     */
    @RequestMapping("/getRecordrealing")
    public RResult getRecordrealing(@RequestBody ReqParam<GetMCaLLUserAsrTxtListParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecordrealing(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 获取会议状态
     * @param param
     * @return
     */
    @RequestMapping("/getRecordState")
    public RResult getRecordState(@RequestBody ReqParam<GetMCStateParam_out> param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            result = outService.getRecordState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    //-----------------------------------------------mc end 分割线----------------------------------------



    //-----------------------------------------------ec start 分割线--------------------------------------

    /**
     * 检测播放状态 ：暂不使用
     * @param
     * @return
     */
    @RequestMapping("/checkPlayFileState")
    public RResult checkPlayFileState(@RequestBody CheckRecordFileStateParam param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
             outService.checkPlayFileState(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }

    /**
     * 获取播放地址
     * @param param
     * @return
     */
    @RequestMapping("/getPlayUrl")
    public RResult getPlayUrl(@RequestBody GetURLToPlayParam param) {
        RResult result = this.createNewResultOfFail();
        if (null == param) {
            result.setMessage("参数为空");
        } else {
            outService.getPlayUrl(result, param);
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }




    //-----------------------------------------------ec end 分割线----------------------------------------




    /*
    * 暂时废弃*/
    public static void main(String[] args) throws IOException, TemplateException {
        // 模板路径
        String templatePath = "C:/Users/Administrator/Desktop/AskToTemplate.pdf";
        // 生成的新文件路径
        String newPDFPath = "C:/Users/Administrator/Desktop/AskToTemplate1.pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bfChinese);

            Map<String,String> dataMap = new HashMap();
            dataMap.put("recordtypename","询问笔录");
            dataMap.put("recordstarttime", "2019年05月22日");
            dataMap.put("recordendtime", "2019年05月23日");
            dataMap.put("recordplace", "深圳石岩");
            dataMap.put("workname1", "公安部");
            dataMap.put("workname2", "研发部");
            dataMap.put("workname3", "市场部");
            dataMap.put("username", "哈哈");
            dataMap.put("sex", "男");
            dataMap.put("age", "18");
            dataMap.put("cardnum", "1234567895454444444546");
            dataMap.put("politicsstatus", "党员");
            dataMap.put("workunits", "制作业");
            dataMap.put("residence", "深圳");
            dataMap.put("phone", "19735880381");
            dataMap.put("domicile", "台湾");
            dataMap.put("both","2019年05月22日");
            dataMap.put("questionandanswer","问:saffffffff\n答:fffffffffffffffffff问:saffffffda房顶上放空间将建军节建军节建军节建军节建军节建军节建军节建军节ffffffffffffff\n答:fffffffffffffffffff");

            for(String key : dataMap.keySet()){
                String value = dataMap.get(key);
                form.setField(key,value);
            }

            stamper.setFormFlattening(true);
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
