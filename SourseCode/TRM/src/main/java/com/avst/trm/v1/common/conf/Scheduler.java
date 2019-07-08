package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.common.util.sq.SQEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

}
