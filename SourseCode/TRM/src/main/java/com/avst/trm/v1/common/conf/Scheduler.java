package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import com.avst.trm.v1.web.sweb.vo.InitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Value("${spring.application.name}")
    private String servername;

    @Value("${server.port}")
    private String port;

    private String url;

    private String urltow;

    //每个小时的第五分钟执行

    /**
     * 需要验证
     */
    @Scheduled(cron = "0 05 1/1 * * *")
    public void testTasks2() {

        LogUtil.intoLog(this.getClass(),"定时任务执行时间testTasks2：" + dateFormat.format(new Date()));

        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        //检测授权
        int authorizebool=serverconfig.getAuthorizebool();
        if(authorizebool!=1){//还没有生成隐性授权文件
            boolean bool= AnalysisSQ.createClientini(serverconfig);
            LogUtil.intoLog(this.getClass(),"initClient authorizebool:"+bool);
        }

        Date date=serverconfig.getWorkstarttime();//数据库的开始时间
        SQEntity sqEntity=AnalysisSQ.getSQEntity();
        if(!DateUtil.format(date).equals(sqEntity.getStartTime())){//对比数据库和ini的开始时间，以防篡改
            CommonCache.clientSQbool=false;
            return ;
        }else{
            CommonCache.getSQEntity=sqEntity;
        }
        LogUtil.intoLog(this.getClass(),DateUtil.format(date)+":DateUtil.format(date)----sqEntity.getStartTime():"+sqEntity.getStartTime());

        //更新最外面的使用时间
        long nowtime=DateUtil.getSeconds();
        int workday=DateUtil.longToTime_day(nowtime-date.getTime());
        AnalysisSQ.updateClientini(workday);

        int bool=AnalysisSQ.checkUseTime();
        if(bool > -1){//授权正确
            CommonCache.clientSQbool=true;
        }else{
            CommonCache.clientSQbool=false;
        }


    }

    /**
     * 30秒一次心跳
     */
    @Scheduled(fixedRate = 30000)
    public void testTasks() {

        InitVO initVO_WEB = CommonCache.getinit_WEB();
        ko: //跳出两层循环标号
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

        com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO initVO_Client = CommonCache.getinit_CLIENT();
        out: //跳出两层循环标号
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
        }

        ReqParam<ControlInfoParamVO> param = new ReqParam<>();

        ControlInfoParamVO controlInfoParamVO = new ControlInfoParamVO();
        controlInfoParamVO.setServername(servername);//服务器注册名
        controlInfoParamVO.setServertitle("业务系统");//服务器中文名
        controlInfoParamVO.setServertitletwo("后台服务器系统");//服务器中文名
        controlInfoParamVO.setUrl("http://localhost:" + port + url);
        controlInfoParamVO.setUrltwo("http://localhost:" + port + urltow);
        controlInfoParamVO.setLoginusername(admininfo.getLoginaccount());//登录账号
        controlInfoParamVO.setLoginpassword(admininfo.getPassword());//登录密码
        controlInfoParamVO.setTotal_item(1);//总业务数
        controlInfoParamVO.setUse_item(1);//可使用业务数
        controlInfoParamVO.setStatus(1);//状态

        param.setParam(controlInfoParamVO);

        try {
            zkControl.getHeartbeat(param);
        } catch (Exception e) {
            LogUtil.intoLog(4,this.getClass(),"Scheduler.testTasks is error, 上报心跳到总控失败");
        }
    }

}
