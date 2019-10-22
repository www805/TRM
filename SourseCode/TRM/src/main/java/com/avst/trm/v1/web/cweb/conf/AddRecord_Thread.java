package com.avst.trm.v1.web.cweb.conf;

import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.mc.req.OverMCParam_out;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.web.cweb.req.policereq.ExportPdfParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService2;
import org.apache.commons.lang.StringUtils;

/**
 *笔录保存
 */
public class AddRecord_Thread extends Thread {
    private String recordssid;//笔录ssid

    private RecordService recordService;

    private String  mtssid;//会议ssid

    private OutService outService;

    private RecordService2 recordService2;



    public AddRecord_Thread(String recordssid, RecordService recordService,RecordService2 recordService2, String mtssid, OutService outService){
        this.recordssid=recordssid;
        this.recordService=recordService;
        this.mtssid=mtssid;
        this.outService=outService;
        this.recordService2=recordService2;
    }

    @Override
    public void run() {
            LogUtil.intoLog("AddRecord_Thread__笔录结束时生成Pdf结束会议开始__recordssid__"+recordssid+"__mtssid__"+mtssid);
            try {
                if (StringUtils.isNotBlank(recordssid)&&null!=recordService){
                    //生成word 和pdf
                    RResult exportPdf_rr=new RResult();
                    ExportPdfParam exportPdfParam=new ExportPdfParam();
                    exportPdfParam.setRecordssid(recordssid);
                    ReqParam reqParam=new ReqParam();
                    reqParam.setParam(exportPdfParam);

                    exportPdf_rr=recordService2.exportPdf(exportPdf_rr, reqParam);
                    if (null != exportPdf_rr && exportPdf_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                        LogUtil.intoLog(this.getClass(),"recordService.exportPdf笔录结束时exportPdf__成功__保存问答");
                    }else{
                        LogUtil.intoLog(this.getClass(),"recordService.exportPdf笔录结束时exportPdf__出错__"+exportPdf_rr.getMessage());
                    }
                }


                if (StringUtils.isNotBlank(mtssid)&&null!=outService){
                    try {
                        //开始结束笔录
                        RResult overMC_rr=new RResult();
                        OverMCParam_out overMCParam_out=new OverMCParam_out();
                        overMCParam_out.setMcType(MCType.AVST);
                        overMCParam_out.setMtssid(mtssid);
                        ReqParam reqParam=new ReqParam();
                        reqParam.setParam(overMCParam_out);

                        overMC_rr = outService.overRercord(overMC_rr,reqParam);
                        if (null != overMC_rr && overMC_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                            LogUtil.intoLog(this.getClass()," outService.overRercord关闭成功__"+overMCParam_out.getMtssid());
                        }else{
                            String msg=overMC_rr==null?"":overMC_rr.getMessage();
                            LogUtil.intoLog(this.getClass()," outService.overRercord关闭失败__"+msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.intoLog("AddRecord_Thread__笔录结束时生成Pdf结束会议结束__recordssid__"+recordssid+"__mtssid__"+mtssid);
    }
}
