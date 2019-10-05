package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.RecordStatusCache;
import com.avst.trm.v1.common.cache.param.RecordStatusCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import com.avst.trm.v1.web.cweb.req.policereq.AddRecordParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService;
import com.avst.trm.v1.web.sweb.vo.InitVO;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时器任务
 * 注意：这种定时器一定要用try包一下，以免内存泄露或者线程异常不能释放
 */
@Component
public class Scheduler {

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ZkControl zkControl;

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private RecordService recordService;

    private String url;

    private String urltow;

    //每个小时的第五分钟执行

    /**
     * 需要验证
     */
    @Scheduled(cron = "1 58 1/1 * * ?")
    public void testTasks2() {

        LogUtil.intoLog(1,this.getClass(),"定时任务执行时间testTasks2：" + dateFormat.format(new Date()),"循环定时器");

        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        //检测授权
        int authorizebool=serverconfig.getAuthorizebool();
        if(authorizebool!=1){//还没有生成隐性授权文件
            boolean bool= AnalysisSQ.createClientini(serverconfig);
            LogUtil.intoLog(3,this.getClass(),"initClient authorizebool:"+bool,"循环定时器");
            return;
        }

        SQEntity sqEntity=AnalysisSQ.getSQEntity();
        if(null==sqEntity){
            LogUtil.intoLog(4,this.getClass(),"sqEntity==null,testTasks2 is over","循环定时器");
            boolean bool= AnalysisSQ.createClientini(serverconfig);
            LogUtil.intoLog(3,this.getClass(),"initClient authorizebool:"+bool,"循环定时器");
            return ;
        }

        Date date=serverconfig.getWorkstarttime();//数据库的开始时间

        String databasetime=DateUtil.format(date);//数据库中的开始时间
        String sqfiletime=sqEntity.getStartTime();//授权文件中的开始时间
        LogUtil.intoLog(3,this.getClass(),databasetime+":databasetime:DateUtil.format(date)----sqEntity.getStartTime():sqfiletime:"+sqfiletime,"循环定时器");
        if(!databasetime.equals(sqfiletime)){//对比数据库和ini的开始时间，以防篡改
            boolean bool= AnalysisSQ.createClientini(serverconfig);
            LogUtil.intoLog(4,this.getClass(),"时间不对授权重新初始化授权initClient authorizebool:"+bool,"循环定时器");
            serverconfig=base_serverconfigMapper.selectById(1);
        }

        //更新最外面的使用时间
        long nowtime=DateUtil.getSeconds();
        int workday=DateUtil.longToTime_day(nowtime-date.getTime());
        AnalysisSQ.updateClientini(workday);

        int bool=AnalysisSQ.checkUseTime();
        LogUtil.intoLog(1,this.getClass(),"AnalysisSQ.checkUseTime()---bool:"+bool,"循环定时器");
        if(bool > -1){//授权正确
            CommonCache.clientSQbool=true;
            int workdays=serverconfig.getWorkdays();
            //修改数据库的使用时间
            if(workdays!=workday){//如果数据库的使用时间跟授权文件中的使用时间不一致，就修改数据库
                serverconfig.setWorkdays(workday);
                int updateById=base_serverconfigMapper.updateById(serverconfig);
                LogUtil.intoLog(1,this.getClass(),"base_serverconfigMapper.updateById--修改数据库最新使用时间-updateById:"+updateById,"循环定时器");
            }

        }else{
            CommonCache.clientSQbool=false;
            if(bool == -100001){
                //重新授权
                CommonCache.clientSQbool=AnalysisSQ.createClientini(serverconfig);
            }
            LogUtil.intoLog(4,this.getClass(),"授权异常，异常码:"+bool,"循环定时器");
        }


    }

//    @Scheduled(fixedRate = 10000) //10秒心跳一次
    /**
     * 1分钟心跳一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void testTasks() {

        InitVO initVO_WEB = CommonCache.getinit_WEB();
        if(null==initVO_WEB||null==initVO_WEB.getPageList()||initVO_WEB.getPageList().size() == 0){
            LogUtil.intoLog(4,this.getClass(),"查看授权是否出问题了，CommonCache.getinit_WEB() is null","循环定时器");
            return ;
        }
        /**提供给总控，服务端的登录地址（从动作缓存中获取）**/
        ko: /**跳出两层循环标号**/
        for (PageVO pageVO : initVO_WEB.getPageList()) {
            if("server_web/base/login".equals(pageVO.getPageid())){
                for (ActionVO actionVO : pageVO.getActionList()) {
                    if("login_main".equals(actionVO.getActionId())){
                        urltow = actionVO.getReqURL();
                        break ko;
                    }
                }
            }
        }
        /**提供给总控，客户端的登录地址（从动作缓存中获取）**/
        com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO initVO_Client = CommonCache.getinit_CLIENT();
        out: /**跳出两层循环标号**/
        for (PageVO pageVO : initVO_Client.getPageList()) {
            if("client_web/base/login".equals(pageVO.getPageid())){
                for (ActionVO actionVO : pageVO.getActionList()) {
                    if ("login_gotomain".equals(actionVO.getActionId())) {
                        url = actionVO.getReqURL();
                        break out;
                    }
                }
            }
        }

