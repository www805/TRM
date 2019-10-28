package com.avst.trm.v1.web.cweb.conf;

import com.avst.trm.v1.common.conf.type.MCType;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.feignclient.mc.MeetingControl;
import com.avst.trm.v1.feignclient.mc.req.GetMCStateParam_out;
import com.avst.trm.v1.feignclient.mc.req.OverAccidentMTParam_out;
import com.avst.trm.v1.web.cweb.cache.RecordProtectCache;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.cache.param.RecordProtectParam;
import com.avst.trm.v1.web.cweb.req.policereq.AddRecordParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 开机检测断电笔录，全部结束
 */
@Component
@Order(value = 5)
public class CheckRecordProtect  implements ApplicationRunner {
    @Autowired
    private RecordService recordService;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private MeetingControl meetingControl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.intoLog(1,this.getClass(),"开始检测异常笔录___");
        List<RecordProtectParam> recordProtectList = RecordProtectCache.getRecordProtectList();
        if (null!=recordProtectList&&recordProtectList.size()>0){
            LogUtil.intoLog(1,this.getClass(),"异常文件数__"+recordProtectList.size());
            for (RecordProtectParam recordProtectParam : recordProtectList) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runRecordProtect(recordProtectParam);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            LogUtil.intoLog(1,this.getClass(),"没有发现异常笔录");
        }
    }

    /**
     * 处理
     */
    public void runRecordProtect(RecordProtectParam recordProtectParam){
        if (null!=recordProtectParam){

            //修改笔录状态
            String recordssid = recordProtectParam.getRecordssid();
            String mtssid = recordProtectParam.getMtssid();

            //笔录问答缓存里面
            List< RecordToProblem> recordToProblems=recordProtectParam.getRecordToProblems();
            if (null!=recordToProblems&&recordToProblems.size()>0){
                RecordrealingCache.initCache();//先初始化一边
                RecordrealingCache.setRecordreal(recordssid,recordToProblems);
            }

            if (null!=recordssid){
                //获取笔录状态，进行中的删除
                Police_record police_record=new Police_record();
                police_record.setSsid(recordssid);
                police_record=police_recordMapper.selectOne(police_record);
                if (null!=police_record){

                    Integer recordbool=police_record.getRecordbool();
                    if (recordbool==1){

                        //开始检测会议状态：不正常或者获取失败需要关闭笔录

                        //获取会议状态
                        Integer mtstate=-1;//会议状态  //会议状态 0初始化，1进行中，2已结束，3暂停
                        if (StringUtils.isNotBlank(mtssid)){
                            ReqParam<GetMCStateParam_out> mt_param=new ReqParam<>();//会议参数
                            GetMCStateParam_out getMCStateParam_out=new GetMCStateParam_out();
                            getMCStateParam_out.setMcType(MCType.AVST);
                            getMCStateParam_out.setMtssid(mtssid);
                            mt_param.setParam(getMCStateParam_out);
                            RResult mt_rr=new RResult();//会议返回
                            mt_rr=meetingControl.getMCState(mt_param);
                            if (null != mt_rr && mt_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                                mtstate=(Integer) mt_rr.getData();
                                LogUtil.intoLog(1,this.getClass(),"检测异常笔录ssid_"+recordssid+"_检测到会议状态__"+mtstate);
                            }
                        }else {
                            mtstate=1;//没有会议需要
                        }

                        //检测到会议的状态不正常
                        if (mtstate==-1){
                            RResult result = new RResult();
                            ReqParam reqParam = new ReqParam();
                            AddRecordParam addRecordParam = new AddRecordParam();
                            addRecordParam.setRecordssid(recordssid);
                            addRecordParam.setMtssid(mtssid);
                            addRecordParam.setRecordbool(2);
                            addRecordParam.setCasebool(1);
                            reqParam.setParam(addRecordParam);
                            recordService.addRecord(result, reqParam);
                            if (null != result && result.getActioncode().equals(Code.SUCCESS.toString())) {
                                LogUtil.intoLog(1,this.getClass(),"检测异常笔录ssid_"+recordssid+"_改为已结束__成功");
                                //开始关闭录像：对接设备录像接口
                                ReqParam<OverAccidentMTParam_out> mt_param=new ReqParam<>();//会议参数
                                OverAccidentMTParam_out overAccidentMTParam_out=new OverAccidentMTParam_out();
                                overAccidentMTParam_out.setMcType(MCType.AVST);
                                overAccidentMTParam_out.setMtssid(mtssid);
                                mt_param.setParam(overAccidentMTParam_out);
                                RResult mt_rr=new RResult();//会议返回
                                mt_rr=  meetingControl.overAccidentMT(mt_param);
                                if (null != mt_rr && mt_rr.getActioncode().equals(Code.SUCCESS.toString())) {
                                    LogUtil.intoLog(1,this.getClass(),"检测异常笔录 meetingControl.overAccidentMT____成功");
                                }
                                //结束后删除
                                RecordProtectCache.delRecordecordProtect(recordssid);
                            }
                        }
                    }
                    if (recordbool!=1&&recordbool!=0){
                        //笔录状态不为进行中和未开始的删除
                        RecordProtectCache.delRecordecordProtect(recordssid);
                    }
                }else {
                    //该笔录不存在了删除
                    RecordProtectCache.delRecordecordProtect(recordssid);
                }
            }


        }
    }
}
