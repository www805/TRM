package com.avst.trm.v1.outsideinterface.conf;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 监测总控同步时间 ZkTimeConfig
 */
@Component
public class ZkTimeConfig {

    @Autowired
    private ZkControl zkControl;

    //获取总控时间进行比对
    public void compare() {

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

                    //计算公式转换成整数
                    JexlEngine jexlEngine = new JexlBuilder().create();
                    String formulas=PropertiesListenerConfig.getProperty("control.servser.formulas");
                    JexlExpression expression = jexlEngine.createExpression(formulas);
                    Integer evaluate = (Integer) expression.evaluate(null);

                    long intervalDay = (newday.getTime() - oldDay.getTime())/(evaluate);

                    //如果时间差过1小时以上，就修改系统时间
                    String servserDate= PropertiesListenerConfig.getProperty("control.servser.date");
                    if (Math.abs(intervalDay) >= Integer.valueOf(servserDate)) {

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
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.intoLog(4,this.getClass(),"getControlTime ZkTimeConfig。run 总控同步时间，请求异常");
        }
    }
}
