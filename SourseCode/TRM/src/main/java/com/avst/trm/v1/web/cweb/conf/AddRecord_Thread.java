package com.avst.trm.v1.web.cweb.conf;

import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.datasourse.police.entity.Police_answer;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtoproblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_answerMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordtoproblemMapper;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.OverMCParam_out;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.OutService;
import com.avst.trm.v1.web.cweb.req.policereq.ExportPdfParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 *笔录保存
 */
public class AddRecord_Thread extends Thread {
    private String recordssid;//笔录ssid

    private RecordService recordService;

    private String  mtssid;//会议ssid

    private OutService outService;

    private List<RecordToProblem> recordToProblems1;

    private Police_recordtoproblemMapper police_recordtoproblemMapper;

    private Police_answerMapper police_answerMapper;

    private Integer recordbool;//笔录状态


    public AddRecord_Thread(String recordssid, RecordService recordService, String mtssid, OutService outService,List<RecordToProblem> recordToProblems1, Police_recordtoproblemMapper police_recordtoproblemMapper, Police_answerMapper police_answerMapper,Integer recordbool){
        this.recordssid=recordssid;
        this.recordService=recordService;
        this.mtssid=mtssid;
        this.outService=outService;
        this.recordToProblems1=recordToProblems1;
        this.police_recordtoproblemMapper=police_recordtoproblemMapper;
        this.police_answerMapper=police_answerMapper;
        this.recordbool=recordbool;
    }

    @Override
    public void run() {

        LogUtil.intoLog("AddRecord_Thread__笔录保存：保存笔录问答开始__recordssid__"+recordssid+"__recordToProblems1__"+recordToProblems1);
        //获取该笔录下的全部题目答案
        EntityWrapper recordToProblemsParam=new EntityWrapper();
        recordToProblemsParam.eq("recordssid",recordssid);
        List<Police_recordtoproblem> recordToProblems=police_recordtoproblemMapper.selectList(recordToProblemsParam);
        if (null!=recordToProblems&&recordToProblems.size()>0){
            for (Police_recordtoproblem recordToProblem : recordToProblems) {
                //删除所有题目
                EntityWrapper answersParam=new EntityWrapper();
                answersParam.eq("recordtoproblemssid",recordToProblem.getSsid());
                int answerdelete_bool=police_answerMapper.delete(answersParam);
                LogUtil.intoLog(this.getClass(),"answerdelete_bool__"+answerdelete_bool);
            }
            int recordtoproblemdelete_bool=police_recordtoproblemMapper.delete(recordToProblemsParam);
            LogUtil.intoLog(this.getClass(),"recordtoproblemdelete_bool__"+recordtoproblemdelete_bool);
        }else{
            LogUtil.intoLog(this.getClass(),"该笔录没有任何题目答案__1");
        }

        //根据参数笔录题目包括答案，新增笔录题目答案
        if (null!=recordToProblems1&&recordToProblems1.size()>0){
            for (int i = 0; i < recordToProblems1.size(); i++) {
                RecordToProblem problem=recordToProblems1.get(i);
                problem.setCreatetime(new Date());
                problem.setSsid(OpenUtil.getUUID_32());
                problem.setOrdernum(Integer.valueOf(i+1));
                problem.setRecordssid(recordssid);
                int recordtoprobleminsert_bool=police_recordtoproblemMapper.insert(problem);
                LogUtil.intoLog(this.getClass(),"recordtoprobleminsert_bool__"+recordtoprobleminsert_bool);
                if (recordtoprobleminsert_bool>0){
                    List<Police_answer> answers=problem.getAnswers();
                    if (null!=answers&&answers.size()>0){
                        for (int j = 0; j < answers.size(); j++) {
                            Police_answer answer  = answers.get(j);
                            answer.setSsid(OpenUtil.getUUID_32());
                            answer.setCreatetime(new Date());
                            answer.setOrdernum(Integer.valueOf(j+1));
                            answer.setRecordtoproblemssid(problem.getSsid());
                            int answerinsert_bool =  police_answerMapper.insert(answer);
                            LogUtil.intoLog(this.getClass(),"answerinsert_bool__"+answerinsert_bool);
                        }
                    }
                }
            }
        }else{
            LogUtil.intoLog(this.getClass(),"该笔录没有任何题目答案__2");
        }
        LogUtil.intoLog("AddRecord_Thread__笔录保存：保存笔录问答结束__recordssid__"+recordssid+"__recordToProblems1__"+recordToProblems1);

        if (null!=recordbool&&recordbool==2){
            LogUtil.intoLog("AddRecord_Thread__笔录结束时生成Pdf结束会议开始__recordssid__"+recordssid+"__mtssid__"+mtssid);
            try {
                if (StringUtils.isNotBlank(recordssid)&&null!=recordService){
                    //生成word 和pdf
                    RResult exportPdf_rr=new RResult();
                    ExportPdfParam exportPdfParam=new ExportPdfParam();
                    exportPdfParam.setRecordssid(recordssid);
                    ReqParam reqParam=new ReqParam();
                    reqParam.setParam(exportPdfParam);

                    exportPdf_rr=recordService.exportPdf(exportPdf_rr, reqParam);
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
}
