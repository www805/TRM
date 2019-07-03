package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.req.ControlInfoParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ToOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器任务
 * 注意：这种定时器一定要用try包一下，以免内存泄露或者线程异常不能释放
 */
@Component
public class SchedulerZk {

    @Autowired
    private ZkControl zkControl;

    @Value("${spring.application.name}")
    private String servername;

    //每个小时的第五分钟执行

    /**
     * 10秒心跳一次
     */
//    @Scheduled(cron = "0 05 1/1 * * *")
    @Scheduled(fixedRate = 10000)
    public void testTasks() {

        ReqParam<ControlInfoParamVO> param = new ReqParam<>();

        ControlInfoParamVO controlInfoParamVO = new ControlInfoParamVO();
        controlInfoParamVO.setServername(servername);//服务器名
        controlInfoParamVO.setTotal_item(1);
        controlInfoParamVO.setUse_item(1);
//        controlInfoParamVO.setCreatetime(DateUtil.getDateAndMinute());//设置当前时间
        controlInfoParamVO.setStatus(1);//状态

        param.setParam(controlInfoParamVO);

        try {
            zkControl.getHeartbeat(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
