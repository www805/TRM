package com.avst.trm.v1.web.standaloneweb.service;


import com.avst.trm.v1.common.conf.type.FDType;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogPageParam;
import com.avst.trm.v1.common.util.log.LogParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.log.LogVO;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetFDLogParam;
import com.avst.trm.v1.feignclient.ec.vo.GetFDLogVO;
import com.avst.trm.v1.feignclient.ec.vo.param.FDLogItem;
import com.avst.trm.v1.web.standaloneweb.req.GetLogListByTypeParam;
import com.avst.trm.v1.web.standaloneweb.req.GetLogTypeListParam;
import com.avst.trm.v1.web.standaloneweb.vo.param.LogTypeParam;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("logService")
public class LogService extends BaseService {

    @Autowired
    private EquipmentControl equipmentControl;

    private Gson gson = new Gson();



    public void getLogListByType(RResult result, GetLogListByTypeParam param ){

        int sysorfd=param.getSysOrFd();

        int Currentpage=param.getCurrentpage();
        if(0==Currentpage){
            Currentpage=1;//默认第一页
        }
        int Pagesize=param.getPagesize();
        if(0==Pagesize){
            Pagesize=10;//默认10条一页
        }
        String Time=param.getTime();
        String Type=param.getType();

        if(StringUtils.isEmpty(Type)){
            param.setTime(DateUtil.getDate());
        }
        if(StringUtils.isEmpty(Type)){
            if(sysorfd==1){
                param.setType("info");
            }else{
                param.setType("0");
            }
        }

        LogVO logVO=new LogVO();

        if(sysorfd==2){
            GetFDLogParam logparam=new GetFDLogParam();

            if(StringUtils.isEmpty(Time)){
                Time=param.getTime();
            }

            logparam.setPage(Currentpage);

            String day=DateUtil.getDay(DateUtil.strToDate2(Time));
            String month=DateUtil.getMonth(DateUtil.strToDate2(Time));
            String year=DateUtil.getYear(DateUtil.strToDate2(Time));
            logparam.setFd(Integer.parseInt(day));
            logparam.setFm(Integer.parseInt(month));
            logparam.setFy(Integer.parseInt(year));
            try {
                logparam.setLogtype(Integer.parseInt(param.getType()));
            } catch (Exception e) {
                LogUtil.intoLog(4,this.getClass(),"日志类型异常，抛错，param.getType()："+param.getType());
                logparam.setLogtype(0);
            }
            logparam.setFdType(FDType.FD_AVST);
            RResult result1=equipmentControl.getFDLog(logparam);
            if(null!=result1&&result1.getActioncode().equals(Code.SUCCESS.toString())){

                GetFDLogVO getFDLogVO=gson.fromJson(gson.toJson(result1.getData()),GetFDLogVO.class);
                if(null!=getFDLogVO){

                    List<FDLogItem> loglist=getFDLogVO.getFdLogItemlist();
                    if(null!=loglist&&loglist.size() > 0){
                        List<LogParam> logList=new ArrayList<LogParam>();
                        for(FDLogItem log:loglist){
                            LogParam logParam=new LogParam();
                            logParam.setType(log.getTp());
                            //logParam.setUser();//设备查询没有用户参数
                            logParam.setMsg(log.getMessage());
                            logParam.setTime(log.getDate());
                            logList.add(logParam);
                        }
                        logVO.setLogList(logList);
                    }

                    LogPageParam logPageParam=new LogPageParam();
                    logPageParam.setPagesize(15);//设备默认15条每页
                    logPageParam.setTime(Time);
                    logPageParam.setType("0");//设备日志查询暂时只有全部
                    logPageParam.setTotalpage(Integer.parseInt(getFDLogVO.getTotalpage()));
                    logPageParam.setCurrentpage(Currentpage);
                    logVO.setLogPageParam(logPageParam);
                }else{
                    LogUtil.intoLog(4,this.getClass(),"设备日志查看返回成功，Json解析失败");
                }
            }else{
                LogUtil.intoLog(4,this.getClass(),"设备日志查看失败，ec返回为空");
            }

        }else {
            logVO=LogUtil.getlog(param);
        }


        if(null==logVO){
            result.setMessage("日志查看结果异常");
        }else if (null==logVO.getLogList()||logVO.getLogList().size()==0){
            result.setMessage("日志查看数据为空");
        }else{
            result.setData(logVO);
            changeResultToSuccess(result);
        }
        return;
    }

    public void getLogTypeList(RResult result, GetLogTypeListParam param ){

        int sysOrFd=param.getSysOrFd();
        if(0==sysOrFd){
            sysOrFd=1;//默认笔录系统日志
        }

        List<LogTypeParam> vo=new ArrayList<LogTypeParam>();
        if(sysOrFd==1){

            LogTypeParam logTypeParam=new LogTypeParam();
            logTypeParam.setName("正常日志");
            logTypeParam.setValue("info");
            vo.add(logTypeParam);
            logTypeParam=new LogTypeParam();
            logTypeParam.setName("警告日志");
            logTypeParam.setValue("warn");
            vo.add(logTypeParam);
            logTypeParam=new LogTypeParam();
            logTypeParam.setName("异常日志");
            logTypeParam.setValue("error");
            vo.add(logTypeParam);
        }else {
            //设备的日志暂时只有一种类型
            LogTypeParam logTypeParam=new LogTypeParam();
            logTypeParam.setName("设备日志");
            logTypeParam.setValue("0");
            vo.add(logTypeParam);

        }

        result.setData(vo);
        changeResultToSuccess(result);

        return;
    }

}
