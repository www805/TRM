package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.req.*;
import com.avst.trm.v1.feignclient.vo.AsrTxtParam_toout;
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


    @RequestMapping("/outtRercordAsrTxtBack")
    public boolean setRercordAsrTxtBack(@RequestBody ReqParam<AsrTxtParam_toout> param, HttpSession session) {
        if (null == param) {
            System.out.println("参数为空");
        } else {
            return outService.setRercordAsrTxtBack(param, session);
        }
        return false;
    }


    /**
     * 会议实时回放，
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


    public static void main(String[] args) throws IOException, TemplateException {
        // 模板路径
        String templatePath = "C:/Users/Administrator/Desktop/ceshi_word.pdf";
        // 生成的新文件路径
        String newPDFPath = "C:/Users/Administrator/Desktop/ceshiword.pdf";
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

            Map<String,String> dataMap = new HashMap();
            dataMap.put("recordstarttime", "2019年05月22日");
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

            Map<String,Object> o=new HashMap();
            o.put("datemap",dataMap);

            //文字类的内容处理
            Map<String,String> datemap = (Map<String,String>)o.get("datemap");
            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setField(key,value);
            }

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
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
