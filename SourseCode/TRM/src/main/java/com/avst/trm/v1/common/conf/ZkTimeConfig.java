package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 当springboot启动的时候，会自动执行该类
 */
@Component
@Order(value = 1)
public class ZkTimeConfig implements ApplicationRunner {

    @Autowired
    private ZkControl zkControl;

    @Value("${control.servser.date}")
    private Integer servserDate;

    //获取服务器时间进行比对
    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            //从总控获取时间
            RResult controlTime = zkControl.getControlTime();

            if(null != controlTime){

                //日期的差距
                String createTime = (String) controlTime.getData();  //获取总控服务器时间
                if (StringUtils.isNotEmpty(createTime)) {

                    String newTime = DateUtil.getDateAndMinute();  //当前服务器时间
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //把获取到的时间转为时间戳
                    Date date = dateFormatter.parse(createTime);

                    //把转成总控时间和当前服务器时间戳进行计算
                    Date newday = dateFormatter.parse(newTime);
                    Date oldDay = dateFormatter.parse(createTime);
                    long intervalDay = (newday.getTime() - oldDay.getTime())/(1*60*60*1000);

                    //如果时间差过1小时以上，就修改系统时间
                    if (Math.abs(intervalDay) >= servserDate) {

                        //修改系统时间
                        String osName = System.getProperty("os.name");
                        String cmd = "";
                        try {
                            SimpleDateFormat rq = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat xs = new SimpleDateFormat("HH:mm:ss");

                            if (osName.matches("^(?i)Windows.*$")) {// Window 系统

                                // 格式 HH:mm:ss  22:35:00
                                cmd = xs.format(date);
                                cmd = "  cmd /c time " + cmd;
                                Runtime.getRuntime().exec(cmd);

                                // 格式：yyyy-MM-dd  2009-03-26
                                cmd = rq.format(date);
                                cmd = " cmd /c date " + cmd;
                                Runtime.getRuntime().exec(cmd);
                            } else {// Linux 系统
                                // 格式：yyyyMMdd  20090326
                                rq = new SimpleDateFormat("yyyyMMdd");
                                cmd = rq.format(date);
                                cmd = "  date -s " + cmd;
                                Runtime.getRuntime().exec(cmd);
                                // 格式 HH:mm:ss  22:35:00
                                cmd = xs.format(date);
                                cmd = "  date -s " + cmd;
                                Runtime.getRuntime().exec(cmd);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

//                System.out.println("服务器获取到的时间：" + date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("结束");
        }




    }


}