        //从缓存中获取超管信息，如果没有就从数据库里拿出来
        Base_admininfo admininfo = CommonCache.getAdmininfo();
        if(null == admininfo){
            admininfo = base_admininfoMapper.selectById(1);
            CommonCache.setAdmininfo(admininfo);
        }

        ReqParam<ControlInfoParamVO> param = new ReqParam<>();

        //获取本机ip地址
        String hostAddress = NetTool.getMyIP();
        if(StringUtils.isBlank(hostAddress)){
            hostAddress = "localhost";
        }

        String servername=PropertiesListenerConfig.getProperty("spring.application.name");
        String port=PropertiesListenerConfig.getProperty("server.port");

        ControlInfoParamVO controlInfoParamVO = new ControlInfoParamVO();
        controlInfoParamVO.setServername(servername);//服务器注册名
        controlInfoParamVO.setServertitle("业务系统");//服务器中文名
        controlInfoParamVO.setServertitletwo("后台服务器系统");//服务器中文名
        controlInfoParamVO.setUrl("http://" + hostAddress + ":" + port + url);//访问本服务器地址
        controlInfoParamVO.setUrltwo("http://" + hostAddress + ":" + port + urltow);
        controlInfoParamVO.setLoginusername(admininfo.getLoginaccount());//登录账号
        controlInfoParamVO.setLoginpassword(admininfo.getPassword());//登录密码
        controlInfoParamVO.setTotal_item(1);//总业务数
        controlInfoParamVO.setUse_item(1);//可使用业务数
        controlInfoParamVO.setStatus(1);//状态

        param.setParam(controlInfoParamVO);

        try {
            zkControl.getHeartbeat(param);
        } catch (Exception e) {
            LogUtil.intoLog(4,this.getClass(),"Scheduler.testTasks is error, 上报心跳到总控失败","循环定时器");
        }
    }

    /**
     * 处理休庭状态
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void recordTasks() {

        synchronized(RecordStatusCache.class){
            List<RecordStatusCacheParam> paramList = RecordStatusCache.getRecordStatusCacheParam();

            if (null != paramList && paramList.size() > 0) {
                //计算公式转换成整数
                JexlEngine jexlEngine = new JexlBuilder().create();
                String formulas=PropertiesListenerConfig.getProperty("record.cache.maxTime");
                JexlExpression expression = jexlEngine.createExpression(formulas);
                Integer maxTimeInt = (Integer) expression.evaluate(null);
                long maxTime = maxTimeInt.longValue();

                long nowtime = new Date().getTime();

                for (int i = 0; i < paramList.size(); i++) {
                    RecordStatusCacheParam param = paramList.get(i);
                    //判断时间如果3分钟没心跳就设为休庭
                    long countTime = nowtime-param.getLasttime();

//                    long maxTime = 3*60*1000;//测试3分钟，实际半小时
                    if (countTime >= maxTime) {
                        //修改笔录状态
                        String ssid = param.getRecordssid();
                        String mtssid = param.getMtssid();

                        RResult result = new RResult();
                        ReqParam reqParam = new ReqParam();
                        AddRecordParam addRecordParam = new AddRecordParam();
                        addRecordParam.setRecordssid(ssid);
                        addRecordParam.setMtssid(mtssid);
                        addRecordParam.setRecordbool(2);
                        addRecordParam.setCasebool(3);
                        reqParam.setParam(addRecordParam);

                        recordService.addRecord(result, reqParam);

                        LogUtil.intoLog(1, this.getClass(), "Scheduler.testTasks is info, 笔录ssid = " + ssid + " 设置休庭成功","循环定时器");
                    }
                }

            }
        }
    }


    /**
     * 定时重启电脑
     */
    public void checkReboot(){

        String os= NetTool.getOsName();
        if (os.startsWith("Win")) {
            try {

                String cmd="shutdown -r";


            } catch (Exception e) {

                e.printStackTrace();
            }
        } else if (os.startsWith("Linux")) {
            LogUtil.intoLog(3,this.getClass(),"暂时没有重启Linux的计划");
        }

    }

    /**
     * 检测压缩文件过期
     */
    public void checkZipTimeOut(){

        try {



        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
